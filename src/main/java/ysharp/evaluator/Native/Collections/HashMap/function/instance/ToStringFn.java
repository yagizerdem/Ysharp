package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
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

        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (var entry : hm.data.entrySet()) {
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
