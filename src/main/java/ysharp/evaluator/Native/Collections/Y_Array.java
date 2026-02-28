package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.parser.Stmt;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;

public class Y_Array {

    // helper
    private static Y_ArrayObject requireArrayThis (Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method 'add' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_ArrayObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'add' can only be called on array objects."
            );
        }

        return  (Y_ArrayObject) obj;
    }


    public static RuntimeObject Y_Array_Prototype;

    static {
        Y_Array_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "array_prototype";
            }
        };

        // arr.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                Y_ArrayObject array = requireArrayThis(interpreter);

                StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                for(int i = 0; i < array.data.size(); i++) {
                    Variable.Variant var = array.data.get(i);
                    if(var.value instanceof RuntimeObject) {
                        Variable toStringFn = ((RuntimeObject) var.value).get("toString");
                        if(toStringFn != null && toStringFn.value.isNativeFunction()) {
                            BoundNativeFunction bound = new BoundNativeFunction(toStringFn.value.asNativeFunction(), var.asRuntimeObject());
                            List<Variable.Variant> args = new ArrayList<>();
                            builder.append(bound.call(interpreter, args));
                        }
                        else {
                            builder.append("<class>");
                        }
                    }
                    else {
                        builder.append(var.value.toString());
                    }

                    builder.append(" ");
                    if(i != array.data.size() -1) {
                        builder.append(", ");
                    }
                }
                builder.append("]");

                return new Variable.Variant(builder.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                TypeTag.OBJECT);
        Y_Array_Prototype.set(toString.getFnName(), toStringVar);

        // arr.add(value)
        class AddFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                Variable.Variant value = arguments.get(0);
                Y_ArrayObject array = requireArrayThis(interpreter);
                array.data.add(value);

                return new Variable.Variant(array.data.size());
            }

            @Override
            public String getFnName() {
                return "add";
            }
        }

        AddFn add = new AddFn();
        Variable addVar = new Variable(
                new Variable.Variant(add),
                true,
                TypeTag.OBJECT);
        Y_Array_Prototype.set(add.getFnName(), addVar);
    }

    public static class Y_ArrayObject extends RuntimeObject {

        private final ArrayList<Variable.Variant> data;

        public Y_ArrayObject(ArrayList<Variable.Variant> data) {
            this.data = data;
            this.prototype = Y_Array_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return !data.isEmpty();
        }

        @Override
        public String getType() {
            return "array";
        }

        @Override
        public String toString() {
            return "array";
        }
    }

    public static class Y_ArrayInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            ArrayList<Variable.Variant> value = new ArrayList<>();
            Y_Array.Y_ArrayObject newArray = new Y_Array.Y_ArrayObject(value);
            newArray.prototype = Y_Array_Prototype;

            return new Variable.Variant(newArray);
        }

        @Override
        public String getFnName() {
            return "Array";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Y_Array.Y_ArrayInit arrayCtor = new Y_Array.Y_ArrayInit();
        Variable.Variant variant = new Variable.Variant(arrayCtor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);
        interpreter.defineGlobal(arrayCtor.getFnName(), var);
    }

}
