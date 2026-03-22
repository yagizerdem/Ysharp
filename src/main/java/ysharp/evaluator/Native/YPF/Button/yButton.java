package ysharp.evaluator.Native.YPF.Button;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Native.YPF.yComponent;

import javax.swing.JButton;
import java.awt.*;
import java.util.List;

public class yButton {

    private static yButtonInstance requireButtonThis(Interpreter interpreter, String fnName) {
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

        yButton_Instance_Prototype.prototype = yClass.ClassPrototype;


        // getUIClassID()
        class GetUIClassIDFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                return new Variable.Variant(btn.button.getUIClassID());
            }

            @Override public String getFnName() { return "getUIClassID"; }
        }


        GetUIClassIDFn getUIClassID = new GetUIClassIDFn();
        Variable getUIClassIDVar = new Variable(new Variable.Variant(getUIClassID),
                true,
                "function");
        yButton_Instance_Prototype.set(getUIClassID.getFnName(), getUIClassIDVar);


        // isDefaultButton()
        class IsDefaultButtonFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                return new Variable.Variant(btn.button.isDefaultButton());
            }

            @Override public String getFnName() { return "isDefaultButton"; }
        }

        IsDefaultButtonFn isDefaultButton = new IsDefaultButtonFn();
        Variable isDefaultButtonVar = new Variable(
                new Variable.Variant(isDefaultButton),
                true,
                "function");
        yButton_Instance_Prototype.set(isDefaultButton.getFnName(), isDefaultButtonVar);

        // isDefaultCapable()
        class IsDefaultCapableFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                return new Variable.Variant(btn.button.isDefaultCapable());
            }

            @Override public String getFnName() { return "isDefaultCapable"; }
        }

        IsDefaultCapableFn isDefaultCapable = new IsDefaultCapableFn();
        Variable isDefaultCapableVar = new Variable(
                new Variable.Variant(isDefaultCapable),
                true,
                "function");
        yButton_Instance_Prototype.set(isDefaultCapable.getFnName(), isDefaultCapableVar);

        // paramString()
        class ParamStringFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                return new Variable.Variant(btn.button.toString()); // paramString protected → workaround
            }

            @Override public String getFnName() { return "paramString"; }
        }

        ParamStringFn paramString = new ParamStringFn();
        Variable paramStringVar = new Variable(
                new Variable.Variant(paramString),
                true,
                "function");
        yButton_Instance_Prototype.set(paramString.getFnName(), paramStringVar);

        // removeNotify()
        class RemoveNotifyFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                btn.button.removeNotify();

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "removeNotify"; }
        }

        RemoveNotifyFn removeNotify = new RemoveNotifyFn();
        Variable removeNotifyVar = new Variable(
                new Variable.Variant(removeNotify),
                true,
                "function");
        yButton_Instance_Prototype.set(removeNotify.getFnName(), removeNotifyVar);


        // setDefaultCapable(boolean)
        class SetDefaultCapableFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                boolean val = args.getFirst().isTruthy();
                btn.button.setDefaultCapable(val);

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "setDefaultCapable"; }
        }

        SetDefaultCapableFn setDefaultCapable = new SetDefaultCapableFn();
        Variable setDefaultCapableVar = new Variable(
                new Variable.Variant(setDefaultCapable),
                true,
                "function");
        yButton_Instance_Prototype.set(setDefaultCapable.getFnName(), setDefaultCapableVar);

        // updateUI()
        class UpdateUIFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yButtonInstance btn = requireButtonThis(interpreter, getFnName());

                btn.button.updateUI();

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "updateUI"; }
        }


        UpdateUIFn updateUI = new UpdateUIFn();
        Variable updateUIVar = new Variable(
                new Variable.Variant(updateUI),
                true,
                "function");
        yButton_Instance_Prototype.set(updateUI.getFnName(), updateUIVar);

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