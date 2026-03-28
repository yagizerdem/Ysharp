package ysharp.evaluator.Native.YPF.Button;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Function;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class yRadioButton {

    public static yRadioButtonInstance requireRadioButtonThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yRadioButtonInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected RadioButton but got '" + obj.getType() + "'"
            );
        }

        return (yRadioButtonInstance) obj;
    }

    public static RuntimeObject yRadioButton_Instance_Prototype;

    static {
        yRadioButton_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__RadioButton__"; }
            @Override public String toString() { return "<prototype:RadioButton>"; }
        };

        yRadioButton_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JRadioButton.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yRadioButton_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yRadioButtonInstance rb =
                                    yRadioButton.requireRadioButtonThis(interpreter, name);

                            JRadioButton jrb = rb.radioButton;

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

                                Object result = selected.invoke(jrb, javaArgs);

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

    public static class yRadioButtonInstance extends yClass.ClassObjectInstance {

        public final JRadioButton radioButton;

        public yRadioButtonInstance() {
            this.radioButton = new JRadioButton();
            this.prototype = yRadioButton_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "RadioButton"; }
        @Override public String toString() { return "<instance:RadioButton>"; }

        @Override
        public Object getNativeJavaObject() {
            return this.radioButton;
        }
    }

    public static class yRadioButtonClass extends yClass.SealedClassObject {

        public yRadioButtonClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yRadioButtonInstance());
        }

        @Override public String getClassName() { return "RadioButton"; }
        @Override public String getType() { return "RadioButton"; }
    }
}