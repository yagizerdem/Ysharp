package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key = arguments.getFirst();
        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant value = hm.data.get(key);

        return value != null ? new Variable.Variant(value.value) : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "get";
    }
}
