package ysharp.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ValuesFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        ArrayList<Variable.Variant> list = new ArrayList<>(whm.data.values());

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "values";
    }
}
