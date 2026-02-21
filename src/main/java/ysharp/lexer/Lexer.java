package ysharp.lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

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

        // literal keywords
        KEYWORD_MAP.put("true", Token.TokenType.TRUE_);
        KEYWORD_MAP.put("false", Token.TokenType.FALSE_);
        KEYWORD_MAP.put("null", Token.TokenType.NULL_);
    }

    public static boolean isKeyword(String word) {
        return KEYWORD_MAP.containsKey(word);
    }

    public static void printTokenStream(List<Token> stream) {
        for (Token token : stream) {
            System.out.println(token.toString());
        }
    }

}
