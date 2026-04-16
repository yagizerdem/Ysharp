package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ContainsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 1, "Trie.contains");

        Variable.Variant key = arguments.getFirst();
        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        return new Variable.Variant(trie.data.contains(key.toString()));
    }

    @Override
    public String getFnName() {
        return "contains";
    }
}
