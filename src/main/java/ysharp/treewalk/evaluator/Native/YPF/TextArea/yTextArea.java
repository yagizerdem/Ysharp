package ysharp.treewalk.evaluator.Native.YPF.TextArea;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class yTextArea {

    public static yTextAreaInstance requireTextAreaThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTextAreaInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected TextArea but got '" + obj.getType() + "'"
            );
        }

        return (yTextAreaInstance) obj;
    }

    public static RuntimeObject yTextArea_Instance_Prototype;

    static {
        yTextArea_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__TextArea__"; }
            @Override public String toString() { return "<prototype:TextArea>"; }
        };

        yTextArea_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JTextArea.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yTextArea_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yTextAreaInstance ta =
                                    yTextArea.requireTextAreaThis(interpreter, name);

                            JTextArea jta = ta.textArea;

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

                                Object result = selected.invoke(jta, javaArgs);

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

    public static class yTextAreaInstance extends yClass.ClassObjectInstance {

        public final JTextArea textArea;

        public yTextAreaInstance() {
            this.textArea = new JTextArea();
            this.prototype = yTextArea_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "TextArea"; }
        @Override public String toString() { return "<instance:TextArea>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.textArea;
        }
    }

    public static class yTextAreaClass extends yClass.SealedClassObject {

        public yTextAreaClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yTextAreaInstance());
        }

        @Override public String getClassName() { return "TextArea"; }
        @Override public String getType() { return "TextArea"; }
    }
}