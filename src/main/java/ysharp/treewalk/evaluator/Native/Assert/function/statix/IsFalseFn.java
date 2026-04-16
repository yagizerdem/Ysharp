package ysharp.treewalk.evaluator.Native.Assert.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsFalseFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant cond = arguments.getFirst();

        if (cond.asBoolean()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected condition to be false"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "isFalse";
    }
}