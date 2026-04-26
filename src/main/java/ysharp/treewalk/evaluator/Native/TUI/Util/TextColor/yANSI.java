package ysharp.treewalk.evaluator.Native.TUI.Util.TextColor;

import com.googlecode.lanterna.TextColor;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;

import java.util.List;

public class yANSI {

    private static yANSIEnum requireANSIThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yANSIEnum)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'ANSI' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yANSIEnum) obj;
    }


    public static class yANSIEnum extends RuntimeObject {
        public TextColor.ANSI ansi;
        public yANSIEnum(TextColor.ANSI ansi){
            this.ansi = ansi;
            this.prototype = yClass.ClassPrototype;

            // ansi.toString()
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

                    yANSIEnum ansi = requireANSIThis(interpreter, getFnName());

                    return new Variable.Variant(new yString.yStringInstance(ansi.ansi.toString()));
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
            return "ANSI";
        }

        @Override
        public String toString() {
            return "<instnace:ANSI>";
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

            this.set("DEFAULT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.DEFAULT)),
                            true,
                            "function"));

            // bright

            this.set("BLACK_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.BLACK_BRIGHT)),
                            true,
                            "function"));

            this.set("RED_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.RED_BRIGHT)),
                            true,
                            "function"));

            this.set("GREEN_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.GREEN_BRIGHT)),
                            true,
                            "function"));

            this.set("YELLOW_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.YELLOW_BRIGHT)),
                            true,
                            "function"));

            this.set("BLUE_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.BLUE_BRIGHT)),
                            true,
                            "function"));

            this.set("MAGENTA_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.MAGENTA_BRIGHT)),
                            true,
                            "function"));

            this.set("CYAN_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.CYAN_BRIGHT)),
                            true,
                            "function"));

            this.set("WHITE_BRIGHT",
                    new Variable(new Variable.Variant(
                            new yTextColor.yTextColorEnum(TextColor.ANSI.WHITE_BRIGHT)),
                            true,
                            "function"));


            // TextColor.ANSI.valueOf(string) // Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.)
            class ValueOfFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
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
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
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
                throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
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

        @Override
        public String toString() {
            return "<class:ANSI>";
        }
    }

}
