package ysharp.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
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
        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        Variable.Variant existing = whm.data.putIfAbsent(key, value);

        return existing != null ? existing : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "putIfAbsent";
    }
}

