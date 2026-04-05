package ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.ArrayDeque.yArrayDeque;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArrayDeque.yArrayDequeInstance original = yArrayDeque.requireArrayDequeThis(interpreter);
        yArrayDeque.yArrayDequeInstance cloned = new yArrayDeque.yArrayDequeInstance();

        cloned.data.addAll(original.data);

        return new Variable.Variant(cloned);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}

