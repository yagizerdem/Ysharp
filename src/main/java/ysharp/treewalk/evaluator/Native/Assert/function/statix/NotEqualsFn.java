package ysharp.treewalk.evaluator.Native.Assert.function.statix;


import ysharp.treewalk.YsharpException;
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
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant a = arguments.getFirst();
        Variable.Variant b = arguments.get(1);

        if (a.equals(b)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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