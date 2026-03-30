package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Variable;

import java.util.List;

public class PeekOrNullFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());
        if (stack.data.isEmpty()) return new Variable.Variant(null);
        return stack.data.peek();
    }

    @Override
    public String getFnName() { return "peekOrNull"; }
}
