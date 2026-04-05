package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yPriorityQueue.yPriorityQueueInstance pq = yPriorityQueue.requirePriorityQueueThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("PriorityQueue[");

        for (int i = 0; i < pq.heap.size(); i++) {
            if (i > 0) sb.append(", ");
            yPriorityQueue.PriorityEntry entry = pq.heap.get(i);
            sb.append("(").append(entry.value.toString())
                    .append(", p=").append(entry.priority).append(")");
        }

        sb.append("]");

        return new Variable.Variant(sb.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}