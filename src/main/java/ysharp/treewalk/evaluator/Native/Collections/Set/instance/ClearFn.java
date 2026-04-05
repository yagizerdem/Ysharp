package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ClearFn extends Function.NativeFunction {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpError {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);
        set.data.clear();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() { return "clear"; }
}
