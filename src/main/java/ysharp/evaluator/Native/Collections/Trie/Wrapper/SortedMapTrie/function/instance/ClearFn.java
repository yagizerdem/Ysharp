package ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.evaluator.Variable;

import java.util.List;

public class ClearFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "Trie.clear");

        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);
        trie.data.clear();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "clear";
    }
}

