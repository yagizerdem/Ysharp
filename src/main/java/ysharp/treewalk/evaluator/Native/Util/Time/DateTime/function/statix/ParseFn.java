package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.yString;
import ysharp.treewalk.evaluator.Variable;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ParseFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String value = requireString(arguments.getFirst(), getFnName(), 1);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            return new Variable.Variant(
                    new yString.yStringInstance(dateTime.toString())
            );
        } catch (DateTimeParseException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "DateTime.parse expected ISO datetime string, got: " + value
            );
        }
    }

    @Override
    public String getFnName() {
        return "parse";
    }
}