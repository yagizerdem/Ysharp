package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.yString;
import ysharp.treewalk.evaluator.Variable;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PlusDaysFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String value = requireString(arguments.get(0), getFnName(), 1);
        int days = requireInt(arguments.get(1), getFnName(), 2);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            LocalDateTime result = dateTime.plusDays(days);

            return new Variable.Variant(
                    new yString.yStringInstance(result.toString())
            );
        } catch (DateTimeParseException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "DateTime.plusDays expected ISO datetime string, got: " + value
            );
        }
    }

    @Override
    public String getFnName() {
        return "plusDays";
    }
}