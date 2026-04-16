package ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yPriorityQueue.yPriorityQueueInstance original = yPriorityQueue.requirePriorityQueueThis(interpreter);
        yPriorityQueue.yPriorityQueueInstance cloned = new yPriorityQueue.yPriorityQueueInstance();

        for (yPriorityQueue.PriorityEntry entry : original.heap) {
            cloned.heap.add(new yPriorityQueue.PriorityEntry(entry.value, entry.priority));
        }

        return new Variable.Variant(cloned);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}

