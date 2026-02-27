package ysharp.evaluator.Native.function.binding;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.lexer.Token;
import ysharp.parser.TypeTag;

import java.util.List;

public class BoundNativeFunction extends Function.NativeFunction {

    private final Function.NativeFunction original;
    private final RuntimeObject thisObj;

    public BoundNativeFunction(Function.NativeFunction original,
                               RuntimeObject thisObj) {
        this.original = original;
        this.thisObj = thisObj;
    }

    @Override
    public int arity() {
        return original.arity();
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Environment oldEnv = interpreter.curEnv;

        try {
            Environment env = new Environment(oldEnv);

            Variable variable = new Variable(
                    new Variable.Variant(thisObj),
                    true,
                    TypeTag.OBJECT);

            env.define("this", variable);

            interpreter.curEnv = env;

            return original.call(interpreter, arguments);

        } finally {
            interpreter.curEnv = oldEnv;
        }
    }

    @Override
    public String getFnName() {
        return original.getFnName();
    }
}