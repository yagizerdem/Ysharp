package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.LinkedList;
import java.util.List;

public class InsertFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 2, "T9Trie.insert");

        Variable.Variant key         = arguments.getFirst();
        Variable.Variant valuesVariant = arguments.get(1);
        yT9Trie.yT9TrieInstance t9 = yT9Trie.requireT9TrieThis(interpreter);

        RuntimeObject obj = valuesVariant.asRuntimeObject();
        if (!(obj instanceof yArray.yArrayInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "T9Trie.insert: second argument must be an Array of values."
            );
        }

        yArray.yArrayInstance arr = (yArray.yArrayInstance) obj;
        LinkedList<Variable.Variant> list = new LinkedList<>(arr.data);

        t9.data.insert(key.toString(), list);

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "insert";
    }
}