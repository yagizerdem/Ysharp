package ysharp.evaluator.Native.YPF;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Util.yCrypto;

import javax.swing.*;
import java.util.List;

public class YPF {


    public static class YPFClass extends yClass.SealedClassObject {

        public YPFClass() {
            this.prototype = yClass.ClassPrototype;

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

            this.set("invokeLater", invokeLaterVar);
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
