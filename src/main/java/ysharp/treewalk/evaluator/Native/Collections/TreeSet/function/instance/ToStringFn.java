package ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("TreeSet[");

        boolean first = true;
        for (Variable.Variant v : ts.data) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(v.toString());
        }

        sb.append("]");

        return new Variable.Variant(new yString.yStringInstance(sb.toString()));
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
