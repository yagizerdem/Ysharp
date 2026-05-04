package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class YieldFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Thread.yield();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "yield";
    }
}