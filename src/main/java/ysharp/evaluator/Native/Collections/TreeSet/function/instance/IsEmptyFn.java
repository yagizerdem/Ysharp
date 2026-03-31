package ysharp.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.evaluator.Variable;

import java.util.List;

public class IsEmptyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,0, "TreeSet.isEmpty");

        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        return new Variable.Variant(ts.data.isEmpty());
    }

    @Override
    public String getFnName() {
        return "isEmpty";
    }
}

