package ysharp.evaluator.Native.TUI.Terminal;

import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import ysharp.YsharpError;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.TUI.Terminal.Abstract.yAbstractTerminal;
import ysharp.evaluator.Native.TUI.Terminal.Abstract.yBaseTerminal;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;
import ysharp.evaluator.yClass;
import ysharp.parser.TypeTag;

import java.util.List;

public class yScrollSwingTerminal {

    public static RuntimeObject yScrollSwingTerminal_Instance_Prototype;

    static {
        yScrollSwingTerminal_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__YSPFScrollTerminal__";
            }
        };
        yScrollSwingTerminal_Instance_Prototype.prototype = yBaseTerminal.yBaseTerminal_Instance_Prototype;
    }

    public static class yScrollSwingTerminalInstance extends yAbstractTerminal.AbstractSwingTerminal {

        public yScrollSwingTerminalInstance() {
            this.prototype = yScrollSwingTerminal_Instance_Prototype;
            this.instance = new ScrollingSwingTerminal();


            Variable autoFlushVar = new Variable(new Variable.Variant(false), false, TypeTag.BOOL);
            this.set("autoFlush", autoFlushVar);
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "YSPFScrollTerminal";
        }

        @Override
        public String toString() {
            return "<instance:YSPFScrollTerminal>";
        }
    }

    public static class yScrollSwingTerminalClass extends yClass.SealedClassObject {

        yScrollSwingTerminalClass(){
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

            yScrollSwingTerminalInstance instance = new yScrollSwingTerminalInstance();

            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "YSPFScrollTerminal";
        }

        @Override
        public String getType() {
            return "YSPFScrollTerminal";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yScrollSwingTerminalClass ctor = new yScrollSwingTerminalClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(
                variant,
                true,
                TypeTag.OBJECT
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}