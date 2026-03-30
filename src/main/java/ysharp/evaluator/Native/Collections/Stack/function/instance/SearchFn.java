package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Variable;

import java.util.List;

public class SearchFn extends Function.NativeFunction implements Callable {

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

        // Stack top -> end of list
        for (int i = stack.data.size() - 1; i >= 0; i--) {

            Variable.Variant element = stack.data.get(i);

            if (element == null && target == null) {
                return new Variable.Variant(stack.data.size() - i);
            }

            if (element != null && element.equals(target)) {
                return new Variable.Variant(stack.data.size() - i);
            }
        }

        return new Variable.Variant(-1);
    }

    @Override
    public String getFnName() {
        return "search";
    }
}
