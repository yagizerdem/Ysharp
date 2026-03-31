package ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ValuesFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 0, "Trie.values");

        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        List<Variable.Variant> valueList = trie.data.values();

        ArrayList<Variable.Variant> list = new ArrayList<>(valueList);

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "values";
    }
}

