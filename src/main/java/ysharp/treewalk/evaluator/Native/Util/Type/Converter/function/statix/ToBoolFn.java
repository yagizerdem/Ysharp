package ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToBoolFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        Variable.Variant variant = arguments.getFirst();

        if(variant.isBoolean())
            return new Variable.Variant(variant.asBoolean());
        else if(variant.isInt())
            return new Variable.Variant(variant.asInt() != 0);
        else if(variant.isDouble())
            return new Variable.Variant(variant.asDouble() != 0.0);
        else if(variant.isString())
            return new Variable.Variant(!variant.asString().isEmpty());
        else if(variant.isNull())
            return new Variable.Variant(false);

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
    }

    @Override
    public String getFnName() {
        return "toBool";
    }
}