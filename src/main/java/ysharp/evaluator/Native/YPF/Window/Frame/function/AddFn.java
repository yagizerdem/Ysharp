package ysharp.evaluator.Native.YPF.Window.Frame.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.YPF.Component.yComponent;
import ysharp.evaluator.Native.YPF.Window.Frame.yFrame;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFn extends Function.NativeFunction implements Callable {
    @Override public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
            throws YsharpError {

        requireArity(args, arity(), getFnName());
        yFrame.yFrameInstance frame = yFrame.requireFrameThis(interpreter, getFnName());

        yComponent.IComponent component = yComponent.requireComponent(args.getFirst(), getFnName(), 1);

        frame.frame.add(component.getComponent());

        return new Variable.Variant(null);
    }

    @Override public String getFnName() { return "add"; }
}