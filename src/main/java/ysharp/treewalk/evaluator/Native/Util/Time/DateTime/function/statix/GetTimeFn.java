package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetTimeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        String time = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return new Variable.Variant(new yString.yStringInstance(time));
    }

    @Override
    public String getFnName() {
        return "getTime";
    }
}