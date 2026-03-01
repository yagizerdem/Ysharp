package ysharp.evaluator.Native.Form;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import javax.swing.*;
import java.util.List;

public class Y_Frame {

    private static Y_FrameObject requireFrameThis(Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Frame method called without valid 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_FrameObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Frame objects."
            );
        }

        return (Y_FrameObject) obj;
    }

    public static RuntimeObject Y_Frame_Prototype;

    static {
        Y_Frame_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "frame_prototype";
            }
        };

        class SetTitleFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_FrameObject frame = requireFrameThis(interpreter);

                String title = arguments.get(0).asString();

                frame.frame.setTitle(title);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setTitle";
            }
        }

        SetTitleFn setTitle = new SetTitleFn();
        Variable peesetTitleVar = new Variable(
                new Variable.Variant(setTitle),
                true,
                TypeTag.OBJECT);
        Y_Frame_Prototype.set(setTitle.getFnName(), peesetTitleVar);

        // frame.setSize(width, height)
        class SetSizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_FrameObject frame = requireFrameThis(interpreter);

                Variable.Variant wVar = arguments.get(0);
                Variable.Variant hVar = arguments.get(1);

                if (!wVar.canImplicitlyConvertNumber() ||
                        !hVar.canImplicitlyConvertNumber()) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'setSize' arguments must be numbers."
                    );
                }

                int w = (int) wVar.implicitlyConvertNumber();
                int h = (int) hVar.implicitlyConvertNumber();

                frame.frame.setSize(w, h);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setSize";
            }
        }

        SetSizeFn setSize = new SetSizeFn();
        Variable setSizeVar = new Variable(
                new Variable.Variant(setSize),
                true,
                TypeTag.OBJECT);
        Y_Frame_Prototype.set(setSize.getFnName(), setSizeVar);

        // frame.show()
        class ShowFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_FrameObject frame = requireFrameThis(interpreter);

                frame.frame.setVisible(true);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "show";
            }
        }

        ShowFn show = new ShowFn();
        Variable showVar = new Variable(
                new Variable.Variant(show),
                true,
                TypeTag.OBJECT);
        Y_Frame_Prototype.set(show.getFnName(), showVar);

        // frame.setLocation(x, y)
        class SetLocationFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_FrameObject frame = requireFrameThis(interpreter);

                Variable.Variant xVar = arguments.get(0);
                Variable.Variant yVar = arguments.get(1);

                if (!xVar.canImplicitlyConvertNumber() ||
                        !yVar.canImplicitlyConvertNumber()) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'setLocation' arguments must be numbers."
                    );
                }

                int x = (int) xVar.implicitlyConvertNumber();
                int y = (int) yVar.implicitlyConvertNumber();

                frame.frame.setLocation(x, y);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setLocation";
            }
        }

        SetLocationFn setLocation = new SetLocationFn();
        Variable setLocationVar = new Variable(
                new Variable.Variant(setLocation),
                true,
                TypeTag.OBJECT);
        Y_Frame_Prototype.set(setLocation.getFnName(), setLocationVar);

        // frame.add(component)
        class AddComponentFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_FrameObject frame = requireFrameThis(interpreter);

                Variable.Variant arg = arguments.get(0);

                if (!arg.isRuntimeObject()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'add' argument must be a Component."
                    );
                }

                RuntimeObject obj = arg.asRuntimeObject();

                if (!(obj instanceof Y_ComponentObject)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'add' argument must be a GUI Component."
                    );
                }

                Y_ComponentObject comp = (Y_ComponentObject) obj;

                frame.frame.add(comp.getComponent());

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "add";
            }
        }

        AddComponentFn add = new AddComponentFn();
        Variable addVar = new Variable(
                new Variable.Variant(add),
                true,
                TypeTag.OBJECT);
        Y_Frame_Prototype.set(add.getFnName(), addVar);
    }

    public static class Y_FrameObject extends RuntimeObject {

        private final JFrame frame;

        public Y_FrameObject() {
            this.frame = new JFrame();
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.prototype = Y_Frame_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Frame";
        }
    }

    public static class Y_FrameInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments) {

            return new Variable.Variant(new Y_FrameObject());
        }

        @Override
        public String getFnName() {
            return "Frame";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_FrameInit ctor = new Y_FrameInit();
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
