package ysharp.evaluator;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeObject {

    protected Map<String, Variable> fields = new HashMap<>();
    // function native function and lambda do not need prototype chain, their behaviour is fixed
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

    public static RuntimeObject Runtime_Object_Prototype; // base prototype

    static  {
            Runtime_Object_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "runtime_object_prototype"; }
        };


    }

}
