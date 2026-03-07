package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Y_TreeMap {

    // helper
    private static Y_TreeMap.Y_TreeMapInstance requireTreeMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_TreeMap.Y_TreeMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on TreeMap objects."
            );
        }

        return (Y_TreeMap.Y_TreeMapInstance) obj;
    }

    public static RuntimeObject Y_TreeMap_Instance_Prototype;

    static {
        Y_TreeMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "tree_map_prototype";
            }
        };
        Y_TreeMap_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // tm.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;
                for (var entry : tm.data.entrySet()) {
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
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // tm.put(key, value)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant previous = tm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(put.getFnName(), putVar);


        // tm.get(key)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant value = tm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(get.getFnName(), getVar);


        // tm.getOrDefault(key, default)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant value = tm.data.get(key);

                return value != null ? value : defaultValue;
            }

            @Override
            public String getFnName() {
                return "getOrDefault";
            }
        }

        GetOrDefaultFn getOrDefault = new GetOrDefaultFn();
        Variable getOrDefaultVar = new Variable(new Variable.Variant(getOrDefault), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(getOrDefault.getFnName(), getOrDefaultVar);


        // tm.remove(key)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant removed = tm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // tm.containsKey(key)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                return new Variable.Variant(tm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // tm.containsValue(value)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                return new Variable.Variant(tm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


        // tm.putIfAbsent(key, value)
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
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant existing = tm.data.putIfAbsent(key, value);

                return existing != null ? existing : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putIfAbsent";
            }
        }

        PutIfAbsentFn putIfAbsent = new PutIfAbsentFn();
        Variable putIfAbsentVar = new Variable(new Variable.Variant(putIfAbsent), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(putIfAbsent.getFnName(), putIfAbsentVar);


        // tm.firstKey()
        class FirstKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                if (tm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return tm.data.firstKey();
            }

            @Override
            public String getFnName() {
                return "firstKey";
            }
        }

        FirstKeyFn firstKey = new FirstKeyFn();
        Variable firstKeyVar = new Variable(new Variable.Variant(firstKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(firstKey.getFnName(), firstKeyVar);


        // tm.lastKey()
        class LastKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                if (tm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return tm.data.lastKey();
            }

            @Override
            public String getFnName() {
                return "lastKey";
            }
        }

        LastKeyFn lastKey = new LastKeyFn();
        Variable lastKeyVar = new Variable(new Variable.Variant(lastKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(lastKey.getFnName(), lastKeyVar);


        // tm.floorKey(key) -> greatest key <= given key
        class FloorKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant result = tm.data.floorKey(key);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "floorKey";
            }
        }

        FloorKeyFn floorKey = new FloorKeyFn();
        Variable floorKeyVar = new Variable(new Variable.Variant(floorKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(floorKey.getFnName(), floorKeyVar);


        // tm.ceilingKey(key) -> smallest key >= given key
        class CeilingKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant result = tm.data.ceilingKey(key);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "ceilingKey";
            }
        }

        CeilingKeyFn ceilingKey = new CeilingKeyFn();
        Variable ceilingKeyVar = new Variable(new Variable.Variant(ceilingKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(ceilingKey.getFnName(), ceilingKeyVar);


        // tm.lowerKey(key) -> greatest key strictly < given key
        class LowerKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant result = tm.data.lowerKey(key);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "lowerKey";
            }
        }

        LowerKeyFn lowerKey = new LowerKeyFn();
        Variable lowerKeyVar = new Variable(new Variable.Variant(lowerKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(lowerKey.getFnName(), lowerKeyVar);


        // tm.higherKey(key) -> smallest key strictly > given key
        class HigherKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Variable.Variant result = tm.data.higherKey(key);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "higherKey";
            }
        }

        HigherKeyFn higherKey = new HigherKeyFn();
        Variable higherKeyVar = new Variable(new Variable.Variant(higherKey), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(higherKey.getFnName(), higherKeyVar);


        // tm.pollFirstEntry() -> removes and returns [key, value] of smallest entry
        class PollFirstEntryFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                if (tm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                var entry = tm.data.pollFirstEntry();

                ArrayList<Variable.Variant> pair = new ArrayList<>();
                pair.add(entry.getKey());
                pair.add(entry.getValue());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(pair));
            }

            @Override
            public String getFnName() {
                return "pollFirstEntry";
            }
        }

        PollFirstEntryFn pollFirstEntry = new PollFirstEntryFn();
        Variable pollFirstEntryVar = new Variable(new Variable.Variant(pollFirstEntry), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(pollFirstEntry.getFnName(), pollFirstEntryVar);


        // tm.pollLastEntry() -> removes and returns [key, value] of largest entry
        class PollLastEntryFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                if (tm.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                var entry = tm.data.pollLastEntry();

                ArrayList<Variable.Variant> pair = new ArrayList<>();
                pair.add(entry.getKey());
                pair.add(entry.getValue());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(pair));
            }

            @Override
            public String getFnName() {
                return "pollLastEntry";
            }
        }

        PollLastEntryFn pollLastEntry = new PollLastEntryFn();
        Variable pollLastEntryVar = new Variable(new Variable.Variant(pollLastEntry), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(pollLastEntry.getFnName(), pollLastEntryVar);


        // tm.subMap(fromKey, toKey) -> new TreeMap with keys in [fromKey, toKey)
        class SubMapFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant fromKey = arguments.get(0);
                Variable.Variant toKey   = arguments.get(1);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Y_TreeMapInstance result = new Y_TreeMapInstance();
                result.data.putAll(tm.data.subMap(fromKey, toKey));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "subMap";
            }
        }

        SubMapFn subMap = new SubMapFn();
        Variable subMapVar = new Variable(new Variable.Variant(subMap), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(subMap.getFnName(), subMapVar);


        // tm.headMap(toKey) -> new TreeMap with keys strictly < toKey
        class HeadMapFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant toKey = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Y_TreeMapInstance result = new Y_TreeMapInstance();
                result.data.putAll(tm.data.headMap(toKey));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "headMap";
            }
        }

        HeadMapFn headMap = new HeadMapFn();
        Variable headMapVar = new Variable(new Variable.Variant(headMap), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(headMap.getFnName(), headMapVar);


        // tm.tailMap(fromKey) -> new TreeMap with keys >= fromKey
        class TailMapFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant fromKey = arguments.get(0);
                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                Y_TreeMapInstance result = new Y_TreeMapInstance();
                result.data.putAll(tm.data.tailMap(fromKey));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "tailMap";
            }
        }

        TailMapFn tailMap = new TailMapFn();
        Variable tailMapVar = new Variable(new Variable.Variant(tailMap), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(tailMap.getFnName(), tailMapVar);


        // tm.keys() -> sorted array of keys
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(tm.data.keySet());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(keys.getFnName(), keysVar);


        // tm.values() -> array of values in key-sorted order
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(tm.data.values());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // tm.entries() -> array of [key, value] pairs in key-sorted order
        class EntriesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : tm.data.entrySet()) {
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
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


        // tm.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                return new Variable.Variant(tm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // tm.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);

                return new Variable.Variant(tm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // tm.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance tm = requireTreeMapThis(interpreter);
                tm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(clear.getFnName(), clearVar);


        // tm.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_TreeMapInstance original = requireTreeMapThis(interpreter);
                Y_TreeMapInstance cloned   = new Y_TreeMapInstance();

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
        Y_TreeMap.Y_TreeMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static class Y_TreeMapInstance extends Y_Class.ClassObjectInstance {

        // Keys are sorted by their natural string representation
        final TreeMap<Variable.Variant, Variable.Variant> data;

        public Y_TreeMapInstance() {
            this.data = new TreeMap<>((a, b) -> {
                String sa = a.toString();
                String sb = b.toString();

                // Try numeric comparison first
                try {
                    double da = Double.parseDouble(sa);
                    double db = Double.parseDouble(sb);
                    return Double.compare(da, db);
                } catch (NumberFormatException e) {
                    return sa.compareTo(sb);
                }
            });
            this.prototype = Y_TreeMap_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "TreeMap";
        }

        @Override
        public String toString() {
            return "<class:tree-map>";
        }
    }

    public static class Y_TreeMapClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_TreeMapInstance newMap = new Y_TreeMapInstance();

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "TreeMap";
        }

        @Override
        public String getType() {
            return "TreeMap";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_TreeMap.Y_TreeMapClass tmCtor = new Y_TreeMap.Y_TreeMapClass();
        Variable.Variant variant = new Variable.Variant(tmCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(tmCtor.getClassName(), var);
    }

}