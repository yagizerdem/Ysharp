package ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.ySortedMapTrie;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class GetValueSuggestionsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "Trie.getValueSuggestions");

        Variable.Variant prefix = arguments.getFirst();
        ySortedMapTrie.ySortedMapTrieInstance trie = ySortedMapTrie.requireTrieThis(interpreter);

        List<Variable.Variant> suggestions =
                trie.data.getValueSuggestions(prefix.toString());

        ArrayList<Variable.Variant> list = new ArrayList<>(suggestions);

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "getValueSuggestions";
    }
}

