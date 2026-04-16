package ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
import ysharp.treewalk.evaluator.Native.Collections.PriorityQueue.yPriorityQueue;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class AddAllFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());
        Variable.Variant other = arguments.getFirst();

        if (other.value instanceof yStack.yStackInstance) {
            stack.data.addAll(((yStack.yStackInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
        }
        else if (other.value instanceof yArray.yArrayInstance) {
            stack.data.addAll(((yArray.yArrayInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
        }
        else if (other.value instanceof yQueue.yQueueInstance) {
            stack.data.addAll(((yQueue.yQueueInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
        }
        else if (other.value instanceof ySet.ySetInstance) {
            stack.data.addAll(((ySet.ySetInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
        }
        else if (other.value instanceof yPriorityQueue.yPriorityQueueInstance) {
            stack.data.addAll(((yPriorityQueue.yPriorityQueueInstance) other.value).getRawVariants().stream().map(x -> new Variable.Variant(x.value)).toList());
        }
        else {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, 0, "Argument must be a vector based collection");
        }

        return new Variable.Variant(stack.data.size());
    }

    @Override
    public String getFnName() { return "addAll"; }
}
