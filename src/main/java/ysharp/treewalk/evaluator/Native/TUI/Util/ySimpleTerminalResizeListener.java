package ysharp.treewalk.evaluator.Native.TUI.Util;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.yClass;

import java.util.List;

public class ySimpleTerminalResizeListener {

    private static ySimpleTerminalResizeListenerInstance requireThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof ySimpleTerminalResizeListenerInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'SimpleTerminalResizeListener' but got '" + obj.getType() + "'."
            );
        }

        return (ySimpleTerminalResizeListenerInstance) obj;
    }


    public static RuntimeObject ySimpleTerminalResizeListener_Instance_Prototype;

    static {
        ySimpleTerminalResizeListener_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__SimpleTerminalResizeListener__";
            }

            @Override
            public String toString() {
                return "<prototype:SimpleTerminalResizeListener>";
            }
        };

        ySimpleTerminalResizeListener_Instance_Prototype.prototype = yClass.ClassPrototype;

        class GetLastKnownSizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                ySimpleTerminalResizeListenerInstance self = requireThis(interpreter, getFnName());

                TerminalSize size = self.listener.getLastKnownSize();

                return new Variable.Variant(
                        new yTerminalSize.yTerminalSizeInstance(size)
                );
            }

            @Override
            public String getFnName() {
                return "getLastKnownSize";
            }
        }

        GetLastKnownSizeFn getLastKnownSize = new GetLastKnownSizeFn();
        ySimpleTerminalResizeListener_Instance_Prototype.set(
                getLastKnownSize.getFnName(),
                new Variable(new Variable.Variant(getLastKnownSize), true, "function")
        );


        class IsTerminalResizedFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                ySimpleTerminalResizeListenerInstance self = requireThis(interpreter, getFnName());

                return new Variable.Variant(
                        self.listener.isTerminalResized()
                );
            }

            @Override
            public String getFnName() {
                return "isTerminalResized";
            }
        }

        IsTerminalResizedFn isTerminalResized = new IsTerminalResizedFn();
        ySimpleTerminalResizeListener_Instance_Prototype.set(
                isTerminalResized.getFnName(),
                new Variable(new Variable.Variant(isTerminalResized), true, "function")
        );


        class OnResizedFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                ySimpleTerminalResizeListenerInstance self = requireThis(interpreter, getFnName());

                Terminal terminal = arguments.get(0).value instanceof Terminal
                        ? (Terminal) arguments.get(0).value
                        : null;

                yTerminalSize.yTerminalSizeInstance size =
                        yTerminalSize.requireTerminalSize(arguments.get(1), getFnName(), 2);

                self.listener.onResized(terminal, size.terminalSize);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "onResized";
            }
        }

        OnResizedFn onResized = new OnResizedFn();
        ySimpleTerminalResizeListener_Instance_Prototype.set(
                onResized.getFnName(),
                new Variable(new Variable.Variant(onResized), true, "function")
        );
    }


    public static class ySimpleTerminalResizeListenerInstance extends yClass.ClassObjectInstance implements yTerminalResizeListener {

        public final SimpleTerminalResizeListener listener;

        public ySimpleTerminalResizeListenerInstance(SimpleTerminalResizeListener listener) {
            this.listener = listener;
            this.prototype = ySimpleTerminalResizeListener_Instance_Prototype;
        }

        @Override
        public TerminalResizeListener getListener() {
            return listener;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "SimpleTerminalResizeListener";
        }

        @Override
        public String toString() {
            return "<instance:SimpleTerminalResizeListener>";
        }
    }

    public static class ySimpleTerminalResizeListenerClass extends yClass.SealedClassObject {

        public ySimpleTerminalResizeListenerClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yTerminalSize.yTerminalSizeInstance size =
                     yTerminalSize.requireTerminalSize(arguments.getFirst(), "SimpleTerminalResizeListener", 1);

            SimpleTerminalResizeListener listener =
                    new SimpleTerminalResizeListener(size.terminalSize);

            return new Variable.Variant(
                    new ySimpleTerminalResizeListenerInstance(listener)
            );
        }

        @Override
        public String getClassName() {
            return "SimpleTerminalResizeListener";
        }

        @Override
        public String getType() {
            return "SimpleTerminalResizeListener";
        }

        @Override
        public String toString() {
            return "<class:SimpleTerminalResizeListener>";
        }
    }


}