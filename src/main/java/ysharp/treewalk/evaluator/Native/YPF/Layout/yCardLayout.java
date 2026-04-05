package ysharp.treewalk.evaluator.Native.YPF.Layout;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.awt.CardLayout;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yCardLayout {

    public static yCardLayout.yCardLayoutInstance requireCardLayoutThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yCardLayoutInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected CardLayout but got '" + obj.getType() + "'"
            );
        }

        return (yCardLayoutInstance) obj;
    }

    public static RuntimeObject yCardLayout_Instance_Prototype;

    static {
        yCardLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__CardLayout__"; }
            @Override public String toString() { return "<prototype:CardLayout>"; }
        };

        yCardLayout_Instance_Prototype.prototype = yClass.ClassPrototype;


        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : CardLayout.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yCardLayout_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yCardLayoutInstance layoutInst =
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

    public static class yCardLayoutInstance extends yClass.ClassObjectInstance {

        public final CardLayout layout;

        public yCardLayoutInstance() {
            this.layout = new CardLayout();
            this.prototype = yCardLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "CardLayout"; }
        @Override public String toString() { return "<instance:CardLayout>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.layout;
        }
    }

    public static class yCardLayoutClass extends yClass.SealedClassObject {

        public yCardLayoutClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yCardLayoutInstance());
        }

        @Override public String getClassName() { return "CardLayout"; }
        @Override public String getType() { return "CardLayout"; }
    }
}