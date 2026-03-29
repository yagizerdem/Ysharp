package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        Variable.Variant value = arguments.getFirst();
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        if(value.isRuntimeObject()) {
            array.data.add(value);
        }
        else {
            array.data.add(new Variable.Variant(value.value));
        }

        return new Variable.Variant(array.data.size());
    }

    @Override
    public String getFnName() {
        return "add";
    }
}