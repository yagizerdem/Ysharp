package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class yArrayDeque {

    // helper
    private static yArrayDeque.yArrayDequeInstance requireArrayDequeThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yArrayDeque.yArrayDequeInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on ArrayDeque objects."
            );
        }

        return (yArrayDeque.yArrayDequeInstance) obj;
    }

    public static RuntimeObject yArrayDeque_Instance_Prototype;

    static {
        yArrayDeque_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "array_deque_prototype";
            }
        };
        yArrayDeque_Instance_Prototype.prototype = yClass.ClassPrototype;

        // deque.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("Deque[");

                boolean first = true;
                for (Variable.Variant v : deque.data) {
                    if (!first) sb.append(", ");
                    first = false;
                    sb.append(v.toString());
                }

                sb.append("]");

                return new Variable.Variant(sb.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // deque.addFirst(value)
        class AddFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                deque.data.addFirst(value);

                return new Variable.Variant(deque.data.size());
            }

            @Override
            public String getFnName() {
                return "addFirst";
            }
        }

        AddFirstFn addFirst = new AddFirstFn();
        Variable addFirstVar = new Variable(new Variable.Variant(addFirst), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(addFirst.getFnName(), addFirstVar);


        // deque.addLast(value)
        class AddLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                deque.data.addLast(value);

                return new Variable.Variant(deque.data.size());
            }

            @Override
            public String getFnName() {
                return "addLast";
            }
        }

        AddLastFn addLast = new AddLastFn();
        Variable addLastVar = new Variable(new Variable.Variant(addLast), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(addLast.getFnName(), addLastVar);


        // deque.removeFirst()
        class RemoveFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.removeFirst();
            }

            @Override
            public String getFnName() {
                return "removeFirst";
            }
        }

        RemoveFirstFn removeFirst = new RemoveFirstFn();
        Variable removeFirstVar = new Variable(new Variable.Variant(removeFirst), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(removeFirst.getFnName(), removeFirstVar);


        // deque.removeLast()
        class RemoveLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.removeLast();
            }

            @Override
            public String getFnName() {
                return "removeLast";
            }
        }

        RemoveLastFn removeLast = new RemoveLastFn();
        Variable removeLastVar = new Variable(new Variable.Variant(removeLast), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(removeLast.getFnName(), removeLastVar);


        // deque.peekFirst()
        class PeekFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.peekFirst();
            }

            @Override
            public String getFnName() {
                return "peekFirst";
            }
        }

        PeekFirstFn peekFirst = new PeekFirstFn();
        Variable peekFirstVar = new Variable(new Variable.Variant(peekFirst), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(peekFirst.getFnName(), peekFirstVar);


        // deque.peekLast()
        class PeekLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.peekLast();
            }

            @Override
            public String getFnName() {
                return "peekLast";
            }
        }

        PeekLastFn peekLast = new PeekLastFn();
        Variable peekLastVar = new Variable(new Variable.Variant(peekLast), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(peekLast.getFnName(), peekLastVar);


        // deque.contains(value)
        class ContainsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                return new Variable.Variant(deque.data.contains(target));
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(new Variable.Variant(contains), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(contains.getFnName(), containsVar);


        // deque.removeFirstOccurrence(value)
        class RemoveFirstOccurrenceFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                return new Variable.Variant(deque.data.removeFirstOccurrence(target));
            }

            @Override
            public String getFnName() {
                return "removeFirstOccurrence";
            }
        }

        RemoveFirstOccurrenceFn removeFirstOccurrence = new RemoveFirstOccurrenceFn();
        Variable removeFirstOccurrenceVar = new Variable(new Variable.Variant(removeFirstOccurrence), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(removeFirstOccurrence.getFnName(), removeFirstOccurrenceVar);


        // deque.removeLastOccurrence(value)
        class RemoveLastOccurrenceFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                return new Variable.Variant(deque.data.removeLastOccurrence(target));
            }

            @Override
            public String getFnName() {
                return "removeLastOccurrence";
            }
        }

        RemoveLastOccurrenceFn removeLastOccurrence = new RemoveLastOccurrenceFn();
        Variable removeLastOccurrenceVar = new Variable(new Variable.Variant(removeLastOccurrence), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(removeLastOccurrence.getFnName(), removeLastOccurrenceVar);


        // deque.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                return new Variable.Variant(deque.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(size.getFnName(), sizeVar);


        // deque.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                return new Variable.Variant(deque.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // deque.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);
                deque.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(clear.getFnName(), clearVar);


        // deque.toArray() -> returns Y_ArrayObject (front to back order)
        class ToArrayFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                ArrayList<Variable.Variant> result = new ArrayList<>(deque.data);

                yArray.yArrayInstance array = new yArray.yArrayInstance(result);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "toArray";
            }
        }

        ToArrayFn toArray = new ToArrayFn();
        Variable toArrayVar = new Variable(new Variable.Variant(toArray), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(toArray.getFnName(), toArrayVar);


        // deque.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance original = requireArrayDequeThis(interpreter);
                yArrayDequeInstance cloned = new yArrayDequeInstance();

                cloned.data.addAll(original.data);

                return new Variable.Variant(cloned);
            }

            @Override
            public String getFnName() {
                return "clone";
            }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(new Variable.Variant(clone), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(clone.getFnName(), cloneVar);


        // --- Stack convenience methods ---

        // deque.push(value)  -> addFirst alias
        class PushFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                deque.data.push(value);

                return new Variable.Variant(deque.data.size());
            }

            @Override
            public String getFnName() {
                return "push";
            }
        }

        PushFn push = new PushFn();
        Variable pushVar = new Variable(new Variable.Variant(push), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(push.getFnName(), pushVar);


        // deque.pop() -> removeFirst alias
        class PopFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.pop();
            }

            @Override
            public String getFnName() {
                return "pop";
            }
        }

        PopFn pop = new PopFn();
        Variable popVar = new Variable(new Variable.Variant(pop), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(pop.getFnName(), popVar);


        // --- Queue convenience methods ---

        // deque.offer(value) -> addLast alias
        class OfferFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                deque.data.offer(value);

                return new Variable.Variant(deque.data.size());
            }

            @Override
            public String getFnName() {
                return "offer";
            }
        }

        OfferFn offer = new OfferFn();
        Variable offerVar = new Variable(new Variable.Variant(offer), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(offer.getFnName(), offerVar);


        // deque.poll() -> removeFirst alias
        class PollFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yArrayDequeInstance deque = requireArrayDequeThis(interpreter);

                if (deque.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return deque.data.poll();
            }

            @Override
            public String getFnName() {
                return "poll";
            }
        }

        PollFn poll = new PollFn();
        Variable pollVar = new Variable(new Variable.Variant(poll), true, "function");
        yArrayDeque.yArrayDeque_Instance_Prototype.set(poll.getFnName(), pollVar);

    }

    public static class yArrayDequeInstance extends yClass.ClassObjectInstance {

        public final ArrayDeque<Variable.Variant> data;

        public yArrayDequeInstance() {
            this.data = new ArrayDeque<>();
            this.prototype = yArrayDeque_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "ArrayDeque";
        }

        @Override
        public String toString() {
            return "<class:array-deque>";
        }
    }

    public static class yArrayDequeClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yArrayDequeInstance newDeque = new yArrayDequeInstance();

            return new Variable.Variant(newDeque);
        }

        @Override
        public String getClassName() {
            return "ArrayDeque";
        }

        @Override
        public String getType() {
            return "ArrayDeque";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yArrayDequeClass dequeCtor = new yArrayDequeClass();
        Variable.Variant variant = new Variable.Variant(dequeCtor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(dequeCtor.getClassName(), var);
    }

}