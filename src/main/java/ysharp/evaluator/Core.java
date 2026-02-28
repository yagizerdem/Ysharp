package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.Y_Array;
import ysharp.evaluator.Native.Collections.Y_Queue;
import ysharp.evaluator.Native.Collections.Y_Stack;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;

import java.util.List;

public class Core {

    public void start() throws Exception{
        Interpreter interpreter = new Interpreter();
        Register(interpreter);

        String program = """
                    var a = new Queue();
                    
                    a.add(1);
                       a.add(2);
                          a.add(3);
                    
                    a.remove();
                    a.remove();
                    a.remove();
                    
                """;



        Preprocess preprocess = new Preprocess();
        List<Cursor.Pchar> buf = preprocess.process(program);
        if(preprocess.hadErrors()){
            printStdErr(preprocess.errors);
            return;
        }

        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();
        if(lexer.hadErrors()) {
            printStdErr(lexer.errors);
            return;
        }

        Parser parser = new Parser(stream);
        List<Stmt> parseTree = parser.parse();
        if(parser.hadErrors()) {
            printStdErr(parser.errors);
            return;
        }


        interpreter.interpret(parseTree);
        if(interpreter.hadErrors()) {
            printStdErr(interpreter.errors);
            return;
        }


        int a = 10;

    }

    private void printStdErr(List<YsharpError> errors) {
        for(YsharpError err : errors) {
            System.err.println(err.toString());
        }
    }

    private static void Register(Interpreter interpreter) throws Exception {
        Y_String.Register(interpreter);

        // collections
        Y_Array.Register(interpreter);
        Y_Stack.Register(interpreter);
        Y_Queue.Register(interpreter);
    }
}
