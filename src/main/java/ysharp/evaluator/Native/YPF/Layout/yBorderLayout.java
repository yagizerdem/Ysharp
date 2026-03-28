package ysharp.evaluator.Native.YPF.Layout;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;

import java.awt.BorderLayout;
import java.util.List;

public class yBorderLayout {

    public static RuntimeObject yBorderLayout_Instance_Prototype;

    static {
        yBorderLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__BorderLayout__"; }
            @Override public String toString() { return "<prototype:BorderLayout>"; }
        };

        yBorderLayout_Instance_Prototype.prototype = yClass.ClassPrototype;


    }

    public static class yBorderLayoutInstance extends yClass.ClassObjectInstance {

        public final BorderLayout layout;

        public yBorderLayoutInstance() {
            this.layout = new BorderLayout();
            this.prototype = yBorderLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "BorderLayout"; }
        @Override public String toString() { return "<instance:BorderLayout>"; }
        @Override
        public Object getNativeJavaObject() { return this.layout;}
    }

    public static class yBorderLayoutClass extends yClass.SealedClassObject {

        public yBorderLayoutClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("NORTH", new Variable(new Variable.Variant(BorderLayout.NORTH), true, "string"));
            this.set("SOUTH", new Variable(new Variable.Variant(BorderLayout.SOUTH), true, "string"));
            this.set("EAST", new Variable(new Variable.Variant(BorderLayout.EAST), true, "string"));
            this.set("WEST", new Variable(new Variable.Variant(BorderLayout.WEST), true, "string"));
            this.set("CENTER", new Variable(new Variable.Variant(BorderLayout.CENTER), true, "string"));
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yBorderLayoutInstance());
        }

        @Override public String getClassName() { return "BorderLayout"; }
        @Override public String getType() { return "BorderLayout"; }
    }
}