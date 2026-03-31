package ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.Trie.Concrete.MapTrie;
import ysharp.evaluator.Native.Collections.Trie.Wrapper.MapTrie.function.instance.*;

import java.util.ArrayList;
import java.util.List;

public class yMapTrie {

    // helper
    public static yMapTrie.yMapTrieInstance requireTrieThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yMapTrie.yMapTrieInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on Trie objects."
            );
        }

        return (yMapTrie.yMapTrieInstance) obj;
    }
    
    public static RuntimeObject yMapTrie_Instance_Prototype;
    
    static {
        yMapTrie_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__MapTrie__";
            }

            @Override
            public String toString() {
                return "<prototype:MapTrie>";
            }
        };
        yMapTrie_Instance_Prototype.prototype = yClass.ClassPrototype;

        // trie.toString()
        yMapTrie_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // trie.insert(key, value)
        yMapTrie_Instance_Prototype.RegisterNativeFn(new InsertFn());
        // trie.get(key) -> value or null
        yMapTrie_Instance_Prototype.RegisterNativeFn(new GetFn());
        // trie.contains(key) -> true/false
        yMapTrie_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // trie.deleteKey(key)
        yMapTrie_Instance_Prototype.RegisterNativeFn(new DeleteKeyFn());
        // trie.getKeySuggestions(prefix) -> Y_ArrayObject of matching keys
        yMapTrie_Instance_Prototype.RegisterNativeFn(new GetKeySuggestionsFn());
        // trie.getValueSuggestions(prefix) -> Y_ArrayObject of matching values
        yMapTrie_Instance_Prototype.RegisterNativeFn(new GetValueSuggestionsFn());
        // trie.keys() -> Y_ArrayObject of all keys in sorted trie order
        yMapTrie_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // trie.values() -> Y_ArrayObject of all values
        yMapTrie_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // trie.size() -> number of keys stored
        yMapTrie_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // trie.isEmpty()
        yMapTrie_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // trie.clear() -> deep clear (keeps root, clears children)
        yMapTrie_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // trie.fastClear() -> replaces root node entirely (faster)
        yMapTrie_Instance_Prototype.RegisterNativeFn(new FastClearFn());
        // trie.print() -> prints trie structure to stdout (debug)
        yMapTrie_Instance_Prototype.RegisterNativeFn(new PrintFn());
    }
    

    public static class yMapTrieInstance extends yClass.ClassObjectInstance {

        // Backed by MapTrie<Variable.Variant> — keys are lowercased+trimmed strings
        public final MapTrie<Variable.Variant> data;

        public yMapTrieInstance() {
            this.data = new MapTrie<>();
            this.prototype = yMapTrie_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "MapTrie";
        }

        @Override
        public String toString() {
            return "<instance:MapTrie>";
        }
    }

    public static class yMapTrieClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "MapTrie");

            yMapTrieInstance newTrie = new yMapTrieInstance();

            return new Variable.Variant(newTrie);
        }

        @Override
        public String getClassName() {
            return "MapTrie";
        }

        @Override
        public String getType() {
            return "MapTrie";
        }

        @Override
        public String toString() {
            return "<class:MapTrie>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yMapTrie.yMapTrieClass trieCtor = new yMapTrie.yMapTrieClass();
        Variable.Variant variant = new Variable.Variant(trieCtor);
        Variable var = new Variable(variant, false, trieCtor.getType());
        interpreter.defineGlobal(trieCtor.getClassName(), var);
    }

}