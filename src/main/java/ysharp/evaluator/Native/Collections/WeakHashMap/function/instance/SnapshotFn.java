package ysharp.evaluator.Native.Collections.WeakHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Native.Collections.WeakHashMap.yWeakHashMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class SnapshotFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yWeakHashMap.yWeakHashMapInstance whm = yWeakHashMap.requireWeakHashMapThis(interpreter);

        java.util.Hashtable<Variable.Variant, Variable.Variant> snap =
                new java.util.Hashtable<>(whm.data);

        yHashTable.yHashTableInstance hashTableObject =
                new yHashTable.yHashTableInstance(snap);

        return new Variable.Variant(hashTableObject);
    }

    @Override
    public String getFnName() {
        return "snapshot";
    }
}

