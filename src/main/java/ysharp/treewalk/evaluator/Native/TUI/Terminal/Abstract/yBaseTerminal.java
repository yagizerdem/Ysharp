package ysharp.treewalk.evaluator.Native.TUI.Terminal.Abstract;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.TUI.Input.yKeyStroke;
import ysharp.treewalk.evaluator.Native.TUI.Util.TextColor.yTextColor;
import ysharp.treewalk.evaluator.Native.TUI.Util.ySGR;
import ysharp.treewalk.evaluator.Native.TUI.Util.yTerminalSize;
import ysharp.treewalk.evaluator.Native.TUI.Util.yTerminalResizeListener;

import java.io.IOException;
import java.util.List;

public class yBaseTerminal {

    // helper
    private static yAbstractTerminal.AbstractTerminal requireTerminalThis (Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Terminal method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yAbstractTerminal.AbstractTerminal)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
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
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                char c = requireChar(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.putCharacter(c);
                    if(terminal.get("autoFlush").value.isTruthy()) terminal.instance.flush();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new PutCharacterFn());

        // terminal.clearScreen()
        class ClearScreenFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.clearScreen();

                    if(terminal.get("autoFlush").value.isTruthy())  terminal.instance.flush();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new ClearScreenFn());

        // terminal.flush()
        class FlushFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.flush();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new FlushFn());

        // terminal.close()
        class CloseFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.close();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new CloseFn());

        // terminal.bell()
        class BellFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.bell();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new BellFn());

        // terminal.enterPrivateMode()
        class EnterPrivateModeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.enterPrivateMode();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new EnterPrivateModeFn());


        // terminal.exitPrivateMode()
        class ExitPrivateModeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.exitPrivateMode();
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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


        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new ExitPrivateModeFn());

        // terminal.setCursorPosition(x, y)
        class SetCursorPositionFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                int x = requireInt(arguments.get(0), getFnName(), 1);
                int y = requireInt(arguments.get(1), getFnName(), 2);

                try {
                    terminal.instance.setCursorPosition(x, y);
                }
                catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new SetCursorPositionFn());

        // terminal.disableSgr(SGR) Deactivates an SGR (Selected Graphic Rendition) code which has previously been activated through enableSGR(..).
        class DisableSgrFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                ySGR.ySGREnum srgEnum = ySGR.requireYSRGEnum(arguments.getFirst(),
                        "disableSgr",
                        1);

                try {
                    terminal.instance.disableSGR(srgEnum.sgr);
                }catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new DisableSgrFn());


        // terminal.enableSgr(SRG)  Activates an SGR (Selected Graphic Rendition) code.
        class EnableSgrFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                ySGR.ySGREnum srgEnum = ySGR.requireYSRGEnum(arguments.getFirst(),
                        "enableSgr",
                        1);

                try {
                    terminal.instance.enableSGR(srgEnum.sgr);
                } catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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


        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new EnableSgrFn());

        // terminal.resetColorAndSGR()
        class ResetColorAndSrgFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    terminal.instance.resetColorAndSGR();
                } catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new ResetColorAndSrgFn());

        // terminal.setBackgroundColor(TextColor)
        class SetBackgroundColorFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                yTextColor.yTextColorEnum textColor = yTextColor.requireYTextColorEnum(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setBackgroundColor(textColor.color);
                } catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new SetBackgroundColorFn());

        // terminal.setForegroundColor(TextColor)
        class SetForegroundColorFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                yTextColor.yTextColorEnum textColor = yTextColor.requireYTextColorEnum(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setForegroundColor(textColor.color);
                } catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new SetForegroundColorFn());

        // terminal.setCursorVisible(bool)
        class SetCursorVisibleFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);
                boolean flag = requireBoolean(arguments.getFirst(), getFnName(), 1);

                try {
                    terminal.instance.setCursorVisible(flag);
                } catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new SetCursorVisibleFn());


        // terminal.write(string | char)
        class WriteFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

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
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new WriteFn());

        // terminal.writeLine(string | char)
        class WriteLineFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

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
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new WriteLineFn());

        // terminal.readKey()
        class ReadKeyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal = requireTerminalThis(interpreter);

                try {
                    KeyStroke key = terminal.instance.readInput();
                    return new Variable.Variant(new yKeyStroke.yKeyStrokeInstance(key));

                }
                catch(IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new ReadKeyFn());

        // terminal.pollKey()
        class PollKeyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

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
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new PollKeyFn());

        // terminal.clearInputBuffer();
        class ClearInputBufferFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

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
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new ClearInputBufferFn());

        // terminal.getTerminalSize();
        class GetTerminalSizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal =
                        requireTerminalThis(interpreter);

                try {
                    TerminalSize terminalSize = terminal.instance.getTerminalSize();
                    return new Variable.Variant(new yTerminalSize.yTerminalSizeInstance(terminalSize));
                }catch (IOException ex) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new GetTerminalSizeFn());

        // terminal.addResizeListener();
        class AddResizeListenerFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yAbstractTerminal.AbstractTerminal terminal =
                        requireTerminalThis(interpreter);

                Variable.Variant listenerVariant = arguments.getFirst();

                if (!listenerVariant.isRuntimeObject()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            0,
                            "Function '" + getFnName() + "' expects a 'TerminalResizeListener' object but got '"
                                    + listenerVariant.getType() + "'."
                    );
                }

                Object raw = listenerVariant.value;


                if (!(raw instanceof yTerminalResizeListener)) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

        yBaseTerminal_Instance_Prototype.RegisterNativeFn(new AddResizeListenerFn());

    }

}
