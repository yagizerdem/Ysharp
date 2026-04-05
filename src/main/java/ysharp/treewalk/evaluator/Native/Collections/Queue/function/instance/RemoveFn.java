package ysharp.treewalk.evaluator.Native.Collections.Queue.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction implements Callable {

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
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'remove' cannot be called on an empty queue."
            );
        }

        return new Variable.Variant(queue.data.remove().value);
    }

    @Override
    public String getFnName() {
        return "remove";
    }
}