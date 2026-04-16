package ysharp.treewalk.evaluator.Native.Collections.WeakHashMap;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.WeakHashMap.function.instance.*;

import java.util.List;
import java.util.WeakHashMap;

public class yWeakHashMap {

    // helper
    public static yWeakHashMap.yWeakHashMapInstance requireWeakHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yWeakHashMap.yWeakHashMapInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on WeakHashMap objects."
            );
        }

        return (yWeakHashMap.yWeakHashMapInstance) obj;
    }

    public static RuntimeObject yWeakHashMap_Instance_Prototype;

    static {
        yWeakHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__WeakHashMap__";
            }

            @Override
            public String toString() {
                return "<prototype:WeakHashMap>";
            }
        };
        yWeakHashMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // whm.toString()
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // whm.put(key, value)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // whm.get(key)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // whm.getOrDefault(key, default)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new GetOrDefaultFn());
        // whm.remove(key)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // whm.containsKey(key)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // whm.containsValue(value)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ContainsValueFn());
        // whm.putIfAbsent(key, value)
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new PutIfAbsentFn());
        // whm.replace(key, value) -> replaces only if key already exists
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ReplaceFn());
        // whm.keys() -> Y_ArrayObject snapshot of current keys
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // whm.values() -> Y_ArrayObject snapshot of current values
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // whm.entries() -> array of [key, value] snapshot pairs
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new EntriesFn());
        // whm.snapshot() -> copies all live entries into a new HashTable
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new SnapshotFn());
        // whm.size()  -- NOTE: may fluctuate as GC reclaims keys
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // whm.isEmpty()
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // whm.clear()
        yWeakHashMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
    }


    public static class yWeakHashMapInstance extends yClass.ClassObjectInstance {

        // Keys held with weak references — GC can reclaim them when no other strong refs exist
        public final WeakHashMap<Variable.Variant, Variable.Variant> data;

        public yWeakHashMapInstance() {
            this.data = new WeakHashMap<>();
            this.prototype = yWeakHashMap_Instance_Prototype;
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
            return "<instance:WeakHashMap>";
        }
    }

    public static class yWeakHashMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            yWeakHashMapInstance newMap = new yWeakHashMapInstance();

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
        @Override
        public String toString() {
            return "<class:WeakHashMap>";
        }

    }

    public static void Register(Interpreter interpreter) throws Exception {
        yWeakHashMap.yWeakHashMapClass whmCtor = new yWeakHashMap.yWeakHashMapClass();
        Variable.Variant variant = new Variable.Variant(whmCtor);
        Variable var = new Variable(variant, false, whmCtor.getType());
        interpreter.defineGlobal(whmCtor.getClassName(), var);
    }

}