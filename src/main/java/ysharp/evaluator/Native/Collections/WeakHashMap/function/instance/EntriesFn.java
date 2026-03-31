package ysharp.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class EntriesFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        ArrayList<Variable.Variant> outerList = new ArrayList<>();

        for (var entry : whm.data.entrySet()) {
            ArrayList<Variable.Variant> pair = new ArrayList<>();
            pair.add(entry.getKey());
            pair.add(entry.getValue());
            outerList.add(new Variable.Variant(new yArray.yArrayInstance(pair)));
        }

        return new Variable.Variant(new yArray.yArrayInstance(outerList));
    }

    @Override
    public String getFnName() {
        return "entries";
    }
}

