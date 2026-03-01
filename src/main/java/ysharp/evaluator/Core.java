package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Form.Y_Button;
import ysharp.evaluator.Native.Form.Y_Frame;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;

import java.util.List;

public class Core {

    public void start() throws Exception{
      try {
          Interpreter interpreter = new Interpreter();
          Register(interpreter);

          String program = """
                var l = new TreeSet();
                
                for var i = 0 ; i < 10 ; i += 1 do 
                    l.add(i);
                end
                
                
                println l.first();
                    
                    
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
      }catch (YsharpError err) {
          System.out.println(err.toString());
      }

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
        Y_Set.Register(interpreter);
        Y_HashTable.Register(interpreter);
        Y_LinkedList.Register(interpreter);
        Y_PriorityQueue.Register(interpreter);
        Y_ArrayDeque.Register(interpreter);
        Y_TreeMap.Register(interpreter);
        Y_HashMap.Register(interpreter);
        Y_TreeSet.Register(interpreter);
        Y_WeakHashMap.Register(interpreter);
        Y_IdentityHashMap.Register(interpreter);

        //forms
        Y_Frame.Register(interpreter);
        Y_Button.Register(interpreter);
    }
}
