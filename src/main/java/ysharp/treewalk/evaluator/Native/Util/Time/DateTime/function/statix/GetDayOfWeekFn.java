package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.time.LocalDateTime;
import java.util.List;

public class GetDayOfWeekFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        String dayOfWeek = LocalDateTime.now()
                .getDayOfWeek()
                .toString();

        return new Variable.Variant(new yString.yStringInstance(dayOfWeek));
    }

    @Override
    public String getFnName() {
        return "getDayOfWeek";
    }
}