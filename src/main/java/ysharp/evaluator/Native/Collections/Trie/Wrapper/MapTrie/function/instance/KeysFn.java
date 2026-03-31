package ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.evaluator.Variable;
import java.util.ArrayList;
import java.util.List;

public class KeysFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "Trie.keys");

        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        List<String> keyList = trie.data.keys();

        ArrayList<Variable.Variant> list = new ArrayList<>();
        for (String s : keyList) {
            list.add(new Variable.Variant(s));
        }

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "keys";
    }
}
