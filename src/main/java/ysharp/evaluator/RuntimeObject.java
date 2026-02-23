package ysharp.evaluator;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeObject {

    protected Map<String, Variable> fields = new HashMap<>();
    protected RuntimeObject prototype;

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

    public void setPrototype(RuntimeObject proto) {
        this.prototype = proto;
    }

    public RuntimeObject getPrototype() {
        return prototype;
    }

    public abstract boolean isTruthy();

    public abstract String getType();


    public static class StringObject extends RuntimeObject {
        final String data;

        public StringObject(String data){
            this.data = data;
        }

        @Override
        public boolean isTruthy() {
            return  !this.data.isEmpty();
        }

        @Override
        public String getType() {
            return "string";
        }

        @Override
        public String toString() {
            return this.data;
        }
    }

    public static class FunctionObject extends RuntimeObject {

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "function";
        }

        @Override
        public String toString() {
            return "function";
        }
    }

    public static class ClassObject extends RuntimeObject {

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "class";
        }

        @Override
        public String toString() {
            return "class";
        }
    }

}
