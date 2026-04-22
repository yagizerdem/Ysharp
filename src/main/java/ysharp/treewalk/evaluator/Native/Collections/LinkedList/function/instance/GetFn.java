package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class GetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Variable.Variant indexVariant = arguments.getFirst();
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

        return new Variable.Variant(current.value.value);
    }

    @Override
    public String getFnName() {
        return "get";
    }
}