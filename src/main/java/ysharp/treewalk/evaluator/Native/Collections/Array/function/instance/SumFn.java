package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SumFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        double sum = 0;

        for (int i = 0; i < array.data.size(); i++) {
            Variable.Variant current = array.data.get(i);
            if (current != null && current.canImplicitlyConvertNumber()) {
                sum += current.implicitlyConvertNumber();
            }
        }

        return new Variable.Variant(sum);
    }

    @Override
    public String getFnName() {
        return "sum";
    }
}

