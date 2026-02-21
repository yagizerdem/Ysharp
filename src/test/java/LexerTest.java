import org.junit.jupiter.api.Test;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.lexer.Token;

import static org.junit.jupiter.api.Assertions.*;

class TokenStreamTest {


    private java.util.List<Token> lex(String input) throws Exception {
        var buf = Preprocess.mergeContinuation(input);
        buf = Preprocess.removeComments(buf);
        return new Lexer(buf).scanTokens();
    }


    @Test
    void escapedIncrement() throws Exception {
        var tokens = lex("\t++ ");
        assertEquals(2, tokens.size());
        assertEquals("++", tokens.get(0).lexeme);
        assertEquals(Token.TokenType.PLUS_PLUS, tokens.get(0).type);
        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(1).type);
    }

    @Test
    void escapeEdgeCase() throws Exception {
        var tokens = lex("\\first_name\\");
        assertEquals(2, tokens.size());
        assertEquals("first_name", tokens.get(0).lexeme);
    }

    @Test
    void escapedIdentifier() throws Exception {
        var tokens = lex("var first_name : string = \"yagiz erdem\"");
        assertEquals(7, tokens.size());

        assertEquals(Token.TokenType.VAR, tokens.get(0).type);
        assertEquals("var", tokens.get(0).lexeme);

        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals("first_name", tokens.get(1).lexeme);

        assertEquals(Token.TokenType.COLON, tokens.get(2).type);
        assertEquals(":", tokens.get(2).lexeme);

        assertEquals(Token.TokenType.TYPE_STRING, tokens.get(3).type);
        assertEquals("string", tokens.get(3).lexeme);

        assertEquals(Token.TokenType.ASSIGN, tokens.get(4).type);
        assertEquals("=", tokens.get(4).lexeme);

        if (tokens.get(5).literal instanceof Token.Literal.Str str) {
            assertEquals(Token.TokenType.STRING, tokens.get(5).type);
            assertEquals("yagiz erdem", str.value());
        } else {
            fail("token at index 5 should be string literal \"yagiz erdem\"");
        }
    }

    @Test
    void maximalMunchOperators() throws Exception {
        var tokens = lex("+ ++ += - -- -= = == ! != < <= << <<= > >= >> >>=");
        assertEquals(19, tokens.size()); // 18 operators + EOF
        assertEquals(Token.TokenType.PLUS,               tokens.get(0).type);
        assertEquals(Token.TokenType.PLUS_PLUS,          tokens.get(1).type);
        assertEquals(Token.TokenType.PLUS_ASSIGN,        tokens.get(2).type);
        assertEquals(Token.TokenType.MINUS,              tokens.get(3).type);
        assertEquals(Token.TokenType.MINUS_MINUS,        tokens.get(4).type);
        assertEquals(Token.TokenType.MINUS_ASSIGN,       tokens.get(5).type);
        assertEquals(Token.TokenType.ASSIGN,             tokens.get(6).type);
        assertEquals(Token.TokenType.EQUAL_EQUAL,        tokens.get(7).type);
        assertEquals(Token.TokenType.BANG,               tokens.get(8).type);
        assertEquals(Token.TokenType.BANG_EQUAL,         tokens.get(9).type);
        assertEquals(Token.TokenType.LESS_THAN,          tokens.get(10).type);
        assertEquals(Token.TokenType.LESS_OR_EQUAL,      tokens.get(11).type);
        assertEquals(Token.TokenType.LEFT_SHIFT,         tokens.get(12).type);
        assertEquals(Token.TokenType.LEFT_SHIFT_ASSIGN,  tokens.get(13).type);
        assertEquals(Token.TokenType.GREATER_THAN,       tokens.get(14).type);
        assertEquals(Token.TokenType.GREATER_OR_EQUAL,   tokens.get(15).type);
        assertEquals(Token.TokenType.RIGHT_SHIFT,        tokens.get(16).type);
        assertEquals(Token.TokenType.RIGHT_SHIFT_ASSIGN, tokens.get(17).type);
    }

    @Test
    void bitwiseOperatorCombinations() throws Exception {
        var tokens = lex("& && &= | || |= ^ ^= ~ << <<= >> >>=");
        assertEquals(14, tokens.size()); // 13 operators + EOF
        assertEquals(Token.TokenType.BITWISE_AND,        tokens.get(0).type);
        assertEquals(Token.TokenType.LOGICAL_AND,        tokens.get(1).type);
        assertEquals(Token.TokenType.BITWISE_AND_ASSIGN, tokens.get(2).type);
        assertEquals(Token.TokenType.BITWISE_OR,         tokens.get(3).type);
        assertEquals(Token.TokenType.LOGICAL_OR,         tokens.get(4).type);
        assertEquals(Token.TokenType.BITWISE_OR_ASSIGN,  tokens.get(5).type);
        assertEquals(Token.TokenType.BITWISE_XOR,        tokens.get(6).type);
        assertEquals(Token.TokenType.BITWISE_XOR_ASSIGN, tokens.get(7).type);
        assertEquals(Token.TokenType.BITWISE_NOT,        tokens.get(8).type);
    }

    @Test
    void integerEdgeCases() throws Exception {
        var tokens = lex("0 123 999999999");
        assertEquals(4, tokens.size()); // 3 ints + EOF
        assertEquals(Token.TokenType.INT, tokens.get(0).type);
        assertEquals(0,         ((Token.Literal.Int) tokens.get(0).literal).value());
        assertEquals(Token.TokenType.INT, tokens.get(1).type);
        assertEquals(123,       ((Token.Literal.Int) tokens.get(1).literal).value());
        assertEquals(Token.TokenType.INT, tokens.get(2).type);
        assertEquals(999999999, ((Token.Literal.Int) tokens.get(2).literal).value());
    }

    @Test
    void doubleEdgeCases() throws Exception {
        var tokens = lex("0.0 123.456 .5 0. 1.0e10 1.5e-5");
        assertEquals(Token.TokenType.DOUBLE, tokens.get(0).type);
        assertEquals(0.0,    ((Token.Literal.Double) tokens.get(0).literal).value(), 1e-10);
        assertEquals(Token.TokenType.DOUBLE, tokens.get(1).type);
        assertEquals(123.456,((Token.Literal.Double) tokens.get(1).literal).value(), 1e-10);
    }

    @Test
    void numberOperatorAmbiguity() throws Exception {
        var tokens = lex("5-3");
        assertEquals(4, tokens.size()); // 5 - 3 EOF
        assertEquals(Token.TokenType.INT,   tokens.get(0).type);
        assertEquals(Token.TokenType.MINUS, tokens.get(1).type);
        assertEquals(Token.TokenType.INT,   tokens.get(2).type);
    }

    @Test
    void dotAmbiguity() throws Exception {
        var tokens = lex("obj.method 3.14 .5");
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(0).type);
        assertEquals(Token.TokenType.DOT,        tokens.get(1).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(2).type);
        assertEquals(Token.TokenType.DOUBLE,     tokens.get(3).type);
    }

    @Test
    void emptyString() throws Exception {
        var tokens = lex("\"\"");
        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.STRING, tokens.get(0).type);
        assertEquals("", ((Token.Literal.Str) tokens.get(0).literal).value());
    }

    @Test
    void stringWithEscapes() throws Exception {
        var tokens = lex("\"hello\\nworld\\t\\\"quote\\\"\"");
        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.STRING, tokens.get(0).type);
        // escape sequences are handled by the lexer
    }

    @Test
    void stringWithSpecialChars() throws Exception {
        var tokens = lex("\"!@#$%^&*(){}[]<>?\"");
        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.STRING, tokens.get(0).type);
        assertEquals("!@#$%^&*(){}[]<>?", ((Token.Literal.Str) tokens.get(0).literal).value());
    }

    @Test
    void multipleStringsInLine() throws Exception {
        var tokens = lex("\"first\" + \"second\"");
        assertEquals(4, tokens.size()); // "first" + "second" EOF
        assertEquals(Token.TokenType.STRING, tokens.get(0).type);
        assertEquals(Token.TokenType.PLUS,   tokens.get(1).type);
        assertEquals(Token.TokenType.STRING, tokens.get(2).type);
    }

    @Test
    void keywordsVsIdentifiers() throws Exception {
        var tokens = lex("for forloop foreach variable var");
        assertEquals(6, tokens.size()); // 5 tokens + EOF
        assertEquals(Token.TokenType.FOR,        tokens.get(0).type);
        assertEquals("for",                      tokens.get(0).lexeme);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals("forloop",                  tokens.get(1).lexeme);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(2).type);
        assertEquals("foreach",                  tokens.get(2).lexeme);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(3).type);
        assertEquals("variable",                 tokens.get(3).lexeme);
        assertEquals(Token.TokenType.VAR,        tokens.get(4).type);
        assertEquals("var",                      tokens.get(4).lexeme);
    }

    @Test
    void allKeywords() throws Exception {
        var tokens = lex("for while do end if elif else then return function class break var const int double string  bool");
        assertEquals(19, tokens.size()); // 20 keywords + EOF
        assertEquals(Token.TokenType.FOR,         tokens.get(0).type);
        assertEquals(Token.TokenType.WHILE,       tokens.get(1).type);
        assertEquals(Token.TokenType.DO,          tokens.get(2).type);
        assertEquals(Token.TokenType.END_,        tokens.get(3).type);
        assertEquals(Token.TokenType.IF,          tokens.get(4).type);
        assertEquals(Token.TokenType.ELIF,        tokens.get(5).type);
        assertEquals(Token.TokenType.ELSE,        tokens.get(6).type);
        assertEquals(Token.TokenType.THEN,        tokens.get(7).type);
        assertEquals(Token.TokenType.RETURN,      tokens.get(8).type);
        assertEquals(Token.TokenType.FUNCTION,    tokens.get(9).type);
        assertEquals(Token.TokenType.CLASS,       tokens.get(10).type);
        assertEquals(Token.TokenType.BREAK,       tokens.get(11).type);
        assertEquals(Token.TokenType.VAR,         tokens.get(12).type);
        assertEquals(Token.TokenType.CONST_,      tokens.get(13).type);
        assertEquals(Token.TokenType.TYPE_INT,    tokens.get(14).type);
        assertEquals(Token.TokenType.TYPE_DOUBLE, tokens.get(15).type);
        assertEquals(Token.TokenType.TYPE_STRING, tokens.get(16).type);
        assertEquals(Token.TokenType.TYPE_BOOL,   tokens.get(17).type);
    }

    @Test
    void booleanLiterals() throws Exception {
        var tokens = lex("true false trueish falsehood");
        assertEquals(5, tokens.size());
        assertEquals(Token.TokenType.TRUE_,      tokens.get(0).type);
        assertEquals(true,  ((Token.Literal.Bool) tokens.get(0).literal).value());
        assertEquals(Token.TokenType.FALSE_,     tokens.get(1).type);
        assertEquals(false, ((Token.Literal.Bool) tokens.get(1).literal).value());
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(2).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(3).type);
    }

    @Test
    void nullKeyword() throws Exception {
        var tokens = lex("null nullptr nullish");
        assertEquals(4, tokens.size());
        assertEquals(Token.TokenType.NULL_,      tokens.get(0).type);
        assertInstanceOf(Token.Literal.Null.class, tokens.get(0).literal);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(2).type);
    }

    @Test
    void underscoreIdentifiers() throws Exception {
        var tokens = lex("_ _var var_ _var_ __private");
        assertEquals(6, tokens.size()); // 5 identifiers + EOF
        for (int i = 0; i < 5; i++) {
            assertEquals(Token.TokenType.IDENTIFIER, tokens.get(i).type);
        }
    }

    @Test
    void escapeAtStart() throws Exception {
        var tokens = lex("\\start_name");
        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(0).type);
        assertEquals("start_name", tokens.get(0).lexeme);
    }

    @Test
    void escapeAtEnd() throws Exception {
        var tokens = lex("name_end\\");
        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(0).type);
        assertEquals("name_end", tokens.get(0).lexeme);
    }

    @Test
    void allGroupingSymbols() throws Exception {
        var tokens = lex("( ) [ ] { }");
        assertEquals(7, tokens.size()); // 6 symbols + EOF
        assertEquals(Token.TokenType.LEFT_PAREN,    tokens.get(0).type);
        assertEquals(Token.TokenType.RIGHT_PAREN,   tokens.get(1).type);
        assertEquals(Token.TokenType.LEFT_BRACKET,  tokens.get(2).type);
        assertEquals(Token.TokenType.RIGHT_BRACKET, tokens.get(3).type);
        assertEquals(Token.TokenType.LEFT_CURLY_BRACE,    tokens.get(4).type);
        assertEquals(Token.TokenType.RIGHT_CURLY_BRACE,   tokens.get(5).type);
    }

    @Test
    void nestedGrouping() throws Exception {
        var tokens = lex("{[(())]}");
        assertEquals(9, tokens.size()); // 8 symbols + EOF
    }

    @Test
    void punctuationSymbols() throws Exception {
        var tokens = lex(": ; , . ?");
        assertEquals(6, tokens.size()); // 5 symbols + EOF
        assertEquals(Token.TokenType.COLON,         tokens.get(0).type);
        assertEquals(Token.TokenType.SEMI_COLON,    tokens.get(1).type);
        assertEquals(Token.TokenType.COMMA,         tokens.get(2).type);
        assertEquals(Token.TokenType.DOT,           tokens.get(3).type);
        assertEquals(Token.TokenType.QUESTION_MARK, tokens.get(4).type);
    }

    @Test
    void mixedWhitespace() throws Exception {
        var tokens = lex("  \t\n  var   \t  x  \n\n  =  \t 5  ");
        assertEquals(5, tokens.size()); // var x = 5 EOF
        assertEquals(Token.TokenType.VAR,        tokens.get(0).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals(Token.TokenType.ASSIGN,     tokens.get(2).type);
        assertEquals(Token.TokenType.INT,        tokens.get(3).type);
    }

    @Test
    void emptyInput() throws Exception {
        var tokens = lex("");
        assertEquals(1, tokens.size()); // only EOF
        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(0).type);
    }

    @Test
    void onlyWhitespace() throws Exception {
        var tokens = lex("   \t\n\n\t   ");
        assertEquals(1, tokens.size()); // only EOF
        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(0).type);
    }

    @Test
    void functionDeclaration() throws Exception {
        var tokens = lex("function add(a: int, b: int): int { return a + b; }");
        assertEquals(Token.TokenType.FUNCTION,   tokens.get(0).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals("add",                      tokens.get(1).lexeme);
        assertEquals(Token.TokenType.LEFT_PAREN, tokens.get(2).type);
    }

    @Test
    void arrayAccess() throws Exception {
        var tokens = lex("arr[i] = arr[i+1] + arr[0]");
        assertEquals(Token.TokenType.IDENTIFIER,    tokens.get(0).type);
        assertEquals(Token.TokenType.LEFT_BRACKET,  tokens.get(1).type);
        assertEquals(Token.TokenType.IDENTIFIER,    tokens.get(2).type);
        assertEquals(Token.TokenType.RIGHT_BRACKET, tokens.get(3).type);
        assertEquals(Token.TokenType.ASSIGN,        tokens.get(4).type);
    }

    @Test
    void ternaryOperator() throws Exception {
        var tokens = lex("x > 0 ? x : -x");
        assertEquals(Token.TokenType.IDENTIFIER,    tokens.get(0).type);
        assertEquals(Token.TokenType.GREATER_THAN,  tokens.get(1).type);
        assertEquals(Token.TokenType.INT,           tokens.get(2).type);
        assertEquals(Token.TokenType.QUESTION_MARK, tokens.get(3).type);
        assertEquals(Token.TokenType.IDENTIFIER,    tokens.get(4).type);
        assertEquals(Token.TokenType.COLON,         tokens.get(5).type);
        assertEquals(Token.TokenType.MINUS,         tokens.get(6).type);
        assertEquals(Token.TokenType.IDENTIFIER,    tokens.get(7).type);
    }

    @Test
    void complexExpression() throws Exception {
        var tokens = lex("result = (a && b) || (c >= d << 2)");
        assertTrue(tokens.size() > 10);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(0).type);
        assertEquals(Token.TokenType.ASSIGN,     tokens.get(1).type);
    }

    @Test
    void chainedAssignments() throws Exception {
        var tokens = lex("a += b -= c *= d /= e %= f");
        assertEquals(Token.TokenType.PLUS_ASSIGN,     tokens.get(1).type);
        assertEquals(Token.TokenType.MINUS_ASSIGN,    tokens.get(3).type);
        assertEquals(Token.TokenType.MULTIPLY_ASSIGN, tokens.get(5).type);
        assertEquals(Token.TokenType.DIVIDE_ASSIGN,   tokens.get(7).type);
        assertEquals(Token.TokenType.MODULO_ASSIGN,   tokens.get(9).type);
    }

    @Test
    void simpleBubbleSort() throws Exception {
        String input = """
function bubbleSort(arr, n: int) {
    const i: int = 0
    const j: int = 0

    for i = 0; i < n - 1; i++ do
        for j = 0; j < n - i - 1; j++ do
            if arr[j] > arr[j + 1] then
                var temp: int = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
            end
        end
    end
end

var numbers: array = [64, 34, 25, 12, 22]
bubbleSort(numbers, 5)
""";
        var tokens = lex(input);

        assertTrue(tokens.size() > 80);
        assertEquals(Token.TokenType.FUNCTION,   tokens.get(0).type);
        assertEquals(Token.TokenType.IDENTIFIER, tokens.get(1).type);
        assertEquals("bubbleSort",               tokens.get(1).lexeme);

        boolean hasFor = false, hasIf = false, hasVar = false, hasEnd = false;
        boolean hasTypeInt = false, hasTypeArray = false;
        boolean hasPlusPlus = false, hasGreaterThan = false;

        for (var token : tokens) {
            if (token.type == Token.TokenType.FOR)          hasFor = true;
            if (token.type == Token.TokenType.IF)           hasIf = true;
            if (token.type == Token.TokenType.VAR)          hasVar = true;
            if (token.type == Token.TokenType.END_)         hasEnd = true;
            if (token.type == Token.TokenType.TYPE_INT)     hasTypeInt = true;
            if (token.type == Token.TokenType.PLUS_PLUS)    hasPlusPlus = true;
            if (token.type == Token.TokenType.GREATER_THAN) hasGreaterThan = true;
        }

        assertTrue(hasFor);
        assertTrue(hasIf);
        assertTrue(hasVar);
        assertTrue(hasEnd);
        assertTrue(hasTypeInt);
        assertTrue(hasPlusPlus);
        assertTrue(hasGreaterThan);

        // brackets balanced
        long leftBrackets  = tokens.stream().filter(t -> t.type == Token.TokenType.LEFT_BRACKET).count();
        long rightBrackets = tokens.stream().filter(t -> t.type == Token.TokenType.RIGHT_BRACKET).count();
        assertEquals(leftBrackets, rightBrackets);

        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(tokens.size() - 1).type);
    }

    @Test
    void switchCaseWithComplexExpressions() throws Exception {
        // Her case'de farklı expression türleri: arithmetic, comparison, string
        String input = """
var x : int = 15;
var result : string = "none";
switch x do
    case 10 + 5:
    do
        result = "fifteen";
    end
    case 3 * 4:
    do
        result = "twelve";
    end
    default:
    do
        result = "other";
    end
end
println result;
""";
        var tokens = lex(input);

        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(tokens.size() - 1).type);

        boolean hasPlus     = tokens.stream().anyMatch(t -> t.type == Token.TokenType.PLUS);
        boolean hasMultiply = tokens.stream().anyMatch(t -> t.type == Token.TokenType.MULTIPLY);
        assertTrue(hasPlus,     "case expression'ı + operatörü içermeli");
        assertTrue(hasMultiply, "case expression'ı * operatörü içermeli");

        long caseCount = tokens.stream().filter(t -> t.lexeme.equals("case")).count();
        assertEquals(2, caseCount);
        assertTrue(tokens.stream().anyMatch(t -> t.lexeme.equals("default")));

        long doCount  = tokens.stream().filter(t -> t.lexeme.equals("do")).count();
        long endCount = tokens.stream().filter(t -> t.type == Token.TokenType.END_).count();
        assertEquals(doCount, endCount);
    }

    @Test
    void switchCaseNestedInsideForLoop() throws Exception {
        // for döngüsü içinde switch — iç içe yapı testi
        String input = """
var i : int = 0;
for i = 0; i < 3; i++ do
    switch i do
        case 0:
        do
            println "zero";
        end
        case 1:
        do
            println "one";
        end
        default:
        do
            println "other";
            break;
        end
    end
end
""";
        var tokens = lex(input);

        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(tokens.size() - 1).type);

        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.FOR));
        assertTrue(tokens.stream().anyMatch(t -> t.lexeme.equals("switch")));

        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.PLUS_PLUS));


        long stringCount = tokens.stream().filter(t -> t.type == Token.TokenType.STRING).count();
        assertEquals(3, stringCount);

        long doCount  = tokens.stream().filter(t -> t.lexeme.equals("do")).count();
        long endCount = tokens.stream().filter(t -> t.type == Token.TokenType.END_).count();
        assertEquals(doCount, endCount);
    }

    @Test
    void typeDeclarationsAllPrimitives() throws Exception {
        // Verify that int, double, string, and bool types are tokenized correctly
        String input = """
var a : int = 10;
var b : double = 3.14;
var c : string = "yagiz";
var d : bool = true;
""";
        var tokens = lex(input);

        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(tokens.size() - 1).type);

        // Each type keyword must appear in the token list
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.TYPE_INT),
                "TYPE_INT token expected");
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.TYPE_DOUBLE),
                "TYPE_DOUBLE token expected");
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.TYPE_STRING),
                "TYPE_STRING token expected");
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.TYPE_BOOL),
                "TYPE_BOOL token expected");

        // Also verify literal values
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.INT
                && ((Token.Literal.Int) t.literal).value() == 10));
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.DOUBLE
                && ((Token.Literal.Double) t.literal).value() == 3.14));
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.STRING
                && ((Token.Literal.Str) t.literal).value().equals("yagiz")));
        assertTrue(tokens.stream().anyMatch(t -> t.type == Token.TokenType.TRUE_));
    }

    @Test
    void typeCharIsNotKeyword() throws Exception {
        String input = """
var ch : char = 'a';
""";
        var tokens = lex(input);

        assertEquals(Token.TokenType.END_OF_FILE, tokens.get(tokens.size() - 1).type);

        var charToken = tokens.stream()
                .filter(t -> t.lexeme.equals("char"))
                .findFirst();

        assertEquals(Token.TokenType.TYPE_CHAR, charToken.get().type);

    }

}