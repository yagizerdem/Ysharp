package ysharp.evaluator.Native.Util.Type;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.List;

public class Type {

    public static class TypeClass extends yClass.SealedClassObject {

        private TypeClass(){
            this.prototype =  yClass.ClassPrototype;

            class GetTypeFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());

                    Variable.Variant variant = arguments.getFirst();

                    return new Variable.Variant(new yString.yStringInstance(variant.getType()));
                }

                @Override
                public String getFnName() {
                    return "getType";
                }
            }

            GetTypeFn getType = new GetTypeFn();
            Variable getTypeVar = new Variable(
                    new Variable.Variant(getType),
                    true,
                    "function");
            this.set(getType.getFnName(), getTypeVar);


            class GetTypeTagFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());

                    Variable.Variant variant = arguments.getFirst();

                    return new Variable.Variant(
                            new yString.yStringInstance(interpreter.curEnv.getType(variant)));
                }

                @Override
                public String getFnName() {
                    return "getTypeTag";
                }
            }

            GetTypeTagFn getTypeTag = new GetTypeTagFn();
            Variable getTypeTagVar = new Variable(
                    new Variable.Variant(getTypeTag),
                    true,
                    "function");
            this.set(getTypeTag.getFnName(), getTypeTagVar);

            class IsClassFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());

                    Variable.Variant variant = arguments.getFirst();

                    return new Variable.Variant(variant.isClass());
                }

                @Override
                public String getFnName() {
                    return "isClass";
                }
            }

            IsClassFn isClass = new IsClassFn();
            Variable isClassVar = new Variable(
                    new Variable.Variant(isClass),
                    true,
                    "function");
            this.set(isClass.getFnName(), isClassVar);

            class IsClassInstanceFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());

                    Variable.Variant variant = arguments.getFirst();

                    return new Variable.Variant(variant.isClassInstance());
                }

                @Override
                public String getFnName() {
                    return "isClassInstance";
                }
            }

            IsClassInstanceFn isClassInstance = new IsClassInstanceFn();
            Variable isClassInstanceVar = new Variable(
                    new Variable.Variant(isClassInstance),
                    true,
                    "function");
            this.set(isClassInstance.getFnName(), isClassInstanceVar);


            class IsFunctionFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                    requireArity(arguments, arity(), getClassName());

                    Variable.Variant variant = arguments.getFirst();

                    return new Variable.Variant(variant.isFunction());
                }

                @Override
                public String getFnName() {
                    return "isFunction";
                }
            }

            IsFunctionFn isFunction = new IsFunctionFn();
            Variable isFunctionVar = new Variable(
                    new Variable.Variant(isFunction),
                    true,
                    "function");
            this.set(isFunction.getFnName(), isFunctionVar);
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
            return "Type";
        }

        @Override
        public String getType() {
            return "Type";
        }

        @Override
        public String toString() {
            return "<class:Type>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        TypeClass ctor = new TypeClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                ctor.getType());

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
