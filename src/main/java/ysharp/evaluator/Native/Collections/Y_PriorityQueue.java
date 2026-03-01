package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;

public class Y_PriorityQueue {

    // helper
    private static Y_PriorityQueue.Y_PriorityQueueObject requirePriorityQueueThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_PriorityQueue.Y_PriorityQueueObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on PriorityQueue objects."
            );
        }

        return (Y_PriorityQueue.Y_PriorityQueueObject) obj;
    }

    static {
        Y_PriorityQueue_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "priority_queue_prototype";
            }
        };

        // pq.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("PriorityQueue[");

                for (int i = 0; i < pq.heap.size(); i++) {
                    if (i > 0) sb.append(", ");
                    PriorityEntry entry = pq.heap.get(i);
                    sb.append("(").append(entry.value.toString())
                            .append(", p=").append(entry.priority).append(")");
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
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(toString.getFnName(), toStringVar);


        // pq.enqueue(value, priority)
        // Lower priority number = higher priority (min-heap)
        class EnqueueFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                Variable.Variant priorityVariant = arguments.get(1);
                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                double priority = ((Number) priorityVariant.value).doubleValue();

                pq.heap.add(new PriorityEntry(value, priority));
                pq.bubbleUp(pq.heap.size() - 1);

                return new Variable.Variant(pq.heap.size());
            }

            @Override
            public String getFnName() {
                return "enqueue";
            }
        }

        EnqueueFn enqueue = new EnqueueFn();
        Variable enqueueVar = new Variable(
                new Variable.Variant(enqueue),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(enqueue.getFnName(), enqueueVar);


        // pq.dequeue() -> returns value with highest priority (lowest number)
        class DequeueFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                if (pq.heap.isEmpty()) {
                    return new Variable.Variant(null);
                }

                PriorityEntry top = pq.heap.get(0);
                int last = pq.heap.size() - 1;

                pq.heap.set(0, pq.heap.get(last));
                pq.heap.remove(last);

                if (!pq.heap.isEmpty()) {
                    pq.siftDown(0);
                }

                return top.value;
            }

            @Override
            public String getFnName() {
                return "dequeue";
            }
        }

        DequeueFn dequeue = new DequeueFn();
        Variable dequeueVar = new Variable(
                new Variable.Variant(dequeue),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(dequeue.getFnName(), dequeueVar);


        // pq.peek() -> returns value without removing
        class PeekFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                if (pq.heap.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return pq.heap.get(0).value;
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(peek.getFnName(), peekVar);


        // pq.peekPriority() -> returns the priority number of the top element
        class PeekPriorityFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                if (pq.heap.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return new Variable.Variant(pq.heap.get(0).priority);
            }

            @Override
            public String getFnName() {
                return "peekPriority";
            }
        }

        PeekPriorityFn peekPriority = new PeekPriorityFn();
        Variable peekPriorityVar = new Variable(
                new Variable.Variant(peekPriority),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(peekPriority.getFnName(), peekPriorityVar);


        // pq.contains(value) -> true/false
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
                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                for (PriorityEntry entry : pq.heap) {
                    if (entry.value.equals(target)) {
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(contains.getFnName(), containsVar);


        // pq.changePriority(value, newPriority) -> true if found and updated
        class ChangePriorityFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                Variable.Variant newPriorityVariant = arguments.get(1);
                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                double newPriority = ((Number) newPriorityVariant.value).doubleValue();

                for (int i = 0; i < pq.heap.size(); i++) {
                    if (pq.heap.get(i).value.equals(target)) {
                        pq.heap.get(i).priority = newPriority;
                        pq.bubbleUp(i);
                        pq.siftDown(i);
                        return new Variable.Variant(true);
                    }
                }

                return new Variable.Variant(false);
            }

            @Override
            public String getFnName() {
                return "changePriority";
            }
        }

        ChangePriorityFn changePriority = new ChangePriorityFn();
        Variable changePriorityVar = new Variable(
                new Variable.Variant(changePriority),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(changePriority.getFnName(), changePriorityVar);


        // pq.remove(value) -> removes first match, returns true/false
        class RemoveFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                for (int i = 0; i < pq.heap.size(); i++) {
                    if (pq.heap.get(i).value.equals(target)) {
                        int last = pq.heap.size() - 1;
                        pq.heap.set(i, pq.heap.get(last));
                        pq.heap.remove(last);

                        if (i < pq.heap.size()) {
                            pq.bubbleUp(i);
                            pq.siftDown(i);
                        }

                        return new Variable.Variant(true);
                    }
                }

                return new Variable.Variant(false);
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(remove.getFnName(), removeVar);


        // pq.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                return new Variable.Variant(pq.heap.size());
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(size.getFnName(), sizeVar);


        // pq.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                return new Variable.Variant(pq.heap.isEmpty());
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // pq.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);
                pq.heap.clear();

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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(clear.getFnName(), clearVar);


        // pq.toArray() -> returns array of values in heap order (not sorted)
        class ToArrayFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                java.util.ArrayList<Variable.Variant> result =
                        new java.util.ArrayList<>();

                for (PriorityEntry entry : pq.heap) {
                    result.add(entry.value);
                }

                Y_Array.Y_ArrayObject array =
                        new Y_Array.Y_ArrayObject(result);

                return new Variable.Variant(array);
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
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(toArray.getFnName(), toArrayVar);


        // pq.drainSorted() -> dequeues all elements in priority order, returns array
        class DrainSortedFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject pq = requirePriorityQueueThis(interpreter);

                java.util.ArrayList<Variable.Variant> result =
                        new java.util.ArrayList<>();

                while (!pq.heap.isEmpty()) {
                    PriorityEntry top = pq.heap.get(0);
                    int last = pq.heap.size() - 1;
                    pq.heap.set(0, pq.heap.get(last));
                    pq.heap.remove(last);
                    if (!pq.heap.isEmpty()) {
                        pq.siftDown(0);
                    }
                    result.add(top.value);
                }

                Y_Array.Y_ArrayObject array =
                        new Y_Array.Y_ArrayObject(result);

                return new Variable.Variant(array);
            }

            @Override
            public String getFnName() {
                return "drainSorted";
            }
        }

        DrainSortedFn drainSorted = new DrainSortedFn();
        Variable drainSortedVar = new Variable(
                new Variable.Variant(drainSorted),
                true,
                TypeTag.OBJECT);
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(drainSorted.getFnName(), drainSortedVar);


        // pq.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_PriorityQueueObject original = requirePriorityQueueThis(interpreter);
                Y_PriorityQueueObject cloned = new Y_PriorityQueueObject();

                for (PriorityEntry entry : original.heap) {
                    cloned.heap.add(new PriorityEntry(entry.value, entry.priority));
                }

                return new Variable.Variant(cloned);
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
        Y_PriorityQueue.Y_PriorityQueue_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static RuntimeObject Y_PriorityQueue_Prototype;

    // Internal entry holding value + priority
    static class PriorityEntry {
        Variable.Variant value;
        double priority;

        PriorityEntry(Variable.Variant value, double priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    public static class Y_PriorityQueueObject extends RuntimeObject {

        // Min-heap: index 0 = highest priority (lowest priority number)
        final ArrayList<PriorityEntry> heap;

        public Y_PriorityQueueObject() {
            this.heap = new ArrayList<>();
            this.prototype = Y_PriorityQueue_Prototype;
        }

        // Bubble up: after insert at end
        void bubbleUp(int index) {
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
        void siftDown(int index) {
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
            return "<class:priority-queue>";
        }
    }

    public static class Y_PriorityQueueInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_PriorityQueueObject newPQ = new Y_PriorityQueueObject();

            return new Variable.Variant(newPQ);
        }

        @Override
        public String getFnName() {
            return "PriorityQueue";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_PriorityQueue.Y_PriorityQueueInit pqCtor = new Y_PriorityQueue.Y_PriorityQueueInit();
        Variable.Variant variant = new Variable.Variant(pqCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(pqCtor.getFnName(), var);
    }

}