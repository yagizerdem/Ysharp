package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.parser.TypeTag;

import java.util.List;

public class Y_Class {

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

        class FooFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                return new Variable.Variant("foo");
            }

            @Override
            public String getFnName() {
                return "foo";
            }


        }

        FooFn foo = new FooFn();
        Variable fooVar = new Variable(
                new Variable.Variant(foo),
                true,
                TypeTag.OBJECT);
            ClassPrototype.set(foo.getFnName(), fooVar);

    }

    static abstract public class ClassObject extends RuntimeObject implements Callable {

        public abstract boolean isSealed();

        public abstract String getClassName();

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
