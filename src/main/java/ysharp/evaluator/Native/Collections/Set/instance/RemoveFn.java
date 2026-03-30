package ysharp.evaluator.Native.Collections.Set.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Set.ySet;
import ysharp.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction {

    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpError {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);

        boolean removed = set.data.remove(arguments.getFirst());
        return new Variable.Variant(removed);
    }

    @Override
    public String getFnName() { return "remove"; }
}

