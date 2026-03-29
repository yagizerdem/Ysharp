package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class ConcatFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant otherVar = arguments.getFirst();
        if (!(otherVar.value instanceof yArray.yArrayInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'concat' argument must be an array."
            );
        }

        yArray.yArrayInstance otherArray = (yArray.yArrayInstance) otherVar.value;
        yArray.yArrayInstance newArray = new yArray.yArrayInstance();

        newArray.data.addAll(array.data);
        newArray.data.addAll(otherArray.data);

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() {
        return "concat";
    }
}
