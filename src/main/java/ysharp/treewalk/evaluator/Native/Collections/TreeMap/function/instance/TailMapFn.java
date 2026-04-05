package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class TailMapFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant fromKey = arguments.getFirst();
        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        yTreeMap.yTreeMapInstance result = new yTreeMap.yTreeMapInstance();
        result.data.putAll(tm.data.tailMap(fromKey));

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "tailMap";
    }
}
