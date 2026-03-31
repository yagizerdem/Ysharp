package ysharp.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
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

        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("Deque[");

        boolean first = true;
        for (Variable.Variant v : deque.data) {
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