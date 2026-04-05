package ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ContainsKeyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key = arguments.getFirst();
        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        return new Variable.Variant(whm.data.containsKey(key));
    }

    @Override
    public String getFnName() {
        return "containsKey";
    }
}

