package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class Log10Fn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 1, getFnName());

        double value = requireNumber(arguments.getFirst(), getFnName(), 1);
        double response = Math.log10(value);

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "log10";
    }
}
