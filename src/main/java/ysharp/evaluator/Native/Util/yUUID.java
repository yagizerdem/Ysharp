package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.List;

public class yUUID {

    public static RuntimeObject yUUID_Instance_Prototype;

    static {}


    public static class yUUID_Instance extends yClass.ClassObjectInstance {

        public yUUID_Instance() {}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "UUID";
        }

        @Override
        public String toString() {
            return "<instance:UUID>";
        }
    }


    public static class yUUIDClass extends yClass.SealedClassObject {

        yUUIDClass(){
            this.prototype =  yClass.ClassPrototype;

            // add static methods here
            // UUID.v4()
            class V4Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    String uuid = java.util.UUID.randomUUID().toString();

                    return new Variable.Variant(uuid);
                }

                @Override
                public String getFnName() {
                    return "v4";
                }
            }

            V4Fn v4 = new V4Fn();
            Variable v4Var = new Variable(
                    new Variable.Variant(v4),
                    true,
                    TypeTag.OBJECT
            );

            this.set(v4.getFnName(), v4Var);

            // UUID.nil()
            class NilFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    return new Variable.Variant("00000000-0000-0000-0000-000000000000");
                }

                @Override
                public String getFnName() {
                    return "nil";
                }
            }

            NilFn nil = new NilFn();
            Variable nilVar = new Variable(
                    new Variable.Variant(nil),
                    true,
                    TypeTag.OBJECT
            );

            this.set(nil.getFnName(), nilVar);


            // UUID.isValid(uuid: string)
            class IsValidFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String value = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        java.util.UUID.fromString(value);
                        return new Variable.Variant(true);
                    }
                    catch (Exception e) {
                        return new Variable.Variant(false);
                    }
                }

                @Override
                public String getFnName() {
                    return "isValid";
                }
            }

            IsValidFn isValid = new IsValidFn();
            Variable isValidVar = new Variable(
                    new Variable.Variant(isValid),
                    true,
                    TypeTag.OBJECT
            );

            this.set(isValid.getFnName(), isValidVar);

            // UUID.parse(uuid: string)
            class ParseFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String value = requireString(arguments.getFirst(), getClassName(), 1);

                    java.util.UUID uuid = java.util.UUID.fromString(value);

                    return new Variable.Variant(uuid.toString());
                }

                @Override
                public String getFnName() {
                    return "parse";
                }
            }

            ParseFn parse = new ParseFn();
            Variable parseVar = new Variable(
                    new Variable.Variant(parse),
                    true,
                    TypeTag.OBJECT
            );

            this.set(parse.getFnName(), parseVar);

        }

        @Override
        public int arity() {
            return 1; // expect callable for argument
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yUUID_Instance instance = new yUUID_Instance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "UUID";
        }

        @Override
        public String getType() {
            return "UUID";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yUUIDClass ctor = new yUUIDClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}
