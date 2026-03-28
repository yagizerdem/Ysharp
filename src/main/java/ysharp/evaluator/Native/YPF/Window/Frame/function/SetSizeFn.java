package ysharp.evaluator.Native.YPF.Window.Frame.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.YPF.Window.Frame.yFrame;
import ysharp.evaluator.Variable;

import java.util.List;

public class SetSizeFn extends Function.NativeFunction implements Callable {
    @Override public int arity() { return 2; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
            throws YsharpError {

        requireArity(args, arity(), getFnName());
        yFrame.yFrameInstance frame = yFrame.requireFrameThis(interpreter, getFnName());

        int w = (int) args.get(0).implicitlyConvertNumber();
        int h = (int) args.get(1).implicitlyConvertNumber();

        frame.frame.setSize(w, h);

        return new Variable.Variant(null);
    }

    @Override public String getFnName() { return "setSize"; }
}
