package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SubSetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,2, "TreeSet.subSet");

        Variable.Variant from = arguments.getFirst();
        Variable.Variant to   = arguments.get(1);
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        yTreeSet.yTreeSetInstance result = new yTreeSet.yTreeSetInstance();
        result.data.addAll(ts.data.subSet(from, to));

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "subSet";
    }
}
