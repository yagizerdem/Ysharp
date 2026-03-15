package ysharp.evaluator;

import ysharp.YsharpError;

public class Variable {

    public Variant value;
    public final boolean isConst;
    public final String typeTag;

    public Variable(Variant value,
                    boolean isConst,
                    String typeTag) {
        this.value = value;
        this.isConst = isConst;
        this.typeTag = typeTag;
    }

    public static class Variant {
        public Object value;


        public Variant(Object value){
            this.value = value;
        }

        // primitives
        public  boolean isInt(){
            return  this.value instanceof Integer;
        }

        public boolean isDouble(){
            return this.value instanceof Double;
        }

        public boolean isChar() {
            return  this.value instanceof Character;
        }

        public boolean isNull(){
            return this.value == null;
        }

        public boolean isBoolean(){
            return  this.value instanceof Boolean;
        }

        public  boolean isNumber(){
            return this.isInt() || this.isDouble();
        }

        public boolean isString() {return this.value instanceof yString.yStringInstance; }

        public boolean isFunction() {
            return this.value instanceof Function;
        }

        public boolean isNativeFunction() {
            return this.value instanceof Function.NativeFunction;
        }

        public boolean isLambda() { return this.value instanceof Function.LambdaObject;}

        public boolean isFunctionLike() {return isLambda() || isNativeFunction() || isFunction();}

        public boolean isClass() {
            return this.value instanceof yClass.ClassObject;
        }

        public boolean isClassInstance() {
            return this.value instanceof yClass.ClassObjectInstance;
        }

        public boolean canImplicitlyConvertNumber(){
            return this.isNumber() ||
                    this.isChar() ||
                    this.isBoolean();
        }

        // runtime objects

        public boolean isRuntimeObject() {
            return value instanceof RuntimeObject;
        }

        // cast

        public Integer asInt(){
            return (Integer) this.value;
        }

        public  Double asDouble(){
            return (Double) this.value;
        }

        public Double asNumber(){
            if(this.isInt()) {
                return this.asInt().doubleValue();
            }

            return this.asDouble();
        }

        public double implicitlyConvertNumber(){
            if(this.isInt()) return this.asInt().doubleValue();
            if(this.isDouble()) return this.asDouble();
            if(this.isBoolean()) return this.asBoolean() ? 1.0 : 0;
            if(this.isChar()) return (double) this.asCharacter();

            return 0;
        }


        public Boolean asBoolean(){
            return (Boolean) this.value;
        }

        public Character asCharacter() {
            return (Character) this.value;
        }

        public RuntimeObject asRuntimeObject() {
            return (RuntimeObject) this.value;
        }

        public String asString() { return ((yString.yStringInstance) this.value).data; }

        public yString.yStringInstance asStringObject() { return ((yString.yStringInstance) this.value); }

        public Function.FunctionObject asFunction() {
            return (Function.FunctionObject) this.value;
        }

        public Function.NativeFunction asNativeFunction() {
            return (Function.NativeFunction) this.value;
        }

        public Callable asCallable(){
            return (Callable) this.value;
        }

        public yClass.ClassObject asClass() {
            return (yClass.ClassObject) this.value;
        }

        yClass.ClassObjectInstance asClassInstance() {
            return (yClass.ClassObjectInstance) this.value;
        }

        public boolean isTruthy() {

            if (isBoolean()) {
                return asBoolean();
            }

            if (isNumber()) {
                return asNumber() != 0;
            }

            if (isChar()) {
                return asCharacter() != '\0';
            }

            if (isRuntimeObject()) {
                return asRuntimeObject().isTruthy();
            }

            return false;
        }

        public boolean isCallable(){
            return this.value instanceof Callable;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Variant other)) return false;

            if (this.value == null || other.value == null)
                return this.value == other.value;

            if (this.isNumber() && other.isNumber()) {
                double a = this.implicitlyConvertNumber();
                double b = other.implicitlyConvertNumber();
                return Math.abs(a - b) < 1e-9;
            }

            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value == null ? 0 : value.hashCode();
        }

        @Override
        public String toString() {
            return this.value.toString();
        }

        public String getType() {

            if (this.value == null) {
                return "null";
            }

            if (isInt()) return "int";

            if (isDouble()) return "double";

            if (isBoolean()) return "bool";

            if (isChar()) return "char";

            if (isRuntimeObject()) {
                RuntimeObject obj = asRuntimeObject();
                return obj.getType();
            }

            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Internal error: Unknown Variant runtime type: " + value.getClass()
            );
        }

    }

    public String getType() {
        return this.typeTag;
    }

}
