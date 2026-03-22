package ysharp.evaluator.Native.YPF;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.TUI.Util.TextColor.yANSI;

import java.awt.*;
import java.util.List;

// abstract class
public class yComponent {

    public static yComponent.IComponent requireComponentThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof IComponent comp)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected Component but got '" + obj.getType() + "'"
            );
        }

        return comp;
    }

    public static yComponent.IComponent requireComponent(Variable.Variant variant, String fn, int index) {
        if (!variant.isRuntimeObject()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a object."
            );
        }

        RuntimeObject obj = variant.asRuntimeObject();

        if(!(obj instanceof yComponent.IComponent)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a Component object."
            );
        }

        return (yComponent.IComponent) obj;
    }

    public static interface IComponent {
        public Component getComponent();
        public IComponent getComponentWrapper();
    }
    public static RuntimeObject yComponent_Instance_Prototype; // base prototype for component types

    static {
        yComponent_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Component__";
            }

            @Override
            public String toString() {
                return "<prototype:Component>";
            }
        };
        yComponent_Instance_Prototype.prototype = yClass.ClassPrototype;


        class GetAlignmentXFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                IComponent comp = requireComponentThis(interpreter, getFnName());

                float val = comp.getComponent().getAlignmentX();

                return new Variable.Variant((double) val);
            }

            @Override
            public String getFnName() {
                return "getAlignmentX";
            }
        }

        GetAlignmentXFn getAlignmentX = new GetAlignmentXFn();
        Variable getAlignmentXVar = new Variable(
                new Variable.Variant(getAlignmentX),
                true,
                "function"
        );
        yComponent_Instance_Prototype.set(getAlignmentX.getFnName(), getAlignmentXVar);

        class GetAlignmentYFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                IComponent comp = requireComponentThis(interpreter, getFnName());

                float val = comp.getComponent().getAlignmentY();

                return new Variable.Variant((double) val);
            }

            @Override
            public String getFnName() {
                return "getAlignmentY";
            }
        }

        GetAlignmentYFn getAlignmentY = new GetAlignmentYFn();
        Variable getAlignmentYVar = new Variable(
                new Variable.Variant(getAlignmentY),
                true,
                "function"
        );
        yComponent_Instance_Prototype.set(getAlignmentY.getFnName(), getAlignmentYVar);
    }
}
