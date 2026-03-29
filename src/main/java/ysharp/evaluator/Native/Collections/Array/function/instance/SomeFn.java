package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class SomeFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
        int callbackArgSize = Function.getArgCount(callback);

        for (int i = 0; i < array.data.size(); i++) {
            Variable.Variant element = array.data.get(i);

            List<Variable.Variant> args = new ArrayList<>();
            if(callbackArgSize >= 1) args.add(element);
            if(callbackArgSize >= 2) args.add(new Variable.Variant(i));
            if(callbackArgSize >= 3) args.add(new Variable.Variant(array));

            Variable.Variant result = callback.call(interpreter, args);

            if (result.isTruthy()) {
                return new Variable.Variant(true);
            }
        }

        return new Variable.Variant(false);
    }

    @Override
    public String getFnName() {
        return "some";
    }
}
