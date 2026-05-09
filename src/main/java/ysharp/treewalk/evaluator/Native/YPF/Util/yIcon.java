package ysharp.treewalk.evaluator.Native.YPF.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.yIO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class yIcon {

    public static yIconInstance requireIconThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yIconInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Icon but got '" + obj.getType() + "'"
            );
        }

        return (yIconInstance) obj;
    }



    public static RuntimeObject yIcon_Instance_Prototype;

    static {
        yIcon_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Icon__"; }
            @Override public String toString() { return "<prototype:Icon>"; }
        };

        yIcon_Instance_Prototype.prototype = yClass.ClassPrototype;


        class SetSizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, 2, getFnName());

                yIconInstance iconInstance = yIcon.requireIconThis(interpreter, getFnName());

                int width = requireInt(arguments.get(0), getFnName(), 1);
                int height = requireInt(arguments.get(1), getFnName(), 2);

                if(iconInstance.icon instanceof ImageIcon imageIcon) {

                    Image scaledImage = imageIcon.getImage().getScaledInstance(
                            width,
                            height,
                            Image.SCALE_SMOOTH
                    );

                    iconInstance.icon = new ImageIcon(scaledImage);

                } else {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1, "icon is not image icon");
                }


                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setSize";
            }
        }

        yIcon_Instance_Prototype.RegisterNativeFn(new SetSizeFn());

    }

    public static class yIconInstance extends yClass.ClassObjectInstance {

        public Icon icon;

        public yIconInstance(Icon icon) {
            this.icon = icon;
            this.prototype = yIcon_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Icon"; }
        @Override public String toString() { return "<instance:Icon>"; }
        @Override public Object getNativeJavaObject() { return this.icon; }
    }

    public static class yIconClass extends yClass.SealedClassObject {

        public yIconClass() {
            this.prototype = yClass.ClassPrototype;


            class CreateBufferedImageIconFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

                    requireArity(arguments, 1, getFnName());
                    String path = requireString(arguments.getFirst(), getFnName(), 1);

                    try {

                        Path resolved =  yIO.resolvePath(interpreter, path);
                        BufferedImage img = ImageIO.read(resolved.toFile());
                        ImageIcon icon = new ImageIcon(img);
                        yIcon.yIconInstance instance = new yIconInstance(icon);
                        return new Variable.Variant(instance);
                    }catch (IOException ex) {
                        throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1 , ex.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "createBufferedImageIcon";
                }
            }

            this.RegisterNativeFn(new CreateBufferedImageIconFn());
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, java.util.List<Variable.Variant> args)
                throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1 , "cannot take instnace of static Icon class");
        }

        @Override public String getClassName() { return "Icon"; }
        @Override public String getType() { return "_Icon_"; }
        @Override public String toString() { return "<class:Icon>"; }
    }
}