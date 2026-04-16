package ysharp.treewalk.evaluator.Native.function.core;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public abstract class Debug  extends Function.NativeFunction {


    // typeOf(x)
    public static class TypeOf extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant v = args.get(0);
            return new Variable.Variant(i.curEnv.getType(v));
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "typeOf"; }
    }

    // assert(condition, message)
    public static class Assert extends Debug {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 2, getFnName());

            Variable.Variant condition = args.get(0);

            if (!condition.isTruthy()) {
                String msg = args.get(1).toString();
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
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
                throws YsharpException {

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
                throws YsharpException {

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


    public static void Register(Interpreter interpreter) throws Exception {

        // typeof
        TypeOf typeOfFn = new TypeOf();
        Variable typeOfFnVar = new Variable(
                new Variable.Variant(typeOfFn),
                true,
                "function"
        );
        interpreter.defineGlobal(typeOfFn.getFnName(), typeOfFnVar);


        // assert
        Assert assertFn = new Assert();
        Variable assertVar = new Variable(
                new Variable.Variant(assertFn),
                true,
                "function"
        );
        interpreter.defineGlobal(assertFn.getFnName(), assertVar);


        // trace
        Trace traceFn = new Trace();
        Variable traceVar = new Variable(
                new Variable.Variant(traceFn),
                true,
                "function"
        );
        interpreter.defineGlobal(traceFn.getFnName(), traceVar);


        // dump
        Dump dumpFn = new Dump();
        Variable dumpVar = new Variable(
                new Variable.Variant(dumpFn),
                true,
                "function"
        );
        interpreter.defineGlobal(dumpFn.getFnName(), dumpVar);
    }
}
