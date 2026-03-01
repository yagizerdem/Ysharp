package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Y_Stack {

    // helper
    private static Y_Stack.Y_StackObject requireStackThis (Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_Stack.Y_StackObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on stack objects."
            );
        }

        return  (Y_Stack.Y_StackObject) obj;
    }

    public static RuntimeObject Y_Stack_Prototype;

    static {
        Y_Stack_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "stack_prototype";
            }
        };

        // stack.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                Y_Stack.Y_StackObject array = requireStackThis(interpreter);

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
        Y_Stack_Prototype.set(toString.getFnName(), toStringVar);

        // arr.add(value)
        class PushFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);
                stack.data.push(value);

                return new Variable.Variant(stack.data.size());
            }

            @Override
            public String getFnName() {
                return "push";
            }
        }

        PushFn push = new PushFn();
        Variable addVar = new Variable(
                new Variable.Variant(push),
                true,
                TypeTag.OBJECT);
        Y_Stack_Prototype.set(push.getFnName(), addVar);

        // stack.pop()
        class PopFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                if (stack.data.isEmpty()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'pop' cannot be called on an empty stack."
                    );
                }

                return stack.data.pop();
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
        Y_Stack_Prototype.set(pop.getFnName(), popVar);

        // stack.peek()
        class PeekFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                if (stack.data.isEmpty()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'peek' cannot be called on an empty stack."
                    );
                }

                return stack.data.peek();
            }

            @Override
            public String getFnName() {
                return "peek";
            }
        }

        PeekFn peek = new PeekFn();
        Variable peekVar = new Variable(
                new Variable.Variant(peek),
                true,
                TypeTag.OBJECT);
        Y_Stack_Prototype.set(peek.getFnName(), peekVar);

        // stack.empty()
        class EmptyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                return new Variable.Variant(stack.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "empty";
            }
        }

        EmptyFn empty = new EmptyFn();
        Variable emptyVar = new Variable(
                new Variable.Variant(empty),
                true,
                TypeTag.OBJECT);
        Y_Stack_Prototype.set(empty.getFnName(), emptyVar);

        // stack.search(element)
        class SearchFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                Variable.Variant target = arguments.get(0);

                // Stack top -> end of list
                for (int i = stack.data.size() - 1; i >= 0; i--) {

                    Variable.Variant element = stack.data.get(i);

                    if (element == null && target == null) {
                        return new Variable.Variant(
                                stack.data.size() - i
                        );
                    }

                    if (element != null && element.equals(target)) {
                        return new Variable.Variant(
                                stack.data.size() - i
                        );
                    }
                }

                return new Variable.Variant(-1);
            }

            @Override
            public String getFnName() {
                return "search";
            }
        }

        SearchFn search = new SearchFn();
        Variable searchVar = new Variable(
                new Variable.Variant(search),
                true,
                TypeTag.OBJECT);
        Y_Stack_Prototype.set(search.getFnName(), searchVar);

        // stack.add(index, value)
        class AddAtIndexFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                Variable.Variant indexVar = arguments.get(0);
                Variable.Variant value = arguments.get(1);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'add' first argument must be an integer index."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index > stack.data.size()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Index out of bounds in 'add'."
                    );
                }

                stack.data.add(index, value);

                return new Variable.Variant(stack.data.size());
            }

            @Override
            public String getFnName() {
                return "add";
            }
        }

        AddAtIndexFn addAtIndex = new AddAtIndexFn();
        Variable addAtIndexVar = new Variable(
                new Variable.Variant(addAtIndex),
                true,
                TypeTag.OBJECT);
        Y_Stack_Prototype.set(addAtIndex.getFnName(), addAtIndexVar);

        // stack.clear()
        class ClearFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                stack.data.clear();

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
        Y_Stack_Prototype.set(clear.getFnName(), clearVar);

        // stack.clone()
        class CloneFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                Stack<Variable.Variant> clonedData =
                        (Stack<Variable.Variant>) stack.data.clone();

                Y_Stack.Y_StackObject newStack =
                        new Y_Stack.Y_StackObject(clonedData);

                return new Variable.Variant(newStack);
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
        Y_Stack_Prototype.set(clone.getFnName(), cloneVar);

        // stack.contains(value)
        class ContainsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_Stack.Y_StackObject stack = requireStackThis(interpreter);

                Variable.Variant target = arguments.get(0);

                for (Variable.Variant element : stack.data) {
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
        Y_Stack_Prototype.set(contains.getFnName(), containsVar);
    }

    public static class Y_StackObject extends RuntimeObject {

        private final Stack<Variable.Variant> data;

        public Y_StackObject(Stack<Variable.Variant> data)  {
            this.data = data;
            this.prototype = Y_Stack_Prototype;
        }

        public Y_StackObject() {
            this.data = new Stack<>();
            this.prototype = Y_Stack_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Stack";
        }

        @Override
        public String toString() {
            return "<class:stack>";
        }
    }

    public static class Y_StackInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            Stack<Variable.Variant> value = new Stack<>();
            Y_Stack.Y_StackObject newStack = new Y_Stack.Y_StackObject(value);

            return new Variable.Variant(newStack);
        }

        @Override
        public String getFnName() {
            return "Stack";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_Stack.Y_StackInit stackCtor = new Y_Stack.Y_StackInit();
        Variable.Variant variant = new Variable.Variant(stackCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(stackCtor.getFnName(), var);
    }
}
