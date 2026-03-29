package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class FillFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        Variable.Variant value = arguments.getFirst();

        for (int i = 0; i < array.data.size(); i++) {
            array.data.set(i, value);
        }

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "fill";
    }
}
