package ysharp.treewalk.evaluator.Native.Collections.LinkedHashMap;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.LinkedHashMap.function.instance.*;
import java.util.LinkedHashMap;
import java.util.List;

public class yLinkedHashMap {

    // helper
    public static yLinkedHashMap.yLinkedHashMapInstance requireLinkedHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yLinkedHashMap.yLinkedHashMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on LinkedHashMap objects."
            );
        }

        return (yLinkedHashMap.yLinkedHashMapInstance) obj;
    }

    public static RuntimeObject yLinkedHashMap_Instance_Prototype;

    static {
        yLinkedHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__LinkedHashMap__";
            }

            @Override
            public String toString() {
                return "<prototype:LinkedHashMap>";
            }
        };
        yLinkedHashMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // lhm.toString()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // lhm.put(key, value)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // lhm.get(key)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // lhm.getOrDefault(key, default)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new GetOrDefaultFn());
        // lhm.remove(key)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // lhm.containsKey(key)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // lhm.containsValue(value)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new ContainsValueFn());
        // lhm.keys() -> Y_ArrayObject in insertion order
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // lhm.values() -> Y_ArrayObject in insertion order
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // lhm.entries() -> array of [key, value] pairs in insertion order
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new EntriesFn());
        // lhm.firstKey()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new FirstKeyFn());
        // lhm.lastKey()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new LastKeyFn());
        // lhm.removeFirst() -> removes and returns value of first inserted entry
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new RemoveFirstFn());
        // lhm.removeLast() -> removes and returns value of last inserted entry
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new RemoveLastFn());
        // lhm.putIfAbsent(key, value)
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new PutIfAbsentFn());
        // lhm.size()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // lhm.isEmpty()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // lhm.clear()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // lhm.clone()
        yLinkedHashMap_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    public static class yLinkedHashMapInstance extends yClass.ClassObjectInstance {

        public final LinkedHashMap<Variable.Variant, Variable.Variant> data;

        public yLinkedHashMapInstance() {
            this.data = new LinkedHashMap<>();
            this.prototype = yLinkedHashMap_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "LinkedHashMap";
        }

        public String toString() {
            return "<instance:LinkedHashMap>";
        }
    }

    public static class yLinkedHashMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yLinkedHashMapInstance newMap = new yLinkedHashMapInstance();

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

        public String toString() {
            return "<class:LinkedHashMap>";
        }

    }

    public static void Register(Interpreter interpreter) throws Exception {
        yLinkedHashMap.yLinkedHashMapClass lhmCtor = new yLinkedHashMap.yLinkedHashMapClass();
        Variable.Variant variant = new Variable.Variant(lhmCtor);
        Variable var = new Variable(variant, false, lhmCtor.getType());
        interpreter.defineGlobal(lhmCtor.getClassName(), var);
    }

}