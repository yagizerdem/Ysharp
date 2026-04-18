package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;

import java.util.List;

public class yStringBuffer {

    public static yStringBuffer.yStringBufferInstance requireStringBufferThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yStringBuffer.yStringBufferInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'StringBuffer' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return (yStringBuffer.yStringBufferInstance) obj;
    }

    public static RuntimeObject yStringBuffer_Instance_Prototype;

    static {
        yStringBuffer_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__StringBuffer__";
            }

            @Override
            public String toString() {
                return "<prototype:__StringBuffer__>";
            }
        };

        yStringBuffer_Instance_Prototype.prototype = yClass.ClassPrototype;


        // sb.append(value)
        class AppendFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return -1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                yStringBuffer.yStringBufferInstance sb = requireStringBufferThis(interpreter, getFnName());

                if (arguments.isEmpty()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
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

                    if (value.value instanceof Boolean) {
                        sb.builder.append((Boolean) value.value);
                    }
                    else if (value.value instanceof Number) {
                        sb.builder.append(((Number) value.value).doubleValue());
                    }
                    else if (value.value instanceof Character) {
                        sb.builder.append((Character) value.value);
                    }
                    else if (value.value instanceof String) {
                        sb.builder.append((String) value.value);
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
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'append' first argument must be an array."
                        );
                    }

                    if(!offsetVar.isIntCompatibleNumber() || !lenVar.isIntCompatibleNumber()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'append()' offset and length must be integers."
                        );
                    }

                    int offset = offsetVar.asInt();
                    int len = lenVar.asInt();

                    List<Variable.Variant> data = ((yArray.yArrayInstance) arrVar.value).data;

                    for (int i = 0; i < data.size(); i++) {
                        Variable.Variant var = data.get(i);

                        if (!var.isChar()) {
                            throw new YsharpException(
                                    YsharpException.YsharpErrorType.PROCESS,
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
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "Invalid offset/length."
                        );
                    }

                    for (int i = offset; i < offset + len; i++) {
                        sb.builder.append(list.get(i).toString());
                    }

                    return new Variable.Variant(sb);
                }

                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
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

        yStringBuffer_Instance_Prototype.set(append.getFnName(), appendVar);


        // sb.appendLine(value)
        class AppendLineFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                yStringBuffer.yStringBufferInstance sb = requireStringBufferThis(interpreter, getFnName());

                if (arguments.isEmpty()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            0,
                            "'append' expects at least 1 argument."
                    );
                }

                if (arguments.size() == 1) {
                    Variable.Variant value = arguments.getFirst();

                    if (value.value == null) {
                        sb.builder.append("null").append('\n');
                        return new Variable.Variant(sb);
                    }

                    if (value.value instanceof Boolean) {
                        sb.builder.append((Boolean) value.value).append('\n');
                    }
                    else if (value.value instanceof Number) {
                        sb.builder.append(((Number) value.value).doubleValue()).append('\n');
                    }
                    else if (value.value instanceof Character) {
                        sb.builder.append((Character) value.value).append('\n');
                    }
                    else if (value.value instanceof String) {
                        sb.builder.append((String) value.value).append('\n');
                    }
                    // fallback
                    else {
                        sb.builder.append(value.value.toString()).append('\n');
                    }

                    return new Variable.Variant(sb);
                }

                if (arguments.size() == 3) {

                    Variable.Variant arrVar = arguments.getFirst();
                    Variable.Variant offsetVar = arguments.get(1);
                    Variable.Variant lenVar = arguments.get(2);

                    if (!(arrVar.value instanceof yArray.yArrayInstance)) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'append' first argument must be an array."
                        );
                    }

                    if(!offsetVar.isIntCompatibleNumber() || !lenVar.isIntCompatibleNumber()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'append()' offset and length must be integers."
                        );
                    }


                    int offset = offsetVar.asInt();
                    int len = lenVar.asInt();

                    List<Variable.Variant> data = ((yArray.yArrayInstance) arrVar.value).data;

                    for (int i = 0; i < data.size(); i++) {
                        Variable.Variant var = data.get(i);

                        if (!var.isChar()) {
                            throw new YsharpException(
                                    YsharpException.YsharpErrorType.PROCESS,
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
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "Invalid offset/length."
                        );
                    }

                    for (int i = offset; i < offset + len; i++) {
                        sb.builder.append(list.get(i).toString()).append('\n');
                    }

                    return new Variable.Variant(sb);
                }

                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        0,
                        "Invalid arguments for append."
                );
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

        yStringBuffer_Instance_Prototype.set(appendLine.getFnName(), appendLineVar);


        // sb.insert(index, data<char|string|int|...>, offset?, len? );
        class InsertFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                yStringBuffer.yStringBufferInstance sb = requireStringBufferThis(interpreter, getFnName());

                if (arguments.size() < 2) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            0,
                            "'insert' expects at least 2 argument."
                    );
                }

                if (arguments.size() == 2) {
                    Variable.Variant index = arguments.getFirst();

                    if(!index.isIntCompatibleNumber()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'insert' index must be number."
                        );
                    }

                    Variable.Variant value = arguments.get(1);

                    if (value.value instanceof Boolean) {
                        sb.builder.insert((int)index.asInt(), (boolean) value.asBoolean());
                    }
                    else if (value.value instanceof Integer) {
                        sb.builder.insert((int)index.asInt(), (int) value.asInt());
                    }
                    else if (value.value instanceof Double) {
                        sb.builder.insert((int)index.asInt(), (double) value.asDouble());
                    }
                    else if (value.value instanceof String) {
                        sb.builder.insert((int)index.asInt(), (String) value.asString());
                    }
                    else if (value.value == null) {
                        sb.builder.insert((int)index.asInt(), "null");
                    }
                    else if (value.value instanceof yArray.yArrayInstance) {
                        var arr = (yArray.yArrayInstance) value.value;
                        for (int i = 0; i < arr.data.size(); i++) {
                            if (!arr.data.get(i).isChar()) {
                                throw new YsharpException(
                                        YsharpException.YsharpErrorType.PROCESS,
                                        0,
                                        "'insert(char[]) expected char at index " + i
                                );
                            }
                        }
                        for (int i = 0; i < arr.data.size(); i++) {
                            char c = arr.data.get(i).asCharacter();
                            sb.builder.insert(index.asInt() + i , c);
                        }
                    }
                    // fallback
                    else {
                        sb.builder.insert((int)index.asInt(), value.value.toString());
                    }

                    return new Variable.Variant(sb);
                }

                if (arguments.size() == 4) {

                    Variable.Variant indexVar = arguments.get(0);
                    Variable.Variant arrVar = arguments.get(1);
                    Variable.Variant offsetVar = arguments.get(2);
                    Variable.Variant lenVar = arguments.get(3);

                    if (!indexVar.isIntCompatibleNumber()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'insert' index must be a number."
                        );
                    }

                    int index = indexVar.asInt();

                    if (!(arrVar.value instanceof yArray.yArrayInstance)) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'insert' second argument must be an array."
                        );
                    }

                    if (!offsetVar.isIntCompatibleNumber() ||
                            !lenVar.isIntCompatibleNumber()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "'insert' offset and length must be numbers."
                        );
                    }

                    int offset = offsetVar.asInt();
                    int len = lenVar.asInt();

                    List<Variable.Variant> data =
                            ((yArray.yArrayInstance) arrVar.value).data;

                    if (offset < 0 || len < 0 || offset + len > data.size()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "Invalid offset/length."
                        );
                    }

                    if (index < 0 || index > sb.builder.length()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "Index out of bounds."
                        );
                    }

                    for (int i = offset; i < offset + len; i++) {
                        if (!data.get(i).isChar()) {
                            throw new YsharpException(
                                    YsharpException.YsharpErrorType.PROCESS,
                                    0,
                                    "'insert(char[]) expected char at index " + i
                            );
                        }
                    }

                    for (int i = 0; i < len; i++) {
                        char c = data.get(offset + i).asCharacter();
                        sb.builder.insert(index + i, c);
                    }

                    return new Variable.Variant(sb);
                }

                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        0,
                        "Invalid arguments for insert."
                );
            }

            @Override
            public String getFnName() {
                return "insert";
            }
        }

        InsertFn insert = new InsertFn();
        Variable insertVar = new Variable(
                new Variable.Variant(insert),
                true,
                "function");

        yStringBuffer_Instance_Prototype.set(insert.getFnName(), insertVar);

        // sb.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yStringBuffer.yStringBufferInstance sb = requireStringBufferThis(interpreter, getFnName());

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

        yStringBuffer_Instance_Prototype.set(toString.getFnName(), toStringVar);

        // sb.clear()
        class ClearFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yStringBuffer.yStringBufferInstance sb = requireStringBufferThis(interpreter, getFnName());

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

        yStringBuffer_Instance_Prototype.set(clear.getFnName(), clearVar);

        // sb.length()
        class LengthFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yStringBuffer.yStringBufferInstance sb =  requireStringBufferThis(interpreter, getFnName());

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

        yStringBuffer_Instance_Prototype.set(length.getFnName(), lengthVar);

    }

    public static class yStringBufferInstance extends yClass.ClassObjectInstance {

        public final StringBuffer builder;

        public yStringBufferInstance() {
            this.builder = new StringBuffer();
            this.prototype = yStringBuffer_Instance_Prototype;
        }

        public yStringBufferInstance(String initial) {
            this.builder = new StringBuffer(initial);
            this.prototype = yStringBuffer_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "StringBuffer";
        }

        @Override
        public String toString() {
            return "<instance:StringBuffer>";
        }
    }

    public static class yStringBufferClass extends yClass.SealedClassObject {

        public yStringBufferClass() {
            this.prototype = yClass.ClassPrototype;

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                throws YsharpException {

            yStringBuilder.yStringBuilderInstance instance = new yStringBuilder.yStringBuilderInstance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "StringBuffer";
        }

        @Override
        public String getType() {
            return "StringBuffer";
        }

        @Override
        public String toString() {
            return "<class:StringBuffer>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yStringBuffer.yStringBufferClass ctor = new yStringBuffer.yStringBufferClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, true, ctor.getType());
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
