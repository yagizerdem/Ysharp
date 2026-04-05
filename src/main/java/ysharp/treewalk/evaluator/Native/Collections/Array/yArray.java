package ysharp.treewalk.evaluator.Native.Collections.Array;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.function.instance.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.function.statix.*;
import ysharp.treewalk.evaluator.Native.Collections.yVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class yArray {

    // helper
    public static yArrayInstance requireArrayThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yArrayInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'array' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yArrayInstance) obj;
    }

    public static RuntimeObject yArray_Instance_Prototype;

    static {
        yArray_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Array__";
            }

            @Override
            public String toString() {
                return "<prototype:Array>";
            }
        };
        yArray_Instance_Prototype.prototype = yVector.Vector_Instance_Prototype;

        // arr.toString()
        yArray_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // arr.add(value)
        yArray_Instance_Prototype.RegisterNativeFn(new AddFn(), Arrays.asList("push")); // alias for add
        // arr.insert(index, value)
        yArray_Instance_Prototype.RegisterNativeFn(new InsertFn());
        // arr.clear()
        yArray_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // arr.clone()
        yArray_Instance_Prototype.RegisterNativeFn(new CloneFn());
        // arr.contains(value)
        yArray_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // arr.ensureCapacity(minCapacity)
        yArray_Instance_Prototype.RegisterNativeFn(new EnsureCapacityFn());
        // arr.size()
        yArray_Instance_Prototype.RegisterNativeFn(new SizeFn(), Arrays.asList("length")); // alias for size
        // arr.remove(index)
        yArray_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // arr.set(index, value)
        yArray_Instance_Prototype.RegisterNativeFn(new SetFn());
        // arr.get(index)
        yArray_Instance_Prototype.RegisterNativeFn(new GetFn());
        // arr.pop()
        yArray_Instance_Prototype.RegisterNativeFn(new PopFn());
        // arr.isEmpty();
        yArray_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // arr.indexOf(value)
        yArray_Instance_Prototype.RegisterNativeFn(new IndexOfFn());
        // arr.lastIndexOf(value)
        yArray_Instance_Prototype.RegisterNativeFn(new LastIndexOfFn());
        // arr.reverse()
        yArray_Instance_Prototype.RegisterNativeFn(new ReverseFn());
        // arr.slice(start, end)
        yArray_Instance_Prototype.RegisterNativeFn(new SliceFn());
        // arr.join(separator)
        yArray_Instance_Prototype.RegisterNativeFn(new JoinFn());
        // arr.map(callback) callback = (element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new MapFn());
        // arr.filter(callback)  callback = (element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new FilterFn());
        // arr.reduce(callback, initialValue) callback = (accumulator, element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new ReduceFn());
        // arr.find(callback)
        yArray_Instance_Prototype.RegisterNativeFn(new FindFn());
        // arr.flat(depth)
        yArray_Instance_Prototype.RegisterNativeFn(new FlatFn());
        // arr.some(callback) callback = (element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new SomeFn());
        // arr.every(callback) callback = (element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new EveryFn());
        // arr.forEach(callback) callback = (element, index, oldArray) =>
        yArray_Instance_Prototype.RegisterNativeFn(new ForEachFn());
        // arr.shift() removes first element
        yArray_Instance_Prototype.RegisterNativeFn(new ShiftFn());
        // arr.unshift(value) adds an element to the beginning of the array
        yArray_Instance_Prototype.RegisterNativeFn(new UnshiftFn());
        // arr.fill(value) replaces all elements in the array with a static value
        yArray_Instance_Prototype.RegisterNativeFn(new FillFn());
        // arr.sort(callback?) sorts the array elements in-place using a custom comparator function
        // callback = (cur, other) =>
        yArray_Instance_Prototype.RegisterNativeFn(new SortFn());
        // arr.findIndex(callback) returns the index of the first element that satisfies the testing function
        yArray_Instance_Prototype.RegisterNativeFn(new FindIndexFn());
        // arr.concat(otherArray) merges two arrays and returns a new array
        yArray_Instance_Prototype.RegisterNativeFn(new ConcatFn());
        // arr.max() returns the maximum numeric value in the array
        yArray_Instance_Prototype.RegisterNativeFn(new MaxFn());
        // arr.min() returns the minimum numeric value in the array
        yArray_Instance_Prototype.RegisterNativeFn(new MinFn());
        // arr.take(n) returns a new array with the first n elements
        yArray_Instance_Prototype.RegisterNativeFn(new TakeFn());
        // arr.skip(n) returns a new array skipping the first n elements
        yArray_Instance_Prototype.RegisterNativeFn(new SkipFn());
        // arr.sum() returns the sum of all numeric values in the array
        yArray_Instance_Prototype.RegisterNativeFn(new SumFn());
        // arr.average() returns the average of all numeric values in the array
        yArray_Instance_Prototype.RegisterNativeFn(new AverageFn());
        // arr.unique() returns a new array with duplicate elements removed
        yArray_Instance_Prototype.RegisterNativeFn(new UniqueFn(), Arrays.asList("distinct"));
        // arr.count(value) returns the number of occurrences of a specific value in the array
        yArray_Instance_Prototype.RegisterNativeFn(new CountFn());
        // arr.shuffle()
        yArray_Instance_Prototype.RegisterNativeFn(new ShuffleFn());
        // arr.asQueryable()
        yArray_Instance_Prototype.RegisterNativeFn(new AsQueryableFn());
        // arr.toNativeArray()
        yArray_Instance_Prototype.RegisterNativeFn(new ToNativeArray());
        // arr.toNativeArrayList()
        yArray_Instance_Prototype.RegisterNativeFn(new ToNativeArrayList());
    }

    public static class yArrayInstance extends yClass.ClassObjectInstance implements yVector.IVector {

        public final ArrayList<Variable.Variant> data;
        public yArrayInstance(ArrayList<Variable.Variant> data) {
            this.data = data;
            this.prototype = yArray_Instance_Prototype;
        }

        public yArrayInstance() {
            this.data = new ArrayList<>();
            this.prototype = yArray_Instance_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Array";
        }

        @Override
        public String toString() {
            return "<instance:Array>";
        }

        @Override
        public ArrayList<Variable.Variant> getData() {
            return this.data;
        }

        @Override
        public Object getNativeJavaObject() {
            ArrayList<Object> nativeArrayList = new ArrayList<>();
            for(int i = 0; i < this.data.size(); i++) {
                nativeArrayList.add(this.data.get(i).asJavaNative());
            }
            return nativeArrayList;
        }

    }

    public static class yArrayClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yArrayClass(){
            this.prototype = yClass.ClassPrototype;

            // Array.isArray(value) returns true if the value is an Array instance
            this.RegisterNativeFn(new IsArrayFn());
            // Array.of(...)
            this.RegisterNativeFn(new OfFn());
            // Array.range(start, end, step?) generates a numeric sequence array
            this.RegisterNativeFn(new RangeFn());

        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            ArrayList<Variable.Variant> value = new ArrayList<>();
            yArray.yArrayInstance newArray = new yArray.yArrayInstance(value);

            return new Variable.Variant(newArray);
        }

        @Override
        public String getClassName() {
            return "Array";
        }

        @Override
        public String getType() {
            return "Array";
        }

        @Override
        public String toString() {
            return "<class:Array>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yArrayClass arrayCtor = new yArrayClass();
        Variable.Variant variant = new Variable.Variant(arrayCtor);
        Variable var = new Variable(variant, true, arrayCtor.getType());
        interpreter.defineGlobal(arrayCtor.getClassName(), var);
    }

}
