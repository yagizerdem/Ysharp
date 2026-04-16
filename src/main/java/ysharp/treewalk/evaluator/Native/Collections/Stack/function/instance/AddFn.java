package ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
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

        Variable.Variant value = arguments.getFirst();
        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());
        stack.data.push(new Variable.Variant(value.value));

        return new Variable.Variant(stack.data.size());
    }

    @Override
    public String getFnName() {
        return "add";
    }
}
