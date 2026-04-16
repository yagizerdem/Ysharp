package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ChangePriorityFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant target = arguments.getFirst();
        Variable.Variant newPriorityVariant = arguments.get(1);
        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        double newPriority = ((Number) newPriorityVariant.value).doubleValue();

        for (int i = 0; i < pq.heap.size(); i++) {
            if (pq.heap.get(i).value.equals(target)) {
                pq.heap.get(i).priority = newPriority;
                pq.bubbleUp(i);
                pq.siftDown(i);
                return new Variable.Variant(true);
            }
        }

        return new Variable.Variant(false);
    }

    @Override
    public String getFnName() {
        return "changePriority";
    }
}
