package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class GetKeySuggestionsFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "Trie.getKeySuggestions");

        Variable.Variant prefix = arguments.getFirst();
        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        List<String> suggestions = trie.data.getKeySuggestions(prefix.toString());

        ArrayList<Variable.Variant> list = new ArrayList<>();
        for (String s : suggestions) {
            list.add(new Variable.Variant(s));
        }

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "getKeySuggestions";
    }
}
