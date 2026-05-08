package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class GetHomeFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments
    ) throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        return new Variable.Variant(new yString.yStringInstance(System.getProperty("user.home")));
    }

    @Override
    public String getFnName() {
        return "getHome";
    }
}