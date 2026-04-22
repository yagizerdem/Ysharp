package ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToCharFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        Variable.Variant variant = arguments.getFirst();

        if(variant.isChar())
            return new Variable.Variant(variant.asCharacter());
        else if(variant.isInt())
            return new Variable.Variant((char) variant.asInt().intValue());
        else if(variant.isString()) {
            String s = variant.asString();
            if(s.length() == 1)
                return new Variable.Variant(s.charAt(0));

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "String must contain exactly one character for " + getFnName() + "().");
        }

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
    }

    @Override
    public String getFnName() {
        return "toChar";
    }
}

