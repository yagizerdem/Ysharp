package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsSubsetFn extends Function.NativeFunction {

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
                    "'isSubsetOf' argument must be a Set."
            );
        }

        ySet.ySetInstance other =
                (ySet.ySetInstance) otherVar.asRuntimeObject();

        boolean result =
                other.data.containsAll(set.data);

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() { return "isSubsetOf"; }
}
