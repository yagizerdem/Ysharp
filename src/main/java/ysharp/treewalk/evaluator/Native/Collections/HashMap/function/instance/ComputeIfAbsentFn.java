package ysharp.treewalk.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ComputeIfAbsentFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant key      = arguments.get(0);
        Variable.Variant fnVariant = arguments.get(1);

        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant existing = hm.data.get(key);
        if (existing != null) {
            return existing;
        }

        if (!fnVariant.isCallable()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "merge: third argument must be a function."
            );
        }

        Callable fn = fnVariant.asCallable();

        List<Variable.Variant> args = new ArrayList<>();
        if(fn.arity() >= 1) args.add(key);

        Variable.Variant result = fn.call(interpreter, args);

        if (result != null && result.value != null) {
            hm.data.put(key, result);
        }

        return result != null ? new Variable.Variant(result.value) : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "computeIfAbsent";
    }
}
