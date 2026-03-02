package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Collections.Trie.Y_MapTrie;
import ysharp.evaluator.Native.Collections.Trie.Y_SortedMapTrie;
import ysharp.evaluator.Native.Collections.Trie.Y_T9Trie;
import ysharp.evaluator.Native.Form.Y_Button;
import ysharp.evaluator.Native.Form.Y_Frame;
import ysharp.evaluator.Native.Threading.Y_Thread;
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
                var t = new Thread((a) => do 
                    for var i = 0; i < 999 ; i += 1 do
                        println i;
                       end 
                    end
                , "test");
                
                t.start();
        
                for var i = 0; i < 999 ; i += 1 do
                        println i;
                
                        if(i > 100) then do t.interrupt(); end
                
                end 
        
                                
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
        Y_MapTrie.Register(interpreter);
        Y_SortedMapTrie.Register(interpreter);
        Y_T9Trie.Register(interpreter);

        //forms
        Y_Frame.Register(interpreter);
        Y_Button.Register(interpreter);

        // threading
        Y_Thread.Register(interpreter);
    }
}
