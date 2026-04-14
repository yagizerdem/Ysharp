package ysharp.treewalk.evaluator.Native.Assert.function.statix;


import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class NotEqualsFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant a = arguments.getFirst();
        Variable.Variant b = arguments.get(1);

        if (a.value.equals(b.value)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected '" + a.value + "' to NOT equal '" + b.value + "'"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "notEquals";
    }
}