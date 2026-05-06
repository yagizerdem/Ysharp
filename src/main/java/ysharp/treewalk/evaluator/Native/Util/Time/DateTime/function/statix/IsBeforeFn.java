package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class IsBeforeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String leftValue = requireString(arguments.get(0), getFnName(), 1);
        String rightValue = requireString(arguments.get(1), getFnName(), 2);

        try {
            LocalDateTime left = LocalDateTime.parse(leftValue);
            LocalDateTime right = LocalDateTime.parse(rightValue);

            return new Variable.Variant(left.isBefore(right));
        } catch (DateTimeParseException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "DateTime.isBefore expected ISO datetime strings"
            );
        }
    }

    @Override
    public String getFnName() {
        return "isBefore";
    }
}