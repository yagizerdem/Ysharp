package ysharp.treewalk.evaluator.Native.Util.Time.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SecondsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        double seconds = System.currentTimeMillis() / 1000.0;

        return new Variable.Variant(seconds);
    }

    @Override
    public String getFnName() {
        return "seconds";
    }
}

