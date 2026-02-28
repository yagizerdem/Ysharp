package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.parser.TypeTag;

import java.util.*;

public class Y_Queue {

    // helper
    private static Y_Queue.Y_QueueObject requireQueueThis (Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_Queue.Y_QueueObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on queue objects."
            );
        }

        return  (Y_Queue.Y_QueueObject) obj;
    }

    public static RuntimeObject Y_Queue_Prototype;

    static {
        Y_Queue_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "queue_prototype";
            }
        };

        // queue.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                Y_Queue.Y_QueueObject queue = requireQueueThis(interpreter);

                StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                int counter = 0;
                for (Variable.Variant var : queue.data) {

                    if (var.value instanceof RuntimeObject) {

                        Variable toStringFn =
                                ((RuntimeObject) var.value).get("toString");

                        if (toStringFn != null &&
                                toStringFn.value.isNativeFunction()) {

                            BoundNativeFunction bound =
                                    new BoundNativeFunction(
                                            toStringFn.value.asNativeFunction(),
                                            var.asRuntimeObject()
                                    );

                            builder.append(
                                    bound.call(interpreter, new ArrayList<>())
                            );
                        }
                        else {
                            builder.append("<class>");
                        }
                    }
                    else {
                        builder.append(var.value.toString());
                    }

                    if(counter < queue.data.size() -1) builder.append(",");
                    builder.append(" ");
                    counter++;
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
        Y_Queue_Prototype.set(toString.getFnName(), toStringVar);

        // queue.add(value)
        class AddFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                Variable.Variant value = arguments.get(0);

                queue.data.add(value);

                return new Variable.Variant(queue.data.size());
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

        Y_Queue_Prototype.set(add.getFnName(), addVar);

        // queue.remove()
        class RemoveFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                if (queue.data.isEmpty()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'remove' cannot be called on an empty queue."
                    );
                }

                return queue.data.remove();
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
        Y_Queue_Prototype.set(remove.getFnName(), removeVar);

        // queue.poll()
        class PollFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                if (queue.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                var data = queue.data.poll();
                if(data == null) return new Variable.Variant(null);
                return data;
            }

            @Override
            public String getFnName() {
                return "poll";
            }
        }

        PollFn poll = new PollFn();
        Variable pollVar = new Variable(
                new Variable.Variant(poll),
                true,
                TypeTag.OBJECT);

        Y_Queue_Prototype.set(poll.getFnName(), pollVar);

        // queue.peek()
        class PeekFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                if (queue.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return queue.data.peek();
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
        Y_Queue_Prototype.set(peek.getFnName(), peekVar);

        // queue.size()
        class SizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                return new Variable.Variant(queue.data.size());
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
        Y_Queue_Prototype.set(size.getFnName(), sizeVar);

        // queue.isEmpty()
        class IsEmptyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_QueueObject queue = requireQueueThis(interpreter);

                return new Variable.Variant(queue.data.isEmpty());
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
        Y_Queue_Prototype.set(isEmpty.getFnName(), isEmptyVar);

    }

    public static class Y_QueueObject extends RuntimeObject {

        private final Queue<Variable.Variant> data;

        public Y_QueueObject(Queue<Variable.Variant> data) {
            this.data = data;
            this.prototype = Y_Queue_Prototype;
        }

        public Y_QueueObject() {
            this.data = new LinkedList<>();
            this.prototype = Y_Queue_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Queue";
        }

        @Override
        public String toString() {
            return "<class:queue>";
        }
    }

    public static class Y_QueueInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            Queue<Variable.Variant> value = new LinkedList<>();
            Y_Queue.Y_QueueObject newQueue = new Y_Queue.Y_QueueObject(value);

            return new Variable.Variant(newQueue);
        }

        @Override
        public String getFnName() {
            return "Queue";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_Queue.Y_QueueInit queueCtor = new Y_Queue.Y_QueueInit();
        Variable.Variant variant = new Variable.Variant(queueCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(queueCtor.getFnName(), var);
    }
}
