package ysharp;

import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;

import java.util.List;

public class Main {
    public static void main(String[] args) throws  Exception {

        String program = "4 + 3";
        var buf = Preprocess.removeComments(Preprocess.mergeContinuation(program));
        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();

        Parser parser = new Parser(stream);

        var parseTree = parser.parse();

        int a =10;

    }
}