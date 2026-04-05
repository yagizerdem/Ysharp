package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class TakeFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        int n = requireInt(arguments.getFirst(), getFnName(), 1);

        int limit = Math.max(0, Math.min(n, array.data.size()));

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();
        newArray.data.addAll(array.data.subList(0, limit));

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() { return "take"; }
}
