package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "T9Trie.toString");

        yT9Trie.yT9TrieInstance t9 = yT9Trie.requireT9TrieThis(interpreter);

        return new Variable.Variant(t9.data.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
