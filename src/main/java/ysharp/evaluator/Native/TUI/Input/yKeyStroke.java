package ysharp.evaluator.Native.TUI.Input;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;
import java.util.List;

public class yKeyStroke {

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
            return "_KeyStroke_";
        }

        @Override
        public String toString() {
            return data.toString();
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

                    return new Variable.Variant(key);
                }

                @Override
                public String getFnName() {
                    return "fromKeyType";
                }
            }

            FromKeyTypeFn f1 = new FromKeyTypeFn();
            this.set(f1.getFnName(), new Variable(new Variable.Variant(f1), true, TypeTag.OBJECT));


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

                    return new Variable.Variant(key);
                }

                @Override
                public String getFnName() {
                    return "fromKeyTypeMods";
                }
            }

            FromKeyTypeModsFn f2 = new FromKeyTypeModsFn();
            this.set(f2.getFnName(), new Variable(new Variable.Variant(f2), true, TypeTag.OBJECT));


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

                    return new Variable.Variant(key);
                }

                @Override
                public String getFnName() {
                    return "fromKeyTypeFull";
                }
            }

            FromKeyTypeFullFn f3 = new FromKeyTypeFullFn();
            this.set(f3.getFnName(), new Variable(new Variable.Variant(f3), true, TypeTag.OBJECT));

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

                    return new Variable.Variant(key);
                }

                @Override
                public String getFnName() {
                    return "fromChar";
                }
            }

            FromCharFn f4 = new FromCharFn();
            this.set(f4.getFnName(), new Variable(new Variable.Variant(f4), true, TypeTag.OBJECT));

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

                    return new Variable.Variant(key);
                }

                @Override
                public String getFnName() {
                    return "fromCharFull";
                }
            }

            FromCharFullFn f5 = new FromCharFullFn();
            this.set(f5.getFnName(), new Variable(new Variable.Variant(f5), true, TypeTag.OBJECT));

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
            return "KeyStroke";
        }
    }

}
