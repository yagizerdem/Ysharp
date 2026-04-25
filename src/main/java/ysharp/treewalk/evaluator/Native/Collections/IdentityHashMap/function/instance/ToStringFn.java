package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
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

        this.requireArity(arguments, 0, "IdentityHashMap.toString");

        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (var entry : ihm.data.entrySet()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(entry.getKey().toString());
            sb.append("=");
            sb.append(entry.getValue().toString());
        }

        sb.append("}");

        return new Variable.Variant(new yString.yStringInstance(sb.toString()));
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
