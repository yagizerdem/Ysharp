package ysharp;

import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Variable;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Expr;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;

import java.util.List;


public class Main {
    public static void main(String[] args) throws  Exception {

        String program = """
                var i = 0;
                for ; i < 10 ; i += 1  do
                    println \"hit\";
                    if i > 5 then do break ; end
                    println i ;
                    end

                """;

        var buf = Preprocess.removeComments(Preprocess.mergeContinuation(program));
        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();

        Parser parser = new Parser(stream);

        List<Stmt> parseTree = parser.parse();

        Interpreter interpreter = new Interpreter();

        for(Stmt s : parseTree){
            interpreter.execute(s);
        }


        int a = 10;

    }
}