package ysharp.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class DequeueFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        if (pq.heap.isEmpty()) {
            return new Variable.Variant(null);
        }

        yPriorityQueue.PriorityEntry top = pq.heap.getFirst();
        int last = pq.heap.size() - 1;

        pq.heap.set(0, pq.heap.get(last));
        pq.heap.remove(last);

        if (!pq.heap.isEmpty()) {
            pq.siftDown(0);
        }

        return top.value;
    }

    @Override
    public String getFnName() {
        return "dequeue";
    }
}
