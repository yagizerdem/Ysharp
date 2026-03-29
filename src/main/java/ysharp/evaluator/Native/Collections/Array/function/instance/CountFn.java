package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class CountFn extends Function.NativeFunction implements Callable {

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
        Variable.Variant target = arguments.getFirst();

        int count = 0;

        for (Variable.Variant element : array.data) {
            if ((element == null && target == null) ||
                    (element != null && element.equals(target))) {
                count++;
            }
        }

        return new Variable.Variant(count);
    }

    @Override
    public String getFnName() {
        return "count";
    }
}
