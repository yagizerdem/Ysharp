package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PutAllFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 1, "IdentityHashMap.putAll");

        Variable.Variant otherVariant = arguments.getFirst();
        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        RuntimeObject otherObj = otherVariant.asRuntimeObject();
        if (!(otherObj instanceof yIdentityHashMap.yIdentityHashMapInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "putAll: argument must be an IdentityHashMap."
            );
        }

        yIdentityHashMap.yIdentityHashMapInstance other = (yIdentityHashMap.yIdentityHashMapInstance) otherObj;
        ihm.data.putAll(other.data);

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "putAll";
    }
}
