package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ReduceFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
        int callbackArgSize = Function.getArgCount(callback);

        Variable.Variant accumulator;
        int startIndex;

        if (arguments.size() > 1 && arguments.get(1) != null) {
            accumulator = arguments.get(1);
            startIndex = 0;
        } else {
            accumulator = array.data.getFirst();
            startIndex = 1;
        }

        for (int i = startIndex; i < array.data.size(); i++) {
            Variable.Variant element = array.data.get(i);

            List<Variable.Variant> args = new ArrayList<>();
            if(callbackArgSize >= 1) args.add(accumulator);
            if(callbackArgSize >= 2) args.add(element);
            if(callbackArgSize >= 3) args.add(new Variable.Variant(i));
            if(callbackArgSize >= 4) args.add(new Variable.Variant(array));

            accumulator = callback.call(interpreter, args);
        }

        return accumulator;
    }

    @Override
    public String getFnName() {
        return "reduce";
    }
}
