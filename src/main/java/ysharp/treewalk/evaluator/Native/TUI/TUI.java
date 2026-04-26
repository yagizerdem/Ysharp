package ysharp.treewalk.evaluator.Native.TUI;

import com.googlecode.lanterna.TextColor;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.TUI.Input.yKeyStroke;
import ysharp.treewalk.evaluator.Native.TUI.Terminal.yDefaultTerminal;
import ysharp.treewalk.evaluator.Native.TUI.Terminal.ySwingTerminal;
import ysharp.treewalk.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.treewalk.evaluator.Native.TUI.Util.ySGR;
import ysharp.treewalk.evaluator.Native.TUI.Util.ySimpleTerminalResizeListener;
import ysharp.treewalk.evaluator.Native.TUI.Util.yTerminalSize;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import java.util.List;

public class TUI {

    public static class TUIClass extends yClass.SealedClassObject {

        TUIClass(){
            this.prototype =  yClass.ClassPrototype;

            // terminal types

            // default terminal
            RegisterClass(new yDefaultTerminal.yDefaultTerminalClass());

            // YPFTerminal
            RegisterClass(new ySwingTerminal.ySwingTerminalClass());

            //

            // fonts
            RegisterClass(new ySGR.ySGRClass());

            // foreground background color
            RegisterClass(new yTextColor.yTextColorClass());

            // key stroke
            RegisterClass(new yKeyStroke.yKeyStrokeClass());

            // terminal size
            RegisterClass(new yTerminalSize.yTerminalSizeClass());

            // terminal resize listener
            RegisterClass(new ySimpleTerminalResizeListener.ySimpleTerminalResizeListenerClass());

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                    -1 ,
                    "cannot take instance of TUI class");

        }

        @Override
        public String getClassName() {
            return "TUI";
        }

        @Override
        public String getType() {
            return "TUI";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        TUIClass ctor = new TUIClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
