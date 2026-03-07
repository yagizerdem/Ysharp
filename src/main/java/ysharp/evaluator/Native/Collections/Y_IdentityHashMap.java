package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class Y_IdentityHashMap {

    // helper
    private static Y_IdentityHashMap.Y_IdentityHashMapInstance requireIdentityHashMapThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_IdentityHashMap.Y_IdentityHashMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on IdentityHashMap objects."
            );
        }

        return (Y_IdentityHashMap.Y_IdentityHashMapInstance) obj;
    }

    public static RuntimeObject Y_IdentityHashMap_Instance_Prototype;

    static {
        Y_IdentityHashMap_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "identity_hash_map_prototype";
            }
        };
        Y_IdentityHashMap_Instance_Prototype.prototype = Y_Class.ClassPrototype;

        // ihm.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.toString");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;
                for (var entry : ihm.data.entrySet()) {
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
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // ihm.put(key, value)
        class PutFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 2, "IdentityHashMap.put");

                Variable.Variant key   = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                Variable.Variant previous = ihm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(put.getFnName(), putVar);


        // ihm.putAll(otherIhm)
        class PutAllFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.putAll");

                Variable.Variant otherVariant = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof Y_IdentityHashMapInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "putAll: argument must be an IdentityHashMap."
                    );
                }

                Y_IdentityHashMapInstance other = (Y_IdentityHashMapInstance) otherObj;
                ihm.data.putAll(other.data);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putAll";
            }
        }

        PutAllFn putAll = new PutAllFn();
        Variable putAllVar = new Variable(new Variable.Variant(putAll), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(putAll.getFnName(), putAllVar);


        // ihm.get(key)
        class GetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.get");

                Variable.Variant key = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                Variable.Variant value = ihm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(get.getFnName(), getVar);


        // ihm.remove(key)
        class RemoveFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.remove");

                Variable.Variant key = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                Variable.Variant removed = ihm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // ihm.containsKey(key)  -- uses reference equality (==), not equals()
        class ContainsKeyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.containsKey");

                Variable.Variant key = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                return new Variable.Variant(ihm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // ihm.containsValue(value)  -- uses reference equality (==)
        class ContainsValueFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.containsValue");

                Variable.Variant value = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                return new Variable.Variant(ihm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


        // ihm.keySet() -> Y_ArrayObject of current keys (identity-based)
        class KeySetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.keySet");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(ihm.data.keySet());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "keySet";
            }
        }

        KeySetFn keySet = new KeySetFn();
        Variable keySetVar = new Variable(new Variable.Variant(keySet), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(keySet.getFnName(), keySetVar);


        // ihm.values() -> Y_ArrayObject of current values
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.values");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(ihm.data.values());

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // ihm.entrySet() -> array of [key, value] pairs
        class EntrySetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.entrySet");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : ihm.data.entrySet()) {
                    ArrayList<Variable.Variant> pair = new ArrayList<>();
                    pair.add(entry.getKey());
                    pair.add(entry.getValue());
                    outerList.add(new Variable.Variant(new Y_Array.Y_ArrayInstance(pair)));
                }

                return new Variable.Variant(new Y_Array.Y_ArrayInstance(outerList));
            }

            @Override
            public String getFnName() {
                return "entrySet";
            }
        }

        EntrySetFn entrySet = new EntrySetFn();
        Variable entrySetVar = new Variable(new Variable.Variant(entrySet), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(entrySet.getFnName(), entrySetVar);


        // ihm.equals(other) -> compares mappings for equality
        class EqualsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 1, "IdentityHashMap.equals");

                Variable.Variant otherVariant = arguments.get(0);
                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof Y_IdentityHashMapInstance)) {
                    return new Variable.Variant(false);
                }

                Y_IdentityHashMapInstance other = (Y_IdentityHashMapInstance) otherObj;

                return new Variable.Variant(ihm.data.equals(other.data));
            }

            @Override
            public String getFnName() {
                return "equals";
            }
        }

        EqualsFn equals = new EqualsFn();
        Variable equalsVar = new Variable(new Variable.Variant(equals), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(equals.getFnName(), equalsVar);


        // ihm.hashCode()
        class HashCodeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.hashCode");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                return new Variable.Variant(ihm.data.hashCode());
            }

            @Override
            public String getFnName() {
                return "hashCode";
            }
        }

        HashCodeFn hashCode = new HashCodeFn();
        Variable hashCodeVar = new Variable(new Variable.Variant(hashCode), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(hashCode.getFnName(), hashCodeVar);


        // ihm.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.size");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                return new Variable.Variant(ihm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // ihm.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.isEmpty");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);

                return new Variable.Variant(ihm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // ihm.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.clear");

                Y_IdentityHashMapInstance ihm = requireIdentityHashMapThis(interpreter);
                ihm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, TypeTag.OBJECT);
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(clear.getFnName(), clearVar);


        // ihm.clone() -> shallow copy
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments, 0, "IdentityHashMap.clone");

                Y_IdentityHashMapInstance original = requireIdentityHashMapThis(interpreter);
                Y_IdentityHashMapInstance cloned   = new Y_IdentityHashMapInstance();

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
        Y_IdentityHashMap.Y_IdentityHashMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static class Y_IdentityHashMapInstance extends Y_Class.ClassObjectInstance {

        // Uses reference equality (==) for key comparison, not equals()
        final IdentityHashMap<Variable.Variant, Variable.Variant> data;

        public Y_IdentityHashMapInstance() {
            this.data = new IdentityHashMap<>();
            this.prototype = Y_IdentityHashMap_Instance_Prototype;
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
            return "<class:identity-hash-map>";
        }
    }

    public static class Y_IdentityHashMapClass extends Y_Class.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            this.requireArity(arguments, 0, "IdentityHashMap");

            Y_IdentityHashMapInstance newMap = new Y_IdentityHashMapInstance();

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
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_IdentityHashMap.Y_IdentityHashMapClass ihmCtor = new Y_IdentityHashMap.Y_IdentityHashMapClass();
        Variable.Variant variant = new Variable.Variant(ihmCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(ihmCtor.getClassName(), var);
    }

}