package ysharp.evaluator.Native.Form;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import javax.swing.*;
import java.util.List;

public class Y_Button {

    private static Y_ButtonObject requireButtonThis(Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Button method called without valid 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_ButtonObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Button objects."
            );
        }

        return (Y_ButtonObject) obj;
    }

    public static RuntimeObject Y_Button_Prototype;

    static {

        Y_Button_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "button_prototype";
            }
        };

        // button.setText(string)
        class SetTextFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_ButtonObject btn = requireButtonThis(interpreter);

                String text = arguments.get(0).asString();

                btn.button.setText(text);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setText";
            }
        }

        SetTextFn setText = new SetTextFn();
        Variable setTextVar = new Variable(
                new Variable.Variant(setText),
                true,
                TypeTag.OBJECT);

        Y_Button_Prototype.set(setText.getFnName(), setTextVar);
    }

    public static class Y_ButtonObject extends Y_ComponentObject {

        protected JButton button;

        public Y_ButtonObject(String text) {
            this.button = new JButton(text);
            this.component = button;
            this.prototype = Y_Button_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Button";
        }
    }

    public static class Y_ButtonInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            String text = arguments.get(0).asString();

            return new Variable.Variant(
                    new Y_ButtonObject(text)
            );
        }

        @Override
        public String getFnName() {
            return "Button";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        Y_ButtonInit ctor = new Y_ButtonInit();

        interpreter.defineGlobal(
                ctor.getFnName(),
                new Variable(
                        new Variable.Variant(ctor),
                        false,
                        TypeTag.OBJECT
                )
        );
    }
}