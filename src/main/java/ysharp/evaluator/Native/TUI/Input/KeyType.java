package ysharp.evaluator.Native.TUI.Input;

import ysharp.YsharpError;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Variable;
import ysharp.evaluator.yClass;

import java.util.List;

public class KeyType {


    static class yKeyTypeEnum extends yClass.SealedClassObject {
        @Override
        public String getType() {
            return "KeyType";
        }

        @Override
        public String getClassName() {
            return "KeyType";
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                    -1 ,
                    "cannot take instance of static KeyType class");
        }
    }
}
