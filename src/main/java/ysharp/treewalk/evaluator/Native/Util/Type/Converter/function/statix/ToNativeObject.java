package ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ToNativeObject extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        Variable.Variant variant = arguments.getFirst();
        return new Variable.Variant(variant.asJavaNative());
    }

    @Override
    public String getFnName() {
        return "toNativeObject";
    }
}
