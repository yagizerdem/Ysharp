package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RadToDegFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 1, getFnName());

        double radians = requireNumber(arguments.getFirst(), getFnName(), 1);
        double response = radians * (180.0 / Math.PI);

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "radToDeg";
    }
}
