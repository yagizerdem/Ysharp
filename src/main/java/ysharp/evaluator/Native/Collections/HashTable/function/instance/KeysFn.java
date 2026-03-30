package ysharp.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Variable;

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
