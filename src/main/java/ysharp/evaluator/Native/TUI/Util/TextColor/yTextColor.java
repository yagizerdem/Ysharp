package ysharp.evaluator.Native.TUI.Util.TextColor;

import com.googlecode.lanterna.TextColor;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.List;

public class yTextColor {

    public static yTextColorEnum requireYTextColorEnum(Variable.Variant v,
                                                       String fn,
                                                       int index) throws YsharpError {

        if (!v.isRuntimeObject()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a object."
            );
        }

        RuntimeObject obj = v.asRuntimeObject();

        if(!(obj instanceof yTextColorEnum)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a TextColor object."
            );
        }

        return (yTextColorEnum) obj;
    }

    public static class yTextColorEnum extends RuntimeObject {

        public TextColor color;

        public yTextColorEnum(TextColor color){
            this.color = color;
            this.prototype = Y_Class.ClassPrototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "TextColor";
        }

        @Override
        public String toString() {
            if(color == null) return "null";
            return color.toString();
        }
    }

    public static class yTextColorClass extends Y_Class.SealedClassObject {

        yTextColorClass(){

            this.prototype = Y_Class.ClassPrototype;

            // ANSI COLORS

            this.set("BLACK",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.BLACK)),
                            true,
                            TypeTag.OBJECT));

            this.set("RED",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.RED)),
                            true,
                            TypeTag.OBJECT));

            this.set("GREEN",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.GREEN)),
                            true,
                            TypeTag.OBJECT));

            this.set("YELLOW",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.YELLOW)),
                            true,
                            TypeTag.OBJECT));

            this.set("BLUE",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.BLUE)),
                            true,
                            TypeTag.OBJECT));

            this.set("MAGENTA",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.MAGENTA)),
                            true,
                            TypeTag.OBJECT));

            this.set("CYAN",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.CYAN)),
                            true,
                            TypeTag.OBJECT));

            this.set("WHITE",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.WHITE)),
                            true,
                            TypeTag.OBJECT));

            // bright

            this.set("BRIGHT_RED",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.RED_BRIGHT)),
                            true,
                            TypeTag.OBJECT));

            this.set("BRIGHT_GREEN",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.GREEN_BRIGHT)),
                            true,
                            TypeTag.OBJECT));

            this.set("BRIGHT_BLUE",
                    new Variable(new Variable.Variant(
                            new yTextColorEnum(TextColor.ANSI.BLUE_BRIGHT)),
                            true,
                            TypeTag.OBJECT));



        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of TextColor class");
        }

        @Override
        public String getClassName() {
            return "TextColor";
        }

        @Override
        public String getType() {
            return "TextColor";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yTextColorClass ctor = new yTextColorClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                TypeTag.OBJECT
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}