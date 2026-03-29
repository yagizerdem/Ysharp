package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class ShiftFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        if (array.data.isEmpty()) {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, 0, "'shift' cannot be called on an empty array.");
        }

        return array.data.removeFirst();
    }

    @Override
    public String getFnName() { return "shift"; }
}

