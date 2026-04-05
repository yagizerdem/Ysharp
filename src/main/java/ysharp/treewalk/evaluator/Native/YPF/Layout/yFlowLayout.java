package ysharp.treewalk.evaluator.Native.YPF.Layout;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;

public class yFlowLayout {

    public static yFlowLayoutInstance requireFlowLayoutThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yFlowLayoutInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected FlowLayout but got '" + obj.getType() + "'"
            );
        }

        return (yFlowLayoutInstance) obj;
    }

    public static RuntimeObject yFlowLayout_Instance_Prototype;

    static {
        yFlowLayout_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__FlowLayout__"; }
            @Override public String toString() { return "<prototype:FlowLayout>"; }
        };

        yFlowLayout_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : FlowLayout.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yFlowLayout_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yFlowLayoutInstance layoutInst =
                                    requireFlowLayoutThis(interpreter, name);

                            FlowLayout layout = layoutInst.layout;

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

    public static class yFlowLayoutInstance extends yClass.ClassObjectInstance {

        public final FlowLayout layout;

        public yFlowLayoutInstance() {
            this.layout = new FlowLayout();
            this.prototype = yFlowLayout_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "FlowLayout"; }
        @Override public String toString() { return "<instance:FlowLayout>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.layout;
        }
    }

    public static class yFlowLayoutClass extends yClass.SealedClassObject {

        public yFlowLayoutClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("LEFT", new Variable(new Variable.Variant(FlowLayout.LEFT), true, "int"));
            this.set("CENTER", new Variable(new Variable.Variant(FlowLayout.CENTER), true, "int"));
            this.set("RIGHT", new Variable(new Variable.Variant(FlowLayout.RIGHT), true, "int"));
            this.set("LEADING", new Variable(new Variable.Variant(FlowLayout.LEADING), true, "int"));
            this.set("TRAILING", new Variable(new Variable.Variant(FlowLayout.TRAILING), true, "int"));
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yFlowLayoutInstance());
        }

        @Override public String getClassName() { return "FlowLayout"; }
        @Override public String getType() { return "FlowLayout"; }
    }
}