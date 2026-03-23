package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Token;

import java.util.List;

public class yClass {

    static private interface IBasePrototype {
         RuntimeObject getPrototype();
    }

    static private RuntimeObject requireThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");
        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }
        return thisVar.value.asRuntimeObject();
    }

    public static RuntimeObject ClassPrototype;

    static {
        ClassPrototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__RootPrototype__";
            }

            @Override
            public String toString() {
                return "<prototype:root>";
            }
        };
        // this is root prototype of the object prototype chain ,
        // there is also function types extended from object but function does not need prototype chain so  that closed for default
        ClassPrototype.prototype =  null;

        class GetTypeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                return new Variable.Variant(requireThis(interpreter).getType());
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
            ClassPrototype.set(getType.getFnName(), getTypeVar);

        class GetPrototypeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                return new Variable.Variant(requireThis(interpreter).getPrototype());
            }

            @Override
            public String getFnName() {
                return "getPrototype";
            }

        }

        GetPrototypeFn getPrototype = new GetPrototypeFn();
        Variable getPrototypeVar = new Variable(
                new Variable.Variant(getPrototype),
                true,
                "function");
        ClassPrototype.set(getPrototype.getFnName(), getPrototypeVar);

    }

    static abstract public class ClassObject extends RuntimeObject implements Callable, IBasePrototype {

        public Environment closure;

        public abstract boolean isSealed();

        public abstract String getClassName();

        @Override
        public RuntimeObject getPrototype() {
            return this.prototype;
        }

        public Token superClassName;

        public RuntimeObject InstancePrototype; // holds instance methods

        public Function.NativeFunction constructor =  null;

        @Override
        public boolean isTruthy() {
            return true;
        }
    }

    static abstract public class SealedClassObject extends ClassObject implements IBasePrototype {
        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public RuntimeObject getPrototype() {
            return super.getPrototype();
        }
    }

    static abstract public class ClassObjectInstance extends RuntimeObject implements IBasePrototype{
        @Override
        public RuntimeObject getPrototype() {
            return this.prototype;
        }
    }

}
