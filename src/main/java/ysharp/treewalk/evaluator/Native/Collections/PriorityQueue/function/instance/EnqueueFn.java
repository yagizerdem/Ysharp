package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class EnqueueFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant value = arguments.getFirst();
        Variable.Variant priorityVariant = arguments.get(1);
        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        double priority = ((Number) priorityVariant.value).doubleValue();

        pq.heap.add(new yPriorityQueue.PriorityEntry(value, priority));
        pq.bubbleUp(pq.heap.size() - 1);

        return new Variable.Variant(pq.heap.size());
    }

    @Override
    public String getFnName() {
        return "enqueue";
    }
}

