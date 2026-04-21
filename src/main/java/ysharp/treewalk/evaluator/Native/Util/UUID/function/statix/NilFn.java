package ysharp.treewalk.evaluator.Native.Util.UUID.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class NilFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        return new Variable.Variant(new yString.yStringInstance("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public String getFnName() {
        return "nil";
    }
}