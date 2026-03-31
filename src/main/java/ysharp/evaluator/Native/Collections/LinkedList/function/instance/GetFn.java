package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant indexVariant = arguments.getFirst();
        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        int index = ((Number) indexVariant.value).intValue();

        if (index < 0 || index >= list.size) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "LinkedList index out of bounds: " + index
            );
        }

        yLinkedList.Node current = list.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    @Override
    public String getFnName() {
        return "get";
    }
}