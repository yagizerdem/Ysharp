package ysharp.evaluator.Native.Collections.Queue.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Queue.yQueue;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

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