package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class LastIndexOfFn extends Function.NativeFunction implements Callable {

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

        for (int i = array.data.size() - 1; i >= 0; i--) {
            Variable.Variant element = array.data.get(i);

            if (element == null && target == null) {
                return new Variable.Variant(i);
            }

            if (element != null && element.equals(target)) {
                return new Variable.Variant(i);
            }
        }

        return new Variable.Variant(-1);
    }

    @Override
    public String getFnName() {
        return "lastIndexOf";
    }
}
