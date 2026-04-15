package ysharp.treewalk.evaluator.Native.Assert.function.statix;


import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsTrueFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant cond = arguments.getFirst();

        if (!cond.asBoolean()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected condition to be true"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "isTrue";
    }
}
