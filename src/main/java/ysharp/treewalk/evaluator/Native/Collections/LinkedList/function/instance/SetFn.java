package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant indexVariant = arguments.getFirst();
        Variable.Variant value = arguments.get(1);
        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        int index = ((Number) indexVariant.value).intValue();

        if (index < 0 || index >= list.size) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "LinkedList index out of bounds: " + index
            );
        }

        yLinkedList.Node current = list.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        Variable.Variant old = current.value;
        current.value = new Variable.Variant(value.value);

        return old;
    }

    @Override
    public String getFnName() {
        return "set";
    }
}
