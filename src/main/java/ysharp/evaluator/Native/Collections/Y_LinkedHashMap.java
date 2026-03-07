package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Y_LinkedHashMap {

    // helper
    private static Y_LinkedHashMap.Y_LinkedHashMapInstance requireLinkedHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_LinkedHashMap.Y_LinkedHashMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on LinkedHashMap objects."
            );
        }

        return (Y_LinkedHashMap.Y_LinkedHashMapInstance) obj;
    }

    public static RuntimeObject Y_LinkedHashMap_Instance_Prototype;

    static {
        Y_LinkedHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "linked_hash_map_prototype";
            }
        };
        Y_LinkedHashMap_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // lhm.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;
                for (var entry : lhm.data.entrySet()) {
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
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // lhm.put(key, value)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant previous = lhm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(put.getFnName(), putVar);


        // lhm.get(key)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant value = lhm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(get.getFnName(), getVar);


        // lhm.getOrDefault(key, default)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant value = lhm.data.get(key);

                return value != null ? value : defaultValue;
            }

            @Override
            public String getFnName() {
                return "getOrDefault";
            }
        }

        GetOrDefaultFn getOrDefault = new GetOrDefaultFn();
        Variable getOrDefaultVar = new Variable(new Variable.Variant(getOrDefault), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(getOrDefault.getFnName(), getOrDefaultVar);


        // lhm.remove(key)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant removed = lhm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // lhm.containsKey(key)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // lhm.containsValue(value)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


        // lhm.keys() -> Y_ArrayObject in insertion order
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(lhm.data.keySet());

                Y_Array.Y_ArrayInstance array = new Y_Array.Y_ArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(keys.getFnName(), keysVar);


        // lhm.values() -> Y_ArrayObject in insertion order
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(lhm.data.values());

                Y_Array.Y_ArrayInstance array = new Y_Array.Y_ArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // lhm.entries() -> array of [key, value] pairs in insertion order
        class EntriesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : lhm.data.entrySet()) {
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
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


        // lhm.firstKey()
        class FirstKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                if (lhm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return lhm.data.keySet().iterator().next();
            }

            @Override
            public String getFnName() {
                return "firstKey";
            }
        }

        FirstKeyFn firstKey = new FirstKeyFn();
        Variable firstKeyVar = new Variable(new Variable.Variant(firstKey), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(firstKey.getFnName(), firstKeyVar);


        // lhm.lastKey()
        class LastKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                if (lhm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                Variable.Variant last = null;
                for (Variable.Variant k : lhm.data.keySet()) {
                    last = k;
                }

                return last;
            }

            @Override
            public String getFnName() {
                return "lastKey";
            }
        }

        LastKeyFn lastKey = new LastKeyFn();
        Variable lastKeyVar = new Variable(new Variable.Variant(lastKey), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(lastKey.getFnName(), lastKeyVar);


        // lhm.removeFirst() -> removes and returns value of first inserted entry
        class RemoveFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                if (lhm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                Variable.Variant firstKey = lhm.data.keySet().iterator().next();

                return lhm.data.remove(firstKey);
            }

            @Override
            public String getFnName() {
                return "removeFirst";
            }
        }

        RemoveFirstFn removeFirst = new RemoveFirstFn();
        Variable removeFirstVar = new Variable(new Variable.Variant(removeFirst), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(removeFirst.getFnName(), removeFirstVar);


        // lhm.removeLast() -> removes and returns value of last inserted entry
        class RemoveLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                if (lhm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                Variable.Variant lastKey = null;
                for (Variable.Variant k : lhm.data.keySet()) {
                    lastKey = k;
                }

                return lhm.data.remove(lastKey);
            }

            @Override
            public String getFnName() {
                return "removeLast";
            }
        }

        RemoveLastFn removeLast = new RemoveLastFn();
        Variable removeLastVar = new Variable(new Variable.Variant(removeLast), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(removeLast.getFnName(), removeLastVar);


        // lhm.putIfAbsent(key, value)
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
                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant existing = lhm.data.putIfAbsent(key, value);

                return existing != null ? existing : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putIfAbsent";
            }
        }

        PutIfAbsentFn putIfAbsent = new PutIfAbsentFn();
        Variable putIfAbsentVar = new Variable(new Variable.Variant(putIfAbsent), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(putIfAbsent.getFnName(), putIfAbsentVar);


        // lhm.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // lhm.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // lhm.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);
                lhm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(clear.getFnName(), clearVar);


        // lhm.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedHashMapInstance original = requireLinkedHashMapThis(interpreter);
                Y_LinkedHashMapInstance cloned   = new Y_LinkedHashMapInstance();

                cloned.data.putAll(original.data);

                return new Variable.Variant(cloned);
            }

            @Override
            public String getFnName() {
                return "clone";
            }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(new Variable.Variant(clone), true, TypeTag.OBJECT);
        Y_LinkedHashMap.Y_LinkedHashMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static class Y_LinkedHashMapInstance extends Y_Class.ClassObjectInstance {

        final LinkedHashMap<Variable.Variant, Variable.Variant> data;

        public Y_LinkedHashMapInstance() {
            this.data = new LinkedHashMap<>();
            this.prototype = Y_LinkedHashMap_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "LinkedHashMap";
        }

        @Override
        public String toString() {
            return "<class:linked-hash-map>";
        }
    }

    public static class Y_LinkedHashMapClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_LinkedHashMapInstance newMap = new Y_LinkedHashMapInstance();

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "LinkedHashMap";
        }

        @Override
        public String getType() {
            return "LinkedHashMap";
        }

    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_LinkedHashMap.Y_LinkedHashMapClass lhmCtor = new Y_LinkedHashMap.Y_LinkedHashMapClass();
        Variable.Variant variant = new Variable.Variant(lhmCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(lhmCtor.getClassName(), var);
    }

}