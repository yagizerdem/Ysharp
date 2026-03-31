package ysharp.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;

import java.util.List;

public class EqualsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "IdentityHashMap.equals");

        Variable.Variant otherVariant = arguments.getFirst();
        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        RuntimeObject otherObj = otherVariant.asRuntimeObject();
        if (!(otherObj instanceof yIdentityHashMap.yIdentityHashMapInstance)) {
            return new Variable.Variant(false);
        }

        yIdentityHashMap.yIdentityHashMapInstance other = (yIdentityHashMap.yIdentityHashMapInstance) otherObj;

        return new Variable.Variant(ihm.data.equals(other.data));
    }

    @Override
    public String getFnName() {
        return "equals";
    }
}
