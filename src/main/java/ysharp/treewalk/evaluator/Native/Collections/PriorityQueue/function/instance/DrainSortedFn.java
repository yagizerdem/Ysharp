package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class DrainSortedFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        java.util.ArrayList<Variable.Variant> result =
                new java.util.ArrayList<>();

        while (!pq.heap.isEmpty()) {
            yPriorityQueue.PriorityEntry top = pq.heap.getFirst();
            int last = pq.heap.size() - 1;
            pq.heap.set(0, pq.heap.get(last));
            pq.heap.remove(last);
            if (!pq.heap.isEmpty()) {
                pq.siftDown(0);
            }
            result.add(top.value);
        }

        yArray.yArrayInstance array =
                new yArray.yArrayInstance(result);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "drainSorted";
    }
}
