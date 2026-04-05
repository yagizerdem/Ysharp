package ysharp.treewalk.evaluator.Native.YPF.Layout;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;

public class yBoxLayout {

    public static yBoxLayoutInstance requireBoxLayoutThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yBoxLayoutInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected BoxLayout but got '" + obj.getType() + "'"
            );
        }

        return (yBoxLayoutInstance) obj;
    }

    public static RuntimeObject yBoxLayout_Instance_Prototype;

    static {
        yBoxLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__BoxLayout__"; }
            @Override public String toString() { return "<prototype:BoxLayout>"; }
        };

        yBoxLayout_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : BoxLayout.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yBoxLayout_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yBoxLayoutInstance layoutInst =
                                    requireBoxLayoutThis(interpreter, name);

                            BoxLayout layout = layoutInst.layout;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    javaArgs[i] = args.get(i).asJavaNative();
                                }

                                List<Method> availableMethods = methodMap.get(name);

                                Method selected = null;

                                for (Method method : availableMethods) {

                                    if (method.getParameterCount() != args.size()) continue;

                                    Parameter[] params = method.getParameters();

                                    boolean skip = false;

                                    for (int j = 0; j < params.length; j++) {
                                        if (!isCompatible(params[j].getType(), javaArgs[j])) {
                                            skip = true;
                                            break;
                                        }
                                    }

                                    if (skip) continue;

                                    selected = method;
                                    break;
                                }

                                if (selected == null) {
                                    throw new YsharpError(
                                            YsharpError.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found"
                                    );
                                }

                                Object result = selected.invoke(layout, javaArgs);

                                return new Variable.Variant(
                                        JavaObjectWrapper.wrap(result)
                                );

                            } catch (Exception e) {
                                throw new YsharpError(
                                        YsharpError.YsharpErrorType.PROCESS,
                                        0,
                                        "Native call failed: " + name
                                );
                            }
                        }

                        @Override public String getFnName() { return name; }

                    }),
                    true,
                    "function"
            ));
        }
    }

    public static class yBoxLayoutInstance extends yClass.ClassObjectInstance {

        public final BoxLayout layout;

        public yBoxLayoutInstance(Container container, int axis) {
            this.layout = new BoxLayout(container, axis);
            this.prototype = yBoxLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "BoxLayout"; }
        @Override public String toString() { return "<instance:BoxLayout>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.layout;
        }
    }

    public static class yBoxLayoutClass extends yClass.SealedClassObject {

        public yBoxLayoutClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("X_AXIS", new Variable(new Variable.Variant(BoxLayout.X_AXIS), true, "int"));
            this.set("Y_AXIS", new Variable(new Variable.Variant(BoxLayout.Y_AXIS), true, "int"));
            this.set("LINE_AXIS", new Variable(new Variable.Variant(BoxLayout.LINE_AXIS), true, "int"));
            this.set("PAGE_AXIS", new Variable(new Variable.Variant(BoxLayout.PAGE_AXIS), true, "int"));
        }

        @Override public int arity() { return 2; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {
            requireArity(args, arity(), getClassName());
            Object containerObj = args.getFirst().asJavaNative();

            if (!(containerObj instanceof Container)) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        0,
                        "First argument must be Container"
                );
            }

            int axis = (int) args.get(1).asJavaNative();

            return new Variable.Variant(
                    new yBoxLayoutInstance((Container) containerObj, axis)
            );
        }

        @Override public String getClassName() { return "BoxLayout"; }
        @Override public String getType() { return "BoxLayout"; }
    }
}