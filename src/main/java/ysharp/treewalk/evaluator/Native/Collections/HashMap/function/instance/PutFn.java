package ysharp.treewalk.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashMap.yHashMap;
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
            throws YsharpException {

        Variable.Variant key   = arguments.get(0);
        Variable.Variant value = arguments.get(1);
        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant previous = hm.data.put(new Variable.Variant(key.value), new Variable.Variant(value.value));

        return previous != null ? previous : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "put";
    }
}
