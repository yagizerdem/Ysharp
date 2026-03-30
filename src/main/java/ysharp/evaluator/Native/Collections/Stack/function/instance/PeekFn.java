package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
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

        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());

        if (stack.data.isEmpty()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'peek' cannot be called on an empty stack."
            );
        }

        return stack.data.peek();
    }

    @Override
    public String getFnName() {
        return "peek";
    }
}
