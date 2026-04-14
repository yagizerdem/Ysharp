package ysharp.treewalk.evaluator.Native.Assert;

import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Assert.function.statix.*;

import java.util.List;

public class yAssert {

    public static RuntimeObject yAssert_Instance_Prototype;

    static {
        yAssert_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Assert__";
            }

            @Override
            public String toString() {
                return "<prototype:Assert>";
            }
        };
    }

    public static class yAssertInstance extends yClass.ClassObjectInstance {

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Assert";
        }

        @Override
        public String toString() {
            return "<instance:Assert>";
        }
    }

    public static class yAssertClass extends yClass.SealedClassObject {

        public yAssertClass() {
            this.prototype = yClass.ClassPrototype;

            // static assert functions
            this.RegisterNativeFn(new EqualsFn());
            this.RegisterNativeFn(new TrueFn());
            this.RegisterNativeFn(new FalseFn());
            this.RegisterNativeFn(new NotEqualsFn());
            this.RegisterNativeFn(new NullFn());
            this.RegisterNativeFn(new NotNullFn());
            this.RegisterNativeFn(new FailFn());
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) {
            return new Variable.Variant(new yAssertInstance());
        }

        @Override
        public String getClassName() {
            return "Assert";
        }

        @Override
        public String getType() {
            return "Assert";
        }

        @Override
        public String toString() {
            return "<class:Assert>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yAssertClass assertClass = new yAssertClass();
        Variable.Variant variant = new Variable.Variant(assertClass);
        Variable var = new Variable(variant, true, assertClass.getType());
        interpreter.defineGlobal(assertClass.getClassName(), var);
    }
}