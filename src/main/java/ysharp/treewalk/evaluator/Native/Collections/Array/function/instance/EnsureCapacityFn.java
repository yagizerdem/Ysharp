package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class EnsureCapacityFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant capVar = arguments.getFirst();

        if (!capVar.canImplicitlyConvertNumber()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'ensureCapacity' argument must be a number."
            );
        }

        int minCapacity = (int) capVar.implicitlyConvertNumber();

        if (minCapacity < 0) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
