package ysharp.treewalk.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RemoveFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant key = arguments.getFirst();
        yHashTable.yHashTableInstance map = yHashTable.requireHashTableThis(interpreter);

        Variable.Variant removed = map.data.remove(key);

        if (removed == null) {
            return new Variable.Variant(null);
        }

        return removed;
    }

    @Override
    public String getFnName() {
        return "remove";
    }
}

