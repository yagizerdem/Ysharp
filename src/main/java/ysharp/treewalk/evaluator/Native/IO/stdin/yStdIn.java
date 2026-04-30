package ysharp.treewalk.evaluator.Native.IO.stdin;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class yStdIn {

    public static RuntimeObject yStdIn_Prototype;

    static {
        yStdIn_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__STDIN__"; }
        };
    }

    public static class yStdInClass extends yClass.ClassObject {

        public yStdInClass() {
            this.prototype = yClass.ClassPrototype;

            class ReadLnFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    try {
                        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
                        String line= buffer.readLine();

                        return new Variable.Variant(new yString.yStringInstance(line));

                    }catch (IOException ex) {
                        throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1, ex.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "readln";
                }
            }
            RegisterNativeFn(new ReadLnFn());

            class ReadKeyFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    try {
                        char c = (char)System.in.read();

                        return new Variable.Variant(c);

                    }catch (IOException ex) {
                        throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1, ex.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "readKey";
                }
            }
            RegisterNativeFn(new ReadKeyFn());

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments
        ) throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "StdIn is static class, cannot take instance"
            );
        }

        @Override
        public String getClassName() {
            return "stdin";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_stdin_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yStdInClass ctor = new yStdInClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}