package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.LINQ.Queryable;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class yStack {

    // helper
    private static yStack.yStackInstance requireStackThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yStackInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'stack' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yStackInstance) obj;
    }

    public static RuntimeObject yStack_Instance_Prototype;

    static {
        yStack_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Stack__";
            }

            @Override
            public String toString() {
                return "<prototype:Stack>";
            }
        };
        yStack_Instance_Prototype.prototype = yClass.ClassPrototype;

        // stack.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yStack.yStackInstance array = requireStackThis(interpreter, getFnName());

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
                            builder.append(var.asRuntimeObject().toString());
                        }
                    }
                    else {
                        builder.append(var.value.toString());
                    }

                    if(i != array.data.size() -1) {
                        builder.append(", ");

                    }
                }
                builder.append(" ");
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
        yStack_Instance_Prototype.set(toString.getFnName(), toStringVar);

        // stack.add(value)
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
                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());
                stack.data.push(new Variable.Variant(value.value));

                return new Variable.Variant(stack.data.size());
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
        yStack_Instance_Prototype.set(add.getFnName(), addVar);
        yStack_Instance_Prototype.set("push", addVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

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
                "function");
        yStack_Instance_Prototype.set(pop.getFnName(), popVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

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
                "function");
        yStack_Instance_Prototype.set(peek.getFnName(), peekVar);
        yStack_Instance_Prototype.set("top", peekVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

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
                "function");
        yStack_Instance_Prototype.set(empty.getFnName(), emptyVar);
        yStack_Instance_Prototype.set("isEmpty", emptyVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

                Variable.Variant target = arguments.get(0);

                // Stack top -> end of list
                for (int i = stack.data.size() - 1; i >= 0; i--) {

                    Variable.Variant element = stack.data.get(i);

                    if (element == null && target == null) {
                        return new Variable.Variant(stack.data.size() - i);
                    }

                    if (element != null && element.equals(target)) {
                        return new Variable.Variant(stack.data.size() - i);
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
                "function");
        yStack_Instance_Prototype.set(search.getFnName(), searchVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

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
                "function");
        yStack_Instance_Prototype.set(clear.getFnName(), clearVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

                Stack<Variable.Variant> clonedData =
                        (Stack<Variable.Variant>) stack.data.clone();

                yStack.yStackInstance newStack =
                        new yStack.yStackInstance(clonedData);

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
                "function");
        yStack_Instance_Prototype.set(clone.getFnName(), cloneVar);

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

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

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
                "function");
        yStack_Instance_Prototype.set(contains.getFnName(), containsVar);

        // stack.reverse()
        class ReverseFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args) {
                yStackInstance stack = requireStackThis(interpreter, getFnName());

                Stack<Variable.Variant> reversed = new Stack<>();
                for (Variable.Variant v : stack.data) {
                    reversed.add(0, v); // ters ekle
                }

                return new Variable.Variant(new yStackInstance(reversed));
            }

            @Override
            public String getFnName() { return "reverse"; }
        }

        ReverseFn reverse = new ReverseFn();
        Variable reverseVar = new Variable(new Variable.Variant(reverse),
                true,
                "function");
        yStack_Instance_Prototype.set(reverse.getFnName(), reverseVar);

        // stack.size()
        class SizeFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());
                return new Variable.Variant(stack.data.size());
            }

            @Override
            public String getFnName() { return "size"; }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size),
                true,
                "function");
        yStack_Instance_Prototype.set(size.getFnName(), sizeVar);

        // stack.toArray()
        class ToArrayFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());

                ArrayList<Variable.Variant> newList = new ArrayList<>(stack.data);

                return new Variable.Variant(new yArray.yArrayInstance(newList));
            }

            @Override
            public String getFnName() {
                return "toArray";
            }
        }

        ToArrayFn toArray = new ToArrayFn();
        Variable toArrayVar = new Variable(
                new Variable.Variant(toArray),
                true,
                "function");
        yStack_Instance_Prototype.set(toArray.getFnName(), toArrayVar);
        yStack_Instance_Prototype.set("toList", toArrayVar);

        // arr.asQueryable()
        class AsQueryableFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yStackInstance stack = requireStackThis(interpreter, getFnName());

                Queryable.QueryableInstance queryable =
                        new Queryable.QueryableInstance(stack.data);

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "asQueryable";
            }
        }

        AsQueryableFn asQueryable = new AsQueryableFn();
        Variable asQueryableVar = new Variable(new Variable.Variant(asQueryable),
                true,
                "function");
        yStack_Instance_Prototype.set(asQueryable.getFnName(), asQueryableVar);

        class PeekOrNullFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());
                if (stack.data.isEmpty()) return new Variable.Variant(null);
                return stack.data.peek();
            }

            @Override
            public String getFnName() { return "peekOrNull"; }
        }

        PeekOrNullFn peekOrNull = new PeekOrNullFn();
        Variable peekOrNullVar = new Variable(new Variable.Variant(peekOrNull),
                true,
                "function");
        yStack_Instance_Prototype.set(peekOrNull.getFnName(), peekOrNullVar);


        class AddAllFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yStack.yStackInstance stack = requireStackThis(interpreter, getFnName());
                Variable.Variant other = arguments.get(0);

                if (other.value instanceof yStack.yStackInstance) {
                    stack.data.addAll(((yStack.yStackInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
                }
                else if (other.value instanceof yArray.yArrayInstance) {
                    stack.data.addAll(((yArray.yArrayInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
                }
                else if (other.value instanceof yQueue.yQueueInstance) {
                    stack.data.addAll(((yQueue.yQueueInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
                }
                else if (other.value instanceof ySet.ySetInstance) {
                    stack.data.addAll(((ySet.ySetInstance) other.value).data.stream().map(x -> new Variable.Variant(x.value)).toList());
                }
                else if (other.value instanceof yPriorityQueue.yPriorityQueueInstance) {
                    stack.data.addAll(((yPriorityQueue.yPriorityQueueInstance) other.value).getRawVariants().stream().map(x -> new Variable.Variant(x.value)).toList());
                }
                else {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, 0, "Argument must be a vector based collection");
                }

                return new Variable.Variant(stack.data.size());
            }

            @Override
            public String getFnName() { return "addAll"; }
        }

        AddAllFn addAll = new AddAllFn();
        Variable addAllVar = new Variable(new Variable.Variant(addAll),
                true,
                "function");
        yStack_Instance_Prototype.set(addAll.getFnName(), addAllVar);
    }

    public static class yStackInstance extends yClass.ClassObjectInstance {

        private final Stack<Variable.Variant> data;

        public yStackInstance(Stack<Variable.Variant> data)  {
            this.data = data;
            this.prototype = yStack_Instance_Prototype;
        }

        public yStackInstance() {
            this.data = new Stack<>();
            this.prototype = yStack_Instance_Prototype;
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
            return "<instance:Stack>";
        }
    }

    public static class yStackClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            Stack<Variable.Variant> value = new Stack<>();
            yStack.yStackInstance newStack = new yStack.yStackInstance(value);

            return new Variable.Variant(newStack);
        }

        @Override
        public String getClassName() {
            return "Stack";
        }

        @Override
        public String getType() {
            return "Stack";
        }

        @Override
        public String toString() {
            return "<class:Stack>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yStack.yStackClass stackCtor = new yStack.yStackClass();
        Variable.Variant variant = new Variable.Variant(stackCtor);
        Variable var = new Variable(variant, false, stackCtor.getType());
        interpreter.defineGlobal(stackCtor.getClassName(), var);
    }
}
