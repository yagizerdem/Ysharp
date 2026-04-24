package ysharp.treewalk.evaluator.Native.Util.Regex.Pattern;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;

import java.util.regex.Pattern;

import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.function.instance.*;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;
import java.util.List;


public class yPattern {


    public static yPattern.yPatternInstance requirePatternThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yPattern.yPatternInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'pattern' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yPattern.yPatternInstance) obj;
    }

    public static RuntimeObject yPattern_Instance_Prototype;

    static {
        yPattern_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Pattern__";
            }

            @Override
            public String toString() {
                return "<prototype:Pattern>";
            }
        };
        yPattern_Instance_Prototype.prototype = yClass.ClassPrototype;

        yPattern_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        yPattern_Instance_Prototype.RegisterNativeFn(new MatcherFn());
        yPattern_Instance_Prototype.RegisterNativeFn(new FlagsFn());
        yPattern_Instance_Prototype.RegisterNativeFn(new SplitFn());
    }
    public static class yPatternInstance extends yClass.ClassObjectInstance {

        public final Pattern pattern;
        public yPatternInstance(String pattern, int flags) {
            this.prototype = yPattern_Instance_Prototype;

            this.pattern = java.util.regex.Pattern.compile(pattern, flags);
        }

        public yPatternInstance(Pattern pattern) {
            this.prototype = yPattern_Instance_Prototype;

            this.pattern = pattern;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Pattern";
        }

        @Override
        public String toString() {
            return "<instance:Pattern>";
        }
    }

    public static class yPatternClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return -1;
        }

        public yPatternClass(){
            this.prototype = yClass.ClassPrototype;

            // register pattern flags
            this.set("CASE_INSENSITIVE", new Variable(
                    new Variable.Variant(Pattern.CASE_INSENSITIVE),
                    true, "int"));

            this.set("MULTILINE", new Variable(
                    new Variable.Variant(Pattern.MULTILINE),
                    true, "int"));

            this.set("DOTALL", new Variable(
                    new Variable.Variant(Pattern.DOTALL),
                    true, "int"));

            this.set("UNICODE_CASE", new Variable(
                    new Variable.Variant(Pattern.UNICODE_CASE),
                    true, "int"));

            this.set("COMMENTS", new Variable(
                    new Variable.Variant(Pattern.COMMENTS),
                    true, "int"));

            this.set("UNIX_LINES", new Variable(
                    new Variable.Variant(Pattern.UNIX_LINES),
                    true, "int"));


            this.set("LITERAL", new Variable(
                    new Variable.Variant(Pattern.LITERAL),
                    true, "int"));

            this.set("CANON_EQ", new Variable(
                    new Variable.Variant(Pattern.CANON_EQ),
                    true, "int"));

        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

            if (arguments.isEmpty()) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "Pattern constructor requires at least 1 argument: pattern (string)."
                );
            }

            if (arguments.size() > 2) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "Pattern constructor accepts at most 2 arguments: pattern (string), flags (int)."
                );
            }

            String pattern = requireString(arguments.getFirst(), getClassName(), 1);
            int flags = 0; // default flag
            if(arguments.size() == 2) flags = requireInt(arguments.get(1), getClassName(), 2);
            yPatternInstance instance = new yPatternInstance(pattern, flags);
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "Pattern";
        }

        @Override
        public String getType() {
            return "_Pattern_";
        }

        @Override
        public String toString() {
            return "<class:Pattern>";
        }
    }

}
