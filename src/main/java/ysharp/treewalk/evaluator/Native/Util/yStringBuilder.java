package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;

import java.util.List;

public class yStringBuilder {

    public static yStringBuilderInstance requireStringBuilderThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yStringBuilderInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'StringBuilder' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return (yStringBuilderInstance) obj;
    }

    public static RuntimeObject yStringBuilder_Instance_Prototype;

    static {
        yStringBuilder_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__StringBuilder__";
            }

            @Override
            public String toString() {
                return "<prototype:StringBuilder>";
            }
        };

        yStringBuilder_Instance_Prototype.prototype = yClass.ClassPrototype;

        // sb.append(value)
        class AppendFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return -1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                if (arguments.isEmpty()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'append' expects at least 1 argument."
                    );
                }

                if (arguments.size() == 1) {
                    Variable.Variant value = arguments.getFirst();

                    if (value.value == null) {
                        sb.builder.append("null");
                        return new Variable.Variant(sb);
                    }

                    // boolean
                    if (value.value instanceof Boolean) {
                        sb.builder.append((Boolean) value.value);
                    }
                    // number (int / double)
                    else if (value.value instanceof Number) {
                        sb.builder.append(((Number) value.value).doubleValue());
                    }
                    // char (senin sistemine bağlı)
                    else if (value.value instanceof Character) {
                        sb.builder.append((Character) value.value);
                    }
                    // string
                    else if (value.value instanceof String) {
                        sb.builder.append((String) value.value);
                    }
                    // array
                    else if (value.value instanceof List<?>) {
                        List<?> list = (List<?>) value.value;
                        for (Object obj : list) {
                            sb.builder.append(obj.toString());
                        }
                    }
                    // fallback
                    else {
                        sb.builder.append(value.value.toString());
                    }

                    return new Variable.Variant(sb);
                }

                if (arguments.size() == 3) {

                    Variable.Variant arrVar = arguments.getFirst();
                    Variable.Variant offsetVar = arguments.get(1);
                    Variable.Variant lenVar = arguments.get(2);

                    if (!(arrVar.value instanceof yArray.yArrayInstance)) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "'append' first argument must be an array."
                        );
                    }

                    int offset = (int) offsetVar.implicitlyConvertNumber();
                    int len = (int) lenVar.implicitlyConvertNumber();

                    List<Variable.Variant> data = ((yArray.yArrayInstance) arrVar.value).data;

                    for (int i = 0; i < data.size(); i++) {
                        Variable.Variant var = data.get(i);

                        if (!var.isChar()) {
                            throw new YsharpError(
                                    YsharpError.YsharpErrorType.PROCESS,
                                    0,
                                    "'append(char[]) expected char at index " + i +
                                            " but got: " +
                                            (var.value == null ? "null" : var.value.getClass().getSimpleName())
                            );
                        }
                    }

                    List<Character> list = data.stream().map(var -> {
                        return  var.asCharacter();
                    }).toList();

                    if (offset < 0 || offset + len > list.size()) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "Invalid offset/length."
                        );
                    }

                    for (int i = offset; i < offset + len; i++) {
                        sb.builder.append(list.get(i).toString());
                    }

                    return new Variable.Variant(sb);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        0,
                        "Invalid arguments for append."
                );
            }

            @Override
            public String getFnName() {
                return "append";
            }
        }

        AppendFn append = new AppendFn();
        Variable appendVar = new Variable(
                new Variable.Variant(append),
                true,
                "function");

        yStringBuilder_Instance_Prototype.set(append.getFnName(), appendVar);

        // sb.appendLine(value)
        class AppendLineFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                Variable.Variant value = arguments.getFirst();

                if (value.value != null) {
                    sb.builder.append(value.value.toString() + "\n");
                }

                return new Variable.Variant(sb);
            }

            @Override
            public String getFnName() {
                return "appendLine";
            }
        }

        AppendLineFn appendLine = new AppendLineFn();
        Variable appendLineVar = new Variable(
                new Variable.Variant(appendLine),
                true,
                "function");

        yStringBuilder_Instance_Prototype.set(appendLine.getFnName(), appendLineVar);

        // sb.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                return new Variable.Variant(
                        new yString.yStringInstance(sb.builder.toString())
                );
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                "function");

        yStringBuilder_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // sb.clear()
        class ClearFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                sb.builder.setLength(0);

                return new Variable.Variant(sb);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(
                new Variable.Variant(clear),
                true,
                "function");

        yStringBuilder_Instance_Prototype.set(clear.getFnName(), clearVar);


        // sb.length()
        class LengthFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                return new Variable.Variant(sb.builder.length());
            }

            @Override
            public String getFnName() {
                return "length";
            }
        }

        LengthFn length = new LengthFn();
        Variable lengthVar = new Variable(
                new Variable.Variant(length),
                true,
                "function");

        yStringBuilder_Instance_Prototype.set(length.getFnName(), lengthVar);
    }

    public static class yStringBuilderInstance extends yClass.ClassObjectInstance {

        public final StringBuilder builder;

        public yStringBuilderInstance() {
            this.builder = new StringBuilder();
            this.prototype = yStringBuilder_Instance_Prototype;
        }

        public yStringBuilderInstance(String initial) {
            this.builder = new StringBuilder(initial);
            this.prototype = yStringBuilder_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "StringBuilder";
        }

        @Override
        public String toString() {
            return "<instance:StringBuilder>";
        }
    }

    public static class yStringBuilderClass extends yClass.SealedClassObject {

        public yStringBuilderClass() {
            this.prototype = yClass.ClassPrototype;

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                throws YsharpError {

            yStringBuilderInstance instance = new yStringBuilderInstance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "StringBuilder";
        }

        @Override
        public String getType() {
            return "StringBuilder";
        }

        @Override
        public String toString() {
            return "<class:StringBuilder>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yStringBuilderClass ctor = new yStringBuilderClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, true, ctor.getType());
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}