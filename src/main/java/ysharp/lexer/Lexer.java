package ysharp.lexer;

import ysharp.YsharpError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final List<Cursor.Pchar> source;
    private final List<Token> tokens = new ArrayList<>();
    private int start   = 0;
    private final Cursor.CursorState cursor = new Cursor.CursorState();
    private int line    = 1;

    public Lexer(List<Cursor.Pchar> source) {
        this.source = source;
        this.cursor.current = 0;
    }


    public static final Map<String, Token.TokenType> KEYWORD_MAP = new HashMap<>();

    static {
        KEYWORD_MAP.put("for", Token.TokenType.FOR);
        KEYWORD_MAP.put("while", Token.TokenType.WHILE);
        KEYWORD_MAP.put("extends", Token.TokenType.EXTENDS);
        KEYWORD_MAP.put("try", Token.TokenType.TRY);
        KEYWORD_MAP.put("catch", Token.TokenType.CATCH);
        KEYWORD_MAP.put("finally", Token.TokenType.FINALLY);
        KEYWORD_MAP.put("do", Token.TokenType.DO);
        KEYWORD_MAP.put("end", Token.TokenType.END_);
        KEYWORD_MAP.put("if", Token.TokenType.IF);
        KEYWORD_MAP.put("elif", Token.TokenType.ELIF);
        KEYWORD_MAP.put("else", Token.TokenType.ELSE);
        KEYWORD_MAP.put("switch", Token.TokenType.SWITCH);
        KEYWORD_MAP.put("case", Token.TokenType.CASE);
        KEYWORD_MAP.put("default", Token.TokenType.DEFAULT);
        KEYWORD_MAP.put("then", Token.TokenType.THEN);
        KEYWORD_MAP.put("return", Token.TokenType.RETURN);
        KEYWORD_MAP.put("function", Token.TokenType.FUNCTION);
        KEYWORD_MAP.put("class", Token.TokenType.CLASS);
        KEYWORD_MAP.put("break", Token.TokenType.BREAK);
        KEYWORD_MAP.put("continue", Token.TokenType.CONTINUE);
        KEYWORD_MAP.put("var", Token.TokenType.VAR);
        KEYWORD_MAP.put("const", Token.TokenType.CONST_);
        KEYWORD_MAP.put("print", Token.TokenType.PRINT);
        KEYWORD_MAP.put("println", Token.TokenType.PRINTLN);
        KEYWORD_MAP.put("use", Token.TokenType.USE);

        // type keywords
        KEYWORD_MAP.put("int", Token.TokenType.TYPE_INT);
        KEYWORD_MAP.put("double", Token.TokenType.TYPE_DOUBLE);
        KEYWORD_MAP.put("string", Token.TokenType.TYPE_STRING);
        KEYWORD_MAP.put("bool", Token.TokenType.TYPE_BOOL);
        KEYWORD_MAP.put("fun", Token.TokenType.TYPE_FUN);
        KEYWORD_MAP.put("char", Token.TokenType.TYPE_CHAR);

        // literal keywords
        KEYWORD_MAP.put("true", Token.TokenType.TRUE_);
        KEYWORD_MAP.put("false", Token.TokenType.FALSE_);
        KEYWORD_MAP.put("null", Token.TokenType.NULL_);
    }

    private static boolean isKeyword(String word) {
        return KEYWORD_MAP.containsKey(word);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAtEnd() {
        return cursor.current >= source.size();
    }

    // Pchar overloads
    private boolean isAlpha(Cursor.Pchar pc)        { return isAlpha(pc.c); }
    private boolean isDigit(Cursor.Pchar pc)        { return isDigit(pc.c); }
    private boolean isAlphaNumeric(Cursor.Pchar pc) { return isAlphaNumeric(pc.c); }

    private void addToken(Token.TokenType type) {
        addToken(type, new Token.Literal.Null());
    }

    private void addToken(Token.TokenType type, Token.Literal literal) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < cursor.current; i++) {
            sb.append(source.get(i).c);
        }
        tokens.add(new Token(type, sb.toString(), literal, line));
    }


    public static void printTokenStream(List<Token> stream) {
        for (Token token : stream) {
            System.out.println(token.toString());
        }
    }

    public List<Token> scanTokens() throws Exception {
        while (!isAtEnd()) {
            start = cursor.current;
            scanToken();
        }
        start = cursor.current;
        addToken(Token.TokenType.END_OF_FILE);
        return tokens;
    }

    private void collectNumber() throws YsharpError {
        while (isDigit(Cursor.peek(source, cursor.current))) Cursor.advance(source, cursor);

        boolean isDouble = false;

        if (Cursor.stopSet(Cursor.peek(source, cursor.current), Cursor.CharMask.Dot)
                && isDigit(Cursor.peekNext(source, cursor.current))) {
            isDouble = true;
            Cursor.advance(source, cursor); // consume '.'
            while (isDigit(Cursor.peek(source, cursor.current))) Cursor.advance(source, cursor);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < cursor.current; i++) {
            sb.append(source.get(i).c);
        }
        String sub = sb.toString();

        if (!isDouble) {
            addToken(Token.TokenType.INT, new Token.Literal.Int(Integer.parseInt(sub)));
        } else {
            addToken(Token.TokenType.DOUBLE, new Token.Literal.Double(Double.parseDouble(sub)));
        }
    }

    private void collectIdentifier() throws YsharpError {
        while (Cursor.peekChar(source, cursor.current) != Cursor.END) {
            if (Cursor.stopSet(Cursor.peekChar(source, cursor.current), Cursor.CharMask.Blank)) break;
            if (Cursor.stopSet(Cursor.peekChar(source, cursor.current), Cursor.CharMask.DoubleQuote)
                    && !Cursor.isEscapedBackslash(source, cursor.current)) break;
            if (!isAlphaNumeric(Cursor.peekChar(source, cursor.current))) break;
            Cursor.advance(source, cursor);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < cursor.current; i++) {
            sb.append(source.get(i).c);
        }
        String identifier = sb.toString();

        // normalize backslash
        StringBuilder normalized = new StringBuilder();
        int[] idx = { 0 };
        while (idx[0] < identifier.length()) {
            char ch = identifier.charAt(idx[0]);
            if (Cursor.stopSet(ch, Cursor.CharMask.Escape)) {
                if (Cursor.isEscapedBackslash(identifier, idx[0])) {
                    normalized.append(ch);
                }
            } else {
                normalized.append(ch);
            }
            idx[0]++;
        }

        String norm = normalized.toString();
        Token token = new Token();

        if (KEYWORD_MAP.containsKey(norm)) {
            Token.TokenType type = KEYWORD_MAP.get(norm);
            Token.Literal literal = new Token.Literal.Null();
            if (type == Token.TokenType.TRUE_)  literal = new Token.Literal.Bool(true);
            if (type == Token.TokenType.FALSE_) literal = new Token.Literal.Bool(false);
            token = new Token(type, norm, literal, this.line);
        } else {
            token = new Token(Token.TokenType.IDENTIFIER, norm, new Token.Literal.Null(), this.line);
        }

        this.tokens.add(token);
    }

    private void collectString() throws YsharpError {
        boolean terminated = false;

        while (Cursor.peekChar(source, cursor.current) != Cursor.END) {
            if (Cursor.stopSet(Cursor.peekChar(source, cursor.current), Cursor.CharMask.DoubleQuote)
                    && !Cursor.isEscapedBackslash(source, cursor.current)) {
                Cursor.advance(source, cursor); // consume closing "
                terminated = true;
                break;
            }
            Cursor.advance(source, cursor);
        }

        if (!terminated) {
            throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, this.line, "Unterminated string literal");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < cursor.current; i++) {
            sb.append(source.get(i).c);
        }
        String sub = sb.toString();

        Token token = new Token(
                Token.TokenType.STRING,
                sub,
                new Token.Literal.Str(sub.substring(1, sub.length() - 1)),
                this.line
        );
        tokens.add(token);
    }

    private void collectChar() throws YsharpError {
        boolean terminated = false;

        while (Cursor.peekChar(source, cursor.current) != Cursor.END) {
            if (Cursor.stopSet(Cursor.peekChar(source, cursor.current), Cursor.CharMask.SingleQuote)
                    && !Cursor.isEscapedBackslash(source, cursor.current)) {
                Cursor.advance(source, cursor); // consume closing "
                terminated = true;
                break;
            }
            Cursor.advance(source, cursor);
        }

        if (!terminated) {
            throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, this.line, "Unterminated char literal");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < cursor.current; i++) {
            sb.append(source.get(i).c);
        }
        String sub = sb.toString();

        if(sb.length() != 3) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SYNTAX,
                    this.line,
                    "Invalid char literal: a character literal must contain exactly one character."
            );
        }

        Token token = new Token(
                Token.TokenType.CHAR,
                sub,
                new Token.Literal.Chr(sub.substring(1, sub.length() - 1).charAt(0)),
                this.line
        );
        tokens.add(token);

    }

    private void scanToken() throws Exception {
        Cursor.Pchar pc = Cursor.advance(source, cursor);
        line = pc.loc.line;
        char c = pc.c;

        switch (c) {
            // single char tokens
            case '(' -> addToken(Token.TokenType.LEFT_PAREN);
            case ')' -> addToken(Token.TokenType.RIGHT_PAREN);
            case '[' -> addToken(Token.TokenType.LEFT_BRACKET);
            case ']' -> addToken(Token.TokenType.RIGHT_BRACKET);
            case '{' -> addToken(Token.TokenType.LEFT_CURLY_BRACE);
            case '}' -> addToken(Token.TokenType.RIGHT_CURLY_BRACE);
            case ',' -> addToken(Token.TokenType.COMMA);
            case '.' -> addToken(Token.TokenType.DOT);
            case ':' -> addToken(Token.TokenType.COLON);
            case '?' -> addToken(Token.TokenType.QUESTION_MARK);
            case ';' -> addToken(Token.TokenType.SEMI_COLON);
            case '~' -> addToken(Token.TokenType.BITWISE_NOT);

            // two char tokens
            case '+' -> {
                if (Cursor.match(source, cursor, '+')) { addToken(Token.TokenType.PLUS_PLUS);   return; }
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.PLUS_ASSIGN); return; }
                addToken(Token.TokenType.PLUS);
            }
            case '-' -> {
                if (Cursor.match(source, cursor, '-')) { addToken(Token.TokenType.MINUS_MINUS);   return; }
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.MINUS_ASSIGN);  return; }
                addToken(Token.TokenType.MINUS);
            }
            case '*' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.MULTIPLY_ASSIGN); return; }
                addToken(Token.TokenType.MULTIPLY);
            }
            case '/' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.DIVIDE_ASSIGN); return; }
                addToken(Token.TokenType.DIVIDE);
            }
            case '%' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.MODULO_ASSIGN); return; }
                addToken(Token.TokenType.MODULO);
            }
            case '&' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.BITWISE_AND_ASSIGN); return; }
                if (Cursor.match(source, cursor, '&')) { addToken(Token.TokenType.LOGICAL_AND);        return; }
                addToken(Token.TokenType.BITWISE_AND);
            }
            case '|' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.BITWISE_OR_ASSIGN); return; }
                if (Cursor.match(source, cursor, '|')) { addToken(Token.TokenType.LOGICAL_OR);        return; }
                addToken(Token.TokenType.BITWISE_OR);
            }
            case '^' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.BITWISE_XOR_ASSIGN); return; }
                addToken(Token.TokenType.BITWISE_XOR);
            }
            case '=' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.EQUAL_EQUAL); return; }
                addToken(Token.TokenType.ASSIGN);
            }
            case '!' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.BANG_EQUAL); return; }
                addToken(Token.TokenType.BANG);
            }

            // three char tokens
            case '<' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.LESS_OR_EQUAL); return; }
                if (Cursor.match(source, cursor, '<')) {
                    if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.LEFT_SHIFT_ASSIGN); return; }
                    addToken(Token.TokenType.LEFT_SHIFT); return;
                }
                addToken(Token.TokenType.LESS_THAN);
            }
            case '>' -> {
                if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.GREATER_OR_EQUAL); return; }
                if (Cursor.match(source, cursor, '>')) {
                    if (Cursor.match(source, cursor, '=')) { addToken(Token.TokenType.RIGHT_SHIFT_ASSIGN); return; }
                    addToken(Token.TokenType.RIGHT_SHIFT); return;
                }
                addToken(Token.TokenType.GREATER_THAN);
            }

            default -> {
                if (Cursor.isSpace(c)) {
                    Cursor.consumeSpace(source, cursor);
                } else if (isAlpha(c)) {
                    collectIdentifier();
                } else if (isDigit(c)) {
                    collectNumber();
                } else if (Cursor.stopSet(c, Cursor.CharMask.DoubleQuote)) {
                    int charIdx = Math.max(cursor.current - 1, 0);
                    if (!Cursor.isEscapedBackslash(source, charIdx)) {
                        collectString();
                    } else {
                        collectIdentifier();
                    }
                }
                else if(Cursor.stopSet(c, Cursor.CharMask.SingleQuote)) {
                    int charIdx = Math.max(cursor.current - 1, 0);
                    if (!Cursor.isEscapedBackslash(source, charIdx)) {
                        collectChar();
                    } else {
                        collectIdentifier();
                    }
                }
                else if (Cursor.stopSet(c, Cursor.CharMask.Escape)) {
                    int charIdx = Math.max(cursor.current - 1, 0);
                    if (Cursor.isEscapedBackslash(source, charIdx)) {
                        collectIdentifier();
                    }
                    // consume unnecessary escape characters
                } else {
                    throw new YsharpError(YsharpError.YsharpErrorType.SYNTAX, this.line, "Unsupported character");
                }
            }
        }
    }

}
