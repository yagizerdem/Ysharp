package ysharp.treewalk.evaluator.Native.Collections;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import java.util.List;

// prototype for all indexed based data structures
public class yVector {

    public interface IVector  {
        public List<Variable.Variant> getData();
    }

    // helper
    public static yVector.IVector requireVectorThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yVector.IVector)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'Vector' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yVector.IVector) obj;
    }


    public static VectorIteratorInstance requireVectorIteratorThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof VectorIteratorInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'VectorIterator' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (VectorIteratorInstance) obj;
    }


    public static  RuntimeObject Vector_Iterator_Instance_Prototype;

    static {
        Vector_Iterator_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__VectorIterator__";
            }

            @Override
            public String toString() {
                return "<prototype:VectorIterator>";
            }
        };
        Vector_Iterator_Instance_Prototype.prototype = yClass.ClassPrototype;

        // vector.getNext()
        class GetNextFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                VectorIteratorInstance iterator = requireVectorIteratorThis(interpreter, getFnName());
                Variable.Variant arrValue = iterator.getNext();
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
        Vector_Iterator_Instance_Prototype.set(getNext.getFnName(), getNextVar);
    }

    public static class VectorIteratorInstance extends yClass.SealedClassObject {
        @Override
        public String getClassName() {
            return "VectorIterator";
        }

        @Override
        public String getType() {
            return "VectorIterator";
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            return null;
        }

        public int cursor = 0;
        public final List<Variable.Variant> data;

        public VectorIteratorInstance(int cursor, List<Variable.Variant> data) {
            this.cursor = cursor;
            this.data = data;
            this.prototype = Vector_Iterator_Instance_Prototype;
        }

        public VectorIteratorInstance(List<Variable.Variant> data) {
            this.cursor = 0;
            this.data = data;
            this.prototype = Vector_Iterator_Instance_Prototype;
        }

        public Variable.Variant getNext() {
            if(this.data.size() <= this.cursor) {
                this.cursor = 0; // reset iterator
                return null;
            };
            var result = this.data.get(this.cursor);
            cursor++;
            return result;
        }

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
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                requireArity(arguments, arity(), getFnName());
                yVector.IVector vector = requireVectorThis(interpreter, getFnName());

                VectorIteratorInstance iteratorInstance = new VectorIteratorInstance(vector.getData());

                return new Variable.Variant(iteratorInstance);
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

    }

}
