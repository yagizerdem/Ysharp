package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsEmptyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 0, "Trie.isEmpty");

        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        return new Variable.Variant(trie.data.size() == 0);
    }

    @Override
    public String getFnName() {
        return "isEmpty";
    }
}
