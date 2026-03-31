package ysharp.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.evaluator.Variable;

import java.util.List;

public class RemoveFirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        if (deque.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        return deque.data.removeFirst();
    }

    @Override
    public String getFnName() {
        return "removeFirst";
    }
}

