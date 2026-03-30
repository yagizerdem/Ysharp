package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Variable;

import java.util.List;
import java.util.Stack;

public class CloneFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());

        Stack<Variable.Variant> clonedData =
                (Stack<Variable.Variant>) stack.data.clone();

        yStack.yStackInstance newStack =
                new yStack.yStackInstance(clonedData);

        return new Variable.Variant(newStack);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}
