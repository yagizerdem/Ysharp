package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PeekPriorityFn extends Function.NativeFunction {

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

        return new Variable.Variant(pq.heap.getFirst().priority);
    }

    @Override
    public String getFnName() {
        return "peekPriority";
    }
}
