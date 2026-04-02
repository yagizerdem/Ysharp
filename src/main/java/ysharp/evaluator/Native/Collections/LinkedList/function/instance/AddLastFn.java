package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddLastFn extends Function.NativeFunction {

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

        yLinkedList.Node newNode = new yLinkedList.Node(new Variable.Variant(value.value));

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

