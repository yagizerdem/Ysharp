package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class Y_WeakHashMap {

    // helper
    private static Y_WeakHashMap.Y_WeakHashMapInstance requireWeakHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_WeakHashMap.Y_WeakHashMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on WeakHashMap objects."
            );
        }

        return (Y_WeakHashMap.Y_WeakHashMapInstance) obj;
    }

    public static RuntimeObject Y_WeakHashMap_Instance_Prototype;

    static {
        Y_WeakHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "weak_hash_map_prototype";
            }
        };
        Y_WeakHashMap_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // whm.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;
                for (var entry : whm.data.entrySet()) {
                    if (!first) sb.append(", ");
                    first = false;
                    sb.append(entry.getKey().toString());
                    sb.append("=");
                    sb.append(entry.getValue().toString());
                }

                sb.append("}");

                return new Variable.Variant(sb.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // whm.put(key, value)
        class PutFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant previous = whm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(put.getFnName(), putVar);


        // whm.get(key)
        class GetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant value = whm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(get.getFnName(), getVar);


        // whm.getOrDefault(key, default)
        class GetOrDefaultFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key          = arguments.get(0);
                Variable.Variant defaultValue = arguments.get(1);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant value = whm.data.get(key);

                return value != null ? value : defaultValue;
            }

            @Override
            public String getFnName() {
                return "getOrDefault";
            }
        }

        GetOrDefaultFn getOrDefault = new GetOrDefaultFn();
        Variable getOrDefaultVar = new Variable(new Variable.Variant(getOrDefault), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(getOrDefault.getFnName(), getOrDefaultVar);


        // whm.remove(key)
        class RemoveFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant removed = whm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // whm.containsKey(key)
        class ContainsKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                return new Variable.Variant(whm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // whm.containsValue(value)
        class ContainsValueFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                return new Variable.Variant(whm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


        // whm.putIfAbsent(key, value)
        class PutIfAbsentFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant existing = whm.data.putIfAbsent(key, value);

                return existing != null ? existing : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putIfAbsent";
            }
        }

        PutIfAbsentFn putIfAbsent = new PutIfAbsentFn();
        Variable putIfAbsentVar = new Variable(new Variable.Variant(putIfAbsent), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(putIfAbsent.getFnName(), putIfAbsentVar);


        // whm.replace(key, value) -> replaces only if key already exists
        class ReplaceFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                Variable.Variant old = whm.data.replace(key, value);

                return old != null ? old : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "replace";
            }
        }

        ReplaceFn replace = new ReplaceFn();
        Variable replaceVar = new Variable(new Variable.Variant(replace), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(replace.getFnName(), replaceVar);


        // whm.keys() -> Y_ArrayObject snapshot of current keys
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(whm.data.keySet());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(keys.getFnName(), keysVar);


        // whm.values() -> Y_ArrayObject snapshot of current values
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(whm.data.values());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // whm.entries() -> array of [key, value] snapshot pairs
        class EntriesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : whm.data.entrySet()) {
                    ArrayList<Variable.Variant> pair = new ArrayList<>();
                    pair.add(entry.getKey());
                    pair.add(entry.getValue());
                    outerList.add(new Variable.Variant(new Y_Array.Y_ArrayInstance(pair)));
                }

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(outerList));
            }

            @Override
            public String getFnName() {
                return "entries";
            }
        }

        EntriesFn entries = new EntriesFn();
        Variable entriesVar = new Variable(new Variable.Variant(entries), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


        // whm.snapshot() -> copies all live entries into a new HashTable
        class SnapshotFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                java.util.Hashtable<Variable.Variant, Variable.Variant> snap =
                        new java.util.Hashtable<>(whm.data);

                Y_HashTable.Y_MapInstance mapObject =
                        new Y_HashTable.Y_MapInstance(snap);

                return new Variable.Variant(mapObject);
            }

            @Override
            public String getFnName() {
                return "snapshot";
            }
        }

        SnapshotFn snapshot = new SnapshotFn();
        Variable snapshotVar = new Variable(new Variable.Variant(snapshot), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(snapshot.getFnName(), snapshotVar);


        // whm.size()  -- NOTE: may fluctuate as GC reclaims keys
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                return new Variable.Variant(whm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // whm.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);

                return new Variable.Variant(whm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // whm.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_WeakHashMapInstance whm = requireWeakHashMapThis(interpreter);
                whm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_WeakHashMap.Y_WeakHashMap_Instance_Prototype.set(clear.getFnName(), clearVar);

    }


    public static class Y_WeakHashMapInstance extends Y_Class.ClassObjectInstance {

        // Keys held with weak references — GC can reclaim them when no other strong refs exist
        final WeakHashMap<Variable.Variant, Variable.Variant> data;

        public Y_WeakHashMapInstance() {
            this.data = new WeakHashMap<>();
            this.prototype = Y_WeakHashMap_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "WeakHashMap";
        }

        @Override
        public String toString() {
            return "<class:weak-hash-map>";
        }
    }

    public static class Y_WeakHashMapClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_WeakHashMapInstance newMap = new Y_WeakHashMapInstance();

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "WeakHashMap";
        }

        @Override
        public String getType() {
            return "WeakHashMap";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_WeakHashMap.Y_WeakHashMapClass whmCtor = new Y_WeakHashMap.Y_WeakHashMapClass();
        Variable.Variant variant = new Variable.Variant(whmCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(whmCtor.getClassName(), var);
    }

}