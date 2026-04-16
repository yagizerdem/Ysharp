package ysharp.treewalk.evaluator.Native.YPF.Layout;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;

public class yGridLayout {

    public static yGridLayoutInstance requireGridLayoutThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yGridLayoutInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected GridLayout but got '" + obj.getType() + "'"
            );
        }

        return (yGridLayoutInstance) obj;
    }

    public static RuntimeObject yGridLayout_Instance_Prototype;

    static {
        yGridLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__GridLayout__"; }
            @Override public String toString() { return "<prototype:GridLayout>"; }
        };

        yGridLayout_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : GridLayout.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yGridLayout_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yGridLayoutInstance layoutInst =
                                    requireGridLayoutThis(interpreter, name);

                            GridLayout layout = layoutInst.layout;

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
                                    throw new YsharpException(
                                            YsharpException.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found"
                                    );
                                }

                                Object result = selected.invoke(layout, javaArgs);

                                return new Variable.Variant(
                                        JavaObjectWrapper.wrap(result)
                                );

                            } catch (Exception e) {
                                throw new YsharpException(
                                        YsharpException.YsharpErrorType.PROCESS,
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

    public static class yGridLayoutInstance extends yClass.ClassObjectInstance {

        public final GridLayout layout;

        public yGridLayoutInstance(int rows, int cols, int hgap, int vgap) {
            this.layout = new GridLayout(rows, cols, hgap, vgap);
            this.prototype = yGridLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "GridLayout"; }
        @Override public String toString() { return "<instance:GridLayout>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.layout;
        }
    }

    public static class yGridLayoutClass extends yClass.SealedClassObject {

        public yGridLayoutClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return -1; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            if (args.isEmpty()) {
                return new Variable.Variant(
                        new yGridLayoutInstance(1, 0, 0, 0)
                );
            }

            if (args.size() == 2) {
                return new Variable.Variant(
                        new yGridLayoutInstance(
                                (int) args.get(0).asJavaNative(),
                                (int) args.get(1).asJavaNative(),
                                0,
                                0
                        )
                );
            }

            if (args.size() == 4) {
                return new Variable.Variant(
                        new yGridLayoutInstance(
                                (int) args.get(0).asJavaNative(),
                                (int) args.get(1).asJavaNative(),
                                (int) args.get(2).asJavaNative(),
                                (int) args.get(3).asJavaNative()
                        )
                );
            }

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Invalid GridLayout constructor arguments. Expected 0, 2 or 4 arguments."
            );

        }

        @Override public String getClassName() { return "GridLayout"; }
        @Override public String getType() { return "GridLayout"; }
    }
}