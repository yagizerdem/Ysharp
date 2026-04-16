package ysharp.treewalk.evaluator.Native.Assert.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class NotNullFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant val = arguments.getFirst();

        if (val.value == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected value to NOT be null"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "notNull";
    }
}
