package ysharp.evaluator.Native.Form;

import ysharp.evaluator.RuntimeObject;

import java.awt.*;

public abstract class Y_ComponentObject extends RuntimeObject {
    protected java.awt.Component component;

    public Component getComponent() {
        return component;
    }
}