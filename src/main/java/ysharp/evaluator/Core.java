package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.function.core.Clock;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;
import ysharp.parser.TypeTag;

import java.util.List;

public class Core {

    public void start() throws Exception{
        String program = """
                    var a = "yagiz erdem";
                    print a.capitalize();
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


        Interpreter interpreter = new Interpreter();

        Y_String.Y_StringInit stringCtor = new Y_String.Y_StringInit();
        Variable.Variant variant = new Variable.Variant(stringCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(stringCtor.getFnName(), var);


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

}
