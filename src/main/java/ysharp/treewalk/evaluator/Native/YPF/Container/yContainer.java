package ysharp.treewalk.evaluator.Native.YPF.Container;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import java.awt.Container;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class yContainer {

    public static yContainerInstance requireContainerThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yContainerInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Container but got '" + obj.getType() + "'"
            );
        }

        return (yContainerInstance) obj;
    }

    public static RuntimeObject yContainer_Instance_Prototype;

    static {
        yContainer_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Container__"; }
            @Override public String toString() { return "<prototype:Container>"; }
        };

        yContainer_Instance_Prototype.prototype = yContainer_Instance_Prototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : Container.class.getMethods()) {

            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yContainer_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override
                        public int arity() {
                            return -1;
                        }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yContainerInstance container =
                                    yContainer.requireContainerThis(interpreter, name);

                            Container jcontainer = container.container;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    Variable.Variant v = args.get(i);
                                    javaArgs[i] = v.asJavaNative();
                                }

                                List<Method> availableMethods = methodMap.get(name);

                                Method m = null;

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

                                    m = method;
                                    break;
                                }

                                if (m == null) {
                                    throw new YsharpException(
                                            YsharpException.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found"
                                    );
                                }

                                Object result = m.invoke(jcontainer, javaArgs);

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
                    true,
                    "function"
            ));
        }
    }

    public static class yContainerInstance extends yClass.ClassObjectInstance {

        public final Container container;

        public yContainerInstance() {
            this.container = new Container();
            this.prototype = yContainer_Instance_Prototype;
        }

        public yContainerInstance(Container container) {
            this.container = container;
            this.prototype = yContainer_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Container"; }
        @Override public String toString() { return "<instance:Container>"; }
        @Override public Object getNativeJavaObject() { return this.container; }
    }

    public static class yContainerClass extends yClass.SealedClassObject {

        public yContainerClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yContainerInstance());
        }

        @Override public String getClassName() { return "Container"; }
        @Override public String getType() { return "Container"; }
    }
}