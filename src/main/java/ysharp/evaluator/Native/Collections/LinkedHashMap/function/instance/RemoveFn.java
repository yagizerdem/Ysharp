package ysharp.evaluator.Native.Collections.LinkedHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedHashMap.yLinkedHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key = arguments.getFirst();
        yLinkedHashMap.yLinkedHashMapInstance lhm = yLinkedHashMap.requireLinkedHashMapThis(interpreter);

        Variable.Variant removed = lhm.data.remove(key);

        return removed != null ? removed : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "remove";
    }
}

