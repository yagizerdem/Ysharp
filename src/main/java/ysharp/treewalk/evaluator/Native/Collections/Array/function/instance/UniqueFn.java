package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class UniqueFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();

        for (Variable.Variant element : array.data) {
            boolean exists = false;
            for (Variable.Variant existing : newArray.data) {
                if ((element == null && existing == null) ||
                        (element != null && element.equals(existing))) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                newArray.data.add(element);
            }
        }

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() {
        return "unique";
    }
}