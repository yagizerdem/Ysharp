package ysharp.evaluator.Native.function.core;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Variable;

import java.util.List;

public abstract class MathFn extends Function.NativeFunction {

    public static class Abs extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            Variable.Variant value = args.get(0);

            if (!value.canImplicitlyConvertNumber()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        getFnName() + " argument 1 must be numeric."
                );
            }

            if (value.isInt()) {
                return new Variable.Variant(Math.abs(value.asInt()));
            }

            return new Variable.Variant(Math.abs(value.asDouble()));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "abs"; }
    }

    public static class Max extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 2, getFnName());

            Variable.Variant v1 = args.get(0);
            Variable.Variant v2 = args.get(1);

            if (!v1.canImplicitlyConvertNumber()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        getFnName() + " argument 1 must be numeric."
                );
            }

            if (!v2.canImplicitlyConvertNumber()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        getFnName() + " argument 2 must be numeric."
                );
            }

            if (v1.isInt() && v2.isInt()) {
                return new Variable.Variant(
                        Math.max(v1.asInt(), v2.asInt())
                );
            }

            double a = v1.implicitlyConvertNumber();
            double b = v2.implicitlyConvertNumber();

            return new Variable.Variant(Math.max(a, b));
        }

        @Override public int arity() { return 2; }

        @Override public String getFnName() { return "max"; }
    }

    public static class Min extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 2, getFnName());

            Variable.Variant v1 = args.get(0);
            Variable.Variant v2 = args.get(1);

            if (!v1.canImplicitlyConvertNumber()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        getFnName() + " argument 1 must be numeric."
                );
            }

            if (!v2.canImplicitlyConvertNumber()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        getFnName() + " argument 2 must be numeric."
                );
            }

            if (v1.isInt() && v2.isInt()) {
                return new Variable.Variant(
                        Math.min(v1.asInt(), v2.asInt())
                );
            }

            double a = v1.implicitlyConvertNumber();
            double b = v2.implicitlyConvertNumber();

            return new Variable.Variant(Math.min(a, b));
        }

        @Override public int arity() { return 2; }

        @Override public String getFnName() { return "min"; }
    }

    public static class Floor extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            double n = requireNumber(args.get(0), getFnName(), 1);

            return new Variable.Variant(Math.floor(n));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "floor"; }
    }

    public static class Ceil extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            double n = requireNumber(args.get(0), getFnName(), 1);

            return new Variable.Variant(Math.ceil(n));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "ceil"; }
    }

    public static class Round extends MathFn {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpError {

            requireArity(args, 1, getFnName());

            double n = requireNumber(args.get(0), getFnName(), 1);

            return new Variable.Variant((double) Math.round(n));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "round"; }
    }

}
