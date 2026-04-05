package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsSubsetOfFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,1, "TreeSet.isSubsetOf");

        Variable.Variant otherVariant = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        RuntimeObject otherObj = otherVariant.asRuntimeObject();
        if (!(otherObj instanceof yTreeSet.yTreeSetInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "isSubsetOf: argument must be a TreeSet."
            );
        }

        yTreeSet.yTreeSetInstance other = (yTreeSet.yTreeSetInstance) otherObj;

        return new Variable.Variant(other.data.containsAll(ts.data));
    }

    @Override
    public String getFnName() {
        return "isSubsetOf";
    }
}
