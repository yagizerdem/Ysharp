package ysharp.treewalk.evaluator;

import ysharp.treewalk.evaluator.Native.YPF.Container.yContainer;

import java.awt.*;

// converts native object to ysharp compatible type
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

        if (obj instanceof Float) {
            return Double.parseDouble(Float.toString((float)obj));
        }

        if (obj instanceof Short) {
            return Integer.parseInt(Short.toString((short)obj));
        }

        if(obj instanceof String) {
            return new yString.yStringInstance(String.valueOf(obj));
        }

        if (obj instanceof RuntimeObject) {
            return obj;
        }

        if(obj instanceof Container) {
            return new yContainer.yContainerInstance((Container) obj);
        }

        // fallback as native java object
        return obj;

    }
}
