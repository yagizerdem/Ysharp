package ysharp.treewalk.evaluator.Native.Util.Type.Converter;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Type.Converter.function.statix.*;

import java.util.List;

public class Converter {

    public static class ConverterClass extends yClass.SealedClassObject {

        public ConverterClass() {
            this.prototype =  yClass.ClassPrototype;

            // Type.Converter.toString(data);
            RegisterNativeFn(new ToStringFn());
            // Type.Converter.toInt(data);
            RegisterNativeFn(new ToIntFn());
            // Type.Converter.toDouble(data);
            RegisterNativeFn(new ToDoubleFn());
            // Type.Converter.toChar(data);
            RegisterNativeFn(new ToCharFn());
            // Type.Converter.toBool(data);
            RegisterNativeFn(new ToBoolFn());
            // Type.Converter.toNativeObject(data);
            RegisterNativeFn(new ToNativeObject());

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Cannot create instance of static class '" + getClassName() + "'."
            );
        }

        @Override
        public String getClassName() {
            return "Converter";
        }

        @Override
        public String getType() {
            return "Converter";
        }

        @Override
        public String toString() {
            return "<class:Converter>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        ConverterClass ctor = new ConverterClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                ctor.getType());

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
