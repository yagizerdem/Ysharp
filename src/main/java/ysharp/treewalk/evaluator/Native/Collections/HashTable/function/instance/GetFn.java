package ysharp.treewalk.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class GetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key = arguments.getFirst();
        yHashTable.yHashTableInstance map = yHashTable.requireHashTableThis(interpreter);

        Variable.Variant value = map.data.get(key);

        if (value == null) {
            return new Variable.Variant(null);
        }

        return value;
    }

    @Override
    public String getFnName() {
        return "get";
    }
}

