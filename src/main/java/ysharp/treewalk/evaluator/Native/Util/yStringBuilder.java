package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

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
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());

                yStringBuilderInstance sb = requireStringBuilderThis(interpreter, getFnName());

                Variable.Variant value = arguments.get(0);

                if (value.value != null) {
                    sb.builder.append(value.value.toString());
                }

                return new Variable.Variant(sb);
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