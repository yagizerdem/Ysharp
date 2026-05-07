package ysharp.treewalk.evaluator.Native.IO;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.Directory.yDirectory;
import ysharp.treewalk.evaluator.Native.IO.File.yFile;
import ysharp.treewalk.evaluator.Native.IO.stderr.yStdErr;
import ysharp.treewalk.evaluator.Native.IO.stdin.yStdIn;
import ysharp.treewalk.evaluator.Native.IO.stdout.yStdOut;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class yIO {

    public static Path resolvePath(Interpreter interpreter, String pathText) {
        Path input = Paths.get(pathText);

        if (input.isAbsolute()) {
            return input.normalize();
        }

        Path base = Paths.get(interpreter.cwd)
                .toAbsolutePath()
                .normalize();

        return base.resolve(input).normalize();
    }


    public static RuntimeObject yIO_Prototype;

    static {
        yIO_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__IO_"; }
        };
    }

    public static class yIOClass extends yClass.ClassObject {

        public yIOClass() {
            this.prototype = yClass.ClassPrototype;

            RegisterClass(new yStdErr.yStdErrClass());
            RegisterClass(new yStdOut.yStdOutClass());
            RegisterClass(new yStdIn.yStdInClass());
            RegisterClass(new yFile.yFileClass());
            RegisterClass(new yDirectory.yDirectoryClass());
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments
        ) throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "IO is static class, cannot take instnace");
        }

        @Override
        public String getClassName() {
            return "IO";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_IO_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yIOClass ctor = new yIOClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}