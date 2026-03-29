package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class FindFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {
        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
        int callbackArgSize = Function.getArgCount(callback);

        for (int i = 0; i < array.data.size(); i++) {
            List<Variable.Variant> args = new ArrayList<>();
            if(callbackArgSize >= 1) args.add(array.data.get(i));
            if(callbackArgSize >= 2) args.add(new Variable.Variant(i));
            if(callbackArgSize >= 3) args.add(new Variable.Variant(array));

            Variable.Variant result = callback.call(interpreter, args);
            if (result.isTruthy()) {
                return array.data.get(i);
            }
        }

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() { return "find"; }
}

