package ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.treewalk.evaluator.Variable;

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

        Variable.Variant key          = arguments.getFirst();
        Variable.Variant defaultValue = arguments.get(1);
        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        Variable.Variant value = whm.data.get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public String getFnName() {
        return "getOrDefault";
    }
}