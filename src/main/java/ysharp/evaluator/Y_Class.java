package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Token;
import ysharp.parser.TypeTag;

import java.util.List;

public class Y_Class {

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
                return "__class_prototype__";
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
                TypeTag.OBJECT);
            ClassPrototype.set(getType.getFnName(), getTypeVar);

    }

    static abstract public class ClassObject extends RuntimeObject implements Callable {

        public abstract boolean isSealed();

        public abstract String getClassName();

        public Token superClassName;

        public RuntimeObject InstancePrototype;

        public Function.NativeFunction constructor =  null;

        @Override
        public boolean isTruthy() {
            return true;
        }
    }

    static abstract public class SealedClassObject extends ClassObject {
        @Override
        public boolean isSealed() {
            return true;
        }
    }

    static abstract public class ClassObjectInstance extends RuntimeObject { }

}
