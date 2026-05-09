package ysharp.treewalk.evaluator.Native.YPF.Label;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Native.YPF.yComponent;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;

public class yLabel {

    public static yLabelInstance requireLabelThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yLabelInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Label but got '" + obj.getType() + "'"
            );
        }

        return (yLabelInstance) obj;
    }

    public static RuntimeObject yLabel_Instance_Prototype;

    static {
        yLabel_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Label__"; }
            @Override public String toString() { return "<prototype:Label>"; }
        };

        yLabel_Instance_Prototype.prototype = yComponent.yComponent_Instance_Prototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JLabel.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yLabel_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yLabelInstance label =
                                    yLabel.requireLabelThis(interpreter, name);

                            JLabel jlabel = label.label;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    Variable.Variant v = args.get(i);
                                    javaArgs[i] = v.asJavaNative();
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

                                Object result = selected.invoke(jlabel, javaArgs);

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

    public static class yLabelInstance extends yClass.ClassObjectInstance  implements yComponent.yBaseComponent {

        public final JLabel label;

        public yLabelInstance() {
            this.label = new JLabel();
            this.prototype = yLabel_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Label"; }
        @Override public String toString() { return "<instance:Label>"; }
        @Override public Object getNativeJavaObject() { return this.label; }
        @Override public Component getComponent() { return this.label; }
    }

    public static class yLabelClass extends yClass.SealedClassObject {

        public yLabelClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yLabelInstance());
        }

        @Override public String getClassName() { return "Label"; }
        @Override public String getType() { return "_Label_"; }
    }
}