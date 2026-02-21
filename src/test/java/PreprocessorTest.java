import org.junit.jupiter.api.Test;
import ysharp.YsharpError;
import ysharp.lexer.Cursor;
import ysharp.lexer.Preprocess;

import static org.junit.jupiter.api.Assertions.*;

public class PreprocessorTest {
    @Test
    void continuationBasic() {
        try {
            String program = "var name = \"yagiz erdem\"";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals(Cursor.pBufferToString(buf), "var name = \"yagiz erdem\"");
        }
        catch (Exception ex) {
            fail();
        }

    }

    @Test
    void continuationMultiLine() {
        try {
            String program = "var a : int = 10; \n var b = 20";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var a : int = 10; \n var b = 20", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationMultiLine2() {
        try {
            String program = "var a : int = 10; \n var b = 20 \n\tstd::string name = \t\"yagiz erdem \"";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var a : int = 10; \n var b = 20 \n\tstd::string name = \t\"yagiz erdem \"", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationTab() {
        try {
            String program = "\t";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("\t", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationTab2() {
        try {
            String program = "yagiz \t erdem";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("yagiz \t erdem", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationEscapeBasic() {
        try {
            String program = "yagiz \t erdem \\\n var num : int = 20 ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("yagiz \t erdem  var num : int = 20 ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationEscapeBasic2() {
        try {
            String program = "yagiz \t erdem \\\n var num : int = 20\\\nvar name: string =\t \"yagiz erdem\" ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("yagiz \t erdem  var num : int = 20var name: string =\t \"yagiz erdem\" ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationUnitedStatesMarch() {
        try {
            String paragraph =
                    "O say can you see, by the dawn's early light,\\\n" +
                            "What so proudly we hail'd at the twilight's last gleaming,\\\n" +
                            "Whose broad stripes and bright stars through the perilous fight\\\n" +
                            "O'er the ramparts we watch'd were so gallantly streaming?\\\n" +
                            "And the rocket's red glare, the bombs bursting in air,\\\n" +
                            "Gave proof through the night that our flag was still there,\\\n" +
                            "O say does that star-spangled banner yet wave\\\n" +
                            "O'er the land of the free and the home of the brave?\\\n";

            String expected =
                    "O say can you see, by the dawn's early light," +
                            "What so proudly we hail'd at the twilight's last gleaming," +
                            "Whose broad stripes and bright stars through the perilous fight" +
                            "O'er the ramparts we watch'd were so gallantly streaming?" +
                            "And the rocket's red glare, the bombs bursting in air," +
                            "Gave proof through the night that our flag was still there," +
                            "O say does that star-spangled banner yet wave" +
                            "O'er the land of the free and the home of the brave?";

            var buf = Preprocess.mergeContinuation(paragraph);
            assertEquals(expected, Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationMergeStringWithEmptyEscape() {
        try {
            String program = "std::string full_name = \"yagiz\" \\ \\ \\\n \\ \"erdem\" ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("std::string full_name = \"yagizerdem\" ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationMergeStringWithEmptyEscape2() {
        try {
            String program =
                    "std::string university = \"dokuz\" \\ \\ \\\n \\ \"eylul\" \\ \"university\"" +
                            " \\ \n \"izmir\" \\\n \"buca\" \\\n \t \"province\" ";

            String expected =
                    "std::string university = \"dokuzeylul\"  \"university\"" +
                            "  \n \"izmirbucaprovince\" ";

            var buf = Preprocess.mergeContinuation(program);
            assertEquals(expected, Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationStringIntContinuation() {
        try {
            String program = "var name : string = \"yagiz erdem\" \\\n var luckNumber : number = 3";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var name : string = \"yagiz erdem\"  var luckNumber : number = 3", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    // this should fail - will be fixed later
    @Test
    void continuationEdgeCase() {
        try {
            String program = "var name : string = \"yagiz erdem\" \\\n \t var luckNumber : number = 3";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var name : string = \"yagiz erdem\"  \t var luckNumber : number = 3", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationInsideString() {
        try {
            String program = "var name : string =  \" yagiz \\\n erdem \" ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var name : string =  \" yagiz \\\n erdem \" ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationInsideString2() {
        try {
            String program = "var name : string =  \" yagiz \\\n erdem \" \\\n \"append later\" ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("var name : string =  \" yagiz \\\n erdem append later\" ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationEscapedChar() {
        try {
            String program = "\\a";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals("\\a", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void continuationUnclosedQuote() {
        try {
            String program = "\\\"yagiz erdem\" ";
            var buf = Preprocess.mergeContinuation(program);
            fail("should throw unclosed double quote error");
        } catch (YsharpError err) {
            assertEquals(YsharpError.YsharpErrorType.SYNTAX, err.getType());
        } catch (Exception ex) {
            fail("wrong exception type: " + ex.getMessage());
        }
    }

    @Test
    void continuationEscapedDoubleQuotes() {
        try {
            String program = " \\t \\\\t \t \\yagiz \\\"yagiz erdem\\\" \\ \\a \\\n  int a =10 ";
            var buf = Preprocess.mergeContinuation(program);
            assertEquals(" \\t \\\\t \t \\yagiz \\\"yagiz erdem\\\"  \\a   int a =10 ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }
}
