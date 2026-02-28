package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;

public class Y_Array {

    // helper
    private static Y_ArrayObject requireArrayThis (Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_ArrayObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on array objects."
            );
        }

        return  (Y_ArrayObject) obj;
    }


    public static RuntimeObject Y_Array_Prototype;

    static {
        Y_Array_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "array_prototype";
            }
        };

        // arr.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                Y_ArrayObject array = requireArrayThis(interpreter);

                StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                for(int i = 0; i < array.data.size(); i++) {
                    Variable.Variant var = array.data.get(i);
                    if(var.value instanceof RuntimeObject) {
                        Variable toStringFn = ((RuntimeObject) var.value).get("toString");
                        if(toStringFn != null && toStringFn.value.isNativeFunction()) {
                            BoundNativeFunction bound = new BoundNativeFunction(toStringFn.value.asNativeFunction(), var.asRuntimeObject());
                            List<Variable.Variant> args = new ArrayList<>();
                            builder.append(bound.call(interpreter, args));
                        }
                        else {
                            builder.append("<class>");
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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(toString.getFnName(), toStringVar);

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

                Variable.Variant value = arguments.get(0);
                Y_ArrayObject array = requireArrayThis(interpreter);
                array.data.add(value);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(add.getFnName(), addVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(insert.getFnName(), insertVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(clear.getFnName(), clearVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

                Y_ArrayObject newArray = new Y_ArrayObject();

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(clone.getFnName(), cloneVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(contains.getFnName(), containsVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(
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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(size.getFnName(), sizeVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(remove.getFnName(), removeVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(set.getFnName(), setVar);

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

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(pop.getFnName(), popVar);

        class IsEmptyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_ArrayObject array = requireArrayThis(interpreter);

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
                TypeTag.OBJECT);
        Y_Array_Prototype.set(isEmpty.getFnName(), isEmptyVar);
    }

    public static class Y_ArrayObject extends RuntimeObject {

        private final ArrayList<Variable.Variant> data;

        public Y_ArrayObject(ArrayList<Variable.Variant> data) {
            this.data = data;
            this.prototype = Y_Array_Prototype;
        }

        public Y_ArrayObject() {
            this.data = new ArrayList<>();
            this.prototype = Y_Array_Prototype;
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
            return "<class:array>";
        }
    }

    public static class Y_ArrayInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            ArrayList<Variable.Variant> value = new ArrayList<>();
            Y_Array.Y_ArrayObject newArray = new Y_Array.Y_ArrayObject(value);

            return new Variable.Variant(newArray);
        }

        @Override
        public String getFnName() {
            return "Array";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_Array.Y_ArrayInit arrayCtor = new Y_Array.Y_ArrayInit();
        Variable.Variant variant = new Variable.Variant(arrayCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(arrayCtor.getFnName(), var);
    }

}
