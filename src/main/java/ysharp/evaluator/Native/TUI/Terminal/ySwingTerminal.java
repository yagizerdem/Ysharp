package ysharp.evaluator.Native.TUI.Terminal;

import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import ysharp.YsharpError;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.TUI.Terminal.Abstract.yAbstractTerminal;
import ysharp.evaluator.Native.TUI.Terminal.Abstract.yBaseTerminal;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;
import ysharp.evaluator.yClass;
import ysharp.parser.TypeTag;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ySwingTerminal {

    public static RuntimeObject ySwingTerminal_Instance_Prototype;

    static {
        ySwingTerminal_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__YSPFTerminal__";
            }
        };
        ySwingTerminal_Instance_Prototype.prototype = yBaseTerminal.yBaseTerminal_Instance_Prototype;
    }

    public static class ySwingTerminalInstance extends  yAbstractTerminal.AbstractSwingTerminal {

        public ySwingTerminalInstance() {
            this.prototype = ySwingTerminal_Instance_Prototype;

            Variable autoFlushVar = new Variable(new Variable.Variant(false), false, TypeTag.BOOL);
            this.set("autoFlush", autoFlushVar);
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "YSPFTerminal";
        }

        @Override
        public String toString() {
            return "<instance:YSPFTerminal>";
        }
    }

    public static class ySwingTerminalClass extends yClass.SealedClassObject {

        ySwingTerminalClass(){
            this.prototype =  yClass.ClassPrototype;
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            requireArity(arguments,0, getClassName());

            ySwingTerminalInstance instance = new ySwingTerminalInstance();

            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "YSPFTerminal";
        }

        @Override
        public String getType() {
            return "YSPFTerminal";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        ySwingTerminalClass ctor = new ySwingTerminalClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
