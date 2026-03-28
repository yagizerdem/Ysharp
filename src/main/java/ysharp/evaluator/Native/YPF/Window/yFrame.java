package ysharp.evaluator.Native.YPF.Window;


import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.evaluator.Native.YPF.yComponent;

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
        class SetTitleFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yFrameInstance frame = requireFrameThis(interpreter, getFnName());

                String title = args.get(0).value.toString();
                frame.frame.setTitle(title);

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "setTitle"; }
        }

        SetTitleFn setTitle = new SetTitleFn();
        Variable setTitleVar = new Variable(
                new Variable.Variant(setTitle),
                true,
                "function"
        );
        yFrame_Instance_Prototype.set(setTitle.getFnName(), setTitleVar);


        // frame.setSize(w, h);
        class SetSizeFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 2; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yFrameInstance frame = requireFrameThis(interpreter, getFnName());

                int w = (int) args.get(0).implicitlyConvertNumber();
                int h = (int) args.get(1).implicitlyConvertNumber();

                frame.frame.setSize(w, h);

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "setSize"; }
        }

        SetSizeFn setSize = new SetSizeFn();
        Variable setSizeVar = new Variable(
                new Variable.Variant(setSize),
                true,
                "function"
        );
        yFrame_Instance_Prototype.set(setSize.getFnName(), setSizeVar);


        // frame.setVisible(boolean);
        class SetVisibleFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yFrameInstance frame = requireFrameThis(interpreter, getFnName());

                boolean visible = args.get(0).isTruthy();
                frame.frame.setVisible(visible);

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "setVisible"; }
        }

        SetVisibleFn setVisible = new SetVisibleFn();
        Variable setVisibleVar = new Variable(
                new Variable.Variant(setVisible),
                true,
                "function"
        );
        yFrame_Instance_Prototype.set(setVisible.getFnName(), setVisibleVar);


        // frame.onClose(callback);
        class OnCloseFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yFrameInstance frame = requireFrameThis(interpreter, getFnName());

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

        OnCloseFn onClose = new OnCloseFn();
        Variable onCloseVar = new Variable(
                new Variable.Variant(onClose),
                true,
                "function"
        );
        yFrame_Instance_Prototype.set(onClose.getFnName(), onCloseVar);


        // frame.add(callback);
        class AddFn extends Function.NativeFunction implements Callable {
            @Override public int arity() { return 1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpError {

                requireArity(args, arity(), getFnName());
                yFrameInstance frame = requireFrameThis(interpreter, getFnName());

                yComponent.IComponent component = yComponent.requireComponent(args.getFirst(), getFnName(), 1);

                frame.frame.add(component.getComponent());

                return new Variable.Variant(null);
            }

            @Override public String getFnName() { return "add"; }
        }
        AddFn add = new AddFn();
        Variable addVar = new Variable(
                new Variable.Variant(add),
                true,
                "function"
        );
        yFrame_Instance_Prototype.set(add.getFnName(), addVar);
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