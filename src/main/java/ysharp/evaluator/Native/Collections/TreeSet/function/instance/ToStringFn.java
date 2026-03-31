package ysharp.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.evaluator.Variable;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

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

        return new Variable.Variant(sb.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
