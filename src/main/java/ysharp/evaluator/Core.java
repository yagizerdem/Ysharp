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
import ysharp.evaluator.Native.Threading.ySemaphore;
import ysharp.evaluator.Native.Threading.yThread;
import ysharp.evaluator.Native.Util.*;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import ysharp.evaluator.Native.TUI.Util.ySGR;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Core {

    public void start() {
      try {
          Interpreter interpreter = new Interpreter();
          Registery.register(interpreter);

          String mainModulePath = "C:\\Users\\yagiz\\Desktop\\Ysharp\\src\\test\\resources\\main.ys";
          String mainModuleContent = new String(Files.readAllBytes(Paths.get(mainModulePath)));



          Preprocess preprocess = new Preprocess();
          List<Cursor.Pchar> buf = preprocess.process(mainModuleContent);
          if(preprocess.hadErrors()){
              StdIO.printStdErr(preprocess.errors);
              return;
          }

          Lexer lexer = new Lexer(buf);
          var stream = lexer.scanTokens();
          if(lexer.hadErrors()) {
              StdIO.printStdErr(lexer.errors);
              return;
          }

          Parser parser = new Parser(stream);
          Parser.Program program = parser.parse();
          if(parser.hadErrors()) {
              StdIO.printStdErr(parser.errors);
              return;
          }


          Loader loader = new Loader(program, mainModulePath);
          loader.loadEnv();

          interpreter.interpret(program.program);
          if(interpreter.hadErrors()) {
              StdIO.printStdErr(interpreter.errors);
              return;
          }


          int a = 10;


      }
      catch (YsharpError err) {
          StdIO.printStdErr("Runtime error:");
          StdIO.printStdErr(err.toString());
      }
      catch (Signal.ThrowSignal ex) {
          StdIO.printStdErr("Uncaught throw:");
          StdIO.printStdErr(ex.value.toString());
      }
      catch (Exception ex) {
          StdIO.printStdErr("Process failed.");
          StdIO.printStdErr("Internal error: " + ex.getClass().getSimpleName());
          StdIO.printStdErr(ex.getMessage());
      }

    }

}
