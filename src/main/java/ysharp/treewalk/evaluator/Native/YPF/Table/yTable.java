package ysharp.treewalk.evaluator.Native.YPF.Table;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Native.YPF.yComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.List;

public class yTable {

    public static yTableInstance requireTableThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTableInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected Table but got '" + obj.getType() + "'"
            );
        }

        return (yTableInstance) obj;
    }

    public static RuntimeObject yTable_Instance_Prototype;

    static {
        yTable_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Table__"; }
            @Override public String toString() { return "<prototype:Table>"; }
        };
        yTable_Instance_Prototype.prototype = yComponent.yComponent_Instance_Prototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JTable.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {

            yTable_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override public int arity() { return -1; }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yTableInstance table =
                                    yTable.requireTableThis(interpreter, name);

                            JTable jt = table.table;

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

                                Object result = selected.invoke(jt, javaArgs);

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

        yTable_Instance_Prototype.set("addRow", new Variable(
                new Variable.Variant(new Function.NativeFunction() {

                    @Override public int arity() { return 1; }

                    @Override
                    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                            throws YsharpException {

                        yTableInstance table = requireTableThis(interpreter, "addRow");

                        Object[] row = (Object[]) args.getFirst().asJavaNative();

                        table.model.addRow(row);

                        return new Variable.Variant(null);
                    }

                    @Override public String getFnName() { return "addRow"; }

                }),
                true,
                "function"
        ));
    }

    public static class yTableInstance extends yClass.ClassObjectInstance  implements yComponent.yBaseComponent {

        public final JTable table;
        public final DefaultTableModel model;

        public yTableInstance() {
            this.model = new DefaultTableModel();
            this.table = new JTable(model);
            this.prototype = yTable_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Table"; }
        @Override public String toString() { return "<instance:Table>"; }
        @Override public Object getNativeJavaObject() {
            return this.table;
        }
        @Override public Component getComponent() { return this.table;}
    }

    public static class yTableClass extends yClass.SealedClassObject {

        public yTableClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override public int arity() { return 0; }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            return new Variable.Variant(new yTableInstance());
        }

        @Override public String getClassName() { return "Table"; }
        @Override public String getType() { return "Table"; }
    }
}