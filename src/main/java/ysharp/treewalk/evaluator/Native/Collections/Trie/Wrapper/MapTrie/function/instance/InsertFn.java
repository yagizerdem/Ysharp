package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class InsertFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 2, "Trie.insert");

        Variable.Variant key   = arguments.getFirst();
        Variable.Variant value = arguments.get(1);
        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        String keyStr = key.toString();

        trie.data.insert(keyStr, value);

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "insert";
    }
}

