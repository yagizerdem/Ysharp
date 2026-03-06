package ysharp.evaluator;

import ysharp.lexer.Token;
import ysharp.parser.TypeTag;

import javax.swing.*;
import java.security.PublicKey;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;

public class Variable {

    public Variant value;
    public final boolean isConst;
    public final TypeTag typeTag;

    public Variable(Variant value,
                    boolean isConst,
                    TypeTag typeTag) {
        this.value = value;
        this.isConst = isConst;
        this.typeTag = typeTag;
    }

    public static class Variant {
        public final Object value;

        public Variant(Object value){
            this.value = value;
        }

        public String getType() {
            return switch (value) {
                case Integer i        -> "int";
                case Double d         -> "double";
                case Boolean b        -> "bool";
                case Character c      -> "char";
                case RuntimeObject o  -> o.getType();
                case null             -> "null";
                default               -> "unknown";
            };
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

        public boolean isString() {return this.value instanceof Y_String.Y_StringObject; }

        public boolean isFunction() {
            return this.value instanceof Function.FunctionObject;
        }

        public boolean isNativeFunction() {
            return this.value instanceof Function.NativeFunction;
        }

        public boolean isLambda() { return this.value instanceof Function.LambdaObject;}

        public boolean isFunctionLike() {return isLambda() || isNativeFunction() || isFunction();}

        public boolean isClass() {
            return this.value instanceof Y_Class.ClassObject;
        }

        public boolean isClassInstance() {
            return this.value instanceof Y_Class.ClassObjectInstance;
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

        public String asString() { return ((Y_String.Y_StringObject) this.value).data; }

        public Y_String.Y_StringObject asStringObject() { return ((Y_String.Y_StringObject) this.value); }

        public Function.FunctionObject asFunction() {
            return (Function.FunctionObject) this.value;
        }

        public Function.NativeFunction asNativeFunction() {
            return (Function.NativeFunction) this.value;
        }

        public Callable asCallable(){
            return (Callable) this.value;
        }

        public Y_Class.ClassObject asClass() {
            return (Y_Class.ClassObject) this.value;
        }

        Y_Class.ClassObjectInstance asClassInstance() {
            return (Y_Class.ClassObjectInstance) this.value;
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

    }

}
