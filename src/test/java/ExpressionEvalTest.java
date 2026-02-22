import org.junit.jupiter.api.Test;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Variable;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvalTest {

    private Object eval(String program) throws Exception {
        var buf = Preprocess.mergeContinuation(program);
        buf = Preprocess.removeComments(buf);
        var stream = new Lexer(buf).scanTokens();
        var exprVec = new Parser(stream).parse();
        var node = exprVec.get(0);
        Interpreter interpreter = new Interpreter();
        return node.accept(interpreter);
    }

    @Test
    void basic() throws Exception {
        var val = eval("4 + 5");
        if (val instanceof Variable.Variant) {
            assertEquals(9, ((Variable.Variant) val).asInt());
        } else {
            fail("should return double");
        }
    }

    @Test
    void complexMath() throws Exception {
        var val = eval("(4 + 5 * 5 -1 / 4)  * (4 *( 2 + 1))");
        if (val instanceof Variable.Variant) {
            assertEquals(348, ((Variable.Variant) val).asInt());
        } else {
            fail("should return double");
        }
    }

    @Test
    void bitwiseOperations() throws Exception {
        // (8|4)&12 = 12, 3<<2 = 12, 12^12 = 0
        var val = eval("((8 | 4) & 12) ^ (3 << 2)");
        if (val instanceof Variable.Variant) {
            assertEquals(0, ((Variable.Variant) val).asInt());
        } else {
            fail("should return int");
        }
    }

    @Test
    void logicalAndComparison() throws Exception {
        var val = eval("(5 > 3 && 10 <= 20) || (15 == 15 && 7 != 8)");
        if (val instanceof Variable.Variant) {
            assertTrue(((Variable.Variant) val).asBoolean());
        } else {
            fail("should return bool");
        }
    }

    @Test
    void ternaryConditional() throws Exception {
        // condition true → (3+7)*2 = 20
        var val = eval("(10 > 5) ? (3 + 7) * 2 : (4 - 2) * 5");
        if (val instanceof Variable.Variant) {
            assertEquals(20, ((Variable.Variant) val).asInt());
        } else {
            fail("should return double");
        }
    }

    @Test
    void nestedTernaryWithBitwise() throws Exception {
        // 8&4=0 → true → 16>>2 + 5 = 4+5 = 9
        var val = eval("(8 & 4) == 0 ? (16 >> 2) + 5 : (7 << 1) - (3 ^ 2)");
        if (val instanceof Variable.Variant) {
            assertEquals(9, ((Variable.Variant) val).asInt());
        } else {
            fail("should return double");
        }
    }

    @Test
    void mixedOperatorPrecedence() throws Exception {
        // 20*3=60, 60/2=30, 30%7=2, 100-50=50, 50+2=52, 52<<1=104, 3&2=2, 2^1=3, 104|3=107
        var val = eval("100 - 50 + 20 * 3 / 2 % 7 << 1 | 3 & 2 ^ 1");
        if (val instanceof Variable.Variant) {
            assertEquals(107, ((Variable.Variant) val).asInt());
        } else {
            fail("should return int");
        }
    }
    @Test
    void intPlusInt() throws Exception {
        // int + int → int
        var val = eval("3 + 4");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(7, v.asInt());
        } else fail();
    }

    @Test
    void intPlusDouble() throws Exception {
        // int + double → double
        var val = eval("3 + 4.0");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(7.0, v.asDouble());
        } else fail();
    }

    @Test
    void intDivInt() throws Exception {
        // int / int → int (integer division)
        var val = eval("10 / 3");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(3, v.asInt()); // 10/3 = 3, kalan düşer
        } else fail();
    }

    @Test
    void intDivDouble() throws Exception {
        // int / double → double
        var val = eval("10 / 4.0");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(2.5, v.asDouble());
        } else fail();
    }

    @Test
    void doubleArithmetic() throws Exception {
        // double + double → double
        var val = eval("1.5 + 2.5");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(4.0, v.asDouble());
        } else fail();
    }

    @Test
    void intModInt() throws Exception {
        // int % int → int
        var val = eval("17 % 5");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(2, v.asInt());
        } else fail();
    }

    @Test
    void mixedChainArithmetic() throws Exception {
        // 2 + 3 * 4 - 1 → int, öncelik: 3*4=12, 2+12=14, 14-1=13
        var val = eval("2 + 3 * 4 - 1");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(13, v.asInt());
        } else fail();
    }

    @Test
    void mixedChainWithDouble() throws Exception {
        // 2 + 3.0 * 4 - 1 → double çünkü 3.0*4=12.0
        var val = eval("2 + 3.0 * 4 - 1");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(13.0, v.asDouble());
        } else fail();
    }

    @Test
    void nestedParenIntArithmetic() throws Exception {
        // ((3 + 2) * (8 - 3)) / 5 → int: 5*5=25, 25/5=5
        var val = eval("((3 + 2) * (8 - 3)) / 5");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(5, v.asInt());
        } else fail();
    }

    @Test
    void nestedParenDoubleArithmetic() throws Exception {
        // ((3 + 2) * (8 - 3)) / 5.0 → double: 25/5.0=5.0
        var val = eval("((3 + 2) * (8 - 3)) / 5.0");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(5.0, v.asDouble());
        } else fail();
    }

    @Test
    void comparisonResultIsBool() throws Exception {
        var val = eval("10 > 3");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isBoolean());
            assertTrue(v.asBoolean());
        } else fail();
    }

    @Test
    void equalityIntVsDouble() throws Exception {
        var val = eval("5 == 5.0");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isBoolean());
            assertTrue(v.asBoolean(), "5 == 5.0 true");
        } else fail();
    }

    @Test
    void ternaryBothInt() throws Exception {
        var val = eval("1 > 0 ? 10 : 20");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(10, v.asInt());
        } else fail();
    }

    @Test
    void ternaryMixedTypes() throws Exception {
        var val = eval("1 > 2 ? 10 : 20.0");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(20.0, v.asDouble());
        } else fail();
    }

    @Test
    void negativeIntArithmetic() throws Exception {
        var val = eval("10 - 15");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(-5, v.asInt());
        } else fail();
    }

    @Test
    void largeIntMultiply() throws Exception {
        var val = eval("1000 * 1000");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isInt());
            assertEquals(1_000_000, v.asInt());
        } else fail();
    }

    @Test
    void doubleSubtraction() throws Exception {
        var val = eval("5.5 - 2.2");
        if (val instanceof Variable.Variant v) {
            assertTrue(v.isDouble());
            assertEquals(3.3, v.asDouble(), 1e-10);
        } else fail();
    }

    @Test
    void ultraComplexMathAndLogic() throws Exception {
        String expr =
                "((((15 * 4) - (100 / (2 + 3))) * (7 % 3 + 1)) >> 1) == 40" +
                        " ? (((8 | 3) & 11) ^ 2) + (5 > 3 && 10 != 8 ? 100 : 0)"    +
                        " : 999";
        var val = eval(expr);
        if (val instanceof Variable.Variant v) {

            assertEquals(109, v.asInt());

        } else {
            fail((val == null ? "null" : val.getClass().getName()));
        }
    }

}