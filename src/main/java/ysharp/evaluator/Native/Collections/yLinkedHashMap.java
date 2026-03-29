package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Array.yArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class yLinkedHashMap {

    // helper
    private static yLinkedHashMap.yLinkedHashMapInstance requireLinkedHashMapThis(Interpreter interpreter) {
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
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

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
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant previous = lhm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(put.getFnName(), putVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant value = lhm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(get.getFnName(), getVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant value = lhm.data.get(key);

                return value != null ? value : defaultValue;
            }

            @Override
            public String getFnName() {
                return "getOrDefault";
            }
        }

        GetOrDefaultFn getOrDefault = new GetOrDefaultFn();
        Variable getOrDefaultVar = new Variable(new Variable.Variant(getOrDefault), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(getOrDefault.getFnName(), getOrDefaultVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant removed = lhm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(remove.getFnName(), removeVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(lhm.data.keySet());

                yArray.yArrayInstance array = new yArray.yArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(keys.getFnName(), keysVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(lhm.data.values());

                yArray.yArrayInstance array = new yArray.yArrayInstance(list);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(values.getFnName(), valuesVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : lhm.data.entrySet()) {
                    ArrayList<Variable.Variant> pair = new ArrayList<>();
                    pair.add(entry.getKey());
                    pair.add(entry.getValue());
                    outerList.add(new Variable.Variant(new yArray.yArrayInstance(pair)));
                }

                return new Variable.Variant(new yArray.yArrayInstance(outerList));
            }

            @Override
            public String getFnName() {
                return "entries";
            }
        }

        EntriesFn entries = new EntriesFn();
        Variable entriesVar = new Variable(new Variable.Variant(entries), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

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
        Variable firstKeyVar = new Variable(new Variable.Variant(firstKey), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(firstKey.getFnName(), firstKeyVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

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
        Variable lastKeyVar = new Variable(new Variable.Variant(lastKey), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(lastKey.getFnName(), lastKeyVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

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
        Variable removeFirstVar = new Variable(new Variable.Variant(removeFirst), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(removeFirst.getFnName(), removeFirstVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

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
        Variable removeLastVar = new Variable(new Variable.Variant(removeLast), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(removeLast.getFnName(), removeLastVar);


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
                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                Variable.Variant existing = lhm.data.putIfAbsent(key, value);

                return existing != null ? existing : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putIfAbsent";
            }
        }

        PutIfAbsentFn putIfAbsent = new PutIfAbsentFn();
        Variable putIfAbsentVar = new Variable(new Variable.Variant(putIfAbsent), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(putIfAbsent.getFnName(), putIfAbsentVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(size.getFnName(), sizeVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);

                return new Variable.Variant(lhm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


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

                yLinkedHashMapInstance lhm = requireLinkedHashMapThis(interpreter);
                lhm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(clear.getFnName(), clearVar);


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

                yLinkedHashMapInstance original = requireLinkedHashMapThis(interpreter);
                yLinkedHashMapInstance cloned   = new yLinkedHashMapInstance();

                cloned.data.putAll(original.data);

                return new Variable.Variant(cloned);
            }

            @Override
            public String getFnName() {
                return "clone";
            }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(new Variable.Variant(clone), true, "function");
        yLinkedHashMap.yLinkedHashMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static class yLinkedHashMapInstance extends yClass.ClassObjectInstance {

        final LinkedHashMap<Variable.Variant, Variable.Variant> data;

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