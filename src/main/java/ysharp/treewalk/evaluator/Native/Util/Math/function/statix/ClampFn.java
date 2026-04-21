package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ClampFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 3, getFnName());

        double value = requireNumber(arguments.get(0), getFnName(), 1);
        double min = requireNumber(arguments.get(1), getFnName(), 2);
        double max = requireNumber(arguments.get(2), getFnName(), 3);

        double response = Math.max(min, Math.min(max, value));

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "clamp";
    }
}
