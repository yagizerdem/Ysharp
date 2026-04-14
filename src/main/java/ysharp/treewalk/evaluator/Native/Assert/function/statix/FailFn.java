package ysharp.treewalk.evaluator.Native.Assert.function.statix;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class FailFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        String message = "Assertion failed";

        if (!arguments.isEmpty()) {
            message = arguments.getFirst().value.toString();
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                0,
                message
        );
    }

    @Override
    public String getFnName() {
        return "fail";
    }
}