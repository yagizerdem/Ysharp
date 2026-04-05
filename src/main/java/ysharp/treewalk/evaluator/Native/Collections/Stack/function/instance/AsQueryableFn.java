package ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
import ysharp.treewalk.evaluator.Native.LINQ.Queryable;
import ysharp.treewalk.evaluator.Variable;

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
