package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class AverageFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        if (array.data.isEmpty()) return new Variable.Variant(0);

        double sum = 0;
        int count = 0;

        for (int i = 0; i < array.data.size(); i++) {
            Variable.Variant current = array.data.get(i);
            if (current != null && current.canImplicitlyConvertNumber()) {
                sum += current.implicitlyConvertNumber();
                count++;
            }
        }

        if (count == 0) return new Variable.Variant(0); // Prevents division by zero

        return new Variable.Variant(sum / count);
    }

    @Override
    public String getFnName() {
        return "average";
    }
}
