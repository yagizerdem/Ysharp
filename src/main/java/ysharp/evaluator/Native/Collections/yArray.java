package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;

import java.util.ArrayList;
import java.util.List;

public class yArray {

    // helper
    private static yArrayInstance requireArrayThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yArrayInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'array' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yArrayInstance) obj;
    }


    public static RuntimeObject yArray_Instance_Prototype;

    static {
        yArray_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Array__";
            }

            @Override
            public String toString() {
                return "<prototype:Array>";
            }
        };
        yArray_Instance_Prototype.prototype = yClass.ClassPrototype;

        // arr.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                for(int i = 0; i < array.data.size(); i++) {
                    Variable.Variant var = array.data.get(i);
                    if(var.value instanceof RuntimeObject) {
                        Variable toStringFn = ((RuntimeObject) var.value).get("toString");
                        if(toStringFn != null && toStringFn.value.isNativeFunction()) {
                            BoundNativeFunction bound = new BoundNativeFunction(toStringFn.value.asNativeFunction(), var.asRuntimeObject(), "this");
                            List<Variable.Variant> args = new ArrayList<>();
                            builder.append(bound.call(interpreter, args));
                        }
                        else {
                            builder.append(var.value.toString());
                        }
                    }
                    else {
                        builder.append(var.value.toString());
                    }

                    builder.append(" ");
                    if(i != array.data.size() -1) {
                        builder.append(", ");
                    }
                }
                builder.append("]");

                return new Variable.Variant(builder.toString());
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
                "function");
        yArray_Instance_Prototype.set(toString.getFnName(), toStringVar);

        // arr.add(value)
        class AddFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                Variable.Variant value = arguments.get(0);
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                if(value.isRuntimeObject()) {
                    array.data.add(value);
                }
                else {
                    array.data.add(new Variable.Variant(value.value));
                }

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "add";
            }
        }

        AddFn add = new AddFn();
        Variable addVar = new Variable(
                new Variable.Variant(add),
                true,
                "function");
        yArray_Instance_Prototype.set(add.getFnName(), addVar);
        yArray_Instance_Prototype.set("push", addVar); // add alias

        // arr.insert(index, value)
        class InsertFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);
                Variable.Variant value    = arguments.get(1);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'insert' first argument must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index > array.data.size()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Index out of bounds for 'insert'."
                    );
                }

                array.data.add(index, value);

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "insert";
            }
        }

        InsertFn insert = new InsertFn();
        Variable insertVar = new Variable(
                new Variable.Variant(insert),
                true,
                "function");
        yArray_Instance_Prototype.set(insert.getFnName(), insertVar);

        // arr.clear()
        class ClearFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                array.data.clear();

                return new Variable.Variant(array.data.size());
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
                "function");
        yArray_Instance_Prototype.set(clear.getFnName(), clearVar);

        // arr.clone()
        class CloneFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                yArrayInstance newArray = new yArrayInstance();

                // shallow copy
                newArray.data.addAll(array.data);

                return new Variable.Variant(newArray);
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
                "function");
        yArray_Instance_Prototype.set(clone.getFnName(), cloneVar);

        // arr.contains(value)
        class ContainsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant target = arguments.get(0);

                for (Variable.Variant element : array.data) {

                    if (element == null && target == null) {
                        return new Variable.Variant(true);
                    }

                    if (element != null && element.equals(target)) {
                        return new Variable.Variant(true);
                    }
                }

                return new Variable.Variant(false);
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(
                new Variable.Variant(contains),
                true,
                "function");
        yArray_Instance_Prototype.set(contains.getFnName(), containsVar);

        // arr.ensureCapacity(minCapacity)
        class EnsureCapacityFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant capVar = arguments.get(0);

                if (!capVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'ensureCapacity' argument must be a number."
                    );
                }

                int minCapacity = (int) capVar.implicitlyConvertNumber();

                if (minCapacity < 0) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'ensureCapacity' capacity cannot be negative."
                    );
                }

                array.data.ensureCapacity(minCapacity);

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "ensureCapacity";
            }
        }

        EnsureCapacityFn ensureCapacity = new EnsureCapacityFn();
        Variable ensureCapacityVar = new Variable(
                new Variable.Variant(ensureCapacity),
                true,
                "function");
        yArray_Instance_Prototype.set(
                ensureCapacity.getFnName(),
                ensureCapacityVar
        );

        // arr.size()
        class SizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                return new Variable.Variant(array.data.size());
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
                "function");
        yArray_Instance_Prototype.set(size.getFnName(), sizeVar);

        // arr.remove(index)
        class RemoveFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'remove' argument must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index >= array.data.size()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Index out of bounds for 'remove'."
                    );
                }

                Variable.Variant removed = array.data.remove(index);

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
                "function");
        yArray_Instance_Prototype.set(remove.getFnName(), removeVar);

        // arr.set(index, value)
        class SetFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);
                Variable.Variant newValue = arguments.get(1);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'set' first argument must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index >= array.data.size()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Index out of bounds for 'set'."
                    );
                }

                Variable.Variant oldValue = array.data.set(index, newValue);

                return oldValue;
            }

            @Override
            public String getFnName() {
                return "set";
            }
        }
        SetFn set = new SetFn();
        Variable setVar = new Variable(
                new Variable.Variant(set),
                true,
                "function");
        yArray_Instance_Prototype.set(set.getFnName(), setVar);

        // arr.get(index)
        class GetFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'get' argument must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index >= array.data.size()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Index out of bounds for 'get'."
                    );
                }

                return array.data.get(index);
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
                "function");

        yArray_Instance_Prototype.set(get.getFnName(), getVar);


        // arr.pop()
        class PopFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                int size = array.data.size();

                if (size == 0) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'pop' cannot be called on an empty array."
                    );
                }

                return array.data.remove(size - 1);
            }

            @Override
            public String getFnName() {
                return "pop";
            }
        }

        PopFn pop = new PopFn();
        Variable popVar = new Variable(
                new Variable.Variant(pop),
                true,
                "function");
        yArray_Instance_Prototype.set(pop.getFnName(), popVar);

        class IsEmptyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                return new Variable.Variant(array.data.isEmpty());
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
                "function");
        yArray_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);

        // arr.indexOf(value)
        class IndexOfFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant target = arguments.get(0);

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    if (element == null && target == null) {
                        return new Variable.Variant(i);
                    }

                    if (element != null && element.equals(target)) {
                        return new Variable.Variant(i);
                    }
                }

                return new Variable.Variant(-1);
            }

            @Override
            public String getFnName() {
                return "indexOf";
            }
        }

        IndexOfFn indexOf = new IndexOfFn();
        Variable indexOfVar = new Variable(
                new Variable.Variant(indexOf),
                true,
                "function");
        yArray_Instance_Prototype.set(indexOf.getFnName(), indexOfVar);


        // arr.lastIndexOf(value)
        class LastIndexOfFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant target = arguments.get(0);

                for (int i = array.data.size() - 1; i >= 0; i--) {
                    Variable.Variant element = array.data.get(i);

                    if (element == null && target == null) {
                        return new Variable.Variant(i);
                    }

                    if (element != null && element.equals(target)) {
                        return new Variable.Variant(i);
                    }
                }

                return new Variable.Variant(-1);
            }

            @Override
            public String getFnName() {
                return "lastIndexOf";
            }
        }

        LastIndexOfFn lastIndexOf = new LastIndexOfFn();
        Variable lastIndexOfVar = new Variable(
                new Variable.Variant(lastIndexOf),
                true,
                "function");
        yArray_Instance_Prototype.set(lastIndexOf.getFnName(), lastIndexOfVar);


        // arr.reverse()
        class ReverseFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                java.util.Collections.reverse(array.data);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "reverse";
            }
        }

        ReverseFn reverse = new ReverseFn();
        Variable reverseVar = new Variable(
                new Variable.Variant(reverse),
                true,
                "function");
        yArray_Instance_Prototype.set(reverse.getFnName(), reverseVar);

        // arr.slice(start, end)
        class SliceFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                Variable.Variant startVar = arguments.get(0);
                Variable.Variant endVar = arguments.get(1);

                if (!startVar.canImplicitlyConvertNumber() || !endVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'slice' arguments must be numbers."
                    );
                }

                int start = (int) startVar.implicitlyConvertNumber();
                int end = (int) endVar.implicitlyConvertNumber();

                if (start < 0) start = array.data.size() + start;
                if (end < 0) end = array.data.size() + end;

                start = Math.max(0, Math.min(start, array.data.size()));
                end = Math.max(start, Math.min(end, array.data.size()));

                yArrayInstance newArray = new yArrayInstance();
                newArray.data.addAll(array.data.subList(start, end));

                return new Variable.Variant(newArray);
            }

            @Override
            public String getFnName() {
                return "slice";
            }
        }

        SliceFn slice = new SliceFn();
        Variable sliceVar = new Variable(
                new Variable.Variant(slice),
                true,
                "function");
        yArray_Instance_Prototype.set(slice.getFnName(), sliceVar);


        // arr.join(separator)
        class JoinFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                String separator = arguments.get(0).value.toString();

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    if (element.value instanceof RuntimeObject) {
                        Variable toStringFn = ((RuntimeObject) element.value).get("toString");
                        if (toStringFn != null && toStringFn.value.isNativeFunction()) {
                            BoundNativeFunction bound = new BoundNativeFunction(
                                    toStringFn.value.asNativeFunction(),
                                    element.asRuntimeObject(),
                                    "this"
                            );
                            List<Variable.Variant> args = new ArrayList<>();
                            builder.append(bound.call(interpreter, args));
                        } else {
                            builder.append(element.value.toString());
                        }
                    } else {
                        builder.append(element.value.toString());
                    }

                    if (i < array.data.size() - 1) {
                        builder.append(separator);
                    }
                }

                return new Variable.Variant(builder.toString());
            }

            @Override
            public String getFnName() {
                return "join";
            }
        }

        JoinFn join = new JoinFn();
        Variable joinVar = new Variable(
                new Variable.Variant(join),
                true,
                "function");
        yArray_Instance_Prototype.set(join.getFnName(), joinVar);


        // arr.map(callback) callback = (element, index, oldArray) =>
        class MapFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(),1);

                yArrayInstance newArray = new yArrayInstance();

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    Variable.Variant result = callback.call(interpreter, args);
                    newArray.data.add(result);
                }

                return new Variable.Variant(newArray);
            }

            @Override
            public String getFnName() {
                return "map";
            }
        }

        MapFn map = new MapFn();
        Variable mapVar = new Variable(
                new Variable.Variant(map),
                true,
                "function");
        yArray_Instance_Prototype.set(map.getFnName(), mapVar);


        // arr.filter(callback)  callback = (element, index, oldArray) =>
        class FilterFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                yArrayInstance newArray = new yArrayInstance();

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    Variable.Variant result = callback.call(interpreter, args);

                    if (result.isTruthy()) {
                        newArray.data.add(element);
                    }
                }

                return new Variable.Variant(newArray);
            }

            @Override
            public String getFnName() {
                return "filter";
            }
        }

        FilterFn filter = new FilterFn();
        Variable filterVar = new Variable(
                new Variable.Variant(filter),
                true,
                "function");
        yArray_Instance_Prototype.set(filter.getFnName(), filterVar);


        // arr.reduce(callback, initialValue)
        class ReduceFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                Variable.Variant accumulator;
                int startIndex;

                if (arguments.size() > 1 && arguments.get(1) != null) {
                    accumulator = arguments.get(1);
                    startIndex = 0;
                } else {
                    accumulator = array.data.get(0);
                    startIndex = 1;
                }

                for (int i = startIndex; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(accumulator);
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    accumulator = callback.call(interpreter, args);
                }

                return accumulator;
            }

            @Override
            public String getFnName() {
                return "reduce";
            }
        }

        ReduceFn reduce = new ReduceFn();
        Variable reduceVar = new Variable(
                new Variable.Variant(reduce),
                true,
                "function");
        yArray_Instance_Prototype.set(reduce.getFnName(), reduceVar);

        // arr.find(callback)
        class FindFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                for (int i = 0; i < array.data.size(); i++) {
                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(array.data.get(i));
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    Variable.Variant result = callback.call(interpreter, args);
                    if (result.isTruthy()) {
                        return array.data.get(i);
                    }
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() { return "find"; }
        }

        FindFn find = new FindFn();
        Variable findVar = new Variable(
                new Variable.Variant(find),
                true,
                "function");
        yArray_Instance_Prototype.set(find.getFnName(), findVar);

        // arr.flat(depth)
        class FlatFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                int depth = requireInt(arguments.getFirst(), getFnName(), 1);

                yArrayInstance result = new yArrayInstance();
                flatten(array, result, depth);

                return new Variable.Variant(result);
            }

            private void flatten(yArrayInstance arr, yArrayInstance result, int depth) {
                for (Variable.Variant element : arr.data) {
                    if (depth > 0 && element.value instanceof yArrayInstance) {
                        flatten((yArrayInstance) element.value, result, depth - 1);
                    } else {
                        result.data.add(element);
                    }
                }
            }

            @Override
            public String getFnName() { return "flat"; }
        }

        FlatFn flat = new FlatFn();
        Variable flatVar = new Variable(
                new Variable.Variant(flat),
                true,
                "function");
        yArray_Instance_Prototype.set(flat.getFnName(), flatVar);

        // arr.some(callback) callback = (element, index, oldArray) =>
        class SomeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    Variable.Variant result = callback.call(interpreter, args);

                    if (result.isTruthy()) {
                        return new Variable.Variant(true);
                    }
                }

                return new Variable.Variant(false);
            }

            @Override
            public String getFnName() {
                return "some";
            }
        }

        SomeFn some = new SomeFn();
        Variable someVar = new Variable(
                new Variable.Variant(some),
                true,
                "function");
        yArray_Instance_Prototype.set(some.getFnName(), someVar);


        // arr.every(callback) callback = (element, index, oldArray) =>
        class EveryFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    Variable.Variant result = callback.call(interpreter, args);

                    if (!result.isTruthy()) {
                        return new Variable.Variant(false);
                    }
                }

                return new Variable.Variant(true);
            }

            @Override
            public String getFnName() {
                return "every";
            }
        }

        EveryFn every = new EveryFn();
        Variable everyVar = new Variable(
                new Variable.Variant(every),
                true,
                "function");
        yArray_Instance_Prototype.set(every.getFnName(), everyVar);

        // arr.forEach(callback) callback = (element, index, oldArray) =>
        class ForEachFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                for (int i = 0; i < array.data.size(); i++) {
                    Variable.Variant element = array.data.get(i);

                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(element);
                    args.add(new Variable.Variant(i));
                    args.add(new Variable.Variant(array));

                    callback.call(interpreter, args);
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "forEach";
            }
        }

        ForEachFn forEach = new ForEachFn();
        Variable forEachVar = new Variable(
                new Variable.Variant(forEach),
                true,
                "function");
        yArray_Instance_Prototype.set(forEach.getFnName(), forEachVar);


        // arr.shift() removes first element
        class ShiftFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());

                if (array.data.isEmpty()) {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, 0, "'shift' cannot be called on an empty array.");
                }

                return array.data.remove(0);
            }

            @Override
            public String getFnName() { return "shift"; }
        }

        ShiftFn shift = new ShiftFn();
        Variable shiftVar = new Variable(
                new Variable.Variant(shift),
                true,
                "function");
        yArray_Instance_Prototype.set(shift.getFnName(), shiftVar);


        // arr.unshift(value) adds an element to the beginning of the array
        class UnshiftFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Variable.Variant value = arguments.get(0);

                array.data.add(0, value);

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "unshift";
            }
        }

        UnshiftFn unshift = new UnshiftFn();
        Variable unshiftVar = new Variable(
                new Variable.Variant(unshift),
                true,
                "function");
        yArray_Instance_Prototype.set(unshift.getFnName(), unshiftVar);


        // arr.fill(value) replaces all elements in the array with a static value
        class FillFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Variable.Variant value = arguments.get(0);

                for (int i = 0; i < array.data.size(); i++) {
                    array.data.set(i, value);
                }

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "fill";
            }
        }

        FillFn fill = new FillFn();
        Variable fillVar = new Variable(
                new Variable.Variant(fill),
                true,
                "function");
        yArray_Instance_Prototype.set(fill.getFnName(), fillVar);


        // arr.sort(callback) sorts the array elements in-place using a custom comparator function
        // callback = (cur, other) =>
        class SortFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yArrayInstance array = requireArrayThis(interpreter, getFnName());
                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                final YsharpError[] sortError = new YsharpError[1];

                try {
                    array.data.sort((cur, other) -> {
                        List<Variable.Variant> args = new ArrayList<>();
                        args.add(cur);
                        args.add(other);

                        try {
                            Variable.Variant result = callback.call(interpreter, args);

                            if (result.canImplicitlyConvertNumber()) {
                                double val = result.implicitlyConvertNumber();
                                if (val > 0) return 1;
                                if (val < 0) return -1;
                                return 0;
                            }

                            return result.isTruthy() ? 1 : -1;

                        } catch (YsharpError e) {
                            sortError[0] = e;
                            return 0;
                        }
                    });
                } catch (IllegalArgumentException e) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Comparison method violates its general contract in 'sort'. Ensure consistent return values."
                    );
                }

                // If an error occurred inside the callback, rethrow it here
                if (sortError[0] != null) {
                    throw sortError[0];
                }

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "sort";
            }
        }

        SortFn sort = new SortFn();
        Variable sortVar = new Variable(
                new Variable.Variant(sort),
                true,
                "function");
        yArray_Instance_Prototype.set(sort.getFnName(), sortVar);
    }

    public static class yArrayInstance extends yClass.ClassObjectInstance {

        public final ArrayList<Variable.Variant> data;

        public yArrayInstance(ArrayList<Variable.Variant> data) {
            this.data = data;
            this.prototype = yArray_Instance_Prototype;
        }

        public yArrayInstance() {
            this.data = new ArrayList<>();
            this.prototype = yArray_Instance_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Array";
        }

        @Override
        public String toString() {
            return "<instance:Array>";
        }
    }

    public static class yArrayClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yArrayClass(){
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            ArrayList<Variable.Variant> value = new ArrayList<>();
            yArray.yArrayInstance newArray = new yArray.yArrayInstance(value);

            return new Variable.Variant(newArray);
        }

        @Override
        public String getClassName() {
            return "Array";
        }

        @Override
        public String getType() {
            return "Array";
        }

        @Override
        public String toString() {
            return "<class:Array>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yArrayClass arrayCtor = new yArrayClass();
        Variable.Variant variant = new Variable.Variant(arrayCtor);
        Variable var = new Variable(variant, false, arrayCtor.getType());
        interpreter.defineGlobal(arrayCtor.getClassName(), var);
    }

}
