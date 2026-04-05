package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IndexOfFn extends Function.NativeFunction implements Callable {

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

        for (int i = 0; i < array.data.size(); i++) {
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
        return "indexOf";
    }
}
