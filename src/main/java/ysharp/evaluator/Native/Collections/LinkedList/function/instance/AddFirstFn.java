package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant value = arguments.getFirst();
        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        yLinkedList.Node newNode = new yLinkedList.Node(value);
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
