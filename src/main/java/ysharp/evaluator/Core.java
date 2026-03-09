package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.*;
import ysharp.evaluator.Native.Collections.Trie.yMapTrie;
import ysharp.evaluator.Native.Collections.Trie.ySortedMapTrie;
import ysharp.evaluator.Native.Collections.Trie.yT9Trie;
import ysharp.evaluator.Native.Form.Y_Button;
import ysharp.evaluator.Native.Form.Y_Frame;
import ysharp.evaluator.Native.Network.yHttp;
import ysharp.evaluator.Native.TUI.Terminal.yDefaultTerminal;
import ysharp.evaluator.Native.TUI.Terminal.ySwingTerminal;
import ysharp.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.evaluator.Native.Threading.yThread;
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
                    var t = new Thread(()=> do 
                        for var i = 0 ; i < 999; i++ do
                            println i;
                        end
                    end);
                   
                   t.start();
                   
                    for var i = 0 ; i < 999; i++ do
                            println i;
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


      }
      catch (YsharpError err) {
          printStdErr("Runtime error:");
          printStdErr(err.toString());
      }
      catch (Signal.ThrowSignal ex) {
          printStdErr("Uncaught throw:");
          printStdErr(ex.value.toString());
      }
      catch (Exception ex) {
          printStdErr("Process failed.");
          printStdErr("Internal error: " + ex.getClass().getSimpleName());
          printStdErr(ex.getMessage());
      }

    }

    private void printStdErr(List<YsharpError> errors) {
        for(YsharpError err : errors) {
            System.err.println(err.toString());
        }
    }

    private void printStdErr(String error) {
        System.err.println(error);
    }

    private static void Register(Interpreter interpreter) throws Exception {
        yString.Register(interpreter);

        // collections
        yArray.Register(interpreter);
        yStack.Register(interpreter);
        yQueue.Register(interpreter);
        ySet.Register(interpreter);
        yHashTable.Register(interpreter);
        yLinkedList.Register(interpreter);
        yPriorityQueue.Register(interpreter);
        yArrayDeque.Register(interpreter);
        yTreeMap.Register(interpreter);
        yHashMap.Register(interpreter);
        yTreeSet.Register(interpreter);
        yWeakHashMap.Register(interpreter);
        yIdentityHashMap.Register(interpreter);
        yMapTrie.Register(interpreter);
        ySortedMapTrie.Register(interpreter);
        yT9Trie.Register(interpreter);

        //forms
        Y_Frame.Register(interpreter);
        Y_Button.Register(interpreter);


        //http
        yHttp.register(interpreter);

        // threading
        yThread.Register(interpreter);


        // utils
        yMath.Register(interpreter);
        yUUID.Register(interpreter);
        yTime.Register(interpreter);
        yFile.Register(interpreter);
        yRandom.Register(interpreter);
        yDateTime.Register(interpreter);
        yCrypto.Register(interpreter);

        // TUI
        yDefaultTerminal.Register(interpreter);
        ySwingTerminal.Register(interpreter);
        ySGR.Register(interpreter);
        yTextColor.Register(interpreter);
    }
}
