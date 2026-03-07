package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Collections.Trie.Y_MapTrie;
import ysharp.evaluator.Native.Collections.Trie.Y_SortedMapTrie;
import ysharp.evaluator.Native.Collections.Trie.Y_T9Trie;
import ysharp.evaluator.Native.Form.Y_Button;
import ysharp.evaluator.Native.Form.Y_Frame;
import ysharp.evaluator.Native.TUI.Terminal.yDefaultTerminal;
import ysharp.evaluator.Native.TUI.Terminal.ySwingTerminal;
import ysharp.evaluator.Native.Threading.Y_Thread;
import ysharp.evaluator.Native.Util.*;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.parser.Stmt;
import ysharp.evaluator.Native.TUI.Util.ySGR;

import java.util.List;

public class Core {

    public void start() throws Exception{
      try {
          Interpreter interpreter = new Interpreter();
          Register(interpreter);

          String program = """
                
                    var terminal = new DefaultTerminal();
                    terminal.putCharacter('c');
                    terminal.flush();
                    terminal.putCharacter('a');
                    terminal.flush();
                    Time.sleep(1000);
                    terminal.clearScreen();
                    terminal.flush();
                    terminal.bell();
                    terminal.setCursorPosition(3,56);
                    terminal.putCharacter('6');
                    terminal.flush();
                  
                    var arr = SGR.values();
                    println arr.toString();
                  
                    class Human {
                        var a  = 10;
                    }
                    
                    var h = new Human();
                    println h.a;
                  
                
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
      catch (Exception ex) {
          System.out.println(ex.getMessage());
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


        // utils
        Y_Math.Register(interpreter);
        Y_UUID.Register(interpreter);
        Y_Time.Register(interpreter);
        Y_File.Register(interpreter);
        Y_Random.Register(interpreter);
        Y_DateTime.Register(interpreter);
        Y_Crypto.Register(interpreter);

        // TUI
        yDefaultTerminal.Register(interpreter);
        ySwingTerminal.Register(interpreter);
        ySGR.Register(interpreter);
    }
}
