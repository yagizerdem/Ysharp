package ysharp.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class FirstKeyFn extends Function.NativeFunction {

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

        return tm.data.firstKey();
    }

    @Override
    public String getFnName() {
        return "firstKey";
    }
}
