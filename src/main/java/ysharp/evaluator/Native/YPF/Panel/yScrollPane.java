package ysharp.evaluator.Native.YPF.Panel;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class yScrollPane {

    public static yScrollPaneInstance requireScrollPaneThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yScrollPaneInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected ScrollPane but got '" + obj.getType() + "'"
            );
        }

        return (yScrollPaneInstance) obj;
    }

    public static RuntimeObject yScrollPane_Instance_Prototype;

    static {
        yScrollPane_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__ScrollPane__"; }
            @Override public String toString() { return "<prototype:ScrollPane>"; }
        };

        yScrollPane_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JScrollPane.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap.computeIfAbsent(m.getName(), k -> new ArrayList<>()).add(m);
        }

        for (String name : methodMap.keySet()) {
            yScrollPane_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yScrollPaneInstance sp =
                                    yScrollPane.requireScrollPaneThis(interpreter, name);

                            JScrollPane jsp = sp.scrollPane;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    javaArgs[i] = args.get(i).asJavaNative();
                                }

                                List<Method> methods = methodMap.get(name);

                                Method selected = null;

                                for (Method m : methods) {
                                    if (m.getParameterCount() != args.size()) continue;

                                    boolean skip = false;
                                    Parameter[] params = m.getParameters();

                                    for (int j = 0; j < params.length; j++) {
                                        if (!isCompatible(params[j].getType(), javaArgs[j])) {
                                            skip = true;
                                            break;
                                        }
                                    }

                                    if (skip) continue;
                                    selected = m;
                                    break;
                                }

                                if (selected == null)
                                    throw new YsharpError(
                                            YsharpError.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found"
                                    );

                                Object result = selected.invoke(jsp, javaArgs);

                                return new Variable.Variant(JavaObjectWrapper.wrap(result));

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
                    true, "function"
            ));
        }
    }

    public static class yScrollPaneInstance extends yClass.ClassObjectInstance {

        public final JScrollPane scrollPane;

        public yScrollPaneInstance() {
            this.scrollPane = new JScrollPane();
            this.prototype = yScrollPane_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "ScrollPane"; }
        @Override public String toString() { return "<instance:ScrollPane>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.scrollPane;
        }
    }

    public static class yScrollPaneClass extends yClass.SealedClassObject {

        public yScrollPaneClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yScrollPaneInstance());
        }

        @Override public String getClassName() { return "ScrollPane"; }
        @Override public String getType() { return "ScrollPane"; }
    }
}