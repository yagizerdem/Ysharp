package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class IndexOfFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant target = arguments.getFirst();
        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        yLinkedList.Node current = list.head;
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

