package ysharp.treewalk.evaluator.Native.YPF.Container.Frame;


import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import javax.swing.JFrame;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yFrame {

    public static yFrameInstance requireFrameThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yFrameInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Expected Frame but got '" + obj.getType() + "'"
            );
        }

        return (yFrameInstance) obj;
    }


    public static RuntimeObject yFrame_Instance_Prototype;

    static {
        yFrame_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__Frame__"; }

            @Override
            public String toString() { return "<prototype:Frame>"; }
        };
        yFrame_Instance_Prototype.prototype = yClass.ClassPrototype;

        // store methods on Frame class , overloads store in same array with same key name
        Map<String, List<Method>> methodMap = new HashMap<>();
        for (Method m : JFrame.class.getMethods()) {

            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {
            yFrame_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override
                        public int arity() {
                            return -1;
                        }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpError {

                            yFrame.yFrameInstance frame =
                                    yFrame.requireFrameThis(interpreter, name);

                            JFrame jframe = frame.frame;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    Variable.Variant v = args.get(i);
                                    javaArgs[i] = v.asJavaNative();
                                }

                                List<Method> availableMethods = methodMap.get(name);

                                Method m = null;
                                for(Method method : availableMethods) {
                                    if(method.getParameterCount() != args.size()) continue;
                                    Parameter[] javaParameters = method.getParameters();
                                    boolean skip = false;
                                    for (int j = 0; j < javaParameters.length; j++) {

                                        if (!isCompatible(javaParameters[j].getType(), javaArgs[j])) {
                                            skip = true;
                                            break;
                                        }
                                    }
                                    if (skip) continue;
                                    m = method;
                                    break;
                                }

                                if(m == null) {
                                    throw  new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found");
                                }

                                Object result = m.invoke(jframe, javaArgs);

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

                        @Override
                        public String getFnName() {
                            return name;
                        }
                    }),
                        true, "function"
            ));
        }
    }

    public static class yFrameInstance extends yClass.ClassObjectInstance {

        public final JFrame frame;

        public yFrameInstance() {
            this.frame = new JFrame();
            this.prototype = yFrame_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Frame"; }
        @Override public String toString() { return "<instance:Frame>"; }
        @Override public Object getNativeJavaObject() { return  this.frame; }
    }

    public static class yFrameClass extends yClass.ClassObject {

        public yFrameClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("EXIT_ON_CLOSE", new Variable(new Variable.Variant(JFrame.EXIT_ON_CLOSE), true, "int"));
            this.set("DISPOSE_ON_CLOSE", new Variable(new Variable.Variant(JFrame.DISPOSE_ON_CLOSE), true, "int"));
            this.set("DO_NOTHING_ON_CLOSE", new Variable(new Variable.Variant(JFrame.DO_NOTHING_ON_CLOSE), true, "int"));
            this.set("HIDE_ON_CLOSE", new Variable(new Variable.Variant(JFrame.HIDE_ON_CLOSE), true, "int"));
        }

        @Override
        public boolean isSealed() {
            return false;
        }

        @Override
        public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpError {

            return new Variable.Variant(new yFrameInstance());
        }

        @Override public String getClassName() { return "Frame"; }
        @Override public String getType() { return "Frame"; }
        @Override public String toString() { return "<class:Frame>"; }
    }
}