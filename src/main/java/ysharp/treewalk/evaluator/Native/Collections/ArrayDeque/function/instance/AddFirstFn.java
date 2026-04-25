package ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class AddFirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant value = arguments.getFirst();
        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        deque.data.addFirst(new Variable.Variant(value.value));

        return new Variable.Variant(deque.data.size());
    }

    @Override
    public String getFnName() {
        return "addFirst";
    }
}
