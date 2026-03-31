package ysharp.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Variable;

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

        yHashTable.yHashTableInstance map = yHashTable.requireHashTableThis(interpreter);

        java.util.ArrayList<Variable.Variant> list =
                new java.util.ArrayList<>();

        for (Variable.Variant value : map.data.values()) {
            list.add(value);
        }

        yArray.yArrayInstance array =
                new yArray.yArrayInstance(list);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "values";
    }
}

