package ysharp.evaluator.Native.Collections.LinkedList.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.LinkedList.yLinkedList;
import ysharp.evaluator.Variable;

import java.util.List;

public class ToArrayFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedList.yLinkedListInstance list = yLinkedList.requireLinkedListThis(interpreter);

        java.util.ArrayList<Variable.Variant> result =
                new java.util.ArrayList<>();

        yLinkedList.Node current = list.head;
        while (current != null) {
            result.add(current.value);
            current = current.next;
        }

        yArray.yArrayInstance array =
                new yArray.yArrayInstance(result);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "toArray";
    }
}

