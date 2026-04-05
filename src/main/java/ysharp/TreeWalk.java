package ysharp;

import ysharp.evaluator.*;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class TreeWalk {

    public static void start(String[] args) {
        Interpreter interpreter = new Interpreter();
        try {
            Registery.register(interpreter);
        }catch (Exception ex) {
            System.err.println(ex.getMessage());
            return;
        }

        if(args.length == 1) {
            executeFile(interpreter, args[0]);
        }
        else {
            REPL(interpreter);
        }

    }

    public static void executeFile(Interpreter interpreter, String mainModulePath) {
        try {
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

    public static void REPL(Interpreter interpreter) {
        System.setErr(System.out);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            interpreter.errors.clear();
            System.out.print("> ");
            String ySharpCommand = scanner.nextLine();

            if(ySharpCommand.trim().equals("exit")) {
                break;
            }

            if(ySharpCommand.trim().equals("help")) {
                // print manual
                continue;
            }

            try {
                Preprocess preprocess = new Preprocess();
                List<Cursor.Pchar> buf = preprocess.process(ySharpCommand);
                if(preprocess.hadErrors()){
                    for(YsharpError err: preprocess.errors) {
                        System.err.println(err.getMessage());
                    }
                    continue;
                }

                Lexer lexer = new Lexer(buf);
                var stream = lexer.scanTokens();
                if(lexer.hadErrors()) {
                    for(YsharpError err: lexer.errors) {
                        System.err.println(err.getMessage());
                    }
                    continue;
                }

                Parser parser = new Parser(stream);
                Parser.Program program = parser.parse();
                if(parser.hadErrors()) {
                    for(YsharpError err: parser.errors) {
                        System.err.println(err.getMessage());
                    }
                    continue;
                }

                Resolver resolver = new Resolver(interpreter);
                resolver.resolve(program.program);

                if(resolver.hadErrors()) {
                    for(YsharpError err: resolver.errors) {
                        System.err.println(err.getMessage());
                    }
                    continue;
                }

                interpreter.interpret(program.program);
                if(interpreter.hadErrors()) {
                    for(YsharpError err: interpreter.errors) {
                        System.err.println(err.getMessage());
                    }
                    continue;
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
}
