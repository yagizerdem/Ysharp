package ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
import ysharp.treewalk.evaluator.Variable;

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
