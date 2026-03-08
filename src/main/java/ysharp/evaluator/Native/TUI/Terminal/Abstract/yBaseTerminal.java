package ysharp.evaluator.Native.TUI.Terminal.Abstract;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.evaluator.Native.TUI.Util.ySGR;
import ysharp.parser.TypeTag;

import java.io.IOException;
import java.util.List;

public class yBaseTerminal {

    // helper
    private static yAbstractTerminal.AbstractTerminal requireTerminalThis (Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Terminal method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yAbstractTerminal.AbstractTerminal)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Terminal instances."
            );
        }

        return (yAbstractTerminal.AbstractTerminal) obj;
    }

    public static RuntimeObject yBaseTerminal_Instance_Prototype;

    static {
        yBaseTerminal_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Terminal__";
            }
        };
        yBaseTerminal_Instance_Prototype.prototype = Y_Class.ClassPrototype;


        // terminal.putCharacter(char)
        class PutCharacterFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                char c = requireChar(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.putCharacter(c);
                    if(terminal.autoFlush) terminal.instance.flush();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to write character to terminal."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "putCharacter";
            }
        }

        PutCharacterFn putCharacter = new PutCharacterFn();
        Variable putCharacterVar = new Variable(
                new Variable.Variant(putCharacter),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(putCharacter.getFnName(), putCharacterVar);


        // terminal.clearScreen()
        class ClearScreenFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.clearScreen();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to clear screen."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clearScreen";
            }
        }

        ClearScreenFn clearScreen = new ClearScreenFn();
        Variable clearScreenVar = new Variable(
                new Variable.Variant(clearScreen),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(clearScreen.getFnName(), clearScreenVar);


        // terminal.flush()
        class FlushFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.flush();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to flush."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "flush";
            }
        }

        FlushFn flush = new FlushFn();
        Variable flushVar = new Variable(
                new Variable.Variant(flush),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(flush.getFnName(), flushVar);

        // terminal.close()
        class CloseFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.close();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to close terminal."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "close";
            }
        }

        CloseFn close = new CloseFn();
        Variable closeVar = new Variable(
                new Variable.Variant(close),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(close.getFnName(), closeVar);

        // terminal.bell()
        class BellFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.bell();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to ring terminal bell."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "bell";
            }
        }

        BellFn bell = new BellFn();
        Variable bellVar = new Variable(
                new Variable.Variant(bell),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(bell.getFnName(), bellVar);


        // terminal.enterPrivateMode()
        class EnterPrivateModeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.enterPrivateMode();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to enter terminal private mode."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "enterPrivateMode";
            }
        }

        EnterPrivateModeFn enterPrivateMode = new EnterPrivateModeFn();
        Variable enterPrivateModeVar = new Variable(
                new Variable.Variant(enterPrivateMode),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(enterPrivateMode.getFnName(), enterPrivateModeVar);


        // terminal.exitPrivateMode()
        class ExitPrivateModeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.exitPrivateMode();
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to exit terminal private mode."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "exitPrivateMode";
            }
        }

        ExitPrivateModeFn exitPrivateMode = new ExitPrivateModeFn();
        Variable exitPrivateModeVar = new Variable(
                new Variable.Variant(exitPrivateMode),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(exitPrivateMode.getFnName(), exitPrivateModeVar);

        // terminal.setCursorPosition(x, y)
        class SetCursorPositionFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                int x = requireInt(arguments.get(0), getFnName(), 1);
                int y = requireInt(arguments.get(1), getFnName(), 2);

                try {
                    terminal.instance.setCursorPosition(x, y);
                }
                catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Failed to set cursor position."
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setCursorPosition";
            }
        }

        SetCursorPositionFn setCursorPosition = new SetCursorPositionFn();
        Variable setCursorPositionVar = new Variable(
                new Variable.Variant(setCursorPosition),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(setCursorPosition.getFnName(), setCursorPositionVar);


        // terminal.disableSgr(SGR) Deactivates an SGR (Selected Graphic Rendition) code which has previously been activated through enableSGR(..).
        class DisableSgrFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                ySGR.ySGREnum srgEnum = ySGR.requireYSRGEnum(arguments.getFirst(),
                        "disableSgr",
                        1);

                try {
                    terminal.instance.disableSGR(srgEnum.sgr);
                }catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.disableSGR: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "disableSgr";
            }
        }

        DisableSgrFn disableSgr = new DisableSgrFn();
        Variable disableSgrVar = new Variable(
                new Variable.Variant(disableSgr),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(disableSgr.getFnName(), disableSgrVar);

        // terminal.enableSgr(SRG)  Activates an SGR (Selected Graphic Rendition) code.
        class EnableSgrFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                ySGR.ySGREnum srgEnum = ySGR.requireYSRGEnum(arguments.getFirst(),
                        "enableSgr",
                        1);

                try {
                    terminal.instance.enableSGR(srgEnum.sgr);
                } catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.enableSGR: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "enableSgr";
            }
        }

        EnableSgrFn enableSgr = new EnableSgrFn();
        Variable enableSgrVar = new Variable(
                new Variable.Variant(enableSgr),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(enableSgr.getFnName(), enableSgrVar);

        // terminal.resetColorAndSGR()
        class ResetColorAndSrgFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.resetColorAndSGR();
                } catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.resetColorAndSGR: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "resetColorAndSGR";
            }
        }

        ResetColorAndSrgFn resetColorAndSGR = new ResetColorAndSrgFn();
        Variable resetColorAndSGRVar = new Variable(
                new Variable.Variant(resetColorAndSGR),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(resetColorAndSGR.getFnName(), resetColorAndSGRVar);

        // terminal.setBackgroundColor(TextColor)
        class SetBackgroundColorFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                yTextColor.yTextColorEnum textColor = yTextColor.requireYTextColorEnum(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setBackgroundColor(textColor.color);
                } catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.setBackgroundColor: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setBackgroundColor";
            }
        }

        SetBackgroundColorFn setBackgroundColor = new SetBackgroundColorFn();
        Variable setBackgroundColorVar = new Variable(
                new Variable.Variant(setBackgroundColor),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(setBackgroundColor.getFnName(), setBackgroundColorVar);


        // terminal.setForegroundColor(TextColor)
        class SetForegroundColorFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                yTextColor.yTextColorEnum textColor = yTextColor.requireYTextColorEnum(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setForegroundColor(textColor.color);
                } catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.setForegroundColor: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setForegroundColor";
            }
        }

        SetForegroundColorFn setForegroundColor = new SetForegroundColorFn();
        Variable setForegroundColorVar = new Variable(
                new Variable.Variant(setForegroundColor),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(setForegroundColor.getFnName(), setForegroundColorVar);

        // terminal.setCursorVisible(bool)
        class SetCursorVisibleFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                boolean flag = requireBoolean(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setCursorVisible(flag);
                } catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.setCursorVisible: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setCursorVisible";
            }
        }

        SetCursorVisibleFn setCursorVisible = new SetCursorVisibleFn();
        Variable setCursorVisibleVar = new Variable(
                new Variable.Variant(setCursorVisible),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(setCursorVisible.getFnName(), setCursorVisibleVar);

        class WriteFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                String text = requireString(arguments.getFirst(), getFnName(), 1);

                try {
                    for(char c : text.toCharArray()) {
                        terminal.instance.putCharacter(c);
                    }
                    if(terminal.autoFlush) terminal.instance.flush();
                }
                catch(IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.write: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "write";
            }
        }


        WriteFn write = new WriteFn();
        Variable writeVar = new Variable(
                new Variable.Variant(write),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(write.getFnName(), writeVar);

        class WriteLineFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                String text = requireString(arguments.getFirst(), getFnName(), 1);

                try {
                    for(char c : text.toCharArray()) {
                        terminal.instance.putCharacter(c);
                    }

                    terminal.instance.putCharacter('\n');
                    if(terminal.autoFlush) terminal.instance.flush();
                }
                catch(IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.writeLine: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "writeLine";
            }
        }

        WriteLineFn writeLine = new WriteLineFn();
        Variable writeLineVar = new Variable(
                new Variable.Variant(writeLine),
                true,
                TypeTag.OBJECT);
        yBaseTerminal_Instance_Prototype.set(writeLine.getFnName(), writeLineVar);

        Variable autoFlushVar = new Variable(new Variable.Variant(false), false, TypeTag.BOOL);
        yBaseTerminal_Instance_Prototype.set("autoFlush", autoFlushVar);
    }

}
