package ysharp.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class ClearFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);
        pq.heap.clear();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "clear";
    }
}
