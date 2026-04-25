package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 0, "Trie.toString");

        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);

        return new Variable.Variant(new yString.yStringInstance(trie.data.toString()));
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
