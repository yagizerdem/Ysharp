package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class FlatFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {
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

