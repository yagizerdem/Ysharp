package ysharp.treewalk.evaluator.Native.Collections.ArrayDeque;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.function.instance.*;

import java.util.ArrayDeque;
import java.util.List;

public class yArrayDeque {

    // helper
    public static yArrayDeque.yArrayDequeInstance requireArrayDequeThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yArrayDeque.yArrayDequeInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
                return "__ArrayDeque__";
            }

            @Override
            public String toString() {
                return "<prototype:ArrayDeque>";
            }
        };
        yArrayDeque_Instance_Prototype.prototype = yClass.ClassPrototype;

        // deque.toString()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // deque.addFirst(value)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new AddFirstFn());
        // deque.addLast(value)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new AddLastFn());
        // deque.removeFirst()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new RemoveFirstFn());
        // deque.removeLast()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new RemoveLastFn());
        // deque.peekFirst()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new PeekFirstFn());
        // deque.peekLast()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new PeekLastFn());
        // deque.contains(value)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // deque.removeFirstOccurrence(value)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new RemoveFirstOccurrenceFn());
        // deque.removeLastOccurrence(value)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new RemoveLastOccurrenceFn());
        // deque.size()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // deque.isEmpty()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // deque.clear()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // deque.toArray() -> returns Y_ArrayObject (front to back order)
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new ToArrayFn());
        // deque.clone()
        yArrayDeque_Instance_Prototype.RegisterNativeFn(new CloneFn());

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
            return "<instance:ArrayDeque>";
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
                throws YsharpException {

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

        @Override
        public String toString() {
            return "<class:ArrayDeque>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yArrayDequeClass dequeCtor = new yArrayDequeClass();
        Variable.Variant variant = new Variable.Variant(dequeCtor);
        Variable var = new Variable(variant, false, dequeCtor.getType());
        interpreter.defineGlobal(dequeCtor.getClassName(), var);
    }

}