import org.junit.jupiter.api.Test;
import ysharp.YsharpError;
import ysharp.lexer.Cursor;
import ysharp.lexer.Preprocess;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void onelinerNoComments() {
        try {
            String program = "var name = \"yagiz erdem\"";
            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals("var name = \"yagiz erdem\"", Cursor.pBufferToString(buf));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            fail();
        }
    }

    @Test
    void onelinerWithComments() {
        try {
            String program = "var name = // \"yagiz erdem\"";
            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals("var name = ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void multiLineWithComments() {
        try {
            String program = "var name = // \"yagiz erdem\" \n int a = 10 \n var address : string = \"Istanbul\" // Izmir-Buca ";
            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals("var name = \n int a = 10 \n var address : string = \"Istanbul\" ", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void multiLineComment() {
        try {
            String program =
                    "/* \n " +
                            "/* this \n" +
                            "/* is \n" +
                            "/* test \n" +
                            "/* comment \n" +
                            "*/ \n" +
                            "var name : string = \"sudenaz yetkin\"";

            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals(" \nvar name : string = \"sudenaz yetkin\"", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void commentInsideString() {
        try {
            String program = "var name : string = \"yagiz //erdem\"";
            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals("var name : string = \"yagiz //erdem\"", Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void unitedStatesMarch() {
        try {
            String paragraph =
                    "O say can you see, by the dawn's early // light,\\\n" +
                            "What so proudly we hail'd at the twilight's last gleaming,\\\n" +
                            "Whose broad stripes and bright stars through the perilous fight\\\n" +
                            "O'er the ramparts we watch'd were so gallantly streaming?\\\n" +
                            "And the rocket's red glare, the bombs bursting in air,\\\n" +
                            "Gave proof through the night that our flag was still there,\\\n" +
                            "O say does that star-spangled banner yet wave\\\n" +
                            "O'er the land of the free and the home of the brave?\\\n";

            String expected = "O say can you see, by the dawn's early ";

            var buf = Preprocess.mergeContinuation(paragraph);
            buf = Preprocess.removeComments(buf);
            assertEquals(expected, Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void mixedComments() {
        try {
            String program =
                    "var name : string = \"yagiz\" \\\n \"er // dem\" \n" +
                            "/* comemnt \n" +
                            "* here \n" +
                            "*/ \n" +
                            "var num : number = 13//45 \n" +
                            "echo hit!";

            String expected =
                    "var name : string = \"yagizer // dem\" \n" +
                            " \n" +
                            "var num : number = 13\n" +
                            "echo hit!";

            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals(expected, Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }

    @Test
    void mixedComments2() {
        try {
            String program =
                    "var name : \\ \\a string = \"yagiz\" \\\n \"er // dem\" \n" +
                            "/* comemnt \n" +
                            "* here \n" +
                            "*/ \n" +
                            "var num : number = 13//45 \n" +
                            "echo hit!";

            String expected =
                    "var name :  \\a string = \"yagizer // dem\" \n" +
                            " \n" +
                            "var num : number = 13\n" +
                            "echo hit!";

            var buf = Preprocess.mergeContinuation(program);
            buf = Preprocess.removeComments(buf);
            assertEquals(expected, Cursor.pBufferToString(buf));
        } catch (Exception ex) { fail(); }
    }
}