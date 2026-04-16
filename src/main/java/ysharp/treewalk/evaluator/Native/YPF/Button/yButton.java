package ysharp.treewalk.evaluator.Native.YPF.Button;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import javax.swing.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yButton {

    public static yButtonInstance requireButtonThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yButtonInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Button but got '" + obj.getType() + "'"
            );
        }

        return (yButtonInstance) obj;
    }

    public static RuntimeObject yButton_Instance_Prototype;

    static {
        yButton_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Button__"; }
            @Override public String toString() { return "<prototype:Button>"; }
        };

        yButton_Instance_Prototype.prototype = yButton_Instance_Prototype;


        Map<String, List<Method>> methodMap = new HashMap<>();
        for (Method m : JButton.class.getMethods()) {

            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {
            yButton_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override
                        public int arity() {
                            return -1;
                        }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yButton.yButtonInstance button =
                                    yButton.requireButtonThis(interpreter, name);

                            JButton jbutton = button.button;

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
                                    throw  new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found");
                                }

                                Object result = m.invoke(jbutton, javaArgs);

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

                        @Override
                        public String getFnName() {
                            return name;
                        }
                    }),
                    true, "function"
            ));
        }

    }

    public static class yButtonInstance extends yClass.ClassObjectInstance  {

        public final JButton button;

        public yButtonInstance() {
            this.button = new JButton();
            this.prototype = yButton_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Button"; }
        @Override public String toString() { return "<instance:Button>"; }
        @Override public Object getNativeJavaObject() {return this.button; }
    }

    public static class yButtonClass extends yClass.SealedClassObject {

        public yButtonClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yButtonInstance());
        }

        @Override public String getClassName() { return "Button"; }
        @Override public String getType() { return "Button"; }
    }
}