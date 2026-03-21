package ysharp.evaluator.Native.TUI.Terminal.Abstract;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.TUI.Input.yKeyStroke;
import ysharp.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.evaluator.Native.TUI.Util.ySGR;
import ysharp.evaluator.Native.TUI.Util.yTerminalSize;
import ysharp.evaluator.Native.TUI.Util.yTerminalResizeListener;

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

            @Override
            public String toString() {
                return "<prototype:Terminal>";
            }
        };
        yBaseTerminal_Instance_Prototype.prototype = yClass.ClassPrototype;


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
                    if(terminal.get("autoFlush").value.isTruthy()) terminal.instance.flush();
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
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
                "function");
        yBaseTerminal_Instance_Prototype.set(setCursorVisible.getFnName(), setCursorVisibleVar);

        // terminal.write(string | char)
        class WriteFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                String text ;

                if(arguments.getFirst().isDouble())
                    text = String.valueOf(arguments.getFirst().asDouble());
                if(arguments.getFirst().isInt())
                    text = String.valueOf(arguments.getFirst().asInt());
                else if(arguments.getFirst().isBoolean())
                    text = String.valueOf(arguments.getFirst().asBoolean());
                else if(arguments.getFirst().isNull())
                    text = "null";
                else if(arguments.getFirst().isRuntimeObject())
                    text = arguments.getFirst().toString();
                else
                    text =  requireStringOrChar(arguments.getFirst(), getFnName(), 1);

                try {
                    for(char c : text.toCharArray()) {
                        terminal.instance.putCharacter(c);
                    }
                    if(terminal.get("autoFlush").value.isTruthy())  terminal.instance.flush();
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
                "function");
        yBaseTerminal_Instance_Prototype.set(write.getFnName(), writeVar);

        // terminal.writeLine(string | char)
        class WriteLineFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                String text ;

                if(arguments.getFirst().isDouble())
                    text = String.valueOf(arguments.getFirst().asDouble());
                if(arguments.getFirst().isInt())
                    text = String.valueOf(arguments.getFirst().asInt());
                else if(arguments.getFirst().isBoolean())
                    text = String.valueOf(arguments.getFirst().asBoolean());
                else if(arguments.getFirst().isNull())
                    text = "null";
                else if(arguments.getFirst().isRuntimeObject())
                    text = arguments.getFirst().toString();
                else
                    text =  requireStringOrChar(arguments.getFirst(), getFnName(), 1);

                try {
                    for(char c : text.toCharArray()) {
                        terminal.instance.putCharacter(c);
                    }

                    terminal.instance.putCharacter('\n');
                    if(terminal.get("autoFlush").value.isTruthy())  terminal.instance.flush();
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
                "function");
        yBaseTerminal_Instance_Prototype.set(writeLine.getFnName(), writeLineVar);


        // terminal.readKey()
        class ReadKeyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    KeyStroke key = terminal.instance.readInput();
                    return new Variable.Variant(new yKeyStroke.yKeyStrokeInstance(key));

                }
                catch(IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.readKey: " + ex.getMessage()
                    );
                }
            }

            @Override
            public String getFnName() {
                return "readKey";
            }
        }

        ReadKeyFn readKey = new ReadKeyFn();
        Variable readKeyVar = new Variable(
                new Variable.Variant(readKey),
                true,
                "function");

        yBaseTerminal_Instance_Prototype.set(readKey.getFnName(), readKeyVar);


        // terminal.pollKey()
        class PollKeyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {

                    KeyStroke key = terminal.instance.pollInput();

                    if (key == null) {
                        return new Variable.Variant(null);
                    }

                    return new Variable.Variant(
                            new yKeyStroke.yKeyStrokeInstance(key)
                    );

                }
                catch(IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.pollKey: " + ex.getMessage()
                    );
                }
            }

            @Override
            public String getFnName() {
                return "pollKey";
            }
        }

        PollKeyFn pollKey = new PollKeyFn();
        Variable pollKeyVar = new Variable(
                new Variable.Variant(pollKey),
                true,
                "function");

        yBaseTerminal_Instance_Prototype.set(pollKey.getFnName(), pollKeyVar);

        // terminal.clearInputBuffer();
        class ClearInputBufferFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal =
                        requireTerminalThis(interpreter);

                try {

                    KeyStroke key;

                    while ((key = terminal.instance.pollInput()) != null) {
                        // discard
                    }

                }
                catch(IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.clearInputBuffer: " + ex.getMessage()
                    );
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clearInputBuffer";
            }
        }

        ClearInputBufferFn clearInputBuffer = new ClearInputBufferFn();
        Variable clearInputBufferVar = new Variable(
                new Variable.Variant(clearInputBuffer),
                true,
                "function");
        yBaseTerminal_Instance_Prototype.set(clearInputBuffer.getFnName(), clearInputBufferVar);


        // terminal.getTerminalSize();
        class GetTerminalSizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal =
                        requireTerminalThis(interpreter);

                try {
                    TerminalSize terminalSize = terminal.instance.getTerminalSize();
                    return new Variable.Variant(new yTerminalSize.yTerminalSizeInstance(terminalSize));
                }catch (IOException ex) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Terminal.getTerminalSize: " + ex.getMessage()
                    );
                }

            }

            @Override
            public String getFnName() {
                return "getTerminalSize";
            }
        }

        GetTerminalSizeFn getTerminalSize = new GetTerminalSizeFn();
        Variable getTerminalSizeVar = new Variable(
                new Variable.Variant(getTerminalSize),
                true,
                "function");

        yBaseTerminal_Instance_Prototype.set(getTerminalSize.getFnName(), getTerminalSizeVar);


        // terminal.addResizeListener();
        class AddResizeListenerFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal =
                        requireTerminalThis(interpreter);

                Variable.Variant listenerVariant = arguments.getFirst();

                if (!listenerVariant.isRuntimeObject()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Function '" + getFnName() + "' expects a 'TerminalResizeListener' object but got '"
                                    + listenerVariant.getType() + "'."
                    );
                }

                Object raw = listenerVariant.value;


                if (!(raw instanceof yTerminalResizeListener)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Function '" + getFnName() + "' expected 'TerminalResizeListener' but got '"
                                    + raw.getClass().getSimpleName() + "'."
                    );
                }

                TerminalResizeListener listener = ((yTerminalResizeListener) raw).getListener();

                terminal.instance.addResizeListener(listener);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "addResizeListener";
            }
        }
        AddResizeListenerFn addResizeListener = new AddResizeListenerFn();
        Variable addResizeListenerVar = new Variable(
                new Variable.Variant(addResizeListener),
                true,
                "function");

        yBaseTerminal_Instance_Prototype.set(addResizeListener.getFnName(), addResizeListenerVar);
    }

}
