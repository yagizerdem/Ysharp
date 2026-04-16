package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class EntrySetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 0, "IdentityHashMap.entrySet");

        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        ArrayList<Variable.Variant> outerList = new ArrayList<>();

        for (var entry : ihm.data.entrySet()) {
            ArrayList<Variable.Variant> pair = new ArrayList<>();
            pair.add(entry.getKey());
            pair.add(entry.getValue());
            outerList.add(new Variable.Variant(new yArray.yArrayInstance(pair)));
        }

        return new Variable.Variant(new yArray.yArrayInstance(outerList));
    }

    @Override
    public String getFnName() {
        return "entrySet";
    }
}
