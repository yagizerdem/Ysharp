package ysharp.evaluator.Native.TUI.Util.TextColor;

import com.googlecode.lanterna.TextColor;
import ysharp.YsharpError;
import ysharp.evaluator.*;

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
            this.prototype = yClass.ClassPrototype;
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

    public static class yTextColorClass extends yClass.SealedClassObject {

        public yTextColorClass(){

            this.prototype = yClass.ClassPrototype;

            // ANSI COLORS
            yANSI.yANSIClass ANSI = new yANSI.yANSIClass();
            this.set(ANSI.getClassName(), new Variable(new Variable.Variant(ANSI), true, "function"));

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
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}