package ysharp;

import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.function.core.Clock;
import ysharp.evaluator.Variable;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;

import java.util.List;


public class Main {
    public static void main(String[] args) throws  Exception {
        String program = """
                    var a = 10;
                    switch a % 3 do
                        case 1 : do 
                            println \"yagiz\";
                            break;
                         end
                        case 2 : do 
                            println \"arzu\";
                            break;
                         end
                         case 1 : do 
                            println \"erdem\";
                            break;
                         end
                         case 1 : do 
                            println \"test\";
                            break;
                         end
                         default : do 
                            println \"default\";
                         end
                       end
                """;


        var buf = Preprocess.removeComments(Preprocess.mergeContinuation(program));
        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();

        Parser parser = new Parser(stream);

        List<Stmt> parseTree = parser.parse();

        Interpreter interpreter = new Interpreter();

        Clock.Sleep sleep = new Clock.Sleep();
        Variable.Variant variant = new Variable.Variant(sleep);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(sleep.getFnName(), var);

        for(Stmt s : parseTree){
            interpreter.execute(s);
        }


        int a = 10;

    }
}