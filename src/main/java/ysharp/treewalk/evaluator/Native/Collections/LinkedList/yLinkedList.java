package ysharp.treewalk.evaluator.Native.Collections.LinkedList;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance.*;

import java.util.List;

public class yLinkedList {

    // helper
    public static yLinkedList.yLinkedListInstance requireLinkedListThis(Interpreter interpreter) {
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
        yLinkedList_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // list.addFirst(value)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new AddFirstFn());
        // list.addLast(value)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new AddLastFn());
        // list.removeFirst()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new RemoveFirstFn());
        // list.removeLast()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new RemoveLastFn());
        // list.peekFirst()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new PeekFirstFn());
        // list.peekLast()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new PeekLastFn());
        // list.get(index)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new GetFn());
        // list.set(index, value)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new SetFn());
        // list.contains(value)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // list.indexOf(value)
        yLinkedList_Instance_Prototype.RegisterNativeFn(new IndexOfFn());
        // list.size()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // list.isEmpty()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // list.clear()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // list.toArray()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new ToArrayFn());
        // list.clone()
        yLinkedList_Instance_Prototype.RegisterNativeFn(new CloneFn());

    }

    // Internal node class
    public static class Node {
        public Variable.Variant value;
        public Node next;

        public Node(Variable.Variant value) {
            this.value = value;
            this.next = null;
        }
    }

    public static class yLinkedListInstance extends yClass.ClassObjectInstance {

        public Node head;
        public Node tail;
        public int size;

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