package ysharp.evaluator.Native.Collections.Trie;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Y_Array;
import ysharp.parser.TypeTag;
import ysharp.evaluator.Native.Collections.Trie.Concrete.MapTrie;

import java.util.ArrayList;
import java.util.List;

public class Y_SortedMapTrie {

    // helper
    private static Y_SortedMapTrie.Y_SortedMapTrieInstance requireTrieThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_SortedMapTrie.Y_SortedMapTrieInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on Trie objects."
            );
        }

        return (Y_SortedMapTrie.Y_SortedMapTrieInstance) obj;
    }

    public static RuntimeObject Y_SortedMapTrie_Instance_Prototype;
    
    static {
        Y_SortedMapTrie_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "trie_prototype";
            }
        };
        Y_SortedMapTrie_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // trie.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.toString");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                return new Variable.Variant(trie.data.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // trie.insert(key, value)
        class InsertFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 2, "Trie.insert");

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                String keyStr = key.toString();

                trie.data.insert(keyStr, value);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "insert";
            }
        }

        InsertFn insert = new InsertFn();
        Variable insertVar = new Variable(new Variable.Variant(insert), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(insert.getFnName(), insertVar);


        // trie.get(key) -> value or null
        class GetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "Trie.get");

                Variable.Variant key = arguments.get(0);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                Variable.Variant result = trie.data.get(key.toString());

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(get.getFnName(), getVar);


        // trie.contains(key) -> true/false
        class ContainsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "Trie.contains");

                Variable.Variant key = arguments.get(0);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                return new Variable.Variant(trie.data.contains(key.toString()));
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(new Variable.Variant(contains), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(contains.getFnName(), containsVar);


        // trie.deleteKey(key)
        class DeleteKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "Trie.deleteKey");

                Variable.Variant key = arguments.get(0);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                trie.data.deleteKey(key.toString());

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "deleteKey";
            }
        }

        DeleteKeyFn deleteKey = new DeleteKeyFn();
        Variable deleteKeyVar = new Variable(new Variable.Variant(deleteKey), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(deleteKey.getFnName(), deleteKeyVar);


        // trie.getKeySuggestions(prefix) -> Y_ArrayObject of matching keys
        class GetKeySuggestionsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "Trie.getKeySuggestions");

                Variable.Variant prefix = arguments.get(0);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                List<String> suggestions = trie.data.getKeySuggestions(prefix.toString());

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
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(getKeySuggestions.getFnName(), getKeySuggestionsVar);


        // trie.getValueSuggestions(prefix) -> Y_ArrayObject of matching values
        class GetValueSuggestionsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "Trie.getValueSuggestions");

                Variable.Variant prefix = arguments.get(0);
                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                List<Variable.Variant> suggestions =
                        trie.data.getValueSuggestions(prefix.toString());

                ArrayList<Variable.Variant> list = new ArrayList<>(suggestions);

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "getValueSuggestions";
            }
        }

        GetValueSuggestionsFn getValueSuggestions = new GetValueSuggestionsFn();
        Variable getValueSuggestionsVar = new Variable(new Variable.Variant(getValueSuggestions), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(getValueSuggestions.getFnName(), getValueSuggestionsVar);


        // trie.keys() -> Y_ArrayObject of all keys in sorted trie order
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.keys");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                List<String> keyList = trie.data.keys();

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
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(keys.getFnName(), keysVar);


        // trie.values() -> Y_ArrayObject of all values
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.values");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                List<Variable.Variant> valueList = trie.data.values();

                ArrayList<Variable.Variant> list = new ArrayList<>(valueList);

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(values.getFnName(), valuesVar);


        // trie.size() -> number of keys stored
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.size");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                return new Variable.Variant(trie.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(size.getFnName(), sizeVar);


        // trie.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.isEmpty");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);

                return new Variable.Variant(trie.data.size() == 0);
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // trie.clear() -> deep clear (keeps root, clears children)
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.clear");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);
                trie.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(clear.getFnName(), clearVar);


        // trie.fastClear() -> replaces root node entirely (faster)
        class FastClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.fastClear");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);
                trie.data.fastClear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "fastClear";
            }
        }

        FastClearFn fastClear = new FastClearFn();
        Variable fastClearVar = new Variable(new Variable.Variant(fastClear), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(fastClear.getFnName(), fastClearVar);


        // trie.print() -> prints trie structure to stdout (debug)
        class PrintFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "Trie.print");

                Y_SortedMapTrieInstance trie = requireTrieThis(interpreter);
                trie.data.print();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "print";
            }
        }

        PrintFn print = new PrintFn();
        Variable printVar = new Variable(new Variable.Variant(print), true, TypeTag.OBJECT);
        Y_SortedMapTrie.Y_SortedMapTrie_Instance_Prototype.set(print.getFnName(), printVar);

    }

    public static class Y_SortedMapTrieInstance extends Y_Class.ClassObjectInstance {

        // Backed by MapTrie<Variable.Variant> — keys are lowercased+trimmed strings
        final MapTrie<Variable.Variant> data;

        public Y_SortedMapTrieInstance() {
            this.data = new MapTrie<>();
            this.prototype = Y_SortedMapTrie_Instance_Prototype;
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
            return "<class:SortedMapTrie>";
        }
    }

    public static class Y_SortedMapTrieClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "SortedMapTrie");

            Y_SortedMapTrieInstance newTrie = new Y_SortedMapTrieInstance();

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
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_SortedMapTrie.Y_SortedMapTrieClass trieCtor = new Y_SortedMapTrie.Y_SortedMapTrieClass();
        Variable.Variant variant = new Variable.Variant(trieCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(trieCtor.getClassName(), var);
    }

}