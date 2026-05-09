package ysharp.treewalk.evaluator.Native.YPF.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.awt.Font;
import java.util.List;

public class yFont {

    public static yFontInstance requireFontThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yFontInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Font but got '" + obj.getType() + "'"
            );
        }

        return (yFontInstance) obj;
    }

    public static RuntimeObject yFont_Instance_Prototype;

    static {
        yFont_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Font__"; }
            @Override public String toString() { return "<prototype:Font>"; }
        };

        yFont_Instance_Prototype.prototype = yClass.ClassPrototype;

        class GetNameFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.getName());
            }

            @Override public String getFnName() { return "getName"; }
        }

        class GetFamilyFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.getFamily());
            }

            @Override public String getFnName() { return "getFamily"; }
        }

        class GetFontNameFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.getFontName());
            }

            @Override public String getFnName() { return "getFontName"; }
        }

        class GetSizeFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.getSize());
            }

            @Override public String getFnName() { return "getSize"; }
        }

        class GetStyleFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.getStyle());
            }

            @Override public String getFnName() { return "getStyle"; }
        }

        class IsPlainFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.isPlain());
            }

            @Override public String getFnName() { return "isPlain"; }
        }

        class IsBoldFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.isBold());
            }

            @Override public String getFnName() { return "isBold"; }
        }

        class IsItalicFn extends Function.NativeFunction {
            @Override public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 0, getFnName());

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                return new Variable.Variant(fontInstance.font.isItalic());
            }

            @Override public String getFnName() { return "isItalic"; }
        }

        class DeriveFontFn extends Function.NativeFunction {
            @Override public int arity() { return -1; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                if (arguments.size() != 1 && arguments.size() != 2) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "Function '" + getFnName() + "' expects 1 or 2 arguments."
                    );
                }

                yFontInstance fontInstance = yFont.requireFontThis(interpreter, getFnName());

                Font derived;

                if (arguments.size() == 1) {
                    int size = requireInt(arguments.get(0), getFnName(), 1);
                    derived = fontInstance.font.deriveFont((float) size);
                } else {
                    int style = requireInt(arguments.get(0), getFnName(), 1);
                    int size = requireInt(arguments.get(1), getFnName(), 2);
                    derived = fontInstance.font.deriveFont(style, (float) size);
                }

                return new Variable.Variant(new yFontInstance(derived));
            }

            @Override public String getFnName() { return "deriveFont"; }
        }

        yFont_Instance_Prototype.RegisterNativeFn(new GetNameFn());
        yFont_Instance_Prototype.RegisterNativeFn(new GetFamilyFn());
        yFont_Instance_Prototype.RegisterNativeFn(new GetFontNameFn());
        yFont_Instance_Prototype.RegisterNativeFn(new GetSizeFn());
        yFont_Instance_Prototype.RegisterNativeFn(new GetStyleFn());
        yFont_Instance_Prototype.RegisterNativeFn(new IsPlainFn());
        yFont_Instance_Prototype.RegisterNativeFn(new IsBoldFn());
        yFont_Instance_Prototype.RegisterNativeFn(new IsItalicFn());
        yFont_Instance_Prototype.RegisterNativeFn(new DeriveFontFn());
    }

    public static class yFontInstance extends yClass.ClassObjectInstance {

        public Font font;

        public yFontInstance(Font font) {
            this.font = font;
            this.prototype = yFont_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Font"; }
        @Override public String toString() { return "<instance:Font>"; }
        @Override public Object getNativeJavaObject() { return this.font; }
    }

    public static class yFontClass extends yClass.SealedClassObject {

        public yFontClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("PLAIN", new Variable(new Variable.Variant(Font.PLAIN), true, "int"));
            this.set("BOLD", new Variable(new Variable.Variant(Font.BOLD), true, "int"));
            this.set("ITALIC", new Variable(new Variable.Variant(Font.ITALIC), true, "int"));

            class CreateFn extends Function.NativeFunction {
                @Override public int arity() { return 3; }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, 3, getFnName());

                    String name = requireString(arguments.get(0), getFnName(), 1);
                    int style = requireInt(arguments.get(1), getFnName(), 2);
                    int size = requireInt(arguments.get(2), getFnName(), 3);

                    Font font = new Font(name, style, size);

                    return new Variable.Variant(new yFontInstance(font));
                }

                @Override public String getFnName() { return "create"; }
            }
            this.RegisterNativeFn(new CreateFn());

            class PlainFn extends Function.NativeFunction {
                @Override public int arity() { return 2; }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, 2, getFnName());

                    String name = requireString(arguments.get(0), getFnName(), 1);
                    int size = requireInt(arguments.get(1), getFnName(), 2);

                    Font font = new Font(name, Font.PLAIN, size);

                    return new Variable.Variant(new yFontInstance(font));
                }

                @Override public String getFnName() { return "plain"; }
            }
            this.RegisterNativeFn(new PlainFn());

            class BoldFn extends Function.NativeFunction {
                @Override public int arity() { return 2; }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, 2, getFnName());

                    String name = requireString(arguments.get(0), getFnName(), 1);
                    int size = requireInt(arguments.get(1), getFnName(), 2);

                    Font font = new Font(name, Font.BOLD, size);

                    return new Variable.Variant(new yFontInstance(font));
                }

                @Override public String getFnName() { return "bold"; }
            }
            this.RegisterNativeFn(new BoldFn());

            class ItalicFn extends Function.NativeFunction {
                @Override public int arity() { return 2; }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, 2, getFnName());

                    String name = requireString(arguments.get(0), getFnName(), 1);
                    int size = requireInt(arguments.get(1), getFnName(), 2);

                    Font font = new Font(name, Font.ITALIC, size);

                    return new Variable.Variant(new yFontInstance(font));
                }

                @Override public String getFnName() { return "italic"; }
            }
            this.RegisterNativeFn(new ItalicFn());
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of static Font class"
            );
        }

        @Override public String getClassName() { return "Font"; }
        @Override public String getType() { return "_Font_"; }
        @Override public String toString() { return "<class:Font>"; }
    }
}