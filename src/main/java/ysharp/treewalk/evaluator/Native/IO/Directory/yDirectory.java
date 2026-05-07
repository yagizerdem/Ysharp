package ysharp.treewalk.evaluator.Native.IO.Directory;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.Directory.function.statix.*;

import java.util.List;

public class yDirectory {

    public static RuntimeObject yDirectory_Prototype;

    static {
        yDirectory_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__DIRECTORY__";
            }
        };
    }

    public static class yDirectoryClass extends yClass.ClassObject {

        public yDirectoryClass() {
            this.prototype = yClass.ClassPrototype;

             this.RegisterNativeFn(new CreateFn());
             this.RegisterNativeFn(new CreateAllFn());
             this.RegisterNativeFn(new ExistsFn());
             this.RegisterNativeFn(new DeleteFn());
             this.RegisterNativeFn(new ListFn());

             // special folders
            this.RegisterNativeFn(new GetHomeFn());
            this.RegisterNativeFn(new GetCurrentFn());
            this.RegisterNativeFn(new GetTempFn());
            this.RegisterNativeFn(new GetAppDataFn());
            this.RegisterNativeFn(new GetConfigFn());
            this.RegisterNativeFn(new GetCacheFn());
            this.RegisterNativeFn(new GetDocumentsFn());
            this.RegisterNativeFn(new GetDesktopFn());
            this.RegisterNativeFn(new GetDownloadsFn());
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
                    "Directory is static class, cannot take instance"
            );
        }

        @Override
        public String getClassName() {
            return "Directory";
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public String getType() {
            return "_Directory_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yDirectoryClass ctor = new yDirectoryClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}