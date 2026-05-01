package ysharp.treewalk.evaluator.Native.IO.file;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class yFile {

    public static RuntimeObject yFile_Prototype;

    static {
        yFile_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__FILE__";
            }
        };
    }

    public static class yFileClass extends yClass.ClassObject {

        public yFileClass() {
            this.prototype = yClass.ClassPrototype;



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

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File is static class, cannot take instance"
            );
        }

        @Override
        public String getClassName() {
            return "File";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_file_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yFileClass ctor = new yFileClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}