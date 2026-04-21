package ysharp.treewalk.evaluator.Native.Util.Math.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RandomFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        double response = Math.random();

        return new Variable.Variant(response);
    }

    @Override
    public String getFnName() {
        return "random";
    }
}
