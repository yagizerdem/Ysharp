package ysharp.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ToArrayFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArrayDeque.yArrayDequeInstance deque = yArrayDeque.requireArrayDequeThis(interpreter);

        ArrayList<Variable.Variant> result = new ArrayList<>(deque.data);

        yArray.yArrayInstance array = new yArray.yArrayInstance(result);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "toArray";
    }
}

