package ysharp.evaluator.Native.Collections.Trie;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Y_Array;
import ysharp.parser.TypeTag;
import ysharp.evaluator.Native.Collections.Trie.Concrete.T9Trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Y_T9Trie {

    // helper
    private static Y_T9Trie.Y_T9TrieInstance requireT9TrieThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_T9Trie.Y_T9TrieInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on T9Trie objects."
            );
        }

        return (Y_T9Trie.Y_T9TrieInstance) obj;
    }

    public static RuntimeObject Y_T9Trie_Instance_Prototype;

    static {
        Y_T9Trie_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "t9_trie_prototype";
            }
        };
        Y_T9Trie_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // t9.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.toString");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                return new Variable.Variant(t9.data.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // t9.insertValue(key, value)
        // Primary insert — maps word -> T9 digits, appends value to that bucket
        class InsertValueFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 2, "T9Trie.insertValue");

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                t9.data.insertValue(key.toString(), value);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "insertValue";
            }
        }

        InsertValueFn insertValue = new InsertValueFn();
        Variable insertValueVar = new Variable(new Variable.Variant(insertValue), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(insertValue.getFnName(), insertValueVar);


        // t9.insert(key, valuesArray)
        // Bulk insert — takes a Y_ArrayObject as the value list for a key
        class InsertFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 2, "T9Trie.insert");

                Variable.Variant key         = arguments.get(0);
                Variable.Variant valuesVariant = arguments.get(1);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                RuntimeObject obj = valuesVariant.asRuntimeObject();
                if (!(obj instanceof Y_Array.Y_ArrayInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "T9Trie.insert: second argument must be an Array of values."
                    );
                }

                Y_Array.Y_ArrayInstance arr = (Y_Array.Y_ArrayInstance) obj;
                LinkedList<Variable.Variant> list = new LinkedList<>(arr.data);

                t9.data.insert(key.toString(), list);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "insert";
            }
        }

        InsertFn insert = new InsertFn();
        Variable insertVar = new Variable(new Variable.Variant(insert), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(insert.getFnName(), insertVar);


        // t9.get(key) -> Y_ArrayObject of values stored under the T9 digits of key
        class GetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "T9Trie.get");

                Variable.Variant key = arguments.get(0);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                LinkedList<Variable.Variant> result = t9.data.get(key.toString());

                ArrayList<Variable.Variant> list = new ArrayList<>(result);

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(get.getFnName(), getVar);


        // t9.contains(key) -> true if the T9 digit sequence of key exists
        class ContainsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "T9Trie.contains");

                Variable.Variant key = arguments.get(0);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                return new Variable.Variant(t9.data.contains(key.toString()));
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(new Variable.Variant(contains), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(contains.getFnName(), containsVar);


        // t9.deleteKey(key) -> deletes the T9 digit sequence of key
        class DeleteKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "T9Trie.deleteKey");

                Variable.Variant key = arguments.get(0);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                t9.data.deleteKey(key.toString());

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "deleteKey";
            }
        }

        DeleteKeyFn deleteKey = new DeleteKeyFn();
        Variable deleteKeyVar = new Variable(new Variable.Variant(deleteKey), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(deleteKey.getFnName(), deleteKeyVar);


        // t9.getKeySuggestions(prefix) -> Y_ArrayObject of T9 digit keys matching prefix
        class GetKeySuggestionsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "T9Trie.getKeySuggestions");

                Variable.Variant prefix = arguments.get(0);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                List<String> suggestions = t9.data.getKeySuggestions(prefix.toString());

                ArrayList<Variable.Variant> list = new ArrayList<>();
                for (String s : suggestions) {
                    list.add(new Variable.Variant(s));
                }

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "getKeySuggestions";
            }
        }

        GetKeySuggestionsFn getKeySuggestions = new GetKeySuggestionsFn();
        Variable getKeySuggestionsVar = new Variable(new Variable.Variant(getKeySuggestions), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(getKeySuggestions.getFnName(), getKeySuggestionsVar);


        // t9.getT9ValueSuggestions(prefix) -> Y_ArrayObject of all values under T9 prefix
        // prefix can be digit string (e.g. "43") or a word (converted to T9 internally)
        class GetT9ValueSuggestionsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "T9Trie.getT9ValueSuggestions");

                Variable.Variant prefix = arguments.get(0);
                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                List<Variable.Variant> suggestions =
                        t9.data.getT9ValueSuggestions(prefix.toString());

                ArrayList<Variable.Variant> list = new ArrayList<>(suggestions);

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "getT9ValueSuggestions";
            }
        }

        GetT9ValueSuggestionsFn getT9ValueSuggestions = new GetT9ValueSuggestionsFn();
        Variable getT9ValueSuggestionsVar = new Variable(new Variable.Variant(getT9ValueSuggestions), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(getT9ValueSuggestions.getFnName(), getT9ValueSuggestionsVar);


        // t9.t9Values() -> Y_ArrayObject of all values across all T9 buckets (flattened)
        class T9ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.t9Values");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                List<Variable.Variant> all = t9.data.t9Values();

                ArrayList<Variable.Variant> list = new ArrayList<>(all);

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "t9Values";
            }
        }

        T9ValuesFn t9Values = new T9ValuesFn();
        Variable t9ValuesVar = new Variable(new Variable.Variant(t9Values), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(t9Values.getFnName(), t9ValuesVar);


        // t9.keys() -> Y_ArrayObject of all T9 digit keys stored
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.keys");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                List<String> keyList = t9.data.keys();

                ArrayList<Variable.Variant> list = new ArrayList<>();
                for (String s : keyList) {
                    list.add(new Variable.Variant(s));
                }

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(keys.getFnName(), keysVar);


        // t9.size() -> total number of values stored across all buckets
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.size");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                return new Variable.Variant(t9.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(size.getFnName(), sizeVar);


        // t9.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.isEmpty");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);

                return new Variable.Variant(t9.data.size() == 0);
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // t9.clear() -> deep clear (keeps root, wipes children)
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.clear");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);
                t9.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(clear.getFnName(), clearVar);


        // t9.fastClear() -> replaces root node entirely (faster)
        class FastClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.fastClear");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);
                t9.data.fastClear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "fastClear";
            }
        }

        FastClearFn fastClear = new FastClearFn();
        Variable fastClearVar = new Variable(new Variable.Variant(fastClear), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(fastClear.getFnName(), fastClearVar);


        // t9.print() -> prints trie structure to stdout (debug)
        class PrintFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "T9Trie.print");

                Y_T9TrieInstance t9 = requireT9TrieThis(interpreter);
                t9.data.print();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "print";
            }
        }

        PrintFn print = new PrintFn();
        Variable printVar = new Variable(new Variable.Variant(print), true, TypeTag.OBJECT);
        Y_T9Trie.Y_T9Trie_Instance_Prototype.set(print.getFnName(), printVar);

    }


    public static class Y_T9TrieInstance extends Y_Class.ClassObjectInstance {

        // Backed by T9Trie<Variable.Variant>
        // Keys are words, internally stored as T9 digit sequences
        // Multiple words can map to the same T9 digits — stored in a LinkedList bucket
        final T9Trie<Variable.Variant> data;

        public Y_T9TrieInstance() {
            this.data = new T9Trie<>();
            this.prototype = Y_T9Trie_Instance_Prototype;
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
            return "<class:t9-trie>";
        }
    }

    public static class Y_T9TrieClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "T9Trie");

            Y_T9TrieInstance newT9 = new Y_T9TrieInstance();

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
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_T9Trie.Y_T9TrieClass t9Ctor = new Y_T9Trie.Y_T9TrieClass();
        Variable.Variant variant = new Variable.Variant(t9Ctor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(t9Ctor.getClassName(), var);
    }

}