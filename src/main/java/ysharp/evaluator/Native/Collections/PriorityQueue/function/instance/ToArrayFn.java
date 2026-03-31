package ysharp.evaluator.Native.Collections.PriorityQueue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class ToArrayFn extends Function.NativeFunction {

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

        for (yPriorityQueue.PriorityEntry entry : pq.heap) {
            result.add(entry.value);
        }

        yArray.yArrayInstance array =
                new yArray.yArrayInstance(result);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "toArray";
    }
}