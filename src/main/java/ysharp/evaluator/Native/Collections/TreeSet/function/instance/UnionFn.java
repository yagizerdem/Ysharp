package ysharp.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;

import java.util.List;

public class UnionFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,1, "TreeSet.union");

        Variable.Variant otherVariant = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        RuntimeObject otherObj = otherVariant.asRuntimeObject();
        if (!(otherObj instanceof yTreeSet.yTreeSetInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "union: argument must be a TreeSet."
            );
        }

        yTreeSet.yTreeSetInstance other  = (yTreeSet.yTreeSetInstance) otherObj;
        yTreeSet.yTreeSetInstance result = new yTreeSet.yTreeSetInstance();

        result.data.addAll(ts.data);
        result.data.addAll(other.data);

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "union";
    }
}
