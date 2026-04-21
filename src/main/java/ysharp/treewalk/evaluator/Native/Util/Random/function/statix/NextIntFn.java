package ysharp.treewalk.evaluator.Native.Util.Random.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.Random;

public class NextIntFn extends Function.NativeFunction {

    private static final Random rng = new Random();

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 2, getFnName());

        double min = requireInt(arguments.get(0), getFnName(), 1);
        double max = requireInt(arguments.get(1), getFnName(), 2);

        int response = ((int) min + rng.nextInt((int) max - (int) min));

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "nextInt";
    }
}
