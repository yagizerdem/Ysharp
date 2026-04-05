package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class LastKeyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        if (tm.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        return tm.data.lastKey();
    }

    @Override
    public String getFnName() {
        return "lastKey";
    }
}
