package ysharp.evaluator.Native.YPF;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.YPF.Button.yButton;
import ysharp.evaluator.Native.YPF.Frame.yFrame;

import javax.swing.*;
import java.util.List;

public class YPF {


    public static class YPFClass extends yClass.SealedClassObject {

        static int initializationCount = 0;

        public YPFClass() {
            if(YPFClass.initializationCount == 0) {
                FlatLightLaf.setup(); // default mode is light
            }
            YPFClass.initializationCount++;

            this.prototype = yClass.ClassPrototype;

            // natives

            // YPF.invokeLater(()=> do // put ui code here end)
             class InvokeLaterFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant fnVar = arguments.getFirst();

                    if (!fnVar.isCallable()) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "invokeLater expects a function."
                        );
                    }

                    Callable callable = fnVar.asCallable();

                    SwingUtilities.invokeLater(() -> {
                        try {
                            callable.call(interpreter, List.of());
                        } catch (YsharpError e) {
                            e.printStackTrace();
                        }
                    });

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "invokeLater";
                }
            }

            InvokeLaterFn invokeLater = new InvokeLaterFn();
            Variable invokeLaterVar = new Variable(
                    new Variable.Variant(invokeLater),
                    true,
                    "function"
            );
            this.set(invokeLater.getFnName(), invokeLaterVar);

            // YPF.lightMode()
            class LightModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, arity(), getFnName());
                    FlatLightLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "lightMode";
                }
            }

            LightModeFn lightMode = new LightModeFn();
            Variable lightModeVar = new Variable(
                    new Variable.Variant(lightMode),
                    true,
                    "function"
            );
            this.set(lightMode.getFnName(), lightModeVar);


            // YPF.darkMode()
            class DarkModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, arity(), getFnName());
                    FlatDarkLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "darkMode";
                }
            }

            DarkModeFn darkMode = new DarkModeFn();
            Variable darkModeVar = new Variable(
                    new Variable.Variant(darkMode),
                    true,
                    "function"
            );
            this.set(darkMode.getFnName(), darkModeVar);

            // YPF.draculaMode()
            class DraculaModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, arity(), getFnName());
                    FlatDarkLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "draculaMode";
                }
            }

            DraculaModeFn draculaMode = new DraculaModeFn();
            Variable draculaModeVar = new Variable(
                    new Variable.Variant(draculaMode),
                    true,
                    "function"
            );
            this.set(draculaMode.getFnName(), draculaModeVar);


            // YPF.Frame
            yFrame.yFrameClass yFrame = new yFrame.yFrameClass();
            this.set(yFrame.getClassName(), new Variable(new Variable.Variant(yFrame), true, yFrame.getType()));

            // YPF.Button
            yButton.yButtonClass yButton = new yButton.yButtonClass();
            this.set(yButton.getClassName(), new Variable(new Variable.Variant(yButton), true, yButton.getType()));

        }

        @Override
        public String getClassName() {
            return "YPF";
        }

        @Override
        public String getType() {
            return "YPF";
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1, "cannot take instance of YPF static class");
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        YPFClass ctor = new YPFClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
