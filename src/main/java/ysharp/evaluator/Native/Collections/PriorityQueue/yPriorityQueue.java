package ysharp.evaluator.Native.Collections.PriorityQueue;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.PriorityQueue.function.instance.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class yPriorityQueue {

    // helper
    public static yPriorityQueue.yPriorityQueueInstance requirePriorityQueueThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yPriorityQueue.yPriorityQueueInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on PriorityQueue objects."
            );
        }

        return (yPriorityQueue.yPriorityQueueInstance) obj;
    }

    public static RuntimeObject yPriorityQueue_Instance_Prototype;

    static {
        yPriorityQueue_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__PriorityQueue__";
            }

            @Override
            public String toString() {
                return "<prototype:PriorityQueue>";
            }
        };
        yPriorityQueue_Instance_Prototype.prototype = yClass.ClassPrototype;

        // pq.toString()
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // pq.enqueue(value, priority)
        // Lower priority number = higher priority (min-heap)
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new EnqueueFn(), Arrays.asList("add"));
        // pq.dequeue() -> returns value with highest priority (lowest number)
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new DequeueFn());
        // pq.peek() -> returns value without removing
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new PeekFn());
        // pq.peekPriority() -> returns the priority number of the top element
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new PeekPriorityFn());
        // pq.contains(value) -> true/false
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // pq.changePriority(value, newPriority) -> true if found and updated
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new ChangePriorityFn());
        // pq.remove(value) -> removes first match, returns true/false
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // pq.size()
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // pq.isEmpty()
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // pq.clear()
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // pq.toArray() -> returns array of values in heap order (not sorted)
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new ToArrayFn());
        // pq.drainSorted() -> dequeues all elements in priority order, returns array
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new DrainSortedFn());
        // pq.clone()
        yPriorityQueue_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    // Internal entry holding value + priority
    public static class PriorityEntry {
        public Variable.Variant value;
        public double priority;

        public PriorityEntry(Variable.Variant value, double priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    public static class yPriorityQueueInstance extends yClass.ClassObjectInstance {

        // Min-heap: index 0 = highest priority (lowest priority number)
        public final ArrayList<PriorityEntry> heap;

        public final ArrayList<Variable.Variant> getRawVariants() {
            ArrayList<Variable.Variant> list = new ArrayList<>();
            heap.forEach( x -> list.add(x.value));
            return list;
        }

        public yPriorityQueueInstance() {
            this.heap = new ArrayList<>();
            this.prototype = yPriorityQueue_Instance_Prototype;
        }

        // Bubble up: after insert at end
        public void bubbleUp(int index) {
            while (index > 0) {
                int parent = (index - 1) / 2;
                if (heap.get(parent).priority > heap.get(index).priority) {
                    PriorityEntry tmp = heap.get(parent);
                    heap.set(parent, heap.get(index));
                    heap.set(index, tmp);
                    index = parent;
                } else {
                    break;
                }
            }
        }

        // Sift down: after removing root
        public void siftDown(int index) {
            int size = heap.size();
            while (true) {
                int left  = 2 * index + 1;
                int right = 2 * index + 2;
                int smallest = index;

                if (left < size && heap.get(left).priority < heap.get(smallest).priority) {
                    smallest = left;
                }
                if (right < size && heap.get(right).priority < heap.get(smallest).priority) {
                    smallest = right;
                }

                if (smallest != index) {
                    PriorityEntry tmp = heap.get(smallest);
                    heap.set(smallest, heap.get(index));
                    heap.set(index, tmp);
                    index = smallest;
                } else {
                    break;
                }
            }
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "PriorityQueue";
        }

        @Override
        public String toString() {
            return "<instance:PriorityQueue>";
        }
    }

    public static class yPriorityQueueClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yPriorityQueueInstance newPQ = new yPriorityQueueInstance();

            return new Variable.Variant(newPQ);
        }

        @Override
        public String getClassName() {
            return "PriorityQueue";
        }

        @Override
        public String getType() {
            return "PriorityQueue";
        }

        @Override
        public String toString() {
            return "<class:PriorityQueue>";
        }

    }

    public static void Register(Interpreter interpreter) throws Exception {
        yPriorityQueue.yPriorityQueueClass pqCtor = new yPriorityQueue.yPriorityQueueClass();
        Variable.Variant variant = new Variable.Variant(pqCtor);
        Variable var = new Variable(variant, false, pqCtor.getType());
        interpreter.defineGlobal(pqCtor.getClassName(), var);
    }

}