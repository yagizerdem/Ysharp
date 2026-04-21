package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.lexer.Token;

import java.util.List;

public class yClass {

    static private interface IClass {
         RuntimeObject getPrototype();
    }

    static private RuntimeObject requireThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");
        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                return new Variable.Variant(new yString.yStringInstance(requireThis(interpreter).getType()));
            }

            @Override
            public String getFnName() {
                return "getType";
            }

        }

        ClassPrototype.RegisterNativeFn(new GetTypeFn());

        class GetPrototypeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                return new Variable.Variant(requireThis(interpreter).getPrototype());
            }

            @Override
            public String getFnName() {
                return "getPrototype";
            }

        }

        ClassPrototype.RegisterNativeFn(new GetPrototypeFn());

    }

    static abstract public class ClassObject extends RuntimeObject implements Callable, IClass {

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

    static abstract public class SealedClassObject extends ClassObject implements IClass {
        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public RuntimeObject getPrototype() {
            return super.getPrototype();
        }
    }

    static abstract public class ClassObjectInstance extends RuntimeObject implements IClass{
        @Override
        public RuntimeObject getPrototype() {
            return this.prototype;
        }
    }

}
