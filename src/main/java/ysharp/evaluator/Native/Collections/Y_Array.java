package ysharp.evaluator.Native.Collections;

import ysharp.evaluator.RuntimeObject;

public class Y_Array {

    public static RuntimeObject Y_Array_Prototype;

    static {
        Y_Array_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "array_prototype";
            }
        };
    }



}
