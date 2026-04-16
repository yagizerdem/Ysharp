package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ConcatFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant otherVar = arguments.getFirst();
        if (!(otherVar.value instanceof yArray.yArrayInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
