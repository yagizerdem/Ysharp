package ysharp.parser;

import ysharp.YsharpError;
import ysharp.lexer.Token;

import javax.print.DocFlavor;
import javax.swing.*;
import java.security.CryptoPrimitive;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.RecursiveTask;

public class Parser {

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

        while (peek().type != Token.TokenType.END_OF_FILE) {

            if (previous().type == Token.TokenType.SEMI_COLON) {
                return;
            }

            switch (peek().type) {
                case VAR:
                case CONST_:
                case IF:
                case ELIF:
                case ELSE:
                case TRY:
                case CATCH:
                case FINALLY:
                case USE:
                case WHILE:
                case FOR:
                case RETURN:
                case SWITCH:
                case BREAK:
                case CONTINUE:
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

    public List<Stmt> parse() {
        List<Stmt> list = new ArrayList<>();
        while (peek().type != Token.TokenType.END_OF_FILE) {
            try {
                list.add(parseDeclaration());
            }catch (YsharpError err) {
                errors.add(err);
                sync();
            }
        }

        return list;
    }


    // expression parser

    private Expr parseAssignment() throws YsharpError{
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

//            if (expr instanceof GetExpr) {
//                GetExpr get = (GetExpr) expr;
//                return new SetExpr(get.object, get.name, value);
//            }
//

            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    op.line,
                    "Invalid assignment target." );
        }

        return expr;
    }

    private Expr parseTernary() throws YsharpError {
        Expr logicalOr = parseLogicalOr();

        // consume ?
        if(match(peek(), Token.TokenType.QUESTION_MARK)) {
            Expr then = parseAssignment();
            // consume :
            consume(Token.TokenType.COLON,
                    "Missing ':' in conditional operator.");

            Expr else_ = parseTernary();

            return new Expr.TernaryExpr(
                    logicalOr, // condition
                    then,
                    else_
            );
        }

        return  logicalOr;
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
                        args
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

    private Expr parsePrimary() throws YsharpError {
        if(match(peek(), Token.TokenType.LEFT_BRACKET)) {
            return parseArrayInitializer();
        }
        else if(match(peek(), Token.TokenType.LEFT_CURLY_BRACE)) {
            return parseMapInitializer();
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

            return new Expr.MapInitializerExpr(entries);
        }

        consume(Token.TokenType.RIGHT_CURLY_BRACE,
                "Expected '}' after map initializer.");
        return new Expr.MapInitializerExpr(entries);
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

    // stmt parser

    private Stmt parseStmt() throws YsharpError {

        if(match(peek(), Token.TokenType.PRINT)) return parsePrint();
        if(match(peek(), Token.TokenType.PRINTLN)) return parsePrintln();
        if(peek().type == Token.TokenType.DO) return parseBlockStmt();
        if(match(peek(), Token.TokenType.IF)) return parseIfStmt();
        if(match(peek(), Token.TokenType.WHILE)) return parseWhileStmt();
        if(match(peek(), Token.TokenType.FOR)) return parseForStmt();
        if(match(peek(), Token.TokenType.BREAK)) return parseBreakStmt();
        if(match(peek(), Token.TokenType.CONTINUE)) return parseContinueStmt();
        if(match(peek(), Token.TokenType.RETURN)) return parseReturnStmt();
        if(match(peek(), Token.TokenType.SWITCH)) return parseSwitchStmt();
        if(match(peek(), Token.TokenType.FUNCTION)) return parseFunctionDeclaration();

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
            stmtList.add(parseDeclaration());
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
            initializer = parseVarDeclaration();
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
            bodyStatements.add(parseDeclaration());
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

    // declaration parser

    private Stmt parseDeclaration() throws YsharpError {
        if(match(peek(), Token.TokenType.VAR)) return parseVarDeclaration();
        if(match(peek(), Token.TokenType.CONST_)) return parseConstDeclaration();
        return  parseStmt();
    }

    private Stmt parseVarDeclaration() throws YsharpError {
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

        return new Stmt.VarDeclaration(identifier, typeTag, initializer);
    }

    private Stmt parseFunctionDeclaration() throws YsharpError {

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

        return new Stmt.FunctionDeclaration(name, params, returnType, body);
    }

    private Stmt parseConstDeclaration() throws YsharpError {
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

        return new Stmt.VarDeclaration(identifier, typeTag, initializer);
    }
}

