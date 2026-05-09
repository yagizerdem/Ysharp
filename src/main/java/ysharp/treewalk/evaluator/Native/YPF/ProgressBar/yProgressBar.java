package ysharp.treewalk.evaluator.Native.YPF.ProgressBar;

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

public class yProgressBar {

    public static yProgressBarInstance requireProgressBarThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yProgressBarInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected ProgressBar but got '" + obj.getType() + "'"
            );
        }

        return (yProgressBarInstance) obj;
    }

    public static RuntimeObject yProgressBar_Instance_Prototype;

    static {
        yProgressBar_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__ProgressBar__"; }
            @Override public String toString() { return "<prototype:ProgressBar>"; }
        };
        yProgressBar_Instance_Prototype.prototype = yComponent.yComponent_Instance_Prototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JProgressBar.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yProgressBar_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yProgressBarInstance pb =
                                    yProgressBar.requireProgressBarThis(interpreter, name);

                            JProgressBar jpb = pb.progressBar;

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

                                Object result = selected.invoke(jpb, javaArgs);

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

    public static class yProgressBarInstance extends yClass.ClassObjectInstance implements yComponent.yBaseComponent {

        public final JProgressBar progressBar;

        public yProgressBarInstance() {
            this.progressBar = new JProgressBar();
            this.prototype = yProgressBar_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "ProgressBar"; }
        @Override public String toString() { return "<instance:ProgressBar>"; }
        @Override public Object getNativeJavaObject() {
            return this.progressBar;
        }
        @Override public Component getComponent() { return this.progressBar;}
    }

    public static class yProgressBarClass extends yClass.SealedClassObject {

        public yProgressBarClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yProgressBarInstance());
        }

        @Override public String getClassName() { return "ProgressBar"; }
        @Override public String getType() { return "ProgressBar"; }
    }
}