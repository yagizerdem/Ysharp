package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class FirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,0, "TreeSet.first");

        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        if (ts.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        return ts.data.first();
    }

    @Override
    public String getFnName() {
        return "first";
    }
}
