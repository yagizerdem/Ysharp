package ysharp.treewalk.evaluator.Native.SQLite.Statement;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.JavaObjectWrapper;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yStatement {

    public static yStatementInstance requireStatementThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yStatementInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Statement but got '" + obj.getType() + "'"
            );
        }

        return (yStatementInstance) obj;
    }

    public static RuntimeObject yStatement_Instance_Prototype;

    static {
        yStatement_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Statement__";
            }

            @Override
            public String toString() {
                return "<prototype:Statement>";
            }
        };
        yStatement_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : Statement.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {
            yStatement_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override
                        public int arity() {
                            return -1;
                        }

                        @Override
                        public Variable.Variant call(Interpreter interpreter,
                                                     List<Variable.Variant> args)
                                throws YsharpException {

                            yStatementInstance statementInstance =
                                    yStatement.requireStatementThis(interpreter, name);

                            Statement statement = statementInstance.statement;

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

                                if (m == null) {
                                    throw new YsharpException(
                                            YsharpException.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found: " + name
                                    );
                                }

                                Object result = m.invoke(statement, javaArgs);

                                return new Variable.Variant(
                                        JavaObjectWrapper.wrap(result)
                                );

                            } catch (YsharpException e) {
                                throw e;
                            } catch (Exception e) {
                                Throwable cause = e.getCause() != null ? e.getCause() : e;

                                throw new YsharpException(
                                        YsharpException.YsharpErrorType.PROCESS,
                                        0,
                                        "Native call failed: " + name + " -> " + cause.getMessage()
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

    public static class yStatementInstance extends yClass.ClassObjectInstance {

        public final Statement statement;

        public yStatementInstance(Statement statement) {
            this.statement = statement;
            this.prototype = yStatement_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Statement";
        }

        @Override
        public String toString() {
            return "<instance:Statement>";
        }

        @Override
        public Object getNativeJavaObject() {
            return this.statement;
        }
    }

    public static class yStatementClass extends yClass.SealedClassObject {

        public yStatementClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of Statement class directly"
            );
        }

        @Override
        public String getClassName() {
            return "Statement";
        }

        @Override
        public String getType() {
            return "Statement";
        }

        @Override
        public String toString() {
            return "<class:Statement>";
        }
    }
}