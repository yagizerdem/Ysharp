package ysharp.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Variable;

import java.util.Hashtable;
import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yHashTable.yHashTableInstance original = yHashTable.requireHashTableThis(interpreter);

        // shallow copy
        Hashtable<Variable.Variant, Variable.Variant> newTable =
                new Hashtable<>(original.data);

        yHashTable.yHashTableInstance clonedMap = new yHashTable.yHashTableInstance(newTable);

        return new Variable.Variant(clonedMap);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}
