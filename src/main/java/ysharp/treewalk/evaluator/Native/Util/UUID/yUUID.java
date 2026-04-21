package ysharp.treewalk.evaluator.Native.Util.UUID;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.UUID.function.statix.*;

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
            RegisterNativeFn(new V4Fn());
            // UUID.nil()
            RegisterNativeFn(new NilFn());
            // UUID.isValid(uuid: string)
            RegisterNativeFn(new IsValidFn());
            // UUID.parse(uuid: string)
            RegisterNativeFn(new ParseFn());

        }

        @Override
        public int arity() {
            return 1; // expect callable for argument
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            yUUID_Instance instance = new yUUID_Instance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "UUID";
        }

        @Override
        public String getType() {
            return "_UUID_";
        }

        @Override
        public String toString() {
            return "<class:UUID>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yUUIDClass ctor = new yUUIDClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}
