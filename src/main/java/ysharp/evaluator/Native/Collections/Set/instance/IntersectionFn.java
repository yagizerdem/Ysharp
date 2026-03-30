package ysharp.evaluator.Native.Collections.Set.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Set.ySet;
import ysharp.evaluator.Variable;

import java.util.HashSet;
import java.util.List;

public class IntersectionFn extends Function.NativeFunction {

    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpError {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);

        Variable.Variant otherVar = arguments.getFirst();

        if (!otherVar.isRuntimeObject() ||
                !(otherVar.asRuntimeObject() instanceof ySet.ySetInstance)) {

            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'intersection' argument must be a Set."
            );
        }

        ySet.ySetInstance other =
                (ySet.ySetInstance) otherVar.asRuntimeObject();

        HashSet<Variable.Variant> newData =
                new HashSet<>(set.data);

        newData.retainAll(other.data);

        return new Variable.Variant(
                new ySet.ySetInstance(newData)
        );
    }

    @Override
    public String getFnName() { return "intersection"; }
}
