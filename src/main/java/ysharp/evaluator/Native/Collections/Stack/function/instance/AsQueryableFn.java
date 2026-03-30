package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Native.LINQ.Queryable;
import ysharp.evaluator.Variable;

import java.util.List;

public class AsQueryableFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
        requireArity(arguments, arity(), getFnName());
        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());

        Queryable.QueryableInstance queryable =
                new Queryable.QueryableInstance(stack.data);

        return new Variable.Variant(queryable);
    }

    @Override
    public String getFnName() {
        return "asQueryable";
    }
}
