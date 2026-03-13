package ysharp.evaluator.Native.TUI;

import ysharp.YsharpError;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.TUI.Input.yKeyStroke;
import ysharp.evaluator.Native.TUI.Terminal.yDefaultTerminal;
import ysharp.evaluator.Native.TUI.Terminal.yScrollSwingTerminal;
import ysharp.evaluator.Native.TUI.Terminal.ySwingTerminal;
import ysharp.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.evaluator.Native.TUI.Util.ySGR;
import ysharp.evaluator.Variable;
import ysharp.evaluator.yClass;
import ysharp.parser.TypeTag;
import java.util.List;

public class TUI {

    public static class TUIClass extends yClass.SealedClassObject {

        TUIClass(){
            this.prototype =  yClass.ClassPrototype;

            // terminal types
            yDefaultTerminal.yDefaultTerminalClass defaultTerminal = new yDefaultTerminal.yDefaultTerminalClass();
            Variable defaultTerminalVar = new Variable(
                    new Variable.Variant(defaultTerminal),
                    true,
                    TypeTag.OBJECT
            );
            this.set(defaultTerminal.getClassName(), defaultTerminalVar);

            ySwingTerminal.ySwingTerminalClass swingTerminal = new ySwingTerminal.ySwingTerminalClass();
            Variable swingTerminalVar = new Variable(
                    new Variable.Variant(swingTerminal),
                    true,
                    TypeTag.OBJECT
            );
            this.set(swingTerminal.getClassName(), swingTerminalVar);

            yScrollSwingTerminal.yScrollSwingTerminalClass scrollSwingTerminal = new yScrollSwingTerminal.yScrollSwingTerminalClass();
            Variable scrollSwingTerminalVar = new Variable(
                    new Variable.Variant(swingTerminal),
                    true,
                    TypeTag.OBJECT
            );
            this.set(scrollSwingTerminal.getClassName(), scrollSwingTerminalVar);

            // fonts
            ySGR.ySGRClass sgr = new ySGR.ySGRClass();
            Variable sgrVar = new Variable(
                    new Variable.Variant(sgr),
                    true,
                    TypeTag.OBJECT
            );
            this.set(sgr.getClassName(), sgrVar);

            // foreground background color
            yTextColor.yTextColorClass textColor = new yTextColor.yTextColorClass();
            Variable textColorVar = new Variable(
                    new Variable.Variant(textColor),
                    true,
                    TypeTag.OBJECT
            );
            this.set(textColor.getClassName(), textColorVar);

            // key stroke
            yKeyStroke.yKeyStrokeClass keyStroke = new yKeyStroke.yKeyStrokeClass();
            Variable yKeyStrokeVar = new Variable(
                    new Variable.Variant(keyStroke),
                    true,
                    TypeTag.OBJECT
            );
            this.set(keyStroke.getClassName(), yKeyStrokeVar);

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
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
                TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
