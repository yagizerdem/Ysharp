package ysharp.treewalk.evaluator.Native.Util.UUID.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class ParseFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String value = requireString(arguments.getFirst(), getFnName(), 1);

        java.util.UUID uuid = java.util.UUID.fromString(value);

        return new Variable.Variant(new yString.yStringInstance(uuid.toString()));
    }

    @Override
    public String getFnName() {
        return "parse";
    }
}

