package ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yString;

import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        Variable.Variant variant = arguments.getFirst();
        if(variant.isInt())
            return new Variable.Variant(new yString.yStringInstance(variant.asInt().toString()));
        else if(variant.isDouble())
            return new Variable.Variant(new yString.yStringInstance(variant.asDouble().toString()));
        else if(variant.isString())
            return new Variable.Variant(new yString.yStringInstance(variant.asString()));
        else if(variant.isBoolean())
            return new Variable.Variant(new yString.yStringInstance(variant.asBoolean().toString()));
        else if(variant.isNull())
            return new Variable.Variant(new yString.yStringInstance("null"));
        else if(variant.isChar())
            return new Variable.Variant(new yString.yStringInstance(variant.asCharacter().toString()));
        else if(variant.isFunctionLike())
            return new Variable.Variant(new yString.yStringInstance("function"));
        else if(variant.isClass())
            return new Variable.Variant(new yString.yStringInstance(variant.asClass().toString()));
        else if(variant.isClassInstance())
            return new Variable.Variant(new yString.yStringInstance(variant.asRuntimeObject().toString()));

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}