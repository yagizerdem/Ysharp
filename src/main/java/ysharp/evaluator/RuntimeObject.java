package ysharp.evaluator;

import ysharp.YsharpError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RuntimeObject {

    public Map<String, Variable> fields = new HashMap<>();
    // function native function and lambda do not need prototype chain, their behaviour is fixed
    public RuntimeObject prototype;

    public void set(String name, Variable value) {
        fields.put(name, value);
    }

    public Variable get(String name) {
        if (fields.containsKey(name)) {
            return fields.get(name);
        }

        if (prototype != null) {
            return prototype.get(name);
        }

        return null;
    }

    public Variable assign(String name, Variable.Variant value) {

        Variable var = this.get(name);

        if (var == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SEMANTIC,
                    -1,
                    "Type '" + this.getType() + "' has no field named '" + name + "'."
            );
        }

        if (var.isConst) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SEMANTIC,
                    -1,
                    "Cannot assign to constant field '" + name + "' in type '" + this.getType() + "'."
            );
        }

        var.value = value;

        return var;
    }

    public void setPrototype(RuntimeObject proto) {
        this.prototype = proto;
    }

    public RuntimeObject getPrototype() {
        return prototype;
    }

    public abstract boolean isTruthy();

    public abstract String getType();

    public static RuntimeObject Runtime_Object_Prototype; // base prototype

    static  {
            Runtime_Object_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "runtime_object_prototype"; }
        };


    }



    // helper functions

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

    protected String requireString(Variable.Variant v,
                                   String fn,
                                   int index) throws YsharpError {

        if (!v.isString()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a string."
            );
        }

        return v.asString();
    }

    protected Y_String.Y_StringInstance requireStringObject(Variable.Variant v,
                                   String fn,
                                   int index) throws YsharpError {

        if (!v.isString()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a string."
            );
        }

        return v.asStringObject();
    }

    protected Callable requireCallable(Variable.Variant v,
                                       String fn,
                                       int index) throws YsharpError {

        if (!v.isCallable()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a function."
            );
        }

        return v.asCallable();
    }
}
