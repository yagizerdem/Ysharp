package ysharp.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant target = arguments.getFirst();
        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        for (int i = 0; i < pq.heap.size(); i++) {
            if (pq.heap.get(i).value.equals(target)) {
                int last = pq.heap.size() - 1;
                pq.heap.set(i, pq.heap.get(last));
                pq.heap.remove(last);

                if (i < pq.heap.size()) {
                    pq.bubbleUp(i);
                    pq.siftDown(i);
                }

                return new Variable.Variant(true);
            }
        }

        return new Variable.Variant(false);
    }

    @Override
    public String getFnName() {
        return "remove";
    }
}
