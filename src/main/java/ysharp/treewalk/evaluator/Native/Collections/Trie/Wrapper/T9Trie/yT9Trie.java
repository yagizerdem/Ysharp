package ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Concrete.T9Trie;
import ysharp.treewalk.evaluator.Native.Collections.Trie.Wrapper.T9Trie.function.instance.*;

import java.util.List;

public class yT9Trie {

    // helper
    public static yT9Trie.yT9TrieInstance requireT9TrieThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yT9Trie.yT9TrieInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on T9Trie objects."
            );
        }

        return (yT9Trie.yT9TrieInstance) obj;
    }

    public static RuntimeObject yT9Trie_Instance_Prototype;

    static {
        yT9Trie_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__T9Trie__";
            }

            @Override
            public String toString() {
                return "<prototype:T9Trie>";
            }
        };
        yT9Trie_Instance_Prototype.prototype = yClass.ClassPrototype;

        // t9.toString()
        yT9Trie_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // t9.insertValue(key, value)
        // Primary insert — maps word -> T9 digits, appends value to that bucket
        yT9Trie_Instance_Prototype.RegisterNativeFn(new InsertValueFn());
        // t9.insert(key, valuesArray)
        // Bulk insert — takes a Y_ArrayObject as the value list for a key
        yT9Trie_Instance_Prototype.RegisterNativeFn(new InsertFn());
        // t9.get(key) -> Y_ArrayObject of values stored under the T9 digits of key
        yT9Trie_Instance_Prototype.RegisterNativeFn(new GetFn());
        // t9.contains(key) -> true if the T9 digit sequence of key exists
        yT9Trie_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // t9.deleteKey(key) -> deletes the T9 digit sequence of key
        yT9Trie_Instance_Prototype.RegisterNativeFn(new DeleteKeyFn());
        // t9.getKeySuggestions(prefix) -> Y_ArrayObject of T9 digit keys matching prefix
        yT9Trie_Instance_Prototype.RegisterNativeFn(new GetKeySuggestionsFn());
        // t9.getT9ValueSuggestions(prefix) -> Y_ArrayObject of all values under T9 prefix
        // prefix can be digit string (e.g. "43") or a word (converted to T9 internally)
        yT9Trie_Instance_Prototype.RegisterNativeFn(new GetT9ValueSuggestionsFn());
        // t9.t9Values() -> Y_ArrayObject of all values across all T9 buckets (flattened)
        yT9Trie_Instance_Prototype.RegisterNativeFn(new T9ValuesFn());
        // t9.keys() -> Y_ArrayObject of all T9 digit keys stored
        yT9Trie_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // t9.size() -> total number of values stored across all buckets
        yT9Trie_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // t9.isEmpty()
        yT9Trie_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // t9.clear() -> deep clear (keeps root, wipes children)
        yT9Trie_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // t9.fastClear() -> replaces root node entirely (faster)
        yT9Trie_Instance_Prototype.RegisterNativeFn(new FastClearFn());
        // t9.print() -> prints trie structure to stdout (debug)
        yT9Trie_Instance_Prototype.RegisterNativeFn(new PrintFn());
    }


    public static class yT9TrieInstance extends yClass.ClassObjectInstance {

        // Backed by T9Trie<Variable.Variant>
        // Keys are words, internally stored as T9 digit sequences
        // Multiple words can map to the same T9 digits — stored in a LinkedList bucket
        public final T9Trie<Variable.Variant> data;

        public yT9TrieInstance() {
            this.data = new T9Trie<>();
            this.prototype = yT9Trie_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "T9Trie";
        }

        @Override
        public String toString() {
            return "<instance:T9Trie>";
        }
    }

    public static class yT9TrieClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "T9Trie");

            yT9TrieInstance newT9 = new yT9TrieInstance();

            return new Variable.Variant(newT9);
        }

        @Override
        public String getClassName() {
            return "T9Trie";
        }

        @Override
        public String getType() {
            return "T9Trie";
        }

        @Override
        public String toString() {
            return "<class:T9Trie>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yT9Trie.yT9TrieClass t9Ctor = new yT9Trie.yT9TrieClass();
        Variable.Variant variant = new Variable.Variant(t9Ctor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(t9Ctor.getClassName(), var);
    }

}