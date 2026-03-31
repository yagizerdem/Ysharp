package ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.evaluator.Variable;

import java.util.List;

public class DeleteKeyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "T9Trie.deleteKey");

        Variable.Variant key = arguments.getFirst();
        yT9Trie.yT9TrieInstance t9 = yT9Trie.requireT9TrieThis(interpreter);

        t9.data.deleteKey(key.toString());

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "deleteKey";
    }
}
