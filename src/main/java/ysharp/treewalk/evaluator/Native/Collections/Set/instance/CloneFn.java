package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.HashSet;
import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpException {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);

        // shallow copy
        HashSet<Variable.Variant> clonedData =
                new HashSet<>(set.data);

        ySet.ySetInstance newSet =
                new ySet.ySetInstance(clonedData);

        return new Variable.Variant(newSet);
    }

    @Override
    public String getFnName() { return "clone"; }
}
