package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.yString;
import ysharp.treewalk.evaluator.Variable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetDateTimeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        String datetime = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return new Variable.Variant(new yString.yStringInstance(datetime));
    }

    @Override
    public String getFnName() {
        return "getDateTime";
    }
}