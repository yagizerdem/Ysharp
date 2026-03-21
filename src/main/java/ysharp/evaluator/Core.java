package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

public class Core {

    public void start() {
      try {
          Interpreter interpreter = new Interpreter();
          Registery.register(interpreter);

          String mainModulePath = "C:\\Users\\yagiz\\Desktop\\ysharp\\Ysharp\\src\\test\\resources\\snakegame\\main.ys";
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

          Resolver resolver = new Resolver(interpreter);
          resolver.resolve(program.program);

          if(resolver.hadErrors()) {
              StdIO.printStdErr(resolver.errors);
              return;
          }


          Loader loader = new Loader(program, mainModulePath);
          Hashtable<String, Variable> exportRegistry = loader.loadEnv();

          for (String key : exportRegistry.keySet()) {
              if(!interpreter.global.existsAt(0, key)) {
                  interpreter.global.define(key, exportRegistry.get(key));
              }
          }

          interpreter.interpret(program.program);
          if(interpreter.hadErrors()) {
              StdIO.printStdErr(interpreter.errors);
              return;
          }
      }
      catch (YsharpError err) {
          if(err.getPrintMessage()) {
              StdIO.printStdErr("Runtime error:");
              StdIO.printStdErr(err.toString());
          }
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
