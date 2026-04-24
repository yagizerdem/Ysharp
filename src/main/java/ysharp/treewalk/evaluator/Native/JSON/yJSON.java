package ysharp.treewalk.evaluator.Native.JSON;

import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Assert.yAssert;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import java.util.List;

public class yJSON {

    public static class yJSONClass extends yClass.SealedClassObject {

        public yJSONClass() {
            this.prototype = yClass.ClassPrototype;


        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) {
            return new Variable.Variant(new yAssert.yAssertInstance());
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
        yJSON.yJSONClass ctor = new yJSON.yJSONClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, true, ctor.getType());
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
