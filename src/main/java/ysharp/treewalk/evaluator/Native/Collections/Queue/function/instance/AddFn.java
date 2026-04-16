package ysharp.treewalk.evaluator.Native.Collections.Queue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class AddFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yQueue.yQueueInstance queue = yQueue.requireQueueThis(interpreter, getFnName());

        Variable.Variant value = arguments.getFirst();

        queue.data.add(new Variable.Variant(value.value));

        return new Variable.Variant(queue.data.size());
    }

    @Override
    public String getFnName() {
        return "add";
    }
}