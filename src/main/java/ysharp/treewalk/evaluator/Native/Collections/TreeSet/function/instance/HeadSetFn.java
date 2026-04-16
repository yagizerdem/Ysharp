package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class HeadSetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments,1, "TreeSet.headSet");

        Variable.Variant to = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        yTreeSet.yTreeSetInstance result = new yTreeSet.yTreeSetInstance();
        result.data.addAll(ts.data.headSet(to));

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "headSet";
    }
}
