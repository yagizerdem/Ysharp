package ysharp.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.evaluator.Variable;

import java.util.List;

public class SizeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        return new Variable.Variant(deque.data.size());
    }

    @Override
    public String getFnName() {
        return "size";
    }
}