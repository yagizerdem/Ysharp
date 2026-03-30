package ysharp.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
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

        this.requireArity(arguments, 1, "IdentityHashMap.containsValue");

        Variable.Variant value = arguments.getFirst();
        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        return new Variable.Variant(ihm.data.containsValue(value));
    }

    @Override
    public String getFnName() {
        return "containsValue";
    }
}
