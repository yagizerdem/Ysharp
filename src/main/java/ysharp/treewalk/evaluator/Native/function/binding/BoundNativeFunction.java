package ysharp.treewalk.evaluator.Native.function.binding;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class BoundNativeFunction extends Function.NativeFunction {

    private final Callable original;
    private final RuntimeObject thisObj;
    private final String key;

    public BoundNativeFunction(Callable original,
                               RuntimeObject thisObj,
                               String key) {
        this.original = original;
        this.thisObj = thisObj;
        this.key = key;
    }

    @Override
    public int arity() {
        return original.arity();
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Environment oldEnv = interpreter.curEnv;

        try {
            Environment env = new Environment(oldEnv);

            Variable variable = new Variable(
                    new Variable.Variant(thisObj),
                    true,
                    "function");

            env.define(key, variable);

            interpreter.curEnv = env;

            return original.call(interpreter, arguments);

        } finally {
            interpreter.curEnv = oldEnv;
        }
    }


    @Override
    public String getFnName() {
        return "bound";
    }

    @Override
    public String toString() {
        if(original instanceof Function.FunctionObject) {
            return "<function:" + ((FunctionObject)original).declaration.name.lexeme + ">";
        }
        else if(original instanceof Function.NativeFunction) {
            return "<function:" + ((NativeFunction)original).getFnName() + ">";
        }
        else if(original instanceof Function.FunctionOverload) {
            return "<function:" + ((FunctionOverload)original).name + ">";
        }

        return toString();
    }
}