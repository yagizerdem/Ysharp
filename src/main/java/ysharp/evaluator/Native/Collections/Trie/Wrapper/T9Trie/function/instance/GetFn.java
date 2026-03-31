package ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.T9Trie.yT9Trie;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments, 1, "T9Trie.get");

        Variable.Variant key = arguments.getFirst();
        yT9Trie.yT9TrieInstance t9 = yT9Trie.requireT9TrieThis(interpreter);

        LinkedList<Variable.Variant> result = t9.data.get(key.toString());

        ArrayList<Variable.Variant> list = new ArrayList<>(result);

        return new Variable.Variant(new yArray.yArrayInstance(list));
    }

    @Override
    public String getFnName() {
        return "get";
    }
}
