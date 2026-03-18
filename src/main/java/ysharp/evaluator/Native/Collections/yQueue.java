package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;

import java.util.*;

public class yQueue {

    // helper
    private static yQueue.yQueueInstance requireQueueThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yQueueInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'queue' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yQueueInstance) obj;
    }

    public static RuntimeObject yQueue_Instance_Prototype;

    static {
        yQueue_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Queue__";
            }

            @Override
            public String toString() {
                return "<prototype:Queue>";
            }
        };
        yQueue_Instance_Prototype.prototype = yClass.ClassPrototype;

        // queue.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yQueue.yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                                            var.asRuntimeObject(),
                                            "this"
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
                "function");
        yQueue_Instance_Prototype.set(toString.getFnName(), toStringVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");

        yQueue_Instance_Prototype.set(add.getFnName(), addVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");
        yQueue_Instance_Prototype.set(remove.getFnName(), removeVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");

        yQueue_Instance_Prototype.set(poll.getFnName(), pollVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");
        yQueue_Instance_Prototype.set(peek.getFnName(), peekVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");
        yQueue_Instance_Prototype.set(size.getFnName(), sizeVar);

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

                yQueueInstance queue = requireQueueThis(interpreter, getFnName());

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
                "function");
        yQueue_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);

    }

    public static class yQueueInstance extends yClass.ClassObjectInstance {

        public final Queue<Variable.Variant> data;

        public yQueueInstance(Queue<Variable.Variant> data) {
            this.data = data;
            this.prototype = yQueue_Instance_Prototype;
        }

        public yQueueInstance() {
            this.data = new LinkedList<>();
            this.prototype = yQueue_Instance_Prototype;
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
            return "<instance:Queue>";
        }
    }

    public static class yQueueClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            Queue<Variable.Variant> value = new LinkedList<>();
            yQueue.yQueueInstance newQueue = new yQueue.yQueueInstance(value);

            return new Variable.Variant(newQueue);
        }

        @Override
        public String getClassName() {
            return "Queue";
        }

        @Override
        public String getType() {
            return "Queue";
        }

        @Override
        public String toString() {
            return "<class:Queue>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yQueue.yQueueClass queueCtor = new yQueue.yQueueClass();
        Variable.Variant variant = new Variable.Variant(queueCtor);
        Variable var = new Variable(variant, false, queueCtor.getType());
        interpreter.defineGlobal(queueCtor.getClassName(), var);
    }
}
