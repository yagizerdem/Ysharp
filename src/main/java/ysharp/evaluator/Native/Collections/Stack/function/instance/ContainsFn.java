package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Variable;

import java.util.List;

public class ContainsFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());

        Variable.Variant target = arguments.getFirst();

        for (Variable.Variant element : stack.data) {
            if (element != null && element.equals(target)) {
                return new Variable.Variant(true);
            }
        }

        return new Variable.Variant(false);
    }

    @Override
    public String getFnName() {
        return "contains";
    }
}
