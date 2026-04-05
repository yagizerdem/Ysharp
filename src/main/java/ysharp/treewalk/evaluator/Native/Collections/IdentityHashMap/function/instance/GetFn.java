package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.Variable;

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

        this.requireArity(arguments, 1, "IdentityHashMap.get");

        Variable.Variant key = arguments.getFirst();
        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        Variable.Variant value = ihm.data.get(key);

        return value != null ? value : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "get";
    }
}

