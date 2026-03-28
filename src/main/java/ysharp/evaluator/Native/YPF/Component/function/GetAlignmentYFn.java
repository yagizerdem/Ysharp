package ysharp.evaluator.Native.YPF.Component.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.YPF.Component.yComponent;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetAlignmentYFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());

        yComponent.IComponent comp = yComponent.requireComponentThis(interpreter, getFnName());

        float val = comp.getComponent().getAlignmentY();

        return new Variable.Variant((double) val);
    }

    @Override
    public String getFnName() {
        return "getAlignmentY";
    }
}