package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormatFn extends Function.NativeFunction {

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
        String pattern = requireString(arguments.get(1), getFnName(), 2);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            return new Variable.Variant(
                    new yString.yStringInstance(dateTime.format(formatter))
            );
        } catch (DateTimeParseException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "DateTime.format expected ISO datetime string, got: " + value
            );
        } catch (IllegalArgumentException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "DateTime.format invalid pattern: " + pattern
            );
        }
    }

    @Override
    public String getFnName() {
        return "format";
    }
}