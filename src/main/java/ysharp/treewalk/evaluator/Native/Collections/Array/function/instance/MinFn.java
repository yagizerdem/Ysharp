package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class MinFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        if (array.data.isEmpty()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'min' cannot be called on an empty array."
            );
        }

        Variable.Variant minVar = array.data.getFirst();
        double minVal = minVar.canImplicitlyConvertNumber()
                ? minVar.implicitlyConvertNumber()
                : Double.MAX_VALUE;

        for (int i = 1; i < array.data.size(); i++) {
            Variable.Variant current = array.data.get(i);
            if (current.canImplicitlyConvertNumber()) {
                double currentVal = current.implicitlyConvertNumber();
                if (currentVal < minVal) {
                    minVal = currentVal;
                    minVar = current;
                }
            }
        }

        return minVar;
    }

    @Override
    public String getFnName() {
        return "min";
    }
}
