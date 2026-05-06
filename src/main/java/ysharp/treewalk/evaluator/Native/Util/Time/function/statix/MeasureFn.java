package ysharp.treewalk.evaluator.Native.Util.Time.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class MeasureFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, 1, getFnName());

        Variable.Variant fnVar = arguments.getFirst();

        if (!(fnVar.value instanceof Callable)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Time.measure expects callable"
            );
        }

        Callable fn = (Callable) fnVar.value;

        long start = System.nanoTime();

        fn.call(interpreter, List.of());

        long end = System.nanoTime();

        double ms = (end - start) / 1_000_000.0;

        return new Variable.Variant(ms);
    }

    @Override
    public String getFnName() {
        return "measure";
    }
}