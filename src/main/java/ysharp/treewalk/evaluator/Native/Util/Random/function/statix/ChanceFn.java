package ysharp.treewalk.evaluator.Native.Util.Random.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.Random;

public class ChanceFn extends Function.NativeFunction {

    private static final Random rng = new Random();

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 1, getFnName());

        double probability = requireNumber(arguments.getFirst(), getFnName(), 1);
        boolean response = rng.nextDouble() < probability;

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "chance";
    }
}