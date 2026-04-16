package ysharp.treewalk.evaluator.Native.TUI.Input;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

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
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                    -1 ,
                    "cannot take instance of static KeyType class");
        }
    }
}
