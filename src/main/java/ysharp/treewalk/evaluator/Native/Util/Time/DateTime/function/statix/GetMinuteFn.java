package ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.time.LocalDateTime;
import java.util.List;

public class GetMinuteFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        int minute = LocalDateTime.now().getMinute();

        return new Variable.Variant(minute);
    }

    @Override
    public String getFnName() {
        return "getMinute";
    }
}