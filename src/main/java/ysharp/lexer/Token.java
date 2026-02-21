package ysharp.lexer;

public class Token {

    public sealed interface Literal permits Literal.Null, Literal.Int,
            Literal.Double, Literal.Bool,
            Literal.Str, Literal.Chr  {
        record Null()              implements Literal {}
        record Int(int value)      implements Literal {}
        record Double(double value)implements Literal {}
        record Bool(boolean value) implements Literal {}
        record Str(String value)   implements Literal {}
        record Chr(char value)   implements Literal {}
    }


    public enum TokenType {
        // basic arithmetic operators
        PLUS, MINUS, MULTIPLY, DIVIDE, MODULO, // + - * / %

        // logic
        LOGICAL_AND, LOGICAL_OR, // && ||

        // bitwise
        BITWISE_AND, BITWISE_OR, BITWISE_XOR, // & | ^
        LEFT_SHIFT, RIGHT_SHIFT, BITWISE_NOT, // >> << ~

        // unary
        BANG, // !

        // unary postfix / prefix
        PLUS_PLUS, // ++
        MINUS_MINUS, // --

        // other
        QUESTION_MARK, // ?
        COLON, // ;
        SEMI_COLON, // ;
        COMMA, // ,
        DOT, // .

        // equality / comparison
        EQUAL_EQUAL, BANG_EQUAL, // == !=
        GREATER_THAN, LESS_THAN, GREATER_OR_EQUAL, LESS_OR_EQUAL, // > < >= <=

        // assignment
        ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, DIVIDE_ASSIGN, MULTIPLY_ASSIGN, // + += -= /= *=
        MODULO_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_ASSIGN, // %= <<= >>=
        BITWISE_AND_ASSIGN, BITWISE_OR_ASSIGN, BITWISE_XOR_ASSIGN, // &= |= ^=

        // literals
        IDENTIFIER, INT, DOUBLE, STRING,
        TRUE_, FALSE_, NULL_,

        // grouping
        LEFT_PAREN, RIGHT_PAREN, LEFT_BRACKET, // ( ) [
        RIGHT_BRACKET, LEFT_CURLY_BRACE, RIGHT_CURLY_BRACE, // ] { }

        END_OF_FILE,

        // reserved keywords
        FOR, // for
        WHILE, // while
        DO, // do
        END_, // end
        IF, // if
        ELIF, // elif
        ELSE, // else
        SWITCH, // switch
        CASE, // case
        DEFAULT, // default
        THEN, // then
        RETURN, // return
        FUNCTION, // function
        CLASS, // class
        EXTENDS, // extends
        BREAK, // break
        CONTINUE, // continue
        VAR, // var
        CONST_, // const
        TRY, // try
        CATCH, // catch
        FINALLY, // finally
        PRINT, // print
        PRINTLN, // println
        USE, // use

        // type declaration keywords
        TYPE_INT, // int
        TYPE_DOUBLE, // double
        TYPE_STRING, // string
        TYPE_BOOL, // bool
        TYPE_FUN, // fun
    }


    public final TokenType type;
    public final String    lexeme;
    public final Literal   literal;
    public final int       line;


    public Token(TokenType type, String lexeme, Literal literal, int line) {
        this.type    = type;
        this.lexeme  = lexeme;
        this.literal = literal;
        this.line    = line;
    }

    public Token() {
        this(null, "", new Literal.Null(), 0);
    }


    @Override
    public String toString() {
        return type.ordinal() + " " + lexeme + " " + literalToString();
    }

    private String literalToString() {
        return switch (literal) {
            case Literal.Null   ignored -> "null";
            case Literal.Int    l       -> Integer.toString(l.value());
            case Literal.Double l       -> Double.toString(l.value());
            case Literal.Bool   l       -> l.value() ? "true" : "false";
            case Literal.Str    l       -> l.value();
            case Literal.Chr    l       -> Character.toString(l.value());
        };
    }
}