package ysharp.treewalk.evaluator.Native.Collections.Queue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PollFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yQueue.yQueueInstance queue = yQueue.requireQueueThis(interpreter, getFnName());

        if (queue.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        var data = queue.data.poll();
        if(data == null) return new Variable.Variant(null);
        return new Variable.Variant(data.value);
    }

    @Override
    public String getFnName() {
        return "poll";
    }
}

