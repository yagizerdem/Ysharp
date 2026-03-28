package ysharp.evaluator.Native.YPF.Button.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.YPF.Button.yButton;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetUIClassIDFn extends Function.NativeFunction implements Callable {
    @Override public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
            throws YsharpError {

        requireArity(args, arity(), getFnName());
        yButton.yButtonInstance btn = yButton.requireButtonThis(interpreter, getFnName());

        return new Variable.Variant(btn.button.getUIClassID());
    }

    @Override public String getFnName() { return "getUIClassID"; }
}
