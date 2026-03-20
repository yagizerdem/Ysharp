package ysharp.evaluator.Native.Util.Type;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.List;

public class Converter {

    public static class ConverterClass extends yClass.SealedClassObject {

        public ConverterClass(){
            this.prototype =  yClass.ClassPrototype;

            // Type.Converter.toString(data);
            class ToStringFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant variant = arguments.getFirst();
                    if(variant.isInt())
                        return new Variable.Variant(new yString.yStringInstance(variant.asInt().toString()));
                    else if(variant.isDouble())
                        return new Variable.Variant(new yString.yStringInstance(variant.asDouble().toString()));
                    else if(variant.isString())
                        return new Variable.Variant(new yString.yStringInstance(variant.asString()));
                    else if(variant.isBoolean())
                        return new Variable.Variant(new yString.yStringInstance(variant.asBoolean().toString()));
                    else if(variant.isNull())
                        return new Variable.Variant(new yString.yStringInstance("null"));
                    else if(variant.isChar())
                        return new Variable.Variant(new yString.yStringInstance(variant.asCharacter().toString()));
                    else if(variant.isFunctionLike())
                        return new Variable.Variant(new yString.yStringInstance("function"));
                    else if(variant.isClass())
                        return new Variable.Variant(new yString.yStringInstance(variant.asClass().toString()));
                    else if(variant.isClassInstance())
                        return new Variable.Variant(new yString.yStringInstance(variant.asRuntimeObject().toString()));

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
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

            // Type.Converter.toInt(data);
            class ToIntFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant variant = arguments.getFirst();
                    if(variant.isInt())
                        return new Variable.Variant((int) variant.asInt());
                    else if(variant.isDouble())
                        return new Variable.Variant(Math.round(variant.asDouble()));
                    else if(variant.isChar())
                        return new Variable.Variant((int)variant.asCharacter());
                    else if(variant.isString())
                        return new Variable.Variant(Integer.valueOf(variant.asString()));
                    else if(variant.isBoolean())
                        return new Variable.Variant(variant.asBoolean() ? 1 : 0);

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
                }

                @Override
                public String getFnName() {
                    return "toInt";
                }
            }

            ToIntFn toInt = new ToIntFn();
            Variable toIntVar = new Variable(
                    new Variable.Variant(toInt),
                    true,
                    "function");
            this.set(toInt.getFnName(), toIntVar);

            // Type.Converter.toDouble(data);
            class ToDoubleFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant variant = arguments.getFirst();

                    if(variant.isDouble())
                        return new Variable.Variant(variant.asDouble());
                    else if(variant.isInt())
                        return new Variable.Variant((double) variant.asInt());
                    else if(variant.isChar())
                        return new Variable.Variant((double) variant.asCharacter());
                    else if(variant.isBoolean())
                        return new Variable.Variant(variant.asBoolean() ? 1.0 : 0.0);
                    else if(variant.isString())
                        return new Variable.Variant(Double.valueOf(variant.asString()));

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
                }

                @Override
                public String getFnName() {
                    return "toDouble";
                }
            }

            ToDoubleFn toDouble = new ToDoubleFn();
            Variable toDoubleVar = new Variable(
                    new Variable.Variant(toDouble),
                    true,
                    "function");
            this.set(toDouble.getFnName(), toDoubleVar);


            // Type.Converter.toChar(data);
            class ToCharFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant variant = arguments.getFirst();

                    if(variant.isChar())
                        return new Variable.Variant(variant.asCharacter());
                    else if(variant.isInt())
                        return new Variable.Variant((char) variant.asInt().intValue());
                    else if(variant.isString()) {
                        String s = variant.asString();
                        if(s.length() == 1)
                            return new Variable.Variant(s.charAt(0));

                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "String must contain exactly one character for " + getFnName() + "().");
                    }

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
                }

                @Override
                public String getFnName() {
                    return "toChar";
                }
            }

            ToCharFn toChar = new ToCharFn();
            Variable toCharVar = new Variable(
                    new Variable.Variant(toChar),
                    true,
                    "function");
            this.set(toChar.getFnName(), toCharVar);


            // Type.Converter.toBool(data);
            class ToBoolFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant variant = arguments.getFirst();

                    if(variant.isBoolean())
                        return new Variable.Variant(variant.asBoolean());
                    else if(variant.isInt())
                        return new Variable.Variant(variant.asInt() != 0);
                    else if(variant.isDouble())
                        return new Variable.Variant(variant.asDouble() != 0.0);
                    else if(variant.isString())
                        return new Variable.Variant(!variant.asString().isEmpty());
                    else if(variant.isNull())
                        return new Variable.Variant(false);

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Unsupported type '" + variant.getType() + "' passed to " + getFnName() + "().");
                }

                @Override
                public String getFnName() {
                    return "toBool";
                }
            }

            ToBoolFn toBool = new ToBoolFn();
            Variable toBoolVar = new Variable(
                    new Variable.Variant(toBool),
                    true,
                    "function");
            this.set(toBool.getFnName(), toBoolVar);
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
                    "Cannot create instance of static class '" + getClassName() + "'."
            );
        }

        @Override
        public String getClassName() {
            return "Converter";
        }

        @Override
        public String getType() {
            return "Converter";
        }

        @Override
        public String toString() {
            return "<class:Converter>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        ConverterClass ctor = new ConverterClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                ctor.getType());

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
