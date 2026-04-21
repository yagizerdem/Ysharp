package ysharp.treewalk.evaluator.Native.Util.UUID.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class V4Fn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String uuid = java.util.UUID.randomUUID().toString();

        return new Variable.Variant(new yString.yStringInstance(uuid));
    }

    @Override
    public String getFnName() {
        return "v4";
    }
}

