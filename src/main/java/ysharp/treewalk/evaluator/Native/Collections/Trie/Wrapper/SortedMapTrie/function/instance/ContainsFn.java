package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

class ContainsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "Trie.contains");

        Variable.Variant key = arguments.getFirst();
        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);

        return new Variable.Variant(trie.data.contains(key.toString()));
    }

    @Override
    public String getFnName() {
        return "contains";
    }
}