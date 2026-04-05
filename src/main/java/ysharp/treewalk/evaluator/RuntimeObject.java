package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpError;

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

    protected String requireStringOrChar(Variable.Variant v,
                                         String fn,
                                         int index) throws YsharpError {

        if (v.isString()) {
            return v.asString();
        }

        if (v.isChar()) {
            return String.valueOf(v.asCharacter());
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                -1,
                fn + " argument " + index + " must be a string or a character."
        );
    }

    protected yString.yStringInstance requireStringObject(Variable.Variant v,
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

    public void RegisterClass(yClass.ClassObject klass) {
        this.set(klass.getClassName(), new Variable(new Variable.Variant(klass), true, klass.getType()));
    }

    public void RegisterNativeFn(Function.NativeFunction fn) {
        this.set(fn.getFnName(), new Variable(new Variable.Variant(fn), true, fn.getType()));
    }

    public void RegisterNativeFn(Function.NativeFunction fn, List<String> aliases) {
        var function = new Variable(new Variable.Variant(fn), true, fn.getType());
        this.set(fn.getFnName(), function);
        for(String alias : aliases) {
            this.set(alias, function);
        }
    }


    public void RegisterInstance(yClass.ClassObjectInstance instance, String identifier) {
        this.set(identifier, new Variable(new Variable.Variant(instance), true, instance.getType()));
    }

    public Object getNativeJavaObject() {
        return null;
    }

    public boolean isCompatible(Class<?> paramType, Object arg) {

        if (arg == null) return true;

        Class<?> argType = arg.getClass();

        // direct match
        if (paramType.isAssignableFrom(argType)) return true;

        // primitive handling
        if (paramType == int.class && arg instanceof Number) return true;
        if (paramType == double.class && arg instanceof Number) return true;
        if (paramType == float.class && arg instanceof Number) return true;
        if (paramType == long.class && arg instanceof Number) return true;

        if (paramType == boolean.class && arg instanceof Boolean) return true;

        return false;
    }
}
