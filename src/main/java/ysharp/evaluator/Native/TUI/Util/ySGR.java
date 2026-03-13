package ysharp.evaluator.Native.TUI.Util;

import com.googlecode.lanterna.SGR;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.yArray;
import ysharp.parser.TypeTag;

import java.util.List;

public class ySGR {

    public static ySGREnum requireYSRGEnum (Variable.Variant v,
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

        if(!(obj instanceof ySGREnum)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a SRG object."
            );
        }

        return (ySGREnum) obj;
    }

    public static class ySGREnum extends RuntimeObject {
        public SGR sgr;
        public ySGREnum(SGR sgr){
            this.sgr = sgr;
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "SGR";
        }

        @Override
        public String toString() {
            if(this.sgr == null) return  "null";
            return this.sgr.toString();
        }
    }

    public static class ySGRClass extends yClass.SealedClassObject {

        public ySGRClass(){
            this.prototype =  yClass.ClassPrototype;

            // enum constants
            Variable blinkVar = new Variable(new Variable.Variant(new ySGREnum(SGR.BLINK)), true, TypeTag.OBJECT);
            this.set("BLINK", blinkVar);

            Variable boldVar = new Variable(new Variable.Variant(new ySGREnum(SGR.BOLD)), true, TypeTag.OBJECT);
            this.set("BOLD", boldVar);

            Variable borderedVar = new Variable(new Variable.Variant(new ySGREnum(SGR.BORDERED)), true, TypeTag.OBJECT);
            this.set("BORDERED", borderedVar);

            Variable circledVar = new Variable(new Variable.Variant(new ySGREnum(SGR.CIRCLED)), true, TypeTag.OBJECT);
            this.set("CIRCLED", circledVar);

            Variable crossedOutVar = new Variable(new Variable.Variant(new ySGREnum(SGR.CROSSED_OUT)), true, TypeTag.OBJECT);
            this.set("CROSSED_OUT", crossedOutVar);

            Variable frakturVar = new Variable(new Variable.Variant(new ySGREnum(SGR.FRAKTUR)), true, TypeTag.OBJECT);
            this.set("FRAKTUR", frakturVar);

            Variable italicVar = new Variable(new Variable.Variant(new ySGREnum(SGR.ITALIC)), true, TypeTag.OBJECT);
            this.set("ITALIC", italicVar);

            Variable reverseVar = new Variable(new Variable.Variant(new ySGREnum(SGR.REVERSE)), true, TypeTag.OBJECT);
            this.set("REVERSE", reverseVar);

            Variable underlineVar = new Variable(new Variable.Variant(new ySGREnum(SGR.UNDERLINE)), true, TypeTag.OBJECT);
            this.set("UNDERLINE", underlineVar);

            // SGR.valueOf(string) // Returns the enum constant of this type with the specified name.
            class ValueOfFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());
                    String value = requireString(arguments.getFirst(), getClassName(), 1);
                    return new Variable.Variant(SGR.valueOf(value));
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
                    TypeTag.OBJECT);
            this.set(valueOf.getFnName(), valueOfVar);

            // SGR.values() Returns an array containing the constants of this enum type, in the order they are declared.
            class ValuesFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());
                    SGR[] arr = SGR.values();
                    yArray.yArrayInstance yArray = new yArray.yArrayInstance();
                    for(SGR sgr : arr) yArray.data.add(new Variable.Variant(new ySGREnum(sgr)));

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
                    TypeTag.OBJECT);
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
                    "cannot take instance of SGR class");
        }

        @Override
        public String getClassName() {
            return "SGR";
        }

        @Override
        public String getType() {
            return "SGR";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        ySGRClass ctor = new ySGRClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, true, TypeTag.OBJECT);
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}