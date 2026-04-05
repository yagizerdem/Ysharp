package ysharp.treewalk.evaluator.Native.Collections.TreeSet;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance.*;

import java.util.List;
import java.util.TreeSet;

public class yTreeSet {

    // helper
    public static yTreeSet.yTreeSetInstance requireTreeSetThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTreeSet.yTreeSetInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "This method can only be called on TreeSet objects."
            );
        }

        return (yTreeSet.yTreeSetInstance) obj;
    }

    public static RuntimeObject yTreeSet_Instance_Prototype;

    static {
        yTreeSet_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__TreeSet__";
            }

            @Override
            public String toString() {
                return "<prototype:TreeSet>";
            }
        };
        yTreeSet_Instance_Prototype.prototype = yClass.ClassPrototype;

        // ts.toString()
        yTreeSet_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // ts.add(value) -> true if added, false if already existed
        yTreeSet_Instance_Prototype.RegisterNativeFn(new AddFn());
        // ts.remove(value) -> true if removed, false if not found
        yTreeSet_Instance_Prototype.RegisterNativeFn(new RemoveFn());
        // ts.contains(value)
        yTreeSet_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // ts.first() -> smallest element
        yTreeSet_Instance_Prototype.RegisterNativeFn(new FirstFn());
        // ts.last() -> largest element
        yTreeSet_Instance_Prototype.RegisterNativeFn(new LastFn());
        // ts.floor(value) -> greatest element <= value
        yTreeSet_Instance_Prototype.RegisterNativeFn(new FloorFn());
        // ts.ceiling(value) -> smallest element >= value
        yTreeSet_Instance_Prototype.RegisterNativeFn(new CeilingFn());
        // ts.lower(value) -> greatest element strictly < value
        yTreeSet_Instance_Prototype.RegisterNativeFn(new LowerFn());
        // ts.higher(value) -> smallest element strictly > value
        yTreeSet_Instance_Prototype.RegisterNativeFn(new HigherFn());
        // ts.pollFirst() -> removes and returns smallest element
        yTreeSet_Instance_Prototype.RegisterNativeFn(new PollFirstFn());
        // ts.pollLast() -> removes and returns largest element
        yTreeSet_Instance_Prototype.RegisterNativeFn(new PollLastFn());
        // ts.subSet(from, to) -> new TreeSet with elements in [from, to)
        yTreeSet_Instance_Prototype.RegisterNativeFn(new SubSetFn());
        // ts.headSet(to) -> new TreeSet with elements strictly < to
        yTreeSet_Instance_Prototype.RegisterNativeFn(new HeadSetFn());
        // ts.tailSet(from) -> new TreeSet with elements >= from
        yTreeSet_Instance_Prototype.RegisterNativeFn(new TailSetFn());
        // ts.union(other) -> new TreeSet = this ∪ other
        yTreeSet_Instance_Prototype.RegisterNativeFn(new UnionFn());
        // ts.intersection(other) -> new TreeSet = this ∩ other
        yTreeSet_Instance_Prototype.RegisterNativeFn(new IntersectionFn());
        // ts.difference(other) -> new TreeSet = this \ other
        yTreeSet_Instance_Prototype.RegisterNativeFn(new DifferenceFn());
        // ts.isSubsetOf(other) -> true if all elements of this are in other
        yTreeSet_Instance_Prototype.RegisterNativeFn(new IsSubsetOfFn());
        // ts.toArray() -> sorted Y_ArrayObject
        yTreeSet_Instance_Prototype.RegisterNativeFn(new ToArrayFn());
        // ts.size()
        yTreeSet_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // ts.isEmpty()
        yTreeSet_Instance_Prototype.RegisterNativeFn(new IsEmptyFn());
        // ts.clear()
        yTreeSet_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // ts.clone()
        yTreeSet_Instance_Prototype.RegisterNativeFn(new CloneFn());
    }


    public static class yTreeSetInstance extends yClass.ClassObjectInstance {

        // Elements kept in sorted order — numeric keys sorted numerically, strings alphabetically
        public final TreeSet<Variable.Variant> data;

        public yTreeSetInstance() {
            this.data = new TreeSet<>((a, b) -> {
                String sa = a.toString();
                String sb = b.toString();

                try {
                    double da = Double.parseDouble(sa);
                    double db = Double.parseDouble(sb);
                    return Double.compare(da, db);
                } catch (NumberFormatException e) {
                    return sa.compareTo(sb);
                }
            });
            this.prototype = yTreeSet_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "TreeSet";
        }

        @Override
        public String toString() {
            return "<instnace:TreeSet>";
        }
    }

    public static class yTreeSetClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yTreeSetInstance newSet = new yTreeSetInstance();

            return new Variable.Variant(newSet);
        }

        @Override
        public String getClassName() {
            return "TreeSet";
        }

        @Override
        public String getType() {
            return "TreeSet";
        }

        @Override
        public String toString() {
            return "<class:TreeSet>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yTreeSet.yTreeSetClass tsCtor = new yTreeSet.yTreeSetClass();
        Variable.Variant variant = new Variable.Variant(tsCtor);
        Variable var = new Variable(variant, false, tsCtor.getType());
        interpreter.defineGlobal(tsCtor.getClassName(), var);
    }

}