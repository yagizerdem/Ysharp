package ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Concrete.MapTrie;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance.ContainsFn;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.SortedMapTrie.function.instance.*;

import java.util.ArrayList;
import java.util.List;

public class ySortedMapTrie {

    // helper
    public static ySortedMapTrie.ySortedMapTrieInstance requireTrieThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof ySortedMapTrie.ySortedMapTrieInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on Trie objects."
            );
        }

        return (ySortedMapTrie.ySortedMapTrieInstance) obj;
    }

    public static RuntimeObject ySortedMapTrie_Instance_Prototype;
    
    static {
        ySortedMapTrie_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__SortedMapTrie__";
            }

            @Override
            public String toString() {
                return "<prototype:SortedMapTrie>";
            }
        };
        ySortedMapTrie_Instance_Prototype.prototype = yClass.ClassPrototype;

        // trie.toString()
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // trie.insert(key, value)
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new InsertFn());
        // trie.get(key) -> value or null
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new GetFn());
        // trie.contains(key) -> true/false
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // trie.deleteKey(key)
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new DeleteKeyFn());
        // trie.getKeySuggestions(prefix) -> Y_ArrayObject of matching keys
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new GetKeySuggestionsFn());
        // trie.getValueSuggestions(prefix) -> Y_ArrayObject of matching values
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new GetValueSuggestionsFn());
        // trie.keys() -> Y_ArrayObject of all keys in sorted trie order
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // trie.values() -> Y_ArrayObject of all values
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // trie.size() -> number of keys stored
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // trie.isEmpty()
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // trie.clear() -> deep clear (keeps root, clears children)
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // trie.fastClear() -> replaces root node entirely (faster)
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new FastClearFn());
        // trie.print() -> prints trie structure to stdout (debug)
        ySortedMapTrie_Instance_Prototype.RegisterNativeFn(new PrintFn());
    }

    public static class ySortedMapTrieInstance extends yClass.ClassObjectInstance {

        // Backed by MapTrie<Variable.Variant> — keys are lowercased+trimmed strings
        public final MapTrie<Variable.Variant> data;

        public ySortedMapTrieInstance() {
            this.data = new MapTrie<>();
            this.prototype = ySortedMapTrie_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "SortedMapTrie";
        }

        @Override
        public String toString() {
            return "<instance:SortedMapTrie>";
        }
    }

    public static class ySortedMapTrieClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "SortedMapTrie");

            ySortedMapTrieInstance newTrie = new ySortedMapTrieInstance();

            return new Variable.Variant(newTrie);
        }

        @Override
        public String getClassName() {
            return "SortedMapTrie";
        }

        @Override
        public String getType() {
            return "SortedMapTrie";
        }

        @Override
        public String toString() {
            return "<class:SortedMapTrie>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        ySortedMapTrie.ySortedMapTrieClass trieCtor = new ySortedMapTrie.ySortedMapTrieClass();
        Variable.Variant variant = new Variable.Variant(trieCtor);
        Variable var = new Variable(variant, false, trieCtor.getType());
        interpreter.defineGlobal(trieCtor.getClassName(), var);
    }

}