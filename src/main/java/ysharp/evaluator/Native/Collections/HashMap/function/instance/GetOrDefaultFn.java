package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetOrDefaultFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key          = arguments.get(0);
        Variable.Variant defaultValue = arguments.get(1);
        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant value = hm.data.get(key);

        return value != null ? new Variable.Variant(value.value) : new Variable.Variant(defaultValue.value);
    }

    @Override
    public String getFnName() {
        return "getOrDefault";
    }
}

