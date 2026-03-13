package ysharp.evaluator.Native.TUI.Util.TextColor;

import com.googlecode.lanterna.TextColor;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.yArray;

import java.util.List;

public class yANSI {

    public static yANSIEnum requireANSIEnum (Variable.Variant v,
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

        if(!(obj instanceof yANSIEnum)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a ANSI object."
            );
        }

        return (yANSIEnum) obj;
    }

    public static class yANSIEnum extends RuntimeObject {
        public TextColor.ANSI ansi;
        public yANSIEnum(TextColor.ANSI ansi){
            this.ansi = ansi;
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "ANSI";
        }

        @Override
        public String toString() {
            if(this.ansi == null) return  "null";
            return this.ansi.toString();
        }
    }

    public static class yANSIClass extends yClass.SealedClassObject {

        public yANSIClass() {
            this.prototype =  yClass.ClassPrototype;

            this.set("BLACK",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.BLACK)),
                            true,
                            "function"));

            this.set("RED",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.RED)),
                            true,
                            "function"));

            this.set("GREEN",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.GREEN)),
                            true,
                            "function"));

            this.set("YELLOW",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.YELLOW)),
                            true,
                            "function"));

            this.set("BLUE",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.BLUE)),
                            true,
                            "function"));

            this.set("MAGENTA",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.MAGENTA)),
                            true,
                            "function"));

            this.set("CYAN",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.CYAN)),
                            true,
                            "function"));

            this.set("WHITE",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.WHITE)),
                            true,
                            "function"));

            // bright

            this.set("BRIGHT_RED",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.RED_BRIGHT)),
                            true,
                            "function"));

            this.set("BRIGHT_GREEN",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.GREEN_BRIGHT)),
                            true,
                            "function"));

            this.set("BRIGHT_BLUE",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.BLUE_BRIGHT)),
                            true,
                            "function"));


            // TextColor.ANSI.valueOf(string) // Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.)
            class ValueOfFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());
                    String value = requireString(arguments.getFirst(), getClassName(), 1);
                    return new Variable.Variant(new yTextColor.yTextColorEnum(TextColor.ANSI.valueOf(value)));
                }

                @Override
                public String getFnName() {
                    return "valueOf";
                }
            }

            ValueOfFn valueOf = new ValueOfFn();
            Variable valueOfVar = new Variable(
                    new Variable.Variant(valueOf),
                    true,
                    "function");
            this.set(valueOf.getFnName(), valueOfVar);


            // TextColor.ANSI.values() an array containing the constants of this enum type, in the order they are declared
            class ValuesFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());
                    TextColor.ANSI[] arr = TextColor.ANSI.values();
                    yArray.yArrayInstance yArray = new yArray.yArrayInstance();
                    for(TextColor.ANSI ansi : arr) yArray.data.add(new Variable.Variant(new yANSIEnum(ansi)));

                    return new Variable.Variant(yArray);
                }

                @Override
                public String getFnName() {
                    return "values";
                }
            }

            ValuesFn values = new ValuesFn();
            Variable valuesVar = new Variable(
                    new Variable.Variant(values),
                    true,
                    "function");
            this.set(values.getFnName(), valuesVar);
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of ANSI class");
        }

        @Override
        public String getClassName() {
            return "ANSI";
        }

        @Override
        public String getType() {
            return "ANSI";
        }
    }

}
