package ysharp.treewalk.evaluator.Native.Util.Regex;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Math.yMath;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;
import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.yPattern;
import ysharp.treewalk.evaluator.Native.Util.yCrypto;
import ysharp.treewalk.evaluator.Native.Util.yFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class yNeedle {


    public static class yNeedleClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of static Needle class");
        }


        yNeedleClass() {
            this.prototype = yClass.ClassPrototype;

            this.prototype.RegisterClass(new yPattern.yPatternClass());
            this.prototype.RegisterClass(new yMatcher.yMatcherClass());
        }

        @Override
        public String getClassName() {
            return "Needle";
        }

        @Override
        public String getType() {
            return "_Needle_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yNeedle.yNeedleClass ctor = new yNeedle.yNeedleClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(),var);
    }
}
