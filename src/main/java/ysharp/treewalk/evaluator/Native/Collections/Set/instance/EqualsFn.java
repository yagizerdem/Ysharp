package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class EqualsFn extends Function.NativeFunction {

    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpException {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);

        Variable.Variant otherVar = arguments.getFirst();

        if (!otherVar.isRuntimeObject() ||
                !(otherVar.asRuntimeObject() instanceof ySet.ySetInstance)) {

            return new Variable.Variant(false);
        }

        ySet.ySetInstance other =
                (ySet.ySetInstance) otherVar.asRuntimeObject();

        if (set.data.size() != other.data.size()) {
            return new Variable.Variant(false);
        }

        boolean result =
                set.data.containsAll(other.data);

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() { return "equals"; }
}