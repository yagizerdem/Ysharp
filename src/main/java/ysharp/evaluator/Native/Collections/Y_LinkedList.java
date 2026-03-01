package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.List;

public class Y_LinkedList {

    // helper
    private static Y_LinkedList.Y_LinkedListObject requireLinkedListThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_LinkedList.Y_LinkedListObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on LinkedList objects."
            );
        }

        return (Y_LinkedList.Y_LinkedListObject) obj;
    }


    static {
        Y_LinkedList_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "linked_list_prototype";
            }
        };

        // list.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("[");

                Node current = list.head;
                boolean first = true;

                while (current != null) {
                    if (!first) {
                        sb.append(" -> ");
                    }
                    first = false;
                    sb.append(current.value.toString());
                    current = current.next;
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
        Y_LinkedList.Y_LinkedList_Prototype.set(toString.getFnName(), toStringVar);


        // list.addFirst(value)
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
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                Node newNode = new Node(value);
                newNode.next = list.head;
                list.head = newNode;

                if (list.tail == null) {
                    list.tail = newNode;
                }

                list.size++;

                return new Variable.Variant(list.size);
            }

            @Override
            public String getFnName() {
                return "addFirst";
            }
        }

        AddFirstFn addFirst = new AddFirstFn();
        Variable addFirstVar = new Variable(
                new Variable.Variant(addFirst),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(addFirst.getFnName(), addFirstVar);


        // list.addLast(value)
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
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                Node newNode = new Node(value);

                if (list.tail == null) {
                    list.head = newNode;
                    list.tail = newNode;
                } else {
                    list.tail.next = newNode;
                    list.tail = newNode;
                }

                list.size++;

                return new Variable.Variant(list.size);
            }

            @Override
            public String getFnName() {
                return "addLast";
            }
        }

        AddLastFn addLast = new AddLastFn();
        Variable addLastVar = new Variable(
                new Variable.Variant(addLast),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(addLast.getFnName(), addLastVar);


        // list.removeFirst()
        class RemoveFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                if (list.head == null) {
                    return new Variable.Variant(null);
                }

                Variable.Variant removed = list.head.value;
                list.head = list.head.next;

                if (list.head == null) {
                    list.tail = null;
                }

                list.size--;

                return removed;
            }

            @Override
            public String getFnName() {
                return "removeFirst";
            }
        }

        RemoveFirstFn removeFirst = new RemoveFirstFn();
        Variable removeFirstVar = new Variable(
                new Variable.Variant(removeFirst),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(removeFirst.getFnName(), removeFirstVar);


        // list.removeLast()
        class RemoveLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                if (list.head == null) {
                    return new Variable.Variant(null);
                }

                Variable.Variant removed;

                if (list.head == list.tail) {
                    removed = list.head.value;
                    list.head = null;
                    list.tail = null;
                } else {
                    Node current = list.head;
                    while (current.next != list.tail) {
                        current = current.next;
                    }
                    removed = list.tail.value;
                    current.next = null;
                    list.tail = current;
                }

                list.size--;

                return removed;
            }

            @Override
            public String getFnName() {
                return "removeLast";
            }
        }

        RemoveLastFn removeLast = new RemoveLastFn();
        Variable removeLastVar = new Variable(
                new Variable.Variant(removeLast),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(removeLast.getFnName(), removeLastVar);


        // list.peekFirst()
        class PeekFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                if (list.head == null) {
                    return new Variable.Variant(null);
                }

                return list.head.value;
            }

            @Override
            public String getFnName() {
                return "peekFirst";
            }
        }

        PeekFirstFn peekFirst = new PeekFirstFn();
        Variable peekFirstVar = new Variable(
                new Variable.Variant(peekFirst),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(peekFirst.getFnName(), peekFirstVar);


        // list.peekLast()
        class PeekLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                if (list.tail == null) {
                    return new Variable.Variant(null);
                }

                return list.tail.value;
            }

            @Override
            public String getFnName() {
                return "peekLast";
            }
        }

        PeekLastFn peekLast = new PeekLastFn();
        Variable peekLastVar = new Variable(
                new Variable.Variant(peekLast),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(peekLast.getFnName(), peekLastVar);


        // list.get(index)
        class GetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant indexVariant = arguments.get(0);
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                int index = ((Number) indexVariant.value).intValue();

                if (index < 0 || index >= list.size) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "LinkedList index out of bounds: " + index
                    );
                }

                Node current = list.head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }

                return current.value;
            }

            @Override
            public String getFnName() {
                return "get";
            }
        }

        GetFn get = new GetFn();
        Variable getVar = new Variable(
                new Variable.Variant(get),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(get.getFnName(), getVar);


        // list.set(index, value)
        class SetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant indexVariant = arguments.get(0);
                Variable.Variant value = arguments.get(1);
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                int index = ((Number) indexVariant.value).intValue();

                if (index < 0 || index >= list.size) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "LinkedList index out of bounds: " + index
                    );
                }

                Node current = list.head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }

                Variable.Variant old = current.value;
                current.value = value;

                return old;
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
        Y_LinkedList.Y_LinkedList_Prototype.set(set.getFnName(), setVar);


        // list.contains(value)
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
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                Node current = list.head;
                while (current != null) {
                    if (current.value.equals(target)) {
                        return new Variable.Variant(true);
                    }
                    current = current.next;
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
        Y_LinkedList.Y_LinkedList_Prototype.set(contains.getFnName(), containsVar);


        // list.indexOf(value)
        class IndexOfFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant target = arguments.get(0);
                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                Node current = list.head;
                int index = 0;

                while (current != null) {
                    if (current.value.equals(target)) {
                        return new Variable.Variant(index);
                    }
                    current = current.next;
                    index++;
                }

                return new Variable.Variant(-1);
            }

            @Override
            public String getFnName() {
                return "indexOf";
            }
        }

        IndexOfFn indexOf = new IndexOfFn();
        Variable indexOfVar = new Variable(
                new Variable.Variant(indexOf),
                true,
                TypeTag.OBJECT);
        Y_LinkedList.Y_LinkedList_Prototype.set(indexOf.getFnName(), indexOfVar);


        // list.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                return new Variable.Variant(list.size);
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
        Y_LinkedList.Y_LinkedList_Prototype.set(size.getFnName(), sizeVar);


        // list.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                return new Variable.Variant(list.size == 0);
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
        Y_LinkedList.Y_LinkedList_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // list.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);
                list.head = null;
                list.tail = null;
                list.size = 0;

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
        Y_LinkedList.Y_LinkedList_Prototype.set(clear.getFnName(), clearVar);


        // list.toArray()
        class ToArrayFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject list = requireLinkedListThis(interpreter);

                java.util.ArrayList<Variable.Variant> result =
                        new java.util.ArrayList<>();

                Node current = list.head;
                while (current != null) {
                    result.add(current.value);
                    current = current.next;
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
        Y_LinkedList.Y_LinkedList_Prototype.set(toArray.getFnName(), toArrayVar);


        // list.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_LinkedListObject original = requireLinkedListThis(interpreter);
                Y_LinkedListObject cloned = new Y_LinkedListObject();

                Node current = original.head;
                while (current != null) {
                    Node newNode = new Node(current.value);
                    if (cloned.tail == null) {
                        cloned.head = newNode;
                        cloned.tail = newNode;
                    } else {
                        cloned.tail.next = newNode;
                        cloned.tail = newNode;
                    }
                    cloned.size++;
                    current = current.next;
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
        Y_LinkedList.Y_LinkedList_Prototype.set(clone.getFnName(), cloneVar);

    }

    public static RuntimeObject Y_LinkedList_Prototype;

    // Internal node class
    static class Node {
        Variable.Variant value;
        Node next;

        Node(Variable.Variant value) {
            this.value = value;
            this.next = null;
        }
    }

    public static class Y_LinkedListObject extends RuntimeObject {

        Node head;
        Node tail;
        int size;

        public Y_LinkedListObject() {
            this.head = null;
            this.tail = null;
            this.size = 0;
            this.prototype = Y_LinkedList_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "LinkedList";
        }

        @Override
        public String toString() {
            return "<class:linked-list>";
        }
    }

    public static class Y_LinkedListInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_LinkedListObject newList = new Y_LinkedListObject();

            return new Variable.Variant(newList);
        }

        @Override
        public String getFnName() {
            return "LinkedList";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_LinkedList.Y_LinkedListInit listCtor = new Y_LinkedList.Y_LinkedListInit();
        Variable.Variant variant = new Variable.Variant(listCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(listCtor.getFnName(), var);
    }

}