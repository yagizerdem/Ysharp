package ysharp.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Variable;

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

        yHashTable.yHashTableInstance map = yHashTable.requireHashTableThis(interpreter);

        java.util.ArrayList<Variable.Variant> outerList =
                new java.util.ArrayList<>();

        for (var entry : map.data.entrySet()) {

            java.util.ArrayList<Variable.Variant> pairList =
                    new java.util.ArrayList<>();

            pairList.add(entry.getKey());
            pairList.add(entry.getValue());

            yArray.yArrayInstance pairArray =
                    new yArray.yArrayInstance(pairList);

            outerList.add(new Variable.Variant(pairArray));
        }

        yArray.yArrayInstance resultArray =
                new yArray.yArrayInstance(outerList);

        return new Variable.Variant(resultArray);
    }

    @Override
    public String getFnName() {
        return "entries";
    }
}

