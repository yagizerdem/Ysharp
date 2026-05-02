package ysharp.treewalk.evaluator.Native.P5ys;

import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.P5ys.function.statix.CreateContextFn;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

public class yCanvas {

    public static RuntimeObject yCanvas_Instance_Prototype;

    static {
        yCanvas_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Canvas__";
            }

            @Override
            public String toString() {
                return "<prototype:Canvas>";
            }
        };
        yCanvas_Instance_Prototype.prototype = yClass.ClassPrototype;

    }

    public static class yCanvasInstance extends yClass.ClassObjectInstance {

        public yCanvasInstance() {
            this.prototype = yCanvas_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Canvas";
        }

        @Override
        public String toString() {
            return "<instance:Canvas>";
        }
    }

    public static class yCanvasClass extends yClass.SealedClassObject {

        public yCanvasClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(ysharp.treewalk.evaluator.Interpreter interpreter,
                                     java.util.List<Variable.Variant> arguments) {
            return new Variable.Variant(new yCanvasInstance());
        }

        @Override
        public String getClassName() {
            return "Canvas";
        }

        @Override
        public String getType() {
            return "_Canvas_";
        }

        @Override
        public String toString() {
            return "<class:Canvas>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yCanvas.yCanvasClass ctor = new yCanvas.yCanvasClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}