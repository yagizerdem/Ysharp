package ysharp.treewalk.evaluator.Native.Util.Time.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.time.Instant;
import java.util.List;

public class FormatFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, 1, getFnName());

        double timestamp = requireNumber(arguments.getFirst(), getFnName(), 1);

        Instant instant = Instant.ofEpochSecond((long) timestamp);

        return new Variable.Variant(instant.toString());
    }

    @Override
    public String getFnName() {
        return "format";
    }
}
