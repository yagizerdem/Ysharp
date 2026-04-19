package ysharp.treewalk.evaluator.Native.function.core;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public abstract class GlobalNatives extends Function.NativeFunction {

    // now() -> epoch milliseconds
    public static class Now extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 0, getFnName());
            return new Variable.Variant(System.currentTimeMillis());
        }

        @Override public int arity() { return 0; }
        @Override public String getFnName() { return "now"; }
    }

    // sleep(ms)
    public static class Sleep extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());
            long ms = (long) requireNumber(args.getFirst(), getFnName(), 1);

            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "sleep interrupted"
                );
            }

            return new Variable.Variant(null);
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "sleep"; }
    }

    // callable(object)
    public static class CallableFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant value = args.get(0);

            boolean result = false;

            // callable check
            if (value.value instanceof Callable) {
                result = true;
            }

            return new Variable.Variant(result);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "callable"; }
    }

    // chr(codepoint)
    public static class ChrFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            long code = (long) requireNumber(args.getFirst(), getFnName(), 1);

            if (code < 0 || code > 0x10FFFF) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "chr() codepoint out of range (0..0x10FFFF)"
                );
            }

            String result = new String(Character.toChars((int) code));

            return new Variable.Variant(result);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "chr"; }
    }

    public static void  Register(Interpreter interpreter) {
        Now nowFn = new Now();
        interpreter.global.define(nowFn.getFnName(),
                new Variable(new Variable.Variant(nowFn), true, nowFn.getType()));

        Sleep sleepFn = new Sleep();
        interpreter.global.define(sleepFn.getFnName(),
                new Variable(new Variable.Variant(sleepFn), true, sleepFn.getType()));

        CallableFn callableFn = new CallableFn();
        interpreter.global.define(callableFn.getFnName(),
                new Variable(new Variable.Variant(callableFn), true, callableFn.getType()));

        ChrFn chrFn = new ChrFn();
        interpreter.global.define(chrFn.getFnName(),
                new Variable(new Variable.Variant(chrFn), true, chrFn.getType()));
    }
}