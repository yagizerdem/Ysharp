package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RemoveLastFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        if (list.head == null) {
            return new Variable.Variant(null);
        }

        Variable.Variant removed;

        if (list.head == list.tail) {
            removed = list.head.value;
            list.head = null;
            list.tail = null;
        } else {
            yLinkedList.Node current = list.head;
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
