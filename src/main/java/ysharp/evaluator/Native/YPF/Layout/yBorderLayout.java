package ysharp.evaluator.Native.YPF.Layout;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;

import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yBorderLayout {

    public static RuntimeObject yBorderLayout_Instance_Prototype;

    static {
        yBorderLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__BorderLayout__"; }
            @Override public String toString() { return "<prototype:BorderLayout>"; }
        };

        yBorderLayout_Instance_Prototype.prototype = yClass.ClassPrototype;


        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : BorderLayout.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yBorderLayout_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yCardLayout.yCardLayoutInstance layoutInst =
                                    yCardLayout.requireCardLayoutThis(interpreter, name);

                            CardLayout layout = layoutInst.layout;

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

    public static class yBorderLayoutInstance extends yClass.ClassObjectInstance {

        public final BorderLayout layout;

        public yBorderLayoutInstance() {
            this.layout = new BorderLayout();
            this.prototype = yBorderLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "BorderLayout"; }
        @Override public String toString() { return "<instance:BorderLayout>"; }
        @Override
        public Object getNativeJavaObject() { return this.layout;}
    }

    public static class yBorderLayoutClass extends yClass.SealedClassObject {

        public yBorderLayoutClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("NORTH", new Variable(new Variable.Variant(BorderLayout.NORTH), true, "string"));
            this.set("SOUTH", new Variable(new Variable.Variant(BorderLayout.SOUTH), true, "string"));
            this.set("EAST", new Variable(new Variable.Variant(BorderLayout.EAST), true, "string"));
            this.set("WEST", new Variable(new Variable.Variant(BorderLayout.WEST), true, "string"));
            this.set("CENTER", new Variable(new Variable.Variant(BorderLayout.CENTER), true, "string"));
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yBorderLayoutInstance());
        }

        @Override public String getClassName() { return "BorderLayout"; }
        @Override public String getType() { return "BorderLayout"; }
    }
}