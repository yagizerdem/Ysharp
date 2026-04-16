package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SubMapFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant fromKey = arguments.getFirst();
        Variable.Variant toKey   = arguments.get(1);
        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        yTreeMap.yTreeMapInstance result = new yTreeMap.yTreeMapInstance();
        result.data.putAll(tm.data.subMap(fromKey, toKey));

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "subMap";
    }
}
