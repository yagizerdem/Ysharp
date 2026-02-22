package ysharp;

import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Variable;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Expr;
import ysharp.parser.Parser;

import java.util.List;


public class Main {
    public static void main(String[] args) throws  Exception {

        String program = "4 + 3";
        var buf = Preprocess.removeComments(Preprocess.mergeContinuation(program));
        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();

        Parser parser = new Parser(stream);

        List<Expr> parseTree = parser.parse();

        Interpreter interpreter = new Interpreter();
        Variable.Variant v = interpreter.evaluate(parseTree.get(0));

        int a = 10;

    }
}