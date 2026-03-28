package ysharp.evaluator.Native.YPF.Window.Frame.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.YPF.Window.Frame.yFrame;
import ysharp.evaluator.Variable;

import javax.swing.*;
import java.util.List;

public class OnCloseFn extends Function.NativeFunction implements Callable {
    @Override public int arity() { return 1; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
            throws YsharpError {

        requireArity(args, arity(), getFnName());
        yFrame.yFrameInstance frame = yFrame.requireFrameThis(interpreter, getFnName());

        Callable cb = requireCallable(args.getFirst(), getFnName(), 1);

        frame.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    cb.call(interpreter, List.of());
                } catch (YsharpError ex) {
                    ex.printStackTrace();
                }
            }
        });

        return new Variable.Variant(null);
    }

    @Override public String getFnName() { return "onClose"; }
}