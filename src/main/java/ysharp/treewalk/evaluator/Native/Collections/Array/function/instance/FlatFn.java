package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class FlatFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {
        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        int depth = requireInt(arguments.getFirst(), getFnName(), 1);

        yArray.yArrayInstance result = new yArray.yArrayInstance();
        flatten(array, result, depth);

        return new Variable.Variant(result);
    }

    private void flatten(yArray.yArrayInstance arr, yArray.yArrayInstance result, int depth) {
        for (Variable.Variant element : arr.data) {
            if (depth > 0 && element.value instanceof yArray.yArrayInstance) {
                flatten((yArray.yArrayInstance) element.value, result, depth - 1);
            } else {
                result.data.add(element);
            }
        }
    }

    @Override
    public String getFnName() { return "flat"; }
}

