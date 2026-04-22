package ysharp.treewalk.evaluator.Native.Util.UUID.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsValidFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, 1, getFnName());

        String value = requireString(arguments.getFirst(), getFnName(), 1);

        try {
            java.util.UUID.fromString(value);
            return new Variable.Variant(true);
        }
        catch (Exception e) {
            return new Variable.Variant(false);
        }
    }

    @Override
    public String getFnName() {
        return "isValid";
    }
}