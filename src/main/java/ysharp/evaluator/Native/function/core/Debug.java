package ysharp.evaluator.Native.function.core;

import ysharp.YsharpError;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.function.NativeFunction;
import ysharp.evaluator.Variable;

import java.util.List;

public abstract class Debug  extends NativeFunction  {


    // typeOf(x)
    public static class TypeOf extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            Variable.Variant v = args.get(0);
            return new Variable.Variant(v.getType());
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "typeOf"; }
    }

    // assert(condition, message)
    public static class Assert extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 2, getFnName());

            Variable.Variant condition = args.get(0);

            if (!condition.isTruthy()) {
                String msg = args.get(1).toString();
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        "Assertion failed: " + msg
                );
            }

            return new Variable.Variant(null);
        }

        @Override public int arity() { return 2; }
        @Override public String getFnName() { return "assert"; }
    }

    public static class Trace extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            System.out.println("[TRACE] " + args.get(0));
            return new Variable.Variant(null);
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "trace"; }
    }

    public static class Dump extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            Variable.Variant v = args.get(0);

            System.out.println(
                    "[DEBUG] value=" + v +
                            ", type=" + v.getType()
            );

            return v;
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "dump"; }
    }

}
