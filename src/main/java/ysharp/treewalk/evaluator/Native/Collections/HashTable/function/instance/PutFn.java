package ysharp.treewalk.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class PutFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
        Variable.Variant key = arguments.get(0);
        Variable.Variant value = arguments.get(1);
        yHashTable.yHashTableInstance array = yHashTable.requireHashTableThis(interpreter);
        array.data.put(key, value);

        return new Variable.Variant(array.data.size());
    }

    @Override
    public String getFnName() {
        return "put";
    }
}
