package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

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

        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (var entry : tm.data.entrySet()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(entry.getKey().toString());
            sb.append("=");
            sb.append(entry.getValue().toString());
        }

        sb.append("}");

        return new Variable.Variant(sb.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}