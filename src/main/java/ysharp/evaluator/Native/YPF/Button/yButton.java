package ysharp.evaluator.Native.YPF.Button;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Native.YPF.Button.function.*;
import ysharp.evaluator.Native.YPF.Component.yComponent;

import javax.swing.JButton;
import java.awt.*;
import java.util.List;

public class yButton {

    public static yButtonInstance requireButtonThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yButtonInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected Button but got '" + obj.getType() + "'"
            );
        }

        return (yButtonInstance) obj;
    }

    public static RuntimeObject yButton_Instance_Prototype;

    static {
        yButton_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Button__"; }
            @Override public String toString() { return "<prototype:Button>"; }
        };

        yButton_Instance_Prototype.prototype = yComponent.yComponent_Instance_Prototype;


        // getUIClassID()
        yButton_Instance_Prototype.RegisterNativeFn(new GetUIClassIDFn());
        // isDefaultButton()
        yButton_Instance_Prototype.RegisterNativeFn(new IsDefaultButtonFn());
        // isDefaultCapable()
        yButton_Instance_Prototype.RegisterNativeFn(new IsDefaultCapableFn());
        // paramString()
        yButton_Instance_Prototype.RegisterNativeFn(new ParamStringFn());
        // removeNotify()
        yButton_Instance_Prototype.RegisterNativeFn(new RemoveNotifyFn());
        // setDefaultCapable(boolean)
        yButton_Instance_Prototype.RegisterNativeFn(new SetDefaultCapableFn());
        // updateUI()
        yButton_Instance_Prototype.RegisterNativeFn(new UpdateUIFn());

    }

    public static class yButtonInstance extends yClass.ClassObjectInstance implements yComponent.IComponent {

        public final JButton button;

        public yButtonInstance() {
            this.button = new JButton();
            this.prototype = yButton_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Button"; }
        @Override public String toString() { return "<instance:Button>"; }

        @Override
        public Component getComponent() {
            return this.button;
        }

        @Override
        public yComponent.IComponent getComponentWrapper() {
            return this;
        }

        @Override
        public Object getNativeJavaObject() {
            return this.button;
        }

    }

    public static class yButtonClass extends yClass.SealedClassObject {

        public yButtonClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yButtonInstance());
        }

        @Override public String getClassName() { return "Button"; }
        @Override public String getType() { return "Button"; }
    }
}