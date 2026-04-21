package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class Atan2Fn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 2, getFnName());

        double y = requireNumber(arguments.get(0), getFnName(), 1);
        double x = requireNumber(arguments.get(1), getFnName(), 2);

        double response = Math.atan2(y, x);

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "atan2";
    }
}

