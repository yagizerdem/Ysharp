package ysharp.treewalk.evaluator.Native.Util.Random.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.Random;

public class NextFloatFn extends Function.NativeFunction {

    private static final Random rng = new Random();

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 2, getFnName());

        double min = requireNumber(arguments.get(0), getFnName(), 1);
        double max = requireNumber(arguments.get(1), getFnName(), 2);

        double response = min + (max - min) * rng.nextDouble();

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "nextFloat";
    }
}