package ysharp.evaluator.Native.Collections.HashTable.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashTable.yHashTable;
import ysharp.evaluator.Variable;

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
