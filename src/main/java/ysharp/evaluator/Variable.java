package ysharp.evaluator;

import java.util.Objects;

public class Variable {

    final Variant value;
    final boolean isConst;
    final String typeTag;

    public Variable(Variant value,
                    boolean isConst,
                    String typeTag) {
        this.value = value;
        this.isConst = isConst;
        this.typeTag = typeTag;
    }

    public static class Variant {
        private final Object value;

        public Variant(Object value){
            this.value = value;
        }

        // primitives
        public  boolean isInt(){
            Class<?> type = this.value.getClass();
            return type == Integer.class;
        }

        public boolean isDouble(){
            Class<?> type = this.value.getClass();
            return type == Double.class;
        }

        public boolean isChar() {
            Class<?> type = this.value.getClass();
            return type == Character.class;
        }

        public boolean isNull(){
            return this.value == null;
        }

        public boolean isBoolean(){
            Class<?> type = this.value.getClass();
            return type == Boolean.class;
        }

        public  boolean isNumber(){
            return this.isInt() || this.isDouble();
        }

        // runtime objects

        public boolean isRuntimeObject() {
            return value instanceof RuntimeObject;
        }

        public boolean isString(){
            Class<?> type = this.value.getClass();
            return type == RuntimeObject.StringObject.class;
        }

        public boolean isFunction(){
            Class<?> type = this.value.getClass();
            return type == RuntimeObject.FunctionObject.class;
        }

        public boolean isClass(){
            Class<?> type = this.value.getClass();
            return type == RuntimeObject.ClassObject.class;
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


        public Boolean asBoolean(){
            return (Boolean) this.value;
        }

        public Character asCharacter() {
            return (Character) this.value;
        }

        public RuntimeObject asRuntimeObject() {
            return (RuntimeObject) this.value;
        }

        public RuntimeObject.FunctionObject asFunction() {
            return (RuntimeObject.FunctionObject) this.value;
        }

        public RuntimeObject.ClassObject asClass() {
            return (RuntimeObject.ClassObject) this.value;
        }

        public RuntimeObject.StringObject asString() {
            return (RuntimeObject.StringObject) this.value;
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
    }

}
