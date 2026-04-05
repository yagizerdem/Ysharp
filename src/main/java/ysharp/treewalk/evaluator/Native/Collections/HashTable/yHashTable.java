package ysharp.treewalk.evaluator.Native.Collections.HashTable;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.HashTable.function.instance.*;

import java.util.Hashtable;
import java.util.List;

public class yHashTable {

    // helper
    public static yHashTable.yHashTableInstance requireHashTableThis (Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yHashTable.yHashTableInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on map objects."
            );
        }

        return  (yHashTable.yHashTableInstance) obj;
    }

    public static RuntimeObject yMap_Instance_Prototype;

    static {
        yMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__HashTable__";
            }

            @Override
            public String toString() {
                return "<prototype:HashTable>";
            }
        };
        yMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // ht.toString()
        yMap_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        //  ht.put(key, value)
        yMap_Instance_Prototype.RegisterNativeFn(new PutFn());
        // ht.get(key)
        yMap_Instance_Prototype.RegisterNativeFn(new GetFn());
        // ht.remove(key)
        yMap_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // ht.containsKey("name")
        yMap_Instance_Prototype.RegisterNativeFn(new ContainsKeyFn());
        // ht.size()
        yMap_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // ht.clear()
        yMap_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // ht.isEmpty()
        yMap_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // ht.keys()
        yMap_Instance_Prototype.RegisterNativeFn(new KeysFn());
        // ht.values()
        yMap_Instance_Prototype.RegisterNativeFn(new ValuesFn());
        // ht.entries()
        yMap_Instance_Prototype.RegisterNativeFn(new EntriesFn());
        // ht.clone()
        yMap_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }


    public static class yHashTableInstance extends yClass.ClassObjectInstance {

        public final Hashtable<Variable.Variant, Variable.Variant> data;

        public yHashTableInstance(Hashtable<Variable.Variant, Variable.Variant> data) {
            this.data = data;
            this.prototype = yMap_Instance_Prototype;
        }

        public yHashTableInstance() {
            this.data = new Hashtable<>();
            this.prototype = yMap_Instance_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "HashTable";
        }

        @Override
        public String toString() {
            return "<instance:HashTable>";
        }
    }

    public static class yMapClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            Hashtable<Variable.Variant, Variable.Variant> value = new Hashtable<>();
            yHashTableInstance newMap = new yHashTableInstance(value);

            return new Variable.Variant(newMap);
        }

        @Override
        public String getClassName() {
            return "HashTable";
        }

        @Override
        public String getType() {
            return "HashTable";
        }

        @Override
        public String toString() {
            return "<class:HashTable>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yMapClass mapCtor = new yMapClass();
        Variable.Variant variant = new Variable.Variant(mapCtor);
        Variable var = new Variable(variant, false, mapCtor.getType());
        interpreter.defineGlobal(mapCtor.getClassName(), var);
    }

}
