package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance.*;
import java.util.IdentityHashMap;
import java.util.List;

public class yIdentityHashMap {

    // helper
    public static yIdentityHashMap.yIdentityHashMapInstance requireIdentityHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yIdentityHashMap.yIdentityHashMapInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on IdentityHashMap objects."
            );
        }

        return (yIdentityHashMap.yIdentityHashMapInstance) obj;
    }

    public static RuntimeObject yIdentityHashMap_Instance_Prototype;

    static {
        yIdentityHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__IdentityHashMap__";
            }

            @Override
            public String toString() {
                return "<prototype:IdentityHashMap>";
            }
        };
        yIdentityHashMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // ihm.toString()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // ihm.put(key, value)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // ihm.putAll(otherIhm)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new PutAllFn());
        // ihm.get(key)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // ihm.remove(key)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // ihm.containsKey(key)  -- uses reference equality (==), not equals()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // ihm.containsValue(value)  -- uses reference equality (==)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new ContainsValueFn());
        // ihm.keySet() -> Y_ArrayObject of current keys (identity-based)
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new KeySetFn());
        // ihm.values() -> Y_ArrayObject of current values
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // ihm.entrySet() -> array of [key, value] pairs
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new EntrySetFn());
        // ihm.equals(other) -> compares mappings for equality
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new EqualsFn());
        // ihm.hashCode()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new HashCodeFn());
        // ihm.size()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // ihm.isEmpty()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // ihm.clear()
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // ihm.clone() -> shallow copy
        yIdentityHashMap_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    public static class yIdentityHashMapInstance extends yClass.ClassObjectInstance {

        // Uses reference equality (==) for key comparison, not equals()
        public final IdentityHashMap<Variable.Variant, Variable.Variant> data;

        public yIdentityHashMapInstance() {
            this.data = new IdentityHashMap<>();
            this.prototype = yIdentityHashMap_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "IdentityHashMap";
        }

        @Override
        public String toString() {
            return "<instance:IdentityHashMap>";
        }
    }

    public static class yIdentityHashMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            this.requireArity(arguments, 0, "IdentityHashMap");

            yIdentityHashMapInstance newMap = new yIdentityHashMapInstance();

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "IdentityHashMap";
        }

        @Override
        public String getType() {
            return "IdentityHashMap";
        }

        @Override
        public String toString() {
            return "<class:IdentityHashMap>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yIdentityHashMap.yIdentityHashMapClass ihmCtor = new yIdentityHashMap.yIdentityHashMapClass();
        Variable.Variant variant = new Variable.Variant(ihmCtor);
        Variable var = new Variable(variant, false, ihmCtor.getType());
        interpreter.defineGlobal(ihmCtor.getClassName(), var);
    }

}