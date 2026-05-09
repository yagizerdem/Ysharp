package ysharp.treewalk.evaluator.Native.YPF.Button;

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

public class yButtonGroup {

    public static yButtonGroupInstance requireButtonGroupThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yButtonGroupInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected ButtonGroup but got '" + obj.getType() + "'"
            );
        }

        return (yButtonGroupInstance) obj;
    }

    public static RuntimeObject yButtonGroup_Instance_Prototype;

    static {
        yButtonGroup_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__ButtonGroup__"; }
            @Override public String toString() { return "<prototype:ButtonGroup>"; }
        };

        yButtonGroup_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : ButtonGroup.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yButtonGroup_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yButtonGroupInstance group =
                                    yButtonGroup.requireButtonGroupThis(interpreter, name);

                            ButtonGroup jgroup = group.group;

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

                                Object result = selected.invoke(jgroup, javaArgs);

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

    public static class yButtonGroupInstance extends yClass.ClassObjectInstance {

        public final ButtonGroup group;

        public yButtonGroupInstance() {
            this.group = new ButtonGroup();
            this.prototype = yButtonGroup_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "ButtonGroup"; }
        @Override public String toString() { return "<instance:ButtonGroup>"; }
        @Override public Object getNativeJavaObject() {
            return this.group;
        }
    }

    public static class yButtonGroupClass extends yClass.SealedClassObject {

        public yButtonGroupClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yButtonGroupInstance());
        }

        @Override public String getClassName() { return "ButtonGroup"; }
        @Override public String getType() { return "ButtonGroup"; }
    }
}