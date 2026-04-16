package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.HashSet;
import java.util.List;

public class UnionFn extends Function.NativeFunction {

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

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'union' argument must be a Set."
            );
        }

        ySet.ySetInstance other =
                (ySet.ySetInstance) otherVar.asRuntimeObject();

        HashSet<Variable.Variant> newData =
                new HashSet<>(set.data);

        newData.addAll(other.data);

        return new Variable.Variant(
                new ySet.ySetInstance(newData)
        );
    }

    @Override
    public String getFnName() { return "union"; }
}
