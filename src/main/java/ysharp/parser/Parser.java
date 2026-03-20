package ysharp.parser;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Util.Type.Type;
import ysharp.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static class Program {
        public List<String> useDeclaration;
        public List<Stmt> program;

        public Program(){
            this.useDeclaration = new ArrayList<>();
            this.program = new ArrayList<>();
        }
    }

    public List<YsharpError> errors;

    public boolean hadErrors (){
        return !errors.isEmpty();
    }

    private final List<Token> tokenStream;

    private int current = 0;

    private Token peek(){
        if(current >= tokenStream.size()) return tokenStream.getLast();
        return tokenStream.get(current);
    }

    private Token peekNext(){
        if(current + 1 >= tokenStream.size()) return tokenStream.getLast();
        return tokenStream.get(current + 1);
    }

    private Token advance(){
        if(current + 1 >= tokenStream.size()) return tokenStream.getLast();
        var token = tokenStream.get(current);
        current++;
        return token;
    }

    private boolean match(Token token, Token.TokenType ...types){
        for(Token.TokenType type : types) {
            if(token.type.equals(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token previous(){
        if(current - 1 < 0) return null;
        return tokenStream.get(current - 1);
    }

    private void consume(Token.TokenType expected, String message) throws YsharpError {
        Token token = peek();
        if(!token.type.equals(expected)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    token.line,
                    message
            );
        }
        advance();
    }

    public Parser(List<Token> tokenStream) {
        this.tokenStream = tokenStream;
        this.errors = new ArrayList<>();
    }

    private void sync() {
        advance();

        while (peek().type != Token.TokenType.END_OF_FILE) {

            if (previous().type == Token.TokenType.SEMI_COLON) {
                return;
            }

            switch (peek().type) {
                case CLASS:
                case FUNCTION:
                case VAR:
                case CONST_:
                case IF:
                case TRY:
                case USE:
                case WHILE:
                case FOR:
                case RETURN:
                case SWITCH:
                case DO:
                case PRINT:
                case PRINTLN:
                    return;
            }

            advance();
        }
    }

    // for debugging
    public List<Expr> parseExprGrammer() {
        List<Expr> list = new ArrayList<>();
        while (peek().type != Token.TokenType.END_OF_FILE) {
            try {
                list.add(parseAssignment());
            }catch (YsharpError err) {
                sync();
            }
        }

        return list;
    }

    public Program parse() {
        Program program = new Program();
        program.useDeclaration = parseUseDeclaration();
        List<Stmt> list = new ArrayList<>();
        while (peek().type != Token.TokenType.END_OF_FILE) {
            try {
                program.program.add(parseDeclaration(true));
            }catch (YsharpError err) {
                errors.add(err);
                sync();
            }
        }

        return program;
    }

    private boolean isLambdaAhead() {

        if (peek().type != Token.TokenType.LEFT_PAREN)
            return false;

        int cursor = current;
        int depth = 0;

        while (cursor < tokenStream.size()) {

            Token t = tokenStream.get(cursor);

            if (t.type == Token.TokenType.LEFT_PAREN)
                depth++;

            else if (t.type == Token.TokenType.RIGHT_PAREN) {
                depth--;
                if (depth == 0) {
                    cursor++;
                    break;
                }
            }

            cursor++;
        }

        if (cursor >= tokenStream.size())
            return false;

        return tokenStream.get(cursor).type == Token.TokenType.RIGHT_ARROW;
    }

    private boolean isForInAhead() {
        if (peek().type != Token.TokenType.FOR) return false;

        int cursor = current + 1;

        if (cursor >= tokenStream.size() || tokenStream.get(cursor).type != Token.TokenType.VAR) {
            return false;
        }
        cursor++;

        if (cursor >= tokenStream.size() || tokenStream.get(cursor).type != Token.TokenType.IDENTIFIER) {
            return false;
        }
        cursor++;

        if (cursor < tokenStream.size() && tokenStream.get(cursor).type == Token.TokenType.COLON) {
            cursor += 2;
        }

        if (cursor >= tokenStream.size()) return false;

        return tokenStream.get(cursor).type == Token.TokenType.IN;
    }

    // expression parser

    private Expr parseAssignment() throws YsharpError{

        if(peek().type == Token.TokenType.LEFT_PAREN && isLambdaAhead()) {
            return parseLambda();
        }

        Expr expr = parseTernary();

        if (match(peek(),
                Token.TokenType.ASSIGN,
                Token.TokenType.PLUS_ASSIGN,
                Token.TokenType.MINUS_ASSIGN,
                Token.TokenType.MULTIPLY_ASSIGN,
                Token.TokenType.DIVIDE_ASSIGN,
                Token.TokenType.MODULO_ASSIGN,
                Token.TokenType.LEFT_SHIFT_ASSIGN,
                Token.TokenType.RIGHT_SHIFT_ASSIGN,
                Token.TokenType.BITWISE_AND_ASSIGN,
                Token.TokenType.BITWISE_XOR_ASSIGN,
                Token.TokenType.BITWISE_OR_ASSIGN)) {

            Token op = previous();
            Expr value = parseAssignment();

            // l_value check
            if (expr instanceof Expr.VariableExpr) {
                return new Expr.AssignmentExpr(expr, op, value);
            }

            if (expr instanceof Expr.GetExpr) {
                Expr.GetExpr get = (Expr.GetExpr) expr;
                return new Expr.SetExpr(get.object, get.name, value);
            }


            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    op.line,
                    "Invalid assignment target." );
        }

        return expr;
    }

    private Expr parseTernary() throws YsharpError {
        Expr nullCoalescing = parseNullCoalescing();

        // consume ?
        if(match(peek(), Token.TokenType.QUESTION_MARK)) {
            Expr then = parseAssignment();
            // consume :
            consume(Token.TokenType.COLON,
                    "Missing ':' in conditional operator.");

            Expr else_ = parseTernary();

            return new Expr.TernaryExpr(
                    nullCoalescing, // condition
                    then,
                    else_
            );
        }

        return  nullCoalescing;
    }

    private Expr parseNullCoalescing() throws YsharpError {
        Expr logicalOr = parseLogicalOr();


        if(match(peek(), Token.TokenType.DOUBLE_QUESTION_MARK)) {
            Token op = previous();
            Expr then = parseLogicalOr();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    logicalOr,
                    op,
                    then
            );
            while (match(peek(), Token.TokenType.DOUBLE_QUESTION_MARK)) {
                op = previous();
                then = parseLogicalOr();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        then
                );
                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return logicalOr;
    }

    private Expr parseLogicalOr() throws YsharpError {
        Expr expr = parseLogicalAnd();

        if(match(peek(), Token.TokenType.LOGICAL_OR)) {
            Token op = previous();
            Expr logicalAnd = parseLogicalAnd();
            Expr.LogicalExpr logicalExpr = new Expr.LogicalExpr(
                    expr,
                    op,
                    logicalAnd
                    );
            while (match(peek(), Token.TokenType.LOGICAL_OR)) {
                op = previous();
                logicalAnd = parseLogicalAnd();
                 Expr.LogicalExpr logicalExpr_ = new Expr.LogicalExpr(
                         logicalExpr,
                        op,
                        logicalAnd
                );
                 logicalExpr = logicalExpr_;
            }

            return logicalExpr;
        }

        return  expr;
    }

    private Expr parseLogicalAnd() throws YsharpError {
        Expr expr = parseBitwiseOr();

        if(match(peek(), Token.TokenType.LOGICAL_AND)) {
            Token op = previous();
            Expr bitwiseOr = parseBitwiseOr();
            Expr.LogicalExpr logicalExpr = new Expr.LogicalExpr(
                    expr,
                    op,
                    bitwiseOr
            );
            while (match(peek(), Token.TokenType.LOGICAL_AND)) {
                op = previous();
                bitwiseOr = parseBitwiseOr();
                Expr.LogicalExpr logicalExpr_ = new Expr.LogicalExpr(
                        logicalExpr,
                        op,
                        bitwiseOr
                );
                logicalExpr = logicalExpr_;
            }

            return logicalExpr;
        }

        return  expr;
    }

    private Expr parseBitwiseOr() throws YsharpError {
        Expr expr = parseBitwiseXor();

        if(match(peek(), Token.TokenType.BITWISE_OR)) {
            Token op = previous();
            Expr bitwiseXOr = parseBitwiseXor();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    bitwiseXOr
            );
            while (match(peek(), Token.TokenType.BITWISE_OR)) {
                op = previous();
                bitwiseXOr = parseBitwiseXor();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        bitwiseXOr
                );
                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return  expr;
    }

    private Expr parseBitwiseXor() throws YsharpError {
        Expr expr = parseBitwiseAnd();

        if(match(peek(), Token.TokenType.BITWISE_XOR)) {
            Token op = previous();
            Expr bitwiseAnd = parseBitwiseAnd();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    bitwiseAnd
            );
            while (match(peek(), Token.TokenType.BITWISE_XOR)) {
                op = previous();
                bitwiseAnd = parseBitwiseAnd();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                         op,
                        bitwiseAnd
                );
                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return  expr;
    }

    private Expr parseBitwiseAnd() throws YsharpError {
        Expr expr = parseEquality();

        if(match(peek(), Token.TokenType.BITWISE_AND)) {
            Token op = previous();
            Expr equality = parseEquality();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    equality
            );
            while (match(peek(), Token.TokenType.BITWISE_AND)) {
                op = previous();
                equality = parseEquality();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        equality
                );
                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return  expr;
    }

    private Expr parseEquality() throws YsharpError {
        Expr expr = parseComparison();

        if (match(peek(),
                Token.TokenType.EQUAL_EQUAL,
                Token.TokenType.BANG_EQUAL)) {

            Token op = previous();

            Expr comparison = parseComparison();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    comparison
            );

            while (match(peek(),
                    Token.TokenType.EQUAL_EQUAL,
                    Token.TokenType.BANG_EQUAL)) {

                op = previous();

                comparison = parseComparison();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        comparison
                );

                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return expr;
    }

    private Expr parseComparison() throws YsharpError {
        Expr expr = parseBitwiseShift();

        if (match(peek(),
                Token.TokenType.GREATER_THAN,
                Token.TokenType.GREATER_OR_EQUAL,
                Token.TokenType.LESS_THAN,
                Token.TokenType.LESS_OR_EQUAL)) {

            Token op = previous();

            Expr right = parseBitwiseShift();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    right
            );

            while (match(peek(),
                    Token.TokenType.GREATER_THAN,
                    Token.TokenType.GREATER_OR_EQUAL,
                    Token.TokenType.LESS_THAN,
                    Token.TokenType.LESS_OR_EQUAL)) {

                op = previous();

                right = parseBitwiseShift();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        right
                );

                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return expr;
    }

    private Expr parseBitwiseShift() throws YsharpError {
        Expr expr = parseTerm();

        if (match(peek(),
                Token.TokenType.RIGHT_SHIFT,
                Token.TokenType.LEFT_SHIFT)) {

            Token op = previous();

            Expr term = parseTerm();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    term
            );

            while (match(peek(),
                    Token.TokenType.RIGHT_SHIFT,
                    Token.TokenType.LEFT_SHIFT)) {

                op = previous();

                term = parseTerm();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        term
                );

                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return expr;
    }

    private Expr parseTerm() throws YsharpError {
        Expr expr = parseFactor();

        if (match(peek(),
                Token.TokenType.PLUS,
                Token.TokenType.MINUS)) {

            Token op = previous();

            Expr factor = parseFactor();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    factor
            );

            while (match(peek(),
                    Token.TokenType.PLUS,
                    Token.TokenType.MINUS)) {

                op = previous();
                factor = parseFactor();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        factor
                );

                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return expr;
    }

    private Expr parseFactor() throws YsharpError {
        Expr expr = parseUnary();

        if (match(peek(),
                Token.TokenType.DIVIDE,
                Token.TokenType.MULTIPLY,
                Token.TokenType.MODULO)) {

            Token op = previous();

            Expr unary = parseUnary();
            Expr.BinaryExpr binaryExpr = new Expr.BinaryExpr(
                    expr,
                    op,
                    unary
            );

            while (match(peek(),
                    Token.TokenType.DIVIDE,
                    Token.TokenType.MULTIPLY,
                    Token.TokenType.MODULO)) {

                op = previous();

                unary = parseUnary();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        op,
                        unary
                );

                binaryExpr = binaryExpr_;
            }

            return binaryExpr;
        }

        return expr;
    }

    private Expr parseUnary() throws YsharpError {
        if (match(peek(),
                Token.TokenType.BANG,
                Token.TokenType.MINUS,
                Token.TokenType.PLUS,
                Token.TokenType.BITWISE_NOT,
                Token.TokenType.PLUS_PLUS,
                Token.TokenType.MINUS_MINUS)) {

            Token op = previous();
            Expr right = parseUnary();
            return new Expr.UnaryExpr(op, right);
        }

        return parsePostfix();
    }

    private Expr parsePostfix() throws YsharpError {
        Expr expr = parseCall();

        while (match(peek(),
                Token.TokenType.PLUS_PLUS,
                Token.TokenType.MINUS_MINUS)) {

            Token op = previous();
            expr = new Expr.PostfixExpr(expr, op);
        }

        return expr;
    }

    private Expr parseCall() throws YsharpError {
        Expr calee = parsePrimary();

        while (peek().type == Token.TokenType.LEFT_PAREN ||
                peek().type == Token.TokenType.DOT) {


            if(match(peek(), Token.TokenType.LEFT_PAREN)) {
                Token leftParen = previous();
                List<Expr> args = new ArrayList<>();

                if(match(peek(), Token.TokenType.RIGHT_PAREN)) {
                    // empty arguments
                }
                else {
                    args.add(parseAssignment());
                    while (match(peek(), Token.TokenType.COMMA)) {
                        args.add(parseAssignment());
                    }

                    consume(Token.TokenType.RIGHT_PAREN,
                            "Expected ')' after arguments.");

                }

                Expr.CallExpr callExpr = new Expr.CallExpr(
                        calee,
                        args,
                        leftParen
                );

                calee = callExpr;

            }
            else if(match(peek(), Token.TokenType.DOT)) {
                Token identifier = advance();
                if(identifier.type != Token.TokenType.IDENTIFIER) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            identifier.line,
                            "");
                }

                Expr.GetExpr getExpr = new Expr.GetExpr(
                        calee,
                        identifier
                );

                calee = getExpr;

            }
        }

        return  calee;
    }

    private Expr parseSuperCall() throws YsharpError {

        Token superToken = previous();

        if (match(peek(), Token.TokenType.LEFT_PAREN)) {
            Token leftParen = previous();
            List<Expr> args = new ArrayList<>();

            if (match(peek(), Token.TokenType.RIGHT_PAREN)) {
                // empty arguments
            } else {
                args.add(parseAssignment());
                while (match(peek(), Token.TokenType.COMMA)) {
                    args.add(parseAssignment());
                }

                consume(Token.TokenType.RIGHT_PAREN,
                        "Expected ')' after arguments.");

            }

            Expr.SuperCallExpr superCallExpr = new Expr.SuperCallExpr(
                    superToken,
                    args,
                    leftParen
            );

            return superCallExpr;
        }
        else {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    superToken.line,
                    "Expected '(' after 'super'."
            );
        }
    }

    private Expr parsePrimary() throws YsharpError {
        if(match(peek(), Token.TokenType.LEFT_BRACKET)) {
            return parseArrayInitializer();
        }
        else if(match(peek(), Token.TokenType.LEFT_CURLY_BRACE)) {
            return parseMapInitializer();
        }
        else if(match(peek(), Token.TokenType.NEW)) {
            return parseNewExpr();
        }
        else if(match(peek(), Token.TokenType.SUPER)) {
            return parseSuperCall();
        }


        return  parseAtom();
    }

    private Expr parseArrayInitializer() throws YsharpError {
        List<Expr> elements = new ArrayList<>();

        if (peek().type != Token.TokenType.RIGHT_BRACKET) {

            elements.add(parseAssignment());

            while (match(peek(), Token.TokenType.COMMA)) {
                elements.add(parseAssignment());
            }
        }

        consume(Token.TokenType.RIGHT_BRACKET,
                "Expected ']' after array elements.");

        return new Expr.ArrayInitializerExpr(elements);
    }

    private Expr parseMapInitializer() throws YsharpError {

        Token leftCurlyBrace = previous();

        List<Expr.MapInitializerExpr.Entry> entries = new ArrayList<>();

        if (peek().type != Token.TokenType.RIGHT_CURLY_BRACE) {

            Token keyToken = peek();

            if (keyToken.type != Token.TokenType.STRING) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        keyToken.line,
                        "Expected string key in map initializer.");
            }

            advance(); // consume string key

            consume(Token.TokenType.COLON,
                    "Expected ':' after map key.");

            Expr value = parseAssignment();
            entries.add(new Expr.MapInitializerExpr.Entry(keyToken, value));

            while (match(peek(), Token.TokenType.COMMA)) {
                keyToken = peek();

                if (keyToken.type != Token.TokenType.STRING) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            keyToken.line,
                            "Expected string key after ','.");
                }

                advance(); // consume string key

                consume(Token.TokenType.COLON,
                        "Expected ':' after map key.");

                value = parseAssignment();
                entries.add(new Expr.MapInitializerExpr.Entry(keyToken, value));
            }

            consume(Token.TokenType.RIGHT_CURLY_BRACE,
                    "Expected '}' after map initializer.");

            return new Expr.MapInitializerExpr(entries, leftCurlyBrace);
        }

        consume(Token.TokenType.RIGHT_CURLY_BRACE,
                "Expected '}' after map initializer.");
        return new Expr.MapInitializerExpr(entries, leftCurlyBrace);
    }

    private Expr parseAtom() throws YsharpError {

        if (match(peek(),
                Token.TokenType.INT,
                Token.TokenType.DOUBLE,
                Token.TokenType.CHAR,
                Token.TokenType.STRING,
                Token.TokenType.NULL_,
                Token.TokenType.TRUE_,
                Token.TokenType.FALSE_)) {

            return new Expr.LiteralExpr(previous());
        }

        if (match(peek(), Token.TokenType.IDENTIFIER)) {
            return new Expr.VariableExpr(previous());
        }

        if (match(peek(), Token.TokenType.LEFT_PAREN)) {
            Expr expr = parseAssignment();
            consume(Token.TokenType.RIGHT_PAREN,
                    "Expected ')' after expression.");
            return new Expr.GroupingExpr(expr);
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.SYNTAX,
                peek().line,
                "Expected expression."
        );
    }

    private Expr parseLambda() throws YsharpError {

        Token leftParen = peek();
        consume(Token.TokenType.LEFT_PAREN,
                "Expected '(' to start lambda parameter list.");

        List<Expr.LambdaExpr.Param> params = new ArrayList<>();

        if (peek().type != Token.TokenType.RIGHT_PAREN) {

            do {
                Token identifier = advance();

                if (identifier.type != Token.TokenType.IDENTIFIER) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            identifier.line,
                            "Expected parameter name in lambda."
                    );
                }

                Token typeTag = null;

                if (match(peek(), Token.TokenType.COLON)) {

                    typeTag = advance();

                    if (typeTag.type != Token.TokenType.IDENTIFIER) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.SYNTAX,
                                typeTag.line,
                                "Expected type identifier after ':' in lambda parameter."
                        );
                    }
                }

                params.add(new Expr.LambdaExpr.Param(identifier, typeTag));

            } while (match(peek(), Token.TokenType.COMMA));
        }

        consume(Token.TokenType.RIGHT_PAREN,
                "Expected ')' after lambda parameter list.");

        Token returnType = null;
        if(match(peek(), Token.TokenType.COLON)) {
            advance(); // consume :
            returnType = advance();
            if(returnType.type != Token.TokenType.IDENTIFIER) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected return type identifier after ':' in lambda."
                );
            }
        }

        consume(Token.TokenType.RIGHT_ARROW,
                "Expected '=>' after lambda parameter list.");

        if(peek().type == Token.TokenType.DO) {
            return new Expr.LambdaExpr(params, returnType, parseBlockStmt(), leftParen);
        }

        return new Expr.LambdaExpr(params, returnType, parseAssignment(), leftParen);
    }

    private Expr parseNewExpr() throws YsharpError {

        Expr constructor = parseCall();

        if (!(constructor instanceof Expr.CallExpr call)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    peek().line,
                    "Expected constructor call after 'new'."
            );
        }

        return new Expr.NewExpr(call.callee, call.arguments);
    }


    // stmt parser

    private Stmt parseStmt() throws YsharpError {

        if(match(peek(), Token.TokenType.PRINT)) return parsePrint();
        if(match(peek(), Token.TokenType.PRINTLN)) return parsePrintln();
        if(peek().type == Token.TokenType.DO) return parseBlockStmt();
        if(match(peek(), Token.TokenType.IF)) return parseIfStmt();
        if(match(peek(), Token.TokenType.WHILE)) return parseWhileStmt();
        if(peek().type == Token.TokenType.FOR) {
            if(isForInAhead()) {
                advance(); // consume for
                return parseForInStmt();
            }
            else {
                advance(); // consume for
                return parseForStmt();
            }
        };
        if(match(peek(), Token.TokenType.BREAK)) return parseBreakStmt();
        if(match(peek(), Token.TokenType.CONTINUE)) return parseContinueStmt();
        if(match(peek(), Token.TokenType.RETURN)) return parseReturnStmt();
        if(match(peek(), Token.TokenType.SWITCH)) return parseSwitchStmt();
        if(match(peek(), Token.TokenType.THROW)) return parseThrowStmt();
        if(match(peek(), Token.TokenType.TRY)) return parseTryStmt();

        return  parseExprStmt();
    }

    private Stmt parseExprStmt() throws YsharpError {
        Expr expr = parseAssignment();
        consume(Token.TokenType.SEMI_COLON,
                "Expected ';' after expression.");

        return new Stmt.ExprStmt(expr);
    }

    private Stmt parsePrint() throws YsharpError {
        Expr expr = parseAssignment();
        consume(Token.TokenType.SEMI_COLON,
                "expected ';' after print statement");

        return new Stmt.PrintStmt(expr);
    }

    private Stmt parsePrintln() throws YsharpError {
        Expr expr = parseAssignment();
        consume(Token.TokenType.SEMI_COLON,
                "expected ';' after println statement");

        return new Stmt.PrintlnStmt(expr);
    }

    private Stmt parseBlockStmt() throws YsharpError {
        consume(Token.TokenType.DO,
                "Expected 'do' to start block.");

        List<Stmt> stmtList = new ArrayList<>();
        while (peek().type != Token.TokenType.END_ &&
                peek().type != Token.TokenType.END_OF_FILE) {
            stmtList.add(parseDeclaration(false));
        }

        consume(Token.TokenType.END_,
                "Expected 'end' to close block.");
        return new Stmt.BlockStmt(stmtList);
    }

    private Stmt parseIfStmt() throws YsharpError {
        Expr condition = parseAssignment();
        consume(Token.TokenType.THEN,
                "Expected 'then' after if condition.");
        Stmt then = parseBlockStmt();
        List<Stmt.IfStmt.ElifStmt> elifStmtList = new ArrayList<>();


        while (match(peek(), Token.TokenType.ELIF)) {
            Expr condition_ = parseAssignment();
            consume(Token.TokenType.THEN,
                    "Expected 'then' after elif condition.");
            Stmt then_ = parseBlockStmt();

            elifStmtList.add(new Stmt.IfStmt.ElifStmt(condition_, then_));
        }

        Stmt else_ = null;
        if(match(peek(), Token.TokenType.ELSE)) {
            else_ = parseBlockStmt();
        }

        return new Stmt.IfStmt(condition,
                then,
                else_,
                elifStmtList);

    }

    private Stmt parseWhileStmt() throws YsharpError {

        Expr condition = parseAssignment();

        if (peek().type == Token.TokenType.END_OF_FILE) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    peek().line,
                    "Expected statement after while condition."
            );
        }

        Stmt body = parseStmt();

        return new Stmt.WhileStmt(condition, body);
    }

    private Stmt parseForStmt() throws YsharpError {

        // initializer
        Stmt initializer = null;

        if (match(peek(), Token.TokenType.SEMI_COLON)) {
            initializer = null;
        }
        else if (match(peek(), Token.TokenType.VAR)) {
            initializer = parseVarDeclaration(false);
        }
        else {
            initializer = parseExprStmt();
        }

        // condition
        Expr condition = null;
        if (peek().type != Token.TokenType.SEMI_COLON) {
            condition = parseAssignment();
        }

        consume(Token.TokenType.SEMI_COLON,
                "Expected ';' after loop condition.");

        // increment
        Expr increment = null;
        if (peek().type != Token.TokenType.DO) {
            increment = parseAssignment();
        }

        consume(Token.TokenType.DO,
                "Expected 'do' after for clauses.");

        // body
        List<Stmt> bodyStatements = new ArrayList<>();
        while (peek().type != Token.TokenType.END_ &&
                peek().type != Token.TokenType.END_OF_FILE) {
            bodyStatements.add(parseDeclaration(false));
        }

        consume(Token.TokenType.END_,
                "Expected 'end' after for block.");

        Stmt body = new Stmt.BlockStmt(bodyStatements);

        return new Stmt.ForStmt(
                initializer,
                condition,
                increment,
                body
        );
    }

    private Stmt parseForInStmt() throws YsharpError {
        consume(Token.TokenType.VAR, "Expected 'var' keyword after 'for' in for-in loop.");

        Token identifier = peek();
        if (identifier.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, identifier.line,
                    "Expected variable name after 'var' in for-in loop, but found: " + identifier.lexeme);
        }
        advance();

        Token typeName = null;
        if (match(peek(), Token.TokenType.COLON)) {
            Token typeToken = peek();

            if (typeToken.literal instanceof Token.Literal.Str strLit) {
                typeName = peek();
                advance();
            } else {
                throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, typeToken.line,
                        "Expected a valid type name after ':', but found: " + typeToken.lexeme);
            }
        }

        Stmt.VarDeclaration initializer = new Stmt.VarDeclaration(identifier, typeName, null, false);

        consume(Token.TokenType.IN, "Expected 'in' keyword after variable '" + identifier.lexeme + "'.");

        Expr iterable = parseAssignment();
        Stmt body = parseBlockStmt();

        return new Stmt.ForInStmt(initializer, iterable, body);
    }

    private Stmt parseBreakStmt() throws YsharpError {
        consume(Token.TokenType.SEMI_COLON,
                "Expected ';' after 'break'.");
        return new Stmt.BreakStmt();
    }

    private Stmt parseContinueStmt() throws YsharpError {
        consume(Token.TokenType.SEMI_COLON,
                "Expected ';' after 'continue'.");
        return new Stmt.ContinueStmt();
    }

    private Stmt parseReturnStmt() throws YsharpError {
        Expr expr = parseAssignment();

        consume(Token.TokenType.SEMI_COLON,
                "Expected ';' after 'return'.");

        return new Stmt.ReturnStmt(expr);
    }

    private Stmt parseSwitchStmt() throws YsharpError {

        Expr condition = parseAssignment();

        consume(Token.TokenType.DO,
                "Expected 'do' after switch expression.");

        List<Stmt.SwitchStmt.CaseClause> cases = new ArrayList<>();
        Stmt defaultClause = null;

        while (peek().type != Token.TokenType.END_ &&
                peek().type != Token.TokenType.END_OF_FILE) {

            if (match(peek(), Token.TokenType.CASE)) {

                Expr matchExpr = parseAssignment();

                consume(Token.TokenType.COLON,
                        "Expected ':' after case expression.");

                Stmt block = parseBlockStmt();

                cases.add(
                        new Stmt.SwitchStmt.CaseClause(matchExpr, block)
                );

            }
            else if (match(peek(), Token.TokenType.DEFAULT)) {

                if (defaultClause != null) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            peek().line,
                            "Multiple default clauses in switch."
                    );
                }

                consume(Token.TokenType.COLON,
                        "Expected ':' after default.");

                defaultClause = parseBlockStmt();
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected 'case', 'default' or 'end' inside switch."
                );
            }
        }

        consume(Token.TokenType.END_,
                "Expected 'end' after switch statement.");

        return new Stmt.SwitchStmt(condition, cases, defaultClause);
    }

    private Stmt parseThrowStmt() throws YsharpError {
      Token throwToken = previous();
      Expr expr = parseAssignment();

      consume(Token.TokenType.SEMI_COLON, "expected semi colon after expression");

      return  new Stmt.ThrowStmt(throwToken, expr);
    }

    private Stmt parseTryStmt() throws YsharpError {

        Token tryToken = previous();

        Stmt tryBlock = parseBlockStmt();

        consume(Token.TokenType.CATCH,
                "Expected 'catch' after try block.");

        consume(Token.TokenType.LEFT_PAREN,
                "Expected '(' after catch.");


        Token errIdentifier = advance();
        if(errIdentifier.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX,
                    errIdentifier.line,
                    "Expected identifier in catch clause.");
        }

        consume(Token.TokenType.RIGHT_PAREN,
                "Expected ')' after catch identifier.");

        Stmt catchBlock = parseBlockStmt();

        Stmt finallyBlock = null;

        if (match(peek(), Token.TokenType.FINALLY)) {

            finallyBlock = parseBlockStmt();
        }

        return new Stmt.TryStmt(
                tryToken,
                tryBlock,
                errIdentifier,
                catchBlock,
                finallyBlock
        );
    }

    // declaration parser

    private Stmt parseDeclaration(boolean isGlobalDeclaration) throws YsharpError {

        boolean flag = match(peek(), Token.TokenType.EXPORT);
        if(!isGlobalDeclaration && flag) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    peek().line,
                    "Export declarations are only allowed at the top level."
            );
        }

        if(match(peek(), Token.TokenType.VAR)) return parseVarDeclaration(flag);
        if(match(peek(), Token.TokenType.FUNCTION)) return parseFunctionDeclaration(flag);
        if(match(peek(), Token.TokenType.CONST_)) return parseConstDeclaration(flag);
        if(match(peek(), Token.TokenType.SEALED)) {
            consume(Token.TokenType.CLASS,
                    "Expected 'class' after 'sealed'.");
            return parseClassDeclaration(true, flag);
        }
        if(match(peek(), Token.TokenType.CLASS)) {
            return parseClassDeclaration(false, isGlobalDeclaration && flag);
        }
        return  parseStmt();
    }

    private Stmt parseVarDeclaration(boolean isExported) throws YsharpError {
        Token identifier = advance();
        Token typeTag = null;
        Expr initializer = null;

        if (identifier.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    identifier.line,
                    "Expected variable name after 'var'."
            );
        }

        if (match(peek(), Token.TokenType.COLON)) {
            if (peek().type == Token.TokenType.END_OF_FILE) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected type after ':'."
                );
            }
            typeTag = advance();
        }

        if (match(peek(), Token.TokenType.ASSIGN)) {
            if (peek().type == Token.TokenType.END_OF_FILE) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected initializer expression after '='."
                );
            }
            initializer = parseAssignment();
        }

        consume(Token.TokenType.SEMI_COLON, "Expected ';' after variable declaration.");

        return new Stmt.VarDeclaration(identifier, typeTag, initializer, isExported);
    }

    private Stmt parseFunctionDeclaration(boolean isExported) throws YsharpError {

        Token name = advance();
        if (name.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    name.line,
                    "Expected function name identifier."
            );
        }

        consume(Token.TokenType.LEFT_PAREN,
                "Expected '(' after function name.");

        List<Stmt.FunctionDeclaration.Param> params = new ArrayList<>();

        if (peek().type != Token.TokenType.RIGHT_PAREN) {

            do {

                Token paramName = advance();
                if (paramName.type != Token.TokenType.IDENTIFIER) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            paramName.line,
                            "Expected parameter name identifier."
                    );
                }

                Token typeToken = null;

                if (peek().type == Token.TokenType.COLON) {

                    advance(); // consume ':'

                    typeToken = advance();
                    if (typeToken.type != Token.TokenType.IDENTIFIER) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.SYNTAX,
                                typeToken.line,
                                "Expected type identifier after ':'."
                        );
                    }
                }

                params.add(
                        new Stmt.FunctionDeclaration.Param(paramName, typeToken)
                );

            } while (match(peek(), Token.TokenType.COMMA));
        }

        consume(Token.TokenType.RIGHT_PAREN,
                "Expected ')' after parameters.");

        Token returnType = null;

        if (peek().type == Token.TokenType.COLON) {

            advance(); // consume ':'

            returnType = advance();
            if (returnType.type != Token.TokenType.IDENTIFIER) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        returnType.line,
                        "Expected return type identifier after ':'."
                );
            }
        }

        Stmt body = parseBlockStmt();

        return new Stmt.FunctionDeclaration(name, params, returnType, body, isExported);
    }

    private Stmt parseConstDeclaration(boolean isExported) throws YsharpError {
        Token identifier = advance();
        Token typeTag = null;
        Expr initializer = null;

        if (identifier.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    identifier.line,
                    "Expected variable name after 'const'."
            );
        }

        if (match(peek(), Token.TokenType.COLON)) {
            if (peek().type == Token.TokenType.END_OF_FILE) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected type after ':'."
                );
            }
            typeTag = advance();
        }

        if(peek().type != Token.TokenType.ASSIGN) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    peek().line,
                    "Expected initializer expression after const declaration."
            );
        }
        advance(); // consume =
        initializer = parseAssignment();

        consume(Token.TokenType.SEMI_COLON, "Expected ';' after variable declaration.");

        return new Stmt.ConstDeclaration(identifier, typeTag, initializer, isExported);
    }

    private Stmt parseClassDeclaration(boolean isSealed, boolean isExported) throws YsharpError {

        Token name = advance();
        if (name.type != Token.TokenType.IDENTIFIER) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    name.line,
                    "Expected class name after 'class'."
            );
        }

        Token extend = null;

        if (match(peek(), Token.TokenType.EXTENDS)) {
            extend = advance();
            if (extend.type != Token.TokenType.IDENTIFIER) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        extend.line,
                        "Expected superclass name after 'extends'."
                );
            }
        }

        consume(Token.TokenType.LEFT_CURLY_BRACE,
                "Expected '{' after class declaration.");

        List<Stmt.ClassDeclaration.Method> methods = new ArrayList<>();
        List<Stmt.ClassDeclaration.Property> properties = new ArrayList<>();

        while (peek().type != Token.TokenType.RIGHT_CURLY_BRACE &&
                peek().type != Token.TokenType.END_OF_FILE) {

            boolean isStatic = match(peek(), Token.TokenType.STATIC);

            // constructor
            if (peek().type == Token.TokenType.CONSTRUCTOR ) {
                Token ctorToken = advance(); // consume constructor
                consume(Token.TokenType.LEFT_PAREN,
                        "Expected '(' after constructor.");
                if (isStatic) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SYNTAX,
                            ctorToken.line,
                            "Constructor cannot be static."
                    );
                }

                List<Stmt.ClassDeclaration.Method.Param> params = new ArrayList<>();

                if (peek().type != Token.TokenType.RIGHT_PAREN) {

                    do {
                        Token paramName = advance();

                        if (paramName.type != Token.TokenType.IDENTIFIER) {
                            throw new YsharpError(
                                    YsharpError.YsharpErrorType.SYNTAX,
                                    paramName.line,
                                    "Expected parameter name."
                            );
                        }

                        Token typeToken = null;

                        if (match(peek(), Token.TokenType.COLON)) {

                            typeToken = advance();

                            if (typeToken.type != Token.TokenType.IDENTIFIER) {
                                throw new YsharpError(
                                        YsharpError.YsharpErrorType.SYNTAX,
                                        typeToken.line,
                                        "Expected type identifier after ':'."
                                );
                            }
                        }

                        params.add(
                                new Stmt.ClassDeclaration.Method.Param(
                                        paramName,
                                        typeToken
                                )
                        );

                    } while (match(peek(), Token.TokenType.COMMA));
                }

                consume(Token.TokenType.RIGHT_PAREN,
                        "Expected ')' after parameters.");

                Token returnType = null;

                if (match(peek(), Token.TokenType.COLON)) {
                    returnType = advance();

                    if (returnType.type != Token.TokenType.IDENTIFIER) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.SYNTAX,
                                returnType.line,
                                "Expected return type identifier."
                        );
                    }
                }

                Stmt body = parseBlockStmt();

                methods.add(
                        new Stmt.ClassDeclaration.Method(
                                ctorToken,
                                params,
                                returnType,
                                body,
                                isStatic,
                                false
                        )
                );


            }

                // method
            else if (peek().type == Token.TokenType.IDENTIFIER &&
                    peekNext().type == Token.TokenType.LEFT_PAREN) {

                // method name
                Token methodName = advance(); // IDENTIFIER

                consume(Token.TokenType.LEFT_PAREN,
                        "Expected '(' after method name.");

                List<Stmt.ClassDeclaration.Method.Param> params = new ArrayList<>();

                if (peek().type != Token.TokenType.RIGHT_PAREN) {

                    do {
                        Token paramName = advance();

                        if (paramName.type != Token.TokenType.IDENTIFIER) {
                            throw new YsharpError(
                                    YsharpError.YsharpErrorType.SYNTAX,
                                    paramName.line,
                                    "Expected parameter name."
                            );
                        }

                        Token typeToken = null;

                        if (match(peek(), Token.TokenType.COLON)) {

                            typeToken = advance();

                            if (typeToken.type != Token.TokenType.IDENTIFIER) {
                                throw new YsharpError(
                                        YsharpError.YsharpErrorType.SYNTAX,
                                        typeToken.line,
                                        "Expected type identifier after ':'."
                                );
                            }
                        }

                        params.add(
                                new Stmt.ClassDeclaration.Method.Param(
                                        paramName,
                                        typeToken
                                )
                        );

                    } while (match(peek(), Token.TokenType.COMMA));
                }

                consume(Token.TokenType.RIGHT_PAREN,
                        "Expected ')' after parameters.");

                Token returnType = null;

                if (match(peek(), Token.TokenType.COLON)) {
                    returnType = advance();

                    if (returnType.type != Token.TokenType.IDENTIFIER) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.SYNTAX,
                                returnType.line,
                                "Expected return type identifier."
                        );
                    }
                }

                Stmt body = parseBlockStmt();

                methods.add(
                        new Stmt.ClassDeclaration.Method(
                                methodName,
                                params,
                                returnType,
                                body,
                                isStatic,
                                false
                        )
                );
            }

            // var property
            else if (match(peek(), Token.TokenType.VAR)) {

                Stmt.VarDeclaration varDecl =
                        (Stmt.VarDeclaration) parseVarDeclaration(false);

                properties.add(
                        new Stmt.ClassDeclaration.Property(
                                varDecl.identifier,
                                varDecl.type,
                                varDecl.initializer,
                                false,
                                isStatic
                        )
                );
            }

            // const property
            else if (match(peek(), Token.TokenType.CONST_)) {

                Stmt.ConstDeclaration constDecl =
                        (Stmt.ConstDeclaration) parseConstDeclaration(false);

                properties.add(
                        new Stmt.ClassDeclaration.Property(
                                constDecl.identifier,
                                constDecl.type,
                                constDecl.initializer,
                                true,
                                isStatic
                        )
                );
            }

            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        peek().line,
                        "Expected class member (function, var, const)."
                );
            }
        }

        consume(Token.TokenType.RIGHT_CURLY_BRACE,
                "Expected '}' after class body.");

        return new Stmt.ClassDeclaration(
                name,
                extend,
                methods,
                properties,
                isSealed,
                isExported
        );
    }

    private List<String> parseUseDeclaration() throws YsharpError {
        ArrayList<String> useDeclarations = new ArrayList<>();
         while (match(peek() , Token.TokenType.USE)) {
            Token modulePath = advance();
            if(modulePath.type != Token.TokenType.STRING) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        modulePath.line,
                        "Expected module path string after 'use'."
                );
            }
             Token.Literal.Str str = (Token.Literal.Str) modulePath.literal;
             useDeclarations.add(str.value());

             consume(Token.TokenType.SEMI_COLON, "expected semi colon after use statement");
         }
        return useDeclarations;
    }

}

