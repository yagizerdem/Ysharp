package ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
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

        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (Variable.Variant v : deque.data) {
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