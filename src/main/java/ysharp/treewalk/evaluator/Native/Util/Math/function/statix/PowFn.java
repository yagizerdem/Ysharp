package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PowFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 2, getFnName()) ;

        double base = requireNumber(arguments.getFirst(), getFnName(), 1);
        double power = requireInt(arguments.get(1), getFnName(), 2);
        double response = Math.pow(base, power);

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "pow";
    }
}