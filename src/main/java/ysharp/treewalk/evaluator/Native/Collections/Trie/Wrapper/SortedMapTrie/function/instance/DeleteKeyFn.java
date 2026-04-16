package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class DeleteKeyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 1, "Trie.deleteKey");

        Variable.Variant key = arguments.getFirst();
        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);

        trie.data.deleteKey(key.toString());

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "deleteKey";
    }
}
