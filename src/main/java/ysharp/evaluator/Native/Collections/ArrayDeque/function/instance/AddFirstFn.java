package ysharp.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant value = arguments.getFirst();
        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        deque.data.addFirst(value);

        return new Variable.Variant(deque.data.size());
    }

    @Override
    public String getFnName() {
        return "addFirst";
    }
}
