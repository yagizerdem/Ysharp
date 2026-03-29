package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class InsertFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant indexVar = arguments.get(0);
        Variable.Variant value    = arguments.get(1);

        if (!indexVar.canImplicitlyConvertNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'insert' first argument must be a number."
            );
        }

        int index = (int) indexVar.implicitlyConvertNumber();

        if (index < 0 || index > array.data.size()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Index out of bounds for 'insert'."
            );
        }

        array.data.add(index, value);

        return new Variable.Variant(array.data.size());
    }

    @Override
    public String getFnName() {
        return "insert";
    }
}