package ysharp.evaluator.Native.Collections.Array.function.statix;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class IsArrayFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {
        requireArity(arguments, arity(), getFnName());
        Variable.Variant target = arguments.getFirst();

        boolean isArr = target.value instanceof yArray.yArrayInstance;
        return new Variable.Variant(isArr);
    }

    @Override
    public String getFnName() { return "isArray"; }
}
