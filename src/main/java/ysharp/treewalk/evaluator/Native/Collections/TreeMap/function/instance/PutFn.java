package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PutFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key   = arguments.getFirst();
        Variable.Variant value = arguments.get(1);
        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        Variable.Variant previous = tm.data.put(key, value);

        return previous != null ? previous : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "put";
    }
}
