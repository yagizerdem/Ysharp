package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class MaxFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        if (array.data.isEmpty()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'max' cannot be called on an empty array."
            );
        }

        Variable.Variant maxVar = array.data.getFirst();
        double maxVal = maxVar.canImplicitlyConvertNumber()
                ? maxVar.implicitlyConvertNumber()
                : Double.MIN_VALUE;

        for (int i = 1; i < array.data.size(); i++) {
            Variable.Variant current = array.data.get(i);
            if (current.canImplicitlyConvertNumber()) {
                double currentVal = current.implicitlyConvertNumber();
                if (currentVal > maxVal) {
                    maxVal = currentVal;
                    maxVar = current;
                }
            }
        }

        return maxVar;
    }

    @Override
    public String getFnName() {
        return "max";
    }
}
