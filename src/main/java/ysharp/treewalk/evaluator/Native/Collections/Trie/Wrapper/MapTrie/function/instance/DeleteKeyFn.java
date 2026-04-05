package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.MapTrie.yMapTrie;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class DeleteKeyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "Trie.deleteKey");

        Variable.Variant key = arguments.getFirst();
        yMapTrie.yMapTrieInstance trie = yMapTrie.requireTrieThis(interpreter);

        trie.data.deleteKey(key.toString());

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "deleteKey";
    }
}
