package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import java.util.List;

// prototype for all indexed based data structures
public class Vector {

    public interface IVector  {
        public List<Variable.Variant> getData();
        public yClass.ClassObjectInstance getConcreteImplementation();
        public Variable.Variant getNext();
    }

    // helper
    public static Vector.IVector requireVectorThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Vector.IVector)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'vector' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (Vector.IVector) obj;
    }

    public static RuntimeObject Vector_Instance_Prototype;

    static {
        Vector_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Vector__";
            }

            @Override
            public String toString() {
                return "<prototype:Vector>";
            }
        };
        Vector_Instance_Prototype.prototype = yClass.ClassPrototype;

        // vector.iter()
        class IterFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                Vector.IVector vector = requireVectorThis(interpreter, getFnName());

                return new Variable.Variant(vector.getConcreteImplementation());
            }

            @Override
            public String getFnName() {
                return "iter";
            }
        }

        IterFn iter = new IterFn();
        Variable iterVar = new Variable(
                new Variable.Variant(iter),
                true,
                "function");
        Vector_Instance_Prototype.set(iter.getFnName(), iterVar);

        // vector.getNext()
        class GetNextFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                Vector.IVector vector = requireVectorThis(interpreter, getFnName());
                Variable.Variant arrValue = vector.getNext();
                if(arrValue == null) return new Variable.Variant(null);

                return new Variable.Variant(arrValue.value);
            }

            @Override
            public String getFnName() {
                return "getNext";
            }
        }

        GetNextFn getNext = new GetNextFn();
        Variable getNextVar = new Variable(
                new Variable.Variant(getNext),
                true,
                "function");
        Vector_Instance_Prototype.set(getNext.getFnName(), getNextVar);
    }

}
