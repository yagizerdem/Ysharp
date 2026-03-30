package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class ReplaceFn extends Function.NativeFunction {

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

        Variable.Variant old = hm.data.replace(key, value);

        return old != null ? new Variable.Variant(old.value) : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "replace";
    }
}