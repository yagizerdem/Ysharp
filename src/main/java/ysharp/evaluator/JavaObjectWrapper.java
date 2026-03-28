package ysharp.evaluator;

public class JavaObjectWrapper {

    static public Object wrap(Object obj) {
        if(obj == null) return null;

        if(     obj instanceof Integer ||
                obj instanceof Character ||
                obj instanceof Boolean ||
                obj instanceof Double
        ) {
            return obj;
        }

        if (obj instanceof Number) {
            return ((Double) obj).doubleValue();
        }

        if(obj instanceof String) {
            return new yString.yStringInstance(String.valueOf(obj));
        }

        if (obj instanceof RuntimeObject) {
            return obj;
        }

        return obj;

    }
}
