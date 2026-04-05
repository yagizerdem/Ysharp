package ysharp.treewalk.evaluator.Native.Collections.LinkedHashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedHashMap.yLinkedHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RemoveLastFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedHashMap.yLinkedHashMapInstance lhm = yLinkedHashMap.requireLinkedHashMapThis(interpreter);

        if (lhm.data.isEmpty()) {
            return new Variable.Variant(null);
        }

        Variable.Variant lastKey = null;
        for (Variable.Variant k : lhm.data.keySet()) {
            lastKey = k;
        }

        return lhm.data.remove(lastKey);
    }

    @Override
    public String getFnName() {
        return "removeLast";
    }
}

