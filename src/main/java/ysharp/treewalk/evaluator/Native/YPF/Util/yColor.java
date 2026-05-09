package ysharp.treewalk.evaluator.Native.YPF.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.function.statix.RangeFn;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class yColor {

    public static class yColorClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yColorClass(){
            this.prototype = yClass.ClassPrototype;
            this.RegisterNativeFn(new RangeFn());

            // add all java awt.Color's
            for (Field field : Color.class.getFields()) {

                if (field.getType() != Color.class) continue;

                try {
                    Color c = (Color) field.get(null);

                    this.set(
                            field.getName(),
                            new Variable(
                                    new Variable.Variant(c),
                                    true,
                                    "Color"
                            )
                    );

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1 ,
                    "YPF.Color is static class, cannot take instance with new expression of static classes");
        }

        @Override
        public String getClassName() {
            return "Color";
        }

        @Override
        public String getType() {
            return "Color";
        }

        @Override
        public String toString() {
            return "<class:Color>";
        }
    }
}
