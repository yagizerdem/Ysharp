package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ContainsFn extends Function.NativeFunction {

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
