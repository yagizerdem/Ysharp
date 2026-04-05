package ysharp.treewalk.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.treewalk.evaluator.Variable;

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

        yHashMap.yHashMapInstance hm = yHashMap.requireHashMapThis(interpreter);

        ArrayList<Variable.Variant> outerList = new ArrayList<>();

        for (var entry : hm.data.entrySet()) {
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

