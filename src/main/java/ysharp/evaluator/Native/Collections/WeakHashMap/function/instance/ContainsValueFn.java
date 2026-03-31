package ysharp.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class ContainsValueFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant value = arguments.getFirst();
        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        return new Variable.Variant(whm.data.containsValue(value));
    }

    @Override
    public String getFnName() {
        return "containsValue";
    }
}
