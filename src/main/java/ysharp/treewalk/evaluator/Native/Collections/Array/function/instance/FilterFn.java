package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class FilterFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
        int callbackArgSize = Function.getArgCount(callback);

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();

        for (int i = 0; i < array.data.size(); i++) {
            Variable.Variant element = array.data.get(i);

            List<Variable.Variant> args = new ArrayList<>();
            if(callbackArgSize >= 1) args.add(element);
            if(callbackArgSize >= 2) args.add(new Variable.Variant(i));
            if(callbackArgSize >= 3) args.add(new Variable.Variant(array));

            Variable.Variant result = callback.call(interpreter, args);

            if (result.isTruthy()) {
                newArray.data.add(element);
            }
        }

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() {
        return "filter";
    }
}

