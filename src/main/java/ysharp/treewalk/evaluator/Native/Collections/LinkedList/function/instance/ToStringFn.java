package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

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

        return new Variable.Variant(new yString.yStringInstance(sb.toString()));
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}
