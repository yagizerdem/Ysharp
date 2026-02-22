package ysharp.parser;

import ysharp.YsharpError;
import ysharp.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {

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
        var token = tokenStream.get(current + 1);
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
    }

    private void sync(){

    }

    public List<Expr> parse() {
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

                factor = parseFactor();
                Expr.BinaryExpr binaryExpr_ = new Expr.BinaryExpr(
                        binaryExpr,
                        previous(),
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
}

