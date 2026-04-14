package ysharp.treewalk.evaluator.Native.Assert.function.statix;


import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class NullFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant val = arguments.getFirst();

        if (val.value != null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected value to be null but got '" + val.value + "'"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "null";
    }
}
