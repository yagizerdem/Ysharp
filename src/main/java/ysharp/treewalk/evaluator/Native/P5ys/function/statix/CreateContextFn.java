package ysharp.treewalk.evaluator.Native.P5ys.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class CreateContextFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());



        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "createContext";
    }
}
