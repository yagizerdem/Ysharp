package ysharp.evaluator.Native.function;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;

import java.util.List;

public abstract class NativeFunction extends RuntimeObject implements Callable {

    protected void requireArity(List<?> args, int expected, String fn)
            throws YsharpError {

        if (args.size() != expected) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " expects exactly " + expected + " arguments."
            );
        }
    }

    protected double requireNumber(Variable.Variant v, String fn, int index)
            throws YsharpError {

        if (!v.isNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be numeric."
            );
        }

        return v.asNumber();
    }

    protected int requireInt(Variable.Variant v,
                             String fn,
                             int index) throws YsharpError {

        if (!v.isInt()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be an integer."
            );
        }

        return v.asInt();
    }

    protected char requireChar(Variable.Variant v,
                               String fn,
                               int index) throws YsharpError {

        if (!v.isChar()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a character."
            );
        }

        return v.asCharacter();
    }

    protected boolean requireBoolean(Variable.Variant v,
                                     String fn,
                                     int index) throws YsharpError {

        if (!v.isBoolean()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a boolean."
            );
        }

        return v.asBoolean();
    }

    protected double requireDouble(Variable.Variant v,
                                   String fn,
                                   int index) throws YsharpError {

        if (!v.isDouble()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a double."
            );
        }

        return v.asDouble();
    }

    public abstract String getFnName();

    public NativeFunction(){
        this.prototype = null;
    }

    @Override
    public String toString() {
        return "native-function";
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    public String getType() {
        return "function";
    }
}
