package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class PutIfAbsentFn extends Function.NativeFunction {

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
        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant existing = hm.data.putIfAbsent(key, value);

        return existing != null ? new Variable.Variant(existing) : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "putIfAbsent";
    }
}
