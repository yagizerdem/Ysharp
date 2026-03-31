package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        yLinkedList.Node current = list.head;
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
