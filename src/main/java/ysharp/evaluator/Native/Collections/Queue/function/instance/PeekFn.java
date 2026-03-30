package ysharp.evaluator.Native.Collections.Queue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Queue.yQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class PeekFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yQueue.yQueueInstance queue = yQueue.requireQueueThis(interpreter, getFnName());

        if (queue.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        return new Variable.Variant(queue.data.peek().value);
    }

    @Override
    public String getFnName() {
        return "peek";
    }
}