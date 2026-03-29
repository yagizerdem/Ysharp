package ysharp.evaluator.Native.Collections.Array.function.statix;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class OfFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return -1; } // variable arity

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();
        newArray.data.addAll(arguments);
        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() { return "of"; }
}
