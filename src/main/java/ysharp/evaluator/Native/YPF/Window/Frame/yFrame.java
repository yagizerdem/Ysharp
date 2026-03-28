package ysharp.evaluator.Native.YPF.Window.Frame;


import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.YPF.Window.Frame.function.*;

import javax.swing.JFrame;
import java.util.List;

public class yFrame {

    public static yFrameInstance requireFrameThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yFrameInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected Frame but got '" + obj.getType() + "'"
            );
        }

        return (yFrameInstance) obj;
    }


    public static RuntimeObject yFrame_Instance_Prototype;

    static {
        yFrame_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__Frame__"; }

            @Override
            public String toString() { return "<prototype:Frame>"; }
        };
        yFrame_Instance_Prototype.prototype = yClass.ClassPrototype;


        // frame.setTitle("title");
        yFrame_Instance_Prototype.RegisterNativeFn(new SetTitleFn());
        // frame.setSize(w, h);
        yFrame_Instance_Prototype.RegisterNativeFn(new SetSizeFn());
        // frame.setVisible(boolean);
        yFrame_Instance_Prototype.RegisterNativeFn(new SetVisibleFn());
        // frame.onClose(callback);
        yFrame_Instance_Prototype.RegisterNativeFn(new OnCloseFn());
        // frame.add(component);
        yFrame_Instance_Prototype.RegisterNativeFn(new AddFn());
    }

    public static class yFrameInstance extends yClass.ClassObjectInstance {

        public final JFrame frame;

        public yFrameInstance() {
            this.frame = new JFrame();
            this.prototype = yFrame_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() { return true; }

        @Override
        public String getType() { return "Frame"; }

        @Override
        public String toString() { return "<instance:Frame>"; }
    }

    public static class yFrameClass extends yClass.SealedClassObject {

        public yFrameClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yFrameInstance());
        }

        @Override public String getClassName() { return "Frame"; }
        @Override public String getType() { return "Frame"; }
        @Override public String toString() { return "<class:Frame>"; }
    }
}