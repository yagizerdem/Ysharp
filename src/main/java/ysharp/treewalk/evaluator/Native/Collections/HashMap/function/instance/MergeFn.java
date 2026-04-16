package ysharp.treewalk.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class MergeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant key      = arguments.get(0);
        Variable.Variant value    = arguments.get(1);
        Variable.Variant fnVariant = arguments.get(2);

        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        Variable.Variant existing = hm.data.get(key);

        if (existing == null) {
            hm.data.put(key, value);
            return value;
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
        args.add(existing);
        args.add(value);

        Variable.Variant result = fn.call(interpreter, args);

        if (result == null || result.value == null) {
            hm.data.remove(key);
            return new Variable.Variant(null);
        }

        hm.data.put(key, result);
        return new Variable.Variant(result.value);
    }

    @Override
    public String getFnName() {
        return "merge";
    }
}
