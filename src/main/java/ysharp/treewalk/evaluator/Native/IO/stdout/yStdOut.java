package ysharp.treewalk.evaluator.Native.IO.stdout;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class yStdOut {

    public static RuntimeObject yStdOut_Prototype;

    static {
        yStdOut_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__STDOUT__"; }
        };
    }

    public static class yStdOutClass extends yClass.ClassObject {

        public yStdOutClass() {
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
                    System.out.print(var.toString());

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "write";
                }
            }
            RegisterNativeFn(new writeFn());

            class writeLnFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant var = arguments.getFirst();
                    System.out.println(var.toString());

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "writeln";
                }
            }
            RegisterNativeFn(new writeLnFn());


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
                    "StdOut is static class, cannot take instance"
            );
        }

        @Override
        public String getClassName() {
            return "stdout";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_stdout_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yStdOutClass ctor = new yStdOutClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}