package ysharp.evaluator.Native.Collections.LinkedHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedHashMap.yLinkedHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class RemoveFirstFn extends Function.NativeFunction {

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

        Variable.Variant firstKey = lhm.data.keySet().iterator().next();

        return lhm.data.remove(firstKey);
    }

    @Override
    public String getFnName() {
        return "removeFirst";
    }
}

