package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;
import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yLinkedList.yLinkedListInstance original = yLinkedList.requireLinkedListThis(interpreter);
        yLinkedList.yLinkedListInstance cloned = new yLinkedList.yLinkedListInstance();

        yLinkedList.Node current = original.head;
        while (current != null) {
            yLinkedList.Node newNode = new yLinkedList.Node(current.value);
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
