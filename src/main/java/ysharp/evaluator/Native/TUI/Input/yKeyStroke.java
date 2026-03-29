package ysharp.evaluator.Native.TUI.Input;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.List;

public class yKeyStroke {

    // helper
    public static KeyType requireKeyType(
            Variable.Variant variant,
            String fn,
            int index) throws YsharpError {

        if (variant == null || !(variant.value instanceof KeyType)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + ": argument " + index + " must be KeyType."
            );
        }

        return (KeyType) variant.value;
    }

    private static yKeyStrokeInstance requireKeyStrokeThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yKeyStrokeInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'KeyStroke' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yKeyStrokeInstance) obj;
    }


    public static RuntimeObject yKeyStroke_Instance_Prototype;

    static {
        yKeyStroke_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__KeyStroke__";
            }
        };
        yKeyStroke_Instance_Prototype.prototype = yClass.ClassPrototype;

        // key.getCharacter()
        class getCharacterFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());
                Character ch = keyStroke.data.getCharacter();

                return new Variable.Variant(ch);
            }

            @Override
            public String getFnName() {
                return "getCharacter";
            }
        }

        getCharacterFn getCharacter = new getCharacterFn();
        Variable getCharacterVar = new Variable(
                new Variable.Variant(getCharacter),
                true,
                "function");
        yKeyStroke_Instance_Prototype.set(getCharacter.getFnName(), getCharacterVar);


        // key.getEventTime()
        class getEventTimeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());
                double eventTime = keyStroke.data.getEventTime();

                return new Variable.Variant(eventTime);
            }

            @Override
            public String getFnName() {
                return "getEventTime";
            }
        }

        getEventTimeFn getEventTime = new getEventTimeFn();
        Variable getEventTimeVar = new Variable(
                new Variable.Variant(getEventTime),
                true,
                "function");
        yKeyStroke_Instance_Prototype.set(getEventTime.getFnName(), getEventTimeVar);

        // key.hashCode()
        class hashCodeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter,  getFnName());

                int hash = keyStroke.data.hashCode();

                return new Variable.Variant(hash);
            }

            @Override
            public String getFnName() {
                return "hashCode";
            }
        }

        hashCodeFn hashCode = new hashCodeFn();
        Variable hashCodeVar = new Variable(
                new Variable.Variant(hashCode),
                true,
                "function");

        yKeyStroke_Instance_Prototype.set(hashCode.getFnName(), hashCodeVar);

        // key.isAltDown()
        class isAltDownFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());

                boolean value = keyStroke.data.isAltDown();

                return new Variable.Variant(value);
            }

            @Override
            public String getFnName() {
                return "isAltDown";
            }
        }

        isAltDownFn isAltDown = new isAltDownFn();
        Variable isAltDownVar = new Variable(
                new Variable.Variant(isAltDown),
                true,
                "function");

        yKeyStroke_Instance_Prototype.set(isAltDown.getFnName(), isAltDownVar);

        // key.isCtrlDown()
        class isCtrlDownFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());

                boolean value = keyStroke.data.isCtrlDown();

                return new Variable.Variant(value);
            }

            @Override
            public String getFnName() {
                return "isCtrlDown";
            }
        }

        isCtrlDownFn isCtrlDown = new isCtrlDownFn();
        Variable isCtrlDownVar = new Variable(
                new Variable.Variant(isCtrlDown),
                true,
                "function");

        yKeyStroke_Instance_Prototype.set(isCtrlDown.getFnName(), isCtrlDownVar);


        // key.isShiftDown()
        class isShiftDownFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());

                boolean value = keyStroke.data.isShiftDown();

                return new Variable.Variant(value);
            }

            @Override
            public String getFnName() {
                return "isShiftDown";
            }
        }

        isShiftDownFn isShiftDown = new isShiftDownFn();
        Variable isShiftDownVar = new Variable(
                new Variable.Variant(isShiftDown),
                true,
                "function");

        yKeyStroke_Instance_Prototype.set(isShiftDown.getFnName(), isShiftDownVar);


        // key.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yKeyStrokeInstance keyStroke = requireKeyStrokeThis(interpreter, getFnName());

                return new Variable.Variant(keyStroke.data.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                "function");
        yKeyStroke_Instance_Prototype.set(toString.getFnName(), toStringVar);

    }

    public static class yKeyStrokeInstance extends yClass.ClassObjectInstance {

        public final KeyStroke data;

        public yKeyStrokeInstance(KeyStroke data) {
            this.data = data;
            this.prototype = yKeyStroke_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "KeyStroke";
        }

        @Override
        public String toString() {
            return "<instnace:KeyStroke>";
        }
    }

    public static class yKeyStrokeClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yKeyStrokeClass(){
            this.prototype = yClass.ClassPrototype;

            class FromKeyTypeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getFnName());

                    KeyType keyType = requireKeyType(arguments.getFirst(), getFnName(), 1);

                    KeyStroke key = new KeyStroke(keyType);

                    return new Variable.Variant(new yKeyStrokeInstance(key));
                }

                @Override
                public String getFnName() {
                    return "fromKeyType";
                }
            }

            FromKeyTypeFn f1 = new FromKeyTypeFn();
            this.set(f1.getFnName(), new Variable(new Variable.Variant(f1), true, "function"));


            class FromKeyTypeModsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getFnName());

                    KeyType keyType = requireKeyType(arguments.get(0), getFnName(), 1);
                    boolean ctrl = requireBoolean(arguments.get(1), getFnName(), 2);
                    boolean alt = requireBoolean(arguments.get(2), getFnName(), 3);

                    KeyStroke key = new KeyStroke(keyType, ctrl, alt);

                    return new Variable.Variant(new yKeyStrokeInstance(key));
                }

                @Override
                public String getFnName() {
                    return "fromKeyTypeMods";
                }
            }

            FromKeyTypeModsFn f2 = new FromKeyTypeModsFn();
            this.set(f2.getFnName(), new Variable(new Variable.Variant(f2), true, "function"));


            class FromKeyTypeFullFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 4;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 4, getFnName());

                    KeyType keyType = requireKeyType(arguments.get(0), getFnName(), 1);
                    boolean ctrl = requireBoolean(arguments.get(1), getFnName(), 2);
                    boolean alt = requireBoolean(arguments.get(2), getFnName(), 3);
                    boolean shift = requireBoolean(arguments.get(3), getFnName(), 4);

                    KeyStroke key = new KeyStroke(keyType, ctrl, alt, shift);

                    return new Variable.Variant(new yKeyStrokeInstance(key));
                }

                @Override
                public String getFnName() {
                    return "fromKeyTypeFull";
                }
            }

            FromKeyTypeFullFn f3 = new FromKeyTypeFullFn();
            this.set(f3.getFnName(), new Variable(new Variable.Variant(f3), true, "function"));

            class FromCharFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getFnName());

                    Character ch = requireChar(arguments.get(0), getFnName(), 1);
                    boolean ctrl = requireBoolean(arguments.get(1), getFnName(), 2);
                    boolean alt = requireBoolean(arguments.get(2), getFnName(), 3);

                    KeyStroke key = new KeyStroke(ch, ctrl, alt);

                    return new Variable.Variant(new yKeyStrokeInstance(key));
                }

                @Override
                public String getFnName() {
                    return "fromChar";
                }
            }

            FromCharFn f4 = new FromCharFn();
            this.set(f4.getFnName(), new Variable(new Variable.Variant(f4), true, "function"));

            class FromCharFullFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 4;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 4, getFnName());

                    Character ch = requireChar(arguments.get(0), getFnName(), 1);
                    boolean ctrl = requireBoolean(arguments.get(1), getFnName(), 2);
                    boolean alt = requireBoolean(arguments.get(2), getFnName(), 3);
                    boolean shift = requireBoolean(arguments.get(3), getFnName(), 4);

                    KeyStroke key = new KeyStroke(ch, ctrl, alt, shift);

                    return new Variable.Variant(new yKeyStrokeInstance(key));
                }

                @Override
                public String getFnName() {
                    return "fromCharFull";
                }
            }

            FromCharFullFn f5 = new FromCharFullFn();
            this.set(f5.getFnName(), new Variable(new Variable.Variant(f5), true, "function"));

        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1, "cannot call key stroke class");
        }

        @Override
        public String getClassName() {
            return "KeyStroke";
        }

        @Override
        public String getType() {
            return "KeyStroke";
        }

        @Override
        public String toString() {
            return "<class:KeyStroke>";
        }
    }

}
