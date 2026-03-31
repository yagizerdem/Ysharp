package ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.evaluator.Variable;

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

        this.requireArity(arguments, 0, "Trie.toString");

        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        return new Variable.Variant(trie.data.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
