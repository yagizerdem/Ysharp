package ysharp.treewalk.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class KeysFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yHashTable.yHashTableInstance map = yHashTable.requireHashTableThis(interpreter);

        java.util.ArrayList<Variable.Variant> list =
                new ArrayList<>();

        for (Variable.Variant key : map.data.keySet()) {
            list.add(key);
        }

        yArray.yArrayInstance array =
                new yArray.yArrayInstance(list);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "keys";
    }
}
