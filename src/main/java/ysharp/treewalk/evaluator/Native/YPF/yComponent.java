package ysharp.treewalk.evaluator.Native.YPF;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class yComponent {

    public  static interface yBaseComponent {
        public Component getComponent();
    }

    public static yBaseComponent requireComponentThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yBaseComponent component)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Component but got '" + obj.getType() + "'"
            );
        }

        return component;
    }


    public static RuntimeObject yComponent_Instance_Prototype;

    static {
        yComponent_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Component__"; }
            @Override public String toString() { return "<prototype:Component>"; }
        };
        yComponent_Instance_Prototype.prototype = yClass.ClassPrototype;

        class OnClickFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                requireArity(arguments, 1, getFnName());
                yBaseComponent baseComponent = requireComponentThis(interpreter, getFnName());
                Callable fn = requireCallable(arguments.getFirst(), getFnName(), 1);

                baseComponent.getComponent().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Interpreter newInterpreter = interpreter.copy();
                        fn.call(newInterpreter, List.of());
                    }
                });

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "onClick";
            }
        }
        yComponent_Instance_Prototype.RegisterNativeFn(new OnClickFn());

        class OnMouseUpFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                requireArity(arguments, 1, getFnName());

                yBaseComponent baseComponent = requireComponentThis(interpreter, getFnName());
                Callable fn = requireCallable(arguments.getFirst(), getFnName(), 1);

                baseComponent.getComponent().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Interpreter newInterpreter = interpreter.copy();
                        fn.call(newInterpreter, List.of());
                    }
                });

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "onMouseUp";
            }
        }
        yComponent_Instance_Prototype.RegisterNativeFn(new OnMouseUpFn());

        class OnMouseDownFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                requireArity(arguments, 1, getFnName());

                yBaseComponent baseComponent = requireComponentThis(interpreter, getFnName());
                Callable fn = requireCallable(arguments.getFirst(), getFnName(), 1);

                baseComponent.getComponent().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Interpreter newInterpreter = interpreter.copy();
                        fn.call(newInterpreter, List.of());
                    }
                });

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "onMouseDown";
            }
        }
        yComponent_Instance_Prototype.RegisterNativeFn(new OnMouseDownFn());

        class OnMouseEnterFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                requireArity(arguments, 1, getFnName());

                yBaseComponent baseComponent = requireComponentThis(interpreter, getFnName());
                Callable fn = requireCallable(arguments.getFirst(), getFnName(), 1);

                baseComponent.getComponent().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Interpreter newInterpreter = interpreter.copy();
                        fn.call(newInterpreter, List.of());
                    }
                });

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "onMouseEnter";
            }
        }
        yComponent_Instance_Prototype.RegisterNativeFn(new OnMouseEnterFn());
    }

}
