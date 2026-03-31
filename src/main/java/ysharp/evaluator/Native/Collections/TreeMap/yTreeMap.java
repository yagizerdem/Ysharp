package ysharp.evaluator.Native.Collections.TreeMap;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.TreeMap.function.instance.*;

import java.util.List;
import java.util.TreeMap;

public class yTreeMap {

    // helper
    public static yTreeMap.yTreeMapInstance requireTreeMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTreeMap.yTreeMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on TreeMap objects."
            );
        }

        return (yTreeMap.yTreeMapInstance) obj;
    }

    public static RuntimeObject yTreeMap_Instance_Prototype;

    static {
        yTreeMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__TreeMap__";
            }

            @Override
            public String toString() {
                return "<prototype:TreeMap>";
            }
        };
        yTreeMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // tm.toString()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // tm.put(key, value)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // tm.get(key)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // tm.getOrDefault(key, default)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new GetOrDefaultFn());
        // tm.remove(key)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // tm.containsKey(key)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // tm.containsValue(value)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new ContainsValueFn());
        // tm.putIfAbsent(key, value)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new PutIfAbsentFn());
        // tm.firstKey()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new FirstKeyFn());
        // tm.lastKey()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new LastKeyFn());
        // tm.floorKey(key) -> greatest key <= given key
        yTreeMap_Instance_Prototype.RegisterNativeFn(new FloorKeyFn());
        // tm.ceilingKey(key) -> smallest key >= given key
        yTreeMap_Instance_Prototype.RegisterNativeFn(new CeilingKeyFn());
        // tm.lowerKey(key) -> greatest key strictly < given key
        yTreeMap_Instance_Prototype.RegisterNativeFn(new LowerKeyFn());
        // tm.higherKey(key) -> smallest key strictly > given key
        yTreeMap_Instance_Prototype.RegisterNativeFn(new HigherKeyFn());
        // tm.pollFirstEntry() -> removes and returns [key, value] of smallest entry
        yTreeMap_Instance_Prototype.RegisterNativeFn(new PollFirstEntryFn());
        // tm.pollLastEntry() -> removes and returns [key, value] of largest entry
        yTreeMap_Instance_Prototype.RegisterNativeFn(new PollLastEntryFn());
        // tm.subMap(fromKey, toKey) -> new TreeMap with keys in [fromKey, toKey)
        yTreeMap_Instance_Prototype.RegisterNativeFn(new SubMapFn());
        // tm.headMap(toKey) -> new TreeMap with keys strictly < toKey
        yTreeMap_Instance_Prototype.RegisterNativeFn(new HeadMapFn());
        // tm.tailMap(fromKey) -> new TreeMap with keys >= fromKey
        yTreeMap_Instance_Prototype.RegisterNativeFn(new TailMapFn());
        // tm.keys() -> sorted array of keys
        yTreeMap_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // tm.values() -> array of values in key-sorted order
        yTreeMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // tm.entries() -> array of [key, value] pairs in key-sorted order
        yTreeMap_Instance_Prototype.RegisterNativeFn(new EntriesFn());
        // tm.size()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // tm.isEmpty()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // tm.clear()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // tm.clone()
        yTreeMap_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    public static class yTreeMapInstance extends yClass.ClassObjectInstance {

        // Keys are sorted by their natural string representation
        public final TreeMap<Variable.Variant, Variable.Variant> data;

        public yTreeMapInstance() {
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
            this.prototype = yTreeMap_Instance_Prototype;
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
            return "<instance:TreeMap>";
        }
    }

    public static class yTreeMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yTreeMapInstance newMap = new yTreeMapInstance();

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

        @Override
        public String toString() {
            return "<class:TreeMap>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yTreeMapClass tmCtor = new yTreeMapClass();
        Variable.Variant variant = new Variable.Variant(tmCtor);
        Variable var = new Variable(variant, false, tmCtor.getType());
        interpreter.defineGlobal(tmCtor.getClassName(), var);
    }

}