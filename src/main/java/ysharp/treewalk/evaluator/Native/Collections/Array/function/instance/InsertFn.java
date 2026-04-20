package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class InsertFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant indexVar = arguments.getFirst();
        Variable.Variant value    = arguments.get(1);

        if (!indexVar.canImplicitlyConvertNumber()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'insert' first argument must be a number."
            );
        }

        int index = (int) indexVar.implicitlyConvertNumber();

        if (index < 0 || index > array.data.size()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Index out of bounds for 'insert'."
            );
        }

        array.data.add(index, new Variable.Variant(value.value));

        return new Variable.Variant(array.data.size());
    }

    @Override
    public String getFnName() {
        return "insert";
    }
}