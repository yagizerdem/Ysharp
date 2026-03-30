package ysharp.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class HashCodeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "IdentityHashMap.hashCode");

        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        return new Variable.Variant(ihm.data.hashCode());
    }

    @Override
    public String getFnName() {
        return "hashCode";
    }
}

