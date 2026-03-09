package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class yHashTable {

    // helper
    private static yHashTable.yMapInstance requireMapThis (Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yHashTable.yMapInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on map objects."
            );
        }

        return  (yHashTable.yMapInstance) obj;
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
                return "map_prototype";
            }
        };
        yMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // map.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;

                for (var entry : map.data.entrySet()) {

                    if (!first) {
                        sb.append(", ");
                    }

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
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(toString.getFnName(), toStringVar);

        //  map.put(10, 100)
        class PutFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                Variable.Variant key = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                yMapInstance array = requireMapThis(interpreter);
                array.data.put(key, value);

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(
                new Variable.Variant(put),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(put.getFnName(), putVar);


        // map.get("name")
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
                yMapInstance map = requireMapThis(interpreter);

                Variable.Variant value = map.data.get(key);

                if (value == null) {
                    return new Variable.Variant(null);
                }

                return value;
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(
                new Variable.Variant(get),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(get.getFnName(), getVar);


        // map.remove("name")
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
                yMapInstance map = requireMapThis(interpreter);

                Variable.Variant removed = map.data.remove(key);

                if (removed == null) {
                    return new Variable.Variant(null);
                }

                return removed;
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(
                new Variable.Variant(remove),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // map.containsKey("name")
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
                yMapInstance map = requireMapThis(interpreter);

                boolean exists = map.data.containsKey(key);

                return new Variable.Variant(exists);
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(
                new Variable.Variant(containsKey),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // map.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                return new Variable.Variant(map.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(
                new Variable.Variant(size),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // map.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);
                map.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(
                new Variable.Variant(clear),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(clear.getFnName(), clearVar);

        // map.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                return new Variable.Variant(map.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(
                new Variable.Variant(isEmpty),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);

        // map.keys()
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                java.util.ArrayList<Variable.Variant> list =
                        new ArrayList<>();

                for (Variable.Variant key : map.data.keySet()) {
                    list.add(key);
                }

                yArray.yArrayInstance array =
                        new yArray.yArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(
                new Variable.Variant(keys),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(keys.getFnName(), keysVar);


        // map.values()
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                java.util.ArrayList<Variable.Variant> list =
                        new java.util.ArrayList<>();

                for (Variable.Variant value : map.data.values()) {
                    list.add(value);
                }

                yArray.yArrayInstance array =
                        new yArray.yArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(
                new Variable.Variant(values),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // map.entries()
        class EntriesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance map = requireMapThis(interpreter);

                java.util.ArrayList<Variable.Variant> outerList =
                        new java.util.ArrayList<>();

                for (var entry : map.data.entrySet()) {

                    java.util.ArrayList<Variable.Variant> pairList =
                            new java.util.ArrayList<>();

                    pairList.add(entry.getKey());
                    pairList.add(entry.getValue());

                    yArray.yArrayInstance pairArray =
                            new yArray.yArrayInstance(pairList);

                    outerList.add(new Variable.Variant(pairArray));
                }

                yArray.yArrayInstance resultArray =
                        new yArray.yArrayInstance(outerList);

                return new Variable.Variant(resultArray);
            }

            @Override
            public String getFnName() {
                return "entries";
            }
        }

        EntriesFn entries = new EntriesFn();
        Variable entriesVar = new Variable(
                new Variable.Variant(entries),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


        // map.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yMapInstance original = requireMapThis(interpreter);

                // shallow copy
                Hashtable<Variable.Variant, Variable.Variant> newTable =
                        new Hashtable<>(original.data);

                yMapInstance clonedMap = new yMapInstance(newTable);

                return new Variable.Variant(clonedMap);
            }

            @Override
            public String getFnName() {
                return "clone";
            }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(
                new Variable.Variant(clone),
                true,
                TypeTag.OBJECT);
        yHashTable.yMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }


    public static class yMapInstance extends yClass.ClassObjectInstance {

        private final Hashtable<Variable.Variant, Variable.Variant> data;

        public yMapInstance(Hashtable<Variable.Variant, Variable.Variant> data) {
            this.data = data;
            this.prototype = yMap_Instance_Prototype;
        }

        public yMapInstance() {
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
            return "<class:hash-table>";
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
            yMapInstance newMap = new yMapInstance(value);

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
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yMapClass mapCtor = new yMapClass();
        Variable.Variant variant = new Variable.Variant(mapCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(mapCtor.getClassName(), var);
    }

}
