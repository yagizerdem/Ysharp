package ysharp.treewalk.evaluator.Native.Collections.Queue;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Queue.function.instance.*;
import ysharp.treewalk.evaluator.Native.Collections.yVector;

import java.util.*;

public class yQueue {

    // helper
    public static yQueue.yQueueInstance requireQueueThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yQueueInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
        yQueue_Instance_Prototype.prototype = yVector.Vector_Instance_Prototype;

        // queue.toString()
        yQueue_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // queue.add(value)
        yQueue_Instance_Prototype.RegisterNativeFn(new AddFn(), Arrays.asList("enqueue"));
        // queue.remove()
        yQueue_Instance_Prototype.RegisterNativeFn(new RemoveFn(), Arrays.asList("dequeue"));
        // queue.poll()
        yQueue_Instance_Prototype.RegisterNativeFn(new PollFn());
        // queue.peek()
        yQueue_Instance_Prototype.RegisterNativeFn(new PeekFn());
        // queue.size()
        yQueue_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // queue.isEmpty()
        yQueue_Instance_Prototype.RegisterNativeFn(new IsEmptyFn(), Arrays.asList("empty"));

    }

    public static class yQueueInstance extends yClass.ClassObjectInstance implements yVector.IVector {

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

        @Override
        public List<Variable.Variant> getData() {
            return new ArrayList<>(this.data);
        }

        @Override
        public Object getNativeJavaObject() {
            Queue<Object> nativeQueue = new LinkedList<>();
            for (var item : this.data) {
                nativeQueue.add(item.asJavaNative());
            }
            return nativeQueue;
        }

    }

    public static class yQueueClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
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
