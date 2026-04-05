package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant indexVar = arguments.get(0);

        if (!indexVar.canImplicitlyConvertNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'remove' argument must be a number."
            );
        }

        int index = (int) indexVar.implicitlyConvertNumber();

        if (index < 0 || index >= array.data.size()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Index out of bounds for 'remove'."
            );
        }

        return array.data.remove(index);
    }

    @Override
    public String getFnName() {
        return "remove";
    }
}

