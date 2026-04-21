package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.function.instance.GetFn;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class TruncFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 1, getFnName());

        double value = requireNumber(arguments.getFirst(), getFnName(), 1);

        double response = (value >= 0) ? Math.floor(value) : Math.ceil(value);

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "trunc";
    }
}
