package ysharp.treewalk.evaluator.Native.TUI.Util.TextColor;

import com.googlecode.lanterna.TextColor;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class yTextColor {

    public static yTextColorEnum requireYTextColorEnum(Variable.Variant v,
                                                       String fn,
                                                       int index) throws YsharpException {

        if (!v.isRuntimeObject()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a object."
            );
        }

        RuntimeObject obj = v.asRuntimeObject();

        if(!(obj instanceof yTextColorEnum)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a TextColor object."
            );
        }

        return (yTextColorEnum) obj;
    }

    private static yTextColorEnum requireTextColorThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTextColorEnum)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'TextColor' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yTextColorEnum) obj;
    }

    public static class yTextColorEnum extends RuntimeObject {

        public TextColor color;

        public yTextColorEnum(TextColor color){
            this.color = color;
            this.prototype = yClass.ClassPrototype;

            // TextColor.toString()
            class ToStringFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    yTextColorEnum textColor = requireTextColorThis(interpreter, getFnName());

                    return new Variable.Variant(new yString.yStringInstance(textColor.color.toString()));
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
            this.set(toString.getFnName(), toStringVar);
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
            return "<instnace:TextColor>";
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
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of static TextColor class");
        }

        @Override
        public String getClassName() {
            return "TextColor";
        }

        @Override
        public String getType() {
            return "TextColor";
        }

        @Override
        public String toString() {
            return "<class:TextColor>";
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