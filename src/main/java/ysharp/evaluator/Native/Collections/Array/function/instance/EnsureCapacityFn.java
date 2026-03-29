package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class EnsureCapacityFn extends Function.NativeFunction implements Callable {

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

        Variable.Variant capVar = arguments.getFirst();

        if (!capVar.canImplicitlyConvertNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'ensureCapacity' argument must be a number."
            );
        }

        int minCapacity = (int) capVar.implicitlyConvertNumber();

        if (minCapacity < 0) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'ensureCapacity' capacity cannot be negative."
            );
        }

        array.data.ensureCapacity(minCapacity);

        return new Variable.Variant(array.data.size());
    }

    @Override
    public String getFnName() {
        return "ensureCapacity";
    }
}
