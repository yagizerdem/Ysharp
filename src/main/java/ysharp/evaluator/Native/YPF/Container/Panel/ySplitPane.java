package ysharp.evaluator.Native.YPF.Container.Panel;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import javax.swing.*;
import java.util.*;

public class ySplitPane {

    public static RuntimeObject ySplitPane_Instance_Prototype;

    static {
        ySplitPane_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__SplitPane__"; }
            @Override public String toString() { return "<prototype:SplitPane>"; }
        };

        ySplitPane_Instance_Prototype.prototype = yClass.ClassPrototype;
    }

    public static class ySplitPaneInstance extends yClass.ClassObjectInstance {

        public final JSplitPane splitPane;

        public ySplitPaneInstance() {
            this.splitPane = new JSplitPane();
            this.prototype = ySplitPane_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "SplitPane"; }

        @Override
        public Object getNativeJavaObject() {
            return this.splitPane;
        }
    }

    public static class ySplitPaneClass extends yClass.SealedClassObject {

        public ySplitPaneClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new ySplitPaneInstance());
        }

        @Override public String getClassName() { return "SplitPane"; }
        @Override public String getType() { return "SplitPane"; }
    }
}