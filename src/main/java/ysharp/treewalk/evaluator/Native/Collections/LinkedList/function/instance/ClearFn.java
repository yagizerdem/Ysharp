package ysharp.treewalk.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ClearFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);
        list.head = null;
        list.tail = null;
        list.size = 0;

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "clear";
    }
}
