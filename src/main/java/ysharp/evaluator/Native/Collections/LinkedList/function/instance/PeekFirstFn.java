package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class PeekFirstFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        if (list.head == null) {
            return new Variable.Variant(null);
        }

        return list.head.value;
    }

    @Override
    public String getFnName() {
        return "peekFirst";
    }
}
