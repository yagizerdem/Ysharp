package ysharp.treewalk.evaluator.Native.IO.File;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.File.function.statix.AppendFn;
import ysharp.treewalk.evaluator.Native.IO.File.function.statix.DeleteFn;
import ysharp.treewalk.evaluator.Native.IO.File.function.statix.ReadFn;
import ysharp.treewalk.evaluator.Native.IO.File.function.statix.WriteFn;

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

            this.prototype.RegisterNativeFn(new ReadFn());
            this.prototype.RegisterNativeFn(new WriteFn());
            this.prototype.RegisterNativeFn(new AppendFn());
            this.prototype.RegisterNativeFn(new DeleteFn());
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
            return "_File_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yFileClass ctor = new yFileClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}