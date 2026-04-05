package ysharp.treewalk.evaluator.Native;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class Range{

    public static class RangeValue  extends RuntimeObject  {
        public final int start;
        public final int end;

        public RangeValue(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "expr_range";
        }

        @Override
        public String toString() {
            return "<expression:range>";
        }
    }

    public static class RangeFn extends Function.NativeFunction {
        @Override
        public String getFnName() {
            return "range";
        }

        @Override
        public int arity() {
            return 2; // start end
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            requireArity(arguments, arity(), getFnName());
            int start = requireInt(arguments.getFirst(), getFnName(), 1);
            int end = requireInt(arguments.get(1), getFnName(), 2);
            return new Variable.Variant(new Range.RangeValue(start, end));
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        RangeFn rangeFn = new RangeFn();
        Variable.Variant variant = new Variable.Variant(rangeFn);
        Variable var = new Variable(variant, true, rangeFn.getType());
        interpreter.defineGlobal(rangeFn.getFnName(), var);
    }
}
