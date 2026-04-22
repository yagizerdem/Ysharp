package ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToDoubleFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        Variable.Variant variant = arguments.getFirst();

        if(variant.isDouble())
            return new Variable.Variant(variant.asDouble());
        else if(variant.isInt())
            return new Variable.Variant((double) variant.asInt());
        else if(variant.isChar())
            return new Variable.Variant((double) variant.asCharacter());
        else if(variant.isBoolean())
            return new Variable.Variant(variant.asBoolean() ? 1.0 : 0.0);
        else if(variant.isString())
            return new Variable.Variant(Double.valueOf(variant.asString()));

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
    }

    @Override
    public String getFnName() {
        return "toDouble";
    }
}