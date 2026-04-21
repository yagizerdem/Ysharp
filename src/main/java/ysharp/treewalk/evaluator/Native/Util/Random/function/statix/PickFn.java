package ysharp.treewalk.evaluator.Native.Util.Random.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.Random;

public class PickFn extends Function.NativeFunction {

    private static final Random rng = new Random();

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 2, getFnName());

        Variable.Variant a = arguments.get(0);
        Variable.Variant b = arguments.get(1);

        return rng.nextBoolean() ? a : b;
    }

    @Override
    public String getFnName() {
        return "pick";
    }
}