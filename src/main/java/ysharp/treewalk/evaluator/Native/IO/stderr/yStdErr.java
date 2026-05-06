package ysharp.treewalk.evaluator.Native.IO.stderr;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class yStdErr {

    public static RuntimeObject yStdErr_Prototype;

    static {
        yStdErr_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__STDERR__"; }
        };
    }

    public static class yStdErrClass extends yClass.ClassObject {

        public yStdErrClass() {
            this.prototype = yClass.ClassPrototype;

            class writeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant var = arguments.getFirst();
                    System.err.print(var.toString());

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "write";
                }
            }
            this.RegisterNativeFn(new writeFn());

            class writeLnFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant var = arguments.getFirst();
                    System.err.println(var.toString());

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "writeln";
                }
            }
            this.RegisterNativeFn(new writeLnFn());

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
                    "StdErr is static class, cannot take instance"
            );
        }


        @Override
        public String getClassName() {
            return "stderr";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_stderr_";
        }

    }

    public static void Register(Interpreter interpreter) throws Exception {

        yStdErrClass ctor = new yStdErrClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}