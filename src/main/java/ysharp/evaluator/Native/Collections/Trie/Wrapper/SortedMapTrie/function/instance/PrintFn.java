package ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.evaluator.Variable;

import java.util.List;

public class PrintFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "Trie.print");

        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);
        trie.data.print();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "print";
    }
}
