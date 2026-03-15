package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.List;

public class yLinkedList {

    // helper
    private static yLinkedList.yLinkedListInstance requireLinkedListThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yLinkedList.yLinkedListInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on LinkedList objects."
            );
        }

        return (yLinkedList.yLinkedListInstance) obj;
    }

    public static RuntimeObject yLinkedList_Instance_Prototype;

    static {
        yLinkedList_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__LinkedList__";
            }

            @Override
            public String toString() {
                return "<prototype:LinkedList>";
            }
        };
        yLinkedList_Instance_Prototype.prototype = yClass.ClassPrototype;

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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(toString.getFnName(), toStringVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(addFirst.getFnName(), addFirstVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(addLast.getFnName(), addLastVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(removeFirst.getFnName(), removeFirstVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(removeLast.getFnName(), removeLastVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(peekFirst.getFnName(), peekFirstVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(peekLast.getFnName(), peekLastVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(get.getFnName(), getVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(set.getFnName(), setVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(contains.getFnName(), containsVar);


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
                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(indexOf.getFnName(), indexOfVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(size.getFnName(), sizeVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);
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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(clear.getFnName(), clearVar);


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

                yLinkedListInstance list = requireLinkedListThis(interpreter);

                java.util.ArrayList<Variable.Variant> result =
                        new java.util.ArrayList<>();

                Node current = list.head;
                while (current != null) {
                    result.add(current.value);
                    current = current.next;
                }

                yArray.yArrayInstance array =
                        new yArray.yArrayInstance(result);

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(toArray.getFnName(), toArrayVar);


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

                yLinkedListInstance original = requireLinkedListThis(interpreter);
                yLinkedListInstance cloned = new yLinkedListInstance();

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
                "function");
        yLinkedList.yLinkedList_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }

    // Internal node class
    static class Node {
        Variable.Variant value;
        Node next;

        Node(Variable.Variant value) {
            this.value = value;
            this.next = null;
        }
    }

    public static class yLinkedListInstance extends yClass.ClassObjectInstance {

        Node head;
        Node tail;
        int size;

        public yLinkedListInstance() {
            this.head = null;
            this.tail = null;
            this.size = 0;
            this.prototype = yLinkedList_Instance_Prototype;
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
            return "<instance:LinkedList>";
        }
    }

    public static class yLinkedListClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yLinkedListInstance newList = new yLinkedListInstance();

            return new Variable.Variant(newList);
        }

        @Override
        public String getClassName() {
            return "LinkedList";
        }

        @Override
        public String getType() {
            return "LinkedList";
        }

        @Override
        public String toString() {
            return "<class:LinkedList>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yLinkedList.yLinkedListClass listCtor = new yLinkedList.yLinkedListClass();
        Variable.Variant variant = new Variable.Variant(listCtor);
        Variable var = new Variable(variant, false, listCtor.getType());
        interpreter.defineGlobal(listCtor.getClassName(), var);
    }

}