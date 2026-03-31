package ysharp.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class ContainsFn extends Function.NativeFunction {

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

        for (yPriorityQueue.PriorityEntry entry : pq.heap) {
            if (entry.value.equals(target)) {
                return new Variable.Variant(true);
            }
        }

        return new Variable.Variant(false);
    }

    @Override
    public String getFnName() {
        return "contains";
    }
}