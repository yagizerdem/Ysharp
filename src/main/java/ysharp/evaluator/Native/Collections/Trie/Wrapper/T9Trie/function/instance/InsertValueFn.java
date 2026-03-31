package ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.evaluator.Variable;

import java.util.List;

public class InsertValueFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 2, "T9Trie.insertValue");

        Variable.Variant key   = arguments.get(0);
        Variable.Variant value = arguments.get(1);
        yT9Trie.yT9TrieInstance t9 = yT9Trie.requireT9TrieThis(interpreter);

        t9.data.insertValue(key.toString(), value);

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "insertValue";
    }
}

