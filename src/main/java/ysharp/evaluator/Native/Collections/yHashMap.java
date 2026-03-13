package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class yHashMap {

    // helper
    private static yHashMap.yHashMapInstance requireHashMapThis(Interpreter interpreter) {
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
                return "hash_map_prototype";
            }
        };
        yHashMap_Instance_Prototype.prototype = yClass.ClassPrototype;

        // hm.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("{");

                boolean first = true;
                for (var entry : hm.data.entrySet()) {
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
        yHashMap.yHashMap_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // hm.put(key, value)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant previous = hm.data.put(key, value);

                return previous != null ? previous : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "put";
            }
        }

        PutFn put = new PutFn();
        Variable putVar = new Variable(new Variable.Variant(put), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(put.getFnName(), putVar);


        // hm.get(key)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant value = hm.data.get(key);

                return value != null ? value : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(new Variable.Variant(get), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(get.getFnName(), getVar);


        // hm.getOrDefault(key, default)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant value = hm.data.get(key);

                return value != null ? value : defaultValue;
            }

            @Override
            public String getFnName() {
                return "getOrDefault";
            }
        }

        GetOrDefaultFn getOrDefault = new GetOrDefaultFn();
        Variable getOrDefaultVar = new Variable(new Variable.Variant(getOrDefault), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(getOrDefault.getFnName(), getOrDefaultVar);


        // hm.remove(key)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant removed = hm.data.remove(key);

                return removed != null ? removed : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(remove.getFnName(), removeVar);


        // hm.containsKey(key)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                return new Variable.Variant(hm.data.containsKey(key));
            }

            @Override
            public String getFnName() {
                return "containsKey";
            }
        }

        ContainsKeyFn containsKey = new ContainsKeyFn();
        Variable containsKeyVar = new Variable(new Variable.Variant(containsKey), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(containsKey.getFnName(), containsKeyVar);


        // hm.containsValue(value)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                return new Variable.Variant(hm.data.containsValue(value));
            }

            @Override
            public String getFnName() {
                return "containsValue";
            }
        }

        ContainsValueFn containsValue = new ContainsValueFn();
        Variable containsValueVar = new Variable(new Variable.Variant(containsValue), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(containsValue.getFnName(), containsValueVar);


        // hm.putIfAbsent(key, value)
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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant existing = hm.data.putIfAbsent(key, value);

                return existing != null ? existing : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putIfAbsent";
            }
        }

        PutIfAbsentFn putIfAbsent = new PutIfAbsentFn();
        Variable putIfAbsentVar = new Variable(new Variable.Variant(putIfAbsent), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(putIfAbsent.getFnName(), putIfAbsentVar);


        // hm.replace(key, value)
        class ReplaceFn extends Function.NativeFunction {

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
                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant old = hm.data.replace(key, value);

                return old != null ? old : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "replace";
            }
        }

        ReplaceFn replace = new ReplaceFn();
        Variable replaceVar = new Variable(new Variable.Variant(replace), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(replace.getFnName(), replaceVar);


        // hm.merge(key, value, remappingFn)
        // if key absent -> put value, else call remappingFn(oldVal, newVal) and store result
        class MergeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 3;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key      = arguments.get(0);
                Variable.Variant value    = arguments.get(1);
                Variable.Variant fnVariant = arguments.get(2);

                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant existing = hm.data.get(key);

                if (existing == null) {
                    hm.data.put(key, value);
                    return value;
                }


                if (!fnVariant.isCallable()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "merge: third argument must be a function."
                    );
                }

                Callable fn = fnVariant.asCallable();

                List<Variable.Variant> args = new ArrayList<>();
                args.add(existing);
                args.add(value);

                Variable.Variant result = fn.call(interpreter, args);

                if (result == null || result.value == null) {
                    hm.data.remove(key);
                    return new Variable.Variant(null);
                }

                hm.data.put(key, result);
                return result;
            }

            @Override
            public String getFnName() {
                return "merge";
            }
        }

        MergeFn merge = new MergeFn();
        Variable mergeVar = new Variable(new Variable.Variant(merge), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(merge.getFnName(), mergeVar);


        // hm.compute(key, remappingFn(key, oldVal))
        // oldVal is null if key absent; if fn returns null, key is removed
        class ComputeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key      = arguments.get(0);
                Variable.Variant fnVariant = arguments.get(1);

                yHashMapInstance hm = requireHashMapThis(interpreter);

                if (!fnVariant.isCallable()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "merge: third argument must be a function."
                    );
                }

                Callable fn = fnVariant.asCallable();

                Variable.Variant oldVal = hm.data.getOrDefault(key, new Variable.Variant(null));

                List<Variable.Variant> args = new ArrayList<>();
                args.add(key);
                args.add(oldVal);

                Variable.Variant result = fn.call(interpreter, args);

                if (result == null || result.value == null) {
                    hm.data.remove(key);
                    return new Variable.Variant(null);
                }

                hm.data.put(key, result);
                return result;
            }

            @Override
            public String getFnName() {
                return "compute";
            }
        }

        ComputeFn compute = new ComputeFn();
        Variable computeVar = new Variable(new Variable.Variant(compute), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(compute.getFnName(), computeVar);


        // hm.computeIfAbsent(key, mappingFn(key))
        class ComputeIfAbsentFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key      = arguments.get(0);
                Variable.Variant fnVariant = arguments.get(1);

                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant existing = hm.data.get(key);
                if (existing != null) {
                    return existing;
                }

                if (!fnVariant.isCallable()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "merge: third argument must be a function."
                    );
                }

                Callable fn = fnVariant.asCallable();

                List<Variable.Variant> args = new ArrayList<>();
                args.add(key);

                Variable.Variant result = fn.call(interpreter, args);

                if (result != null && result.value != null) {
                    hm.data.put(key, result);
                }

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "computeIfAbsent";
            }
        }

        ComputeIfAbsentFn computeIfAbsent = new ComputeIfAbsentFn();
        Variable computeIfAbsentVar = new Variable(new Variable.Variant(computeIfAbsent), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(computeIfAbsent.getFnName(), computeIfAbsentVar);


        // hm.computeIfPresent(key, remappingFn(key, oldVal))
        class ComputeIfPresentFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant key      = arguments.get(0);
                Variable.Variant fnVariant = arguments.get(1);

                yHashMapInstance hm = requireHashMapThis(interpreter);

                Variable.Variant existing = hm.data.get(key);
                if (existing == null) {
                    return new Variable.Variant(null);
                }

                if (!fnVariant.isCallable()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "merge: third argument must be a function."
                    );
                }

                Callable fn = fnVariant.asCallable();

                List<Variable.Variant> args = new ArrayList<>();
                args.add(key);
                args.add(existing);

                Variable.Variant result = fn.call(interpreter, args);

                if (result == null || result.value == null) {
                    hm.data.remove(key);
                    return new Variable.Variant(null);
                }

                hm.data.put(key, result);
                return result;
            }

            @Override
            public String getFnName() {
                return "computeIfPresent";
            }
        }

        ComputeIfPresentFn computeIfPresent = new ComputeIfPresentFn();
        Variable computeIfPresentVar = new Variable(new Variable.Variant(computeIfPresent), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(computeIfPresent.getFnName(), computeIfPresentVar);


        // hm.keys()
        class KeysFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(hm.data.keySet());

                return new Variable.Variant(new yArray.yArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "keys";
            }
        }

        KeysFn keys = new KeysFn();
        Variable keysVar = new Variable(new Variable.Variant(keys), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(keys.getFnName(), keysVar);


        // hm.values()
        class ValuesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(hm.data.values());

                return new Variable.Variant(new yArray.yArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "values";
            }
        }

        ValuesFn values = new ValuesFn();
        Variable valuesVar = new Variable(new Variable.Variant(values), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(values.getFnName(), valuesVar);


        // hm.entries()
        class EntriesFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                ArrayList<Variable.Variant> outerList = new ArrayList<>();

                for (var entry : hm.data.entrySet()) {
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
        yHashMap.yHashMap_Instance_Prototype.set(entries.getFnName(), entriesVar);


        // hm.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                return new Variable.Variant(hm.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(size.getFnName(), sizeVar);


        // hm.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);

                return new Variable.Variant(hm.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // hm.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance hm = requireHashMapThis(interpreter);
                hm.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, "function");
        yHashMap.yHashMap_Instance_Prototype.set(clear.getFnName(), clearVar);


        // hm.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yHashMapInstance original = requireHashMapThis(interpreter);
                yHashMapInstance cloned   = new yHashMapInstance();

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
        yHashMap.yHashMap_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static class yHashMapInstance extends yClass.ClassObjectInstance {

        final HashMap<Variable.Variant, Variable.Variant> data;

        public yHashMapInstance() {
            this.data = new HashMap<>();
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
            return "<class:hash-map>";
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
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yHashMap.yHashMapClass hmCtor = new yHashMap.yHashMapClass();
        Variable.Variant variant = new Variable.Variant(hmCtor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(hmCtor.getClassName(), var);
    }

}