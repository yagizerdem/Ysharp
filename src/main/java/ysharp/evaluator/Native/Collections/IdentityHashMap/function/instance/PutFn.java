package ysharp.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class PutFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 2, "IdentityHashMap.put");

        Variable.Variant key   = arguments.get(0);
        Variable.Variant value = arguments.get(1);
        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        Variable.Variant previous = ihm.data.put(key, value);

        return previous != null ? previous : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "put";
    }
}