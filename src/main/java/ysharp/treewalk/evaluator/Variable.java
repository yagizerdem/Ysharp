package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpException;

public class Variable {

    public Variant value;
    public final boolean isConst;
    public final String typeTag;
    public final boolean enableRedeclare;

    public Variable(Variant value,
                    boolean isConst,
                    String typeTag) {
        this.value = value;
        this.isConst = isConst;
        this.typeTag = typeTag;
        this.enableRedeclare = false;
    }

    public Variable(Variant value,
                    boolean isConst,
                    String typeTag,
                    boolean enableRedeclare) {
        this.value = value;
        this.isConst = isConst;
        this.typeTag = typeTag;
        this.enableRedeclare = enableRedeclare;
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

        public boolean isFunctionObject() {
            return this.value instanceof Function.FunctionObject;
        }

        public boolean isNativeFunction() {
            return this.value instanceof Function.NativeFunction;
        }


        public boolean isLambda() { return this.value instanceof Function.LambdaObject;}
        public boolean isFunctionLike() {return this.value instanceof Function; }

        public boolean isFunctionOverload() {return this.value instanceof Function.FunctionOverload; }


        public boolean isClass() {
            return this.value instanceof yClass.ClassObject;
        }

        public boolean isClassInstance() {
            return this.value instanceof yClass.ClassObjectInstance;
        }

        public boolean isClassLike() { return this.isClass() || this.isClassInstance(); }

        public boolean isIntCompatibleNumber() {
            if(isInt()) {
                return  true;
            }
            return isDouble() &&
                    Math.abs(this.asDouble() - this.asDouble().intValue()) <  0.000001;
        }

        public boolean isPrimitive() {
            return  isBoolean() ||
                    isNull() ||
                    isString() ||
                    isNumber() ||
                    isChar();
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
            if(this.isDouble()) {
                return  this.asDouble().intValue();
            }
            return (Integer) this.value;
        }

        public  Double asDouble(){
            return (Double) this.value;
        }

        public Double asNumber(){
            if(this.isInt()) {
                return this.asInt().doubleValue();
            }

            if(this.isChar()) {
                return  this.asInt().doubleValue();
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

        public Function.FunctionObject asFunctionObject() {
            return (Function.FunctionObject) this.value;
        }

        public Function.FunctionOverload asFunctionOverload() {
            return (Function.FunctionOverload) this.value;
        }
        public Function.NativeFunction asNativeFunction() {
            return (Function.NativeFunction) this.value;
        }

        public Callable asCallable(){
            return (Callable) this.value;
        }

        public Function.LambdaObject asLambda(){
            return (Function.LambdaObject) this.value;
        }

        public yClass.ClassObject asClass() {
            return (yClass.ClassObject) this.value;
        }

        yClass.ClassObjectInstance asClassInstance() {
            return (yClass.ClassObjectInstance) this.value;
        }

        public Object asJavaNative() {
            if (this.isString()) return this.asString();
            else if (this.isInt()) return this.asInt();
            else if (this.isDouble()) return this.asDouble();
            else if(this.isBoolean()) return  this.asBoolean();
            else if(this.isChar()) return  this.asCharacter();
            else if(this.isNull()) return  null;
            else if (this.isRuntimeObject()) return this.asRuntimeObject().getNativeJavaObject();

            // at this point value is already a java native since ysharp data types must be runtime object or primitives that ysharp supports
            // if not java native there should be a bug in code !?
            // fallback
            return this.value;
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
            if(this.value == null) return "null";
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

            if (isString()) return "string";

            if (isRuntimeObject()) {
                RuntimeObject obj = asRuntimeObject();
                return obj.getType();
            }

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Internal error: Unknown Variant runtime type: " + value.getClass()
            );
        }

    }

    public String getType() {
        return this.typeTag;
    }

}
