package ysharp.evaluator.Native.Collections.HashMap;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.HashMap.function.instance.*;

import java.util.HashMap;
import java.util.List;

public class yHashMap {

    // helper
    public static yHashMap.yHashMapInstance requireHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yHashMap.yHashMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on HashMap objects."
            );
        }

        return (yHashMap.yHashMapInstance) obj;
    }

    public static RuntimeObject yHashMap_Instance_Prototype;

    static {
        yHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__HashMap__";
            }

            @Override
            public String toString() {
                return "<prototype:HashMap>";
            }
        };
        yHashMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // hm.toString()
        yHashMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // hm.put(key, value)
        yHashMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // hm.get(key)
        yHashMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // hm.getOrDefault(key, default)
        yHashMap_Instance_Prototype.RegisterNativeFn(new GetOrDefaultFn());
        // hm.remove(key)
        yHashMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // hm.containsKey(key)
        yHashMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // hm.containsValue(value)
        yHashMap_Instance_Prototype.RegisterNativeFn(new ContainsValueFn());
        // hm.putIfAbsent(key, value)
        yHashMap_Instance_Prototype.RegisterNativeFn(new PutIfAbsentFn());
        // hm.replace(key, value)
        yHashMap_Instance_Prototype.RegisterNativeFn(new ReplaceFn());
        // hm.merge(key, value, remappingFn)
        // if key absent -> put value, else call remappingFn(oldVal, newVal) and store result
        yHashMap_Instance_Prototype.RegisterNativeFn(new MergeFn());
        // hm.compute(key, remappingFn(key, oldVal))
        // oldVal is null if key absent; if fn returns null, key is removed
        yHashMap_Instance_Prototype.RegisterNativeFn(new ComputeFn());
        // hm.computeIfAbsent(key, mappingFn(key))
        yHashMap_Instance_Prototype.RegisterNativeFn(new ComputeIfAbsentFn());
        // hm.computeIfPresent(key, remappingFn(key, oldVal))
        yHashMap_Instance_Prototype.RegisterNativeFn(new ComputeIfPresentFn());
        // hm.keys()
        yHashMap_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // hm.values()
        yHashMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // hm.entries()
        yHashMap_Instance_Prototype.RegisterNativeFn(new EntriesFn());
        // hm.size()
        yHashMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // hm.isEmpty()
        yHashMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // hm.clear()
        yHashMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // hm.clone()
        yHashMap_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    public static class yHashMapInstance extends yClass.ClassObjectInstance {

        public final HashMap<Variable.Variant, Variable.Variant> data;

        public yHashMapInstance() {
            this.data = new HashMap<>();
            this.prototype = yHashMap_Instance_Prototype;
        }

        public yHashMapInstance(HashMap<Variable.Variant, Variable.Variant> map) {
            this.data = map;
            this.prototype = yHashMap_Instance_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "HashMap";
        }

        @Override
        public String toString() {
            return "<instance:HashMap>";
        }

        @Override
        public Object getNativeJavaObject() {
            HashMap<Object, Object> nativeHashMap = new HashMap<>();
            for(Variable.Variant key : this.data.keySet()) {
                Object data = this.data.get(key).asJavaNative();
                nativeHashMap.put(key.asJavaNative(), data);
            }
            return nativeHashMap;
        }

    }

    public static class yHashMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yHashMapInstance newMap = new yHashMapInstance();

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "HashMap";
        }

        @Override
        public String getType() {
            return "HashMap";
        }

        @Override
        public String toString() {
            return "<class:HashMap>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yHashMap.yHashMapClass hmCtor = new yHashMap.yHashMapClass();
        Variable.Variant variant = new Variable.Variant(hmCtor);
        Variable var = new Variable(variant, false, hmCtor.getType());
        interpreter.defineGlobal(hmCtor.getClassName(), var);
    }

}