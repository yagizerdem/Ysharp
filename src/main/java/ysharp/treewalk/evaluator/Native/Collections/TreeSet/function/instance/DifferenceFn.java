package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class DifferenceFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments,1, "TreeSet.difference");

        Variable.Variant otherVariant = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        RuntimeObject otherObj = otherVariant.asRuntimeObject();
        if (!(otherObj instanceof yTreeSet.yTreeSetInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "difference: argument must be a TreeSet."
            );
        }

        yTreeSet.yTreeSetInstance other  = (yTreeSet.yTreeSetInstance) otherObj;
        yTreeSet.yTreeSetInstance result = new yTreeSet.yTreeSetInstance();

        for (Variable.Variant v : ts.data) {
            if (!other.data.contains(v)) {
                result.data.add(v);
            }
        }

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "difference";
    }
}
