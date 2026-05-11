package ysharp.treewalk.evaluator.Native.YPF.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.YPF.List.yList;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yBox {


    public static yBox.yBoxInstance requireBoxThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yBox.yBoxInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Box but got '" + obj.getType() + "'"
            );
        }

        return (yBoxInstance) obj;
    }

    public static RuntimeObject yBox_Instance_Prototype;

    static {
        yBox_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Box__";
            }

            @Override
            public String toString() {
                return "<prototype:Box>";
            }
        };

        yBox_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : Box.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            if (Modifier.isStatic(m.getModifiers())) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yBox_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yBox.yBoxInstance yboxWrapper =
                                    yBox.requireBoxThis(interpreter, name);

                            Box box_ = (Box) yboxWrapper.box;

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

                                Object result = selected.invoke(box_, javaArgs);

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

    public static class yBoxInstance extends yClass.ClassObjectInstance {

        public Object box;

        public yBoxInstance(Object box) {
            this.box = box;
            this.prototype = yBox_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Box";
        }

        @Override
        public String toString() {
            return "<instance:Box>";
        }

        @Override
        public Object getNativeJavaObject() {
            return this.box;
        }
    }



    public static class yBoxClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yBoxClass() {
            this.prototype = yClass.ClassPrototype;

            Map<String, List<Method>> methodMap = new HashMap<>();

            for (Method m : Box.class.getMethods()) {
                if (m.getDeclaringClass() == Object.class) continue;

                if (!Modifier.isStatic(m.getModifiers())) continue;

                methodMap
                        .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                        .add(m);
            }

            for (String name : methodMap.keySet()) {

                this.set(name, new Variable(
                        new Variable.Variant(new Function.NativeFunction() {

                            @Override public int arity() { return -1; }

                            @Override
                            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                    throws YsharpException {

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
                                                "method overload not found: Box." + name
                                        );
                                    }

                                    Object result = selected.invoke(null, javaArgs);

                                    return new Variable.Variant(
                                            JavaObjectWrapper.wrap(result)
                                    );

                                } catch (YsharpException e) {
                                    throw e;
                                } catch (Exception e) {
                                    throw new YsharpException(
                                            YsharpException.YsharpErrorType.PROCESS,
                                            0,
                                            "Native static call failed: Box." + name + " -> " + e.getMessage()
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

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "YPF.Box is static class, cannot take instance with new expression of static classes"
            );
        }

        @Override
        public String getClassName() {
            return "Box";
        }

        @Override
        public String getType() {
            return "_Box_";
        }

        @Override
        public String toString() {
            return "<class:Box>";
        }
    }
}