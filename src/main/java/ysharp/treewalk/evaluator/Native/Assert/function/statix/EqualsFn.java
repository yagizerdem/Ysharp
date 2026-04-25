package ysharp.treewalk.evaluator.Native.Assert.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class EqualsFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant a = arguments.get(0);
        Variable.Variant b = arguments.get(1);

        if(a.value == null || b.value ==  null) {
            return new Variable.Variant(a.value == null && b.value == null);
        }

        if (!a.equals(b)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Assertion failed: expected '" + a.value + "' to equal '" + b.value + "'"
            );
        }

        return new Variable.Variant(true);
    }

    @Override
    public String getFnName() {
        return "equals";
    }
}