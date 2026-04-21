package ysharp.treewalk.evaluator.Native.Util.Random.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import java.util.List;
import java.util.Random;

public class NextFn extends Function.NativeFunction {

    private static final Random rng = new Random();

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        double response = rng.nextDouble();

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "next";
    }
}

