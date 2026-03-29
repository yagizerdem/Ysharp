package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.Collections.Array.yArray;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class yTreeSet {

    // helper
    private static yTreeSet.yTreeSetInstance requireTreeSetThis(Interpreter interpreter) {
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
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                StringBuilder sb = new StringBuilder();
                sb.append("TreeSet[");

                boolean first = true;
                for (Variable.Variant v : ts.data) {
                    if (!first) sb.append(", ");
                    first = false;
                    sb.append(v.toString());
                }

                sb.append("]");

                return new Variable.Variant(sb.toString());
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(new Variable.Variant(toString), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(toString.getFnName(), toStringVar);


        // ts.add(value) -> true if added, false if already existed
        class AddFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.add");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                return new Variable.Variant(ts.data.add(value));
            }

            @Override
            public String getFnName() {
                return "add";
            }
        }

        AddFn add = new AddFn();
        Variable addVar = new Variable(new Variable.Variant(add), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(add.getFnName(), addVar);


        // ts.remove(value) -> true if removed, false if not found
        class RemoveFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.remove");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                return new Variable.Variant(ts.data.remove(value));
            }

            @Override
            public String getFnName() {
                return "remove";
            }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(new Variable.Variant(remove), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(remove.getFnName(), removeVar);


        // ts.contains(value)
        class ContainsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.contains");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                return new Variable.Variant(ts.data.contains(value));
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(new Variable.Variant(contains), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(contains.getFnName(), containsVar);


        // ts.first() -> smallest element
        class FirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.first");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                if (ts.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return ts.data.first();
            }

            @Override
            public String getFnName() {
                return "first";
            }
        }

        FirstFn first = new FirstFn();
        Variable firstVar = new Variable(new Variable.Variant(first), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(first.getFnName(), firstVar);


        // ts.last() -> largest element
        class LastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.last");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                if (ts.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return ts.data.last();
            }

            @Override
            public String getFnName() {
                return "last";
            }
        }

        LastFn last = new LastFn();
        Variable lastVar = new Variable(new Variable.Variant(last), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(last.getFnName(), lastVar);


        // ts.floor(value) -> greatest element <= value
        class FloorFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.floor");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                Variable.Variant result = ts.data.floor(value);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "floor";
            }
        }

        FloorFn floor = new FloorFn();
        Variable floorVar = new Variable(new Variable.Variant(floor), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(floor.getFnName(), floorVar);


        // ts.ceiling(value) -> smallest element >= value
        class CeilingFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.ceiling");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                Variable.Variant result = ts.data.ceiling(value);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "ceiling";
            }
        }

        CeilingFn ceiling = new CeilingFn();
        Variable ceilingVar = new Variable(new Variable.Variant(ceiling), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(ceiling.getFnName(), ceilingVar);


        // ts.lower(value) -> greatest element strictly < value
        class LowerFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.lower");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                Variable.Variant result = ts.data.lower(value);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "lower";
            }
        }

        LowerFn lower = new LowerFn();
        Variable lowerVar = new Variable(new Variable.Variant(lower), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(lower.getFnName(), lowerVar);


        // ts.higher(value) -> smallest element strictly > value
        class HigherFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.higher");

                Variable.Variant value = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                Variable.Variant result = ts.data.higher(value);

                return result != null ? result : new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "higher";
            }
        }

        HigherFn higher = new HigherFn();
        Variable higherVar = new Variable(new Variable.Variant(higher), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(higher.getFnName(), higherVar);


        // ts.pollFirst() -> removes and returns smallest element
        class PollFirstFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.pollFirst");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                if (ts.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return ts.data.pollFirst();
            }

            @Override
            public String getFnName() {
                return "pollFirst";
            }
        }

        PollFirstFn pollFirst = new PollFirstFn();
        Variable pollFirstVar = new Variable(new Variable.Variant(pollFirst), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(pollFirst.getFnName(), pollFirstVar);


        // ts.pollLast() -> removes and returns largest element
        class PollLastFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.pollLast");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                if (ts.data.isEmpty()) {
                    return new Variable.Variant(null);
                }

                return ts.data.pollLast();
            }

            @Override
            public String getFnName() {
                return "pollLast";
            }
        }

        PollLastFn pollLast = new PollLastFn();
        Variable pollLastVar = new Variable(new Variable.Variant(pollLast), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(pollLast.getFnName(), pollLastVar);


        // ts.subSet(from, to) -> new TreeSet with elements in [from, to)
        class SubSetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,2, "TreeSet.subSet");

                Variable.Variant from = arguments.get(0);
                Variable.Variant to   = arguments.get(1);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                yTreeSetInstance result = new yTreeSetInstance();
                result.data.addAll(ts.data.subSet(from, to));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "subSet";
            }
        }

        SubSetFn subSet = new SubSetFn();
        Variable subSetVar = new Variable(new Variable.Variant(subSet), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(subSet.getFnName(), subSetVar);


        // ts.headSet(to) -> new TreeSet with elements strictly < to
        class HeadSetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.headSet");

                Variable.Variant to = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                yTreeSetInstance result = new yTreeSetInstance();
                result.data.addAll(ts.data.headSet(to));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "headSet";
            }
        }

        HeadSetFn headSet = new HeadSetFn();
        Variable headSetVar = new Variable(new Variable.Variant(headSet), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(headSet.getFnName(), headSetVar);


        // ts.tailSet(from) -> new TreeSet with elements >= from
        class TailSetFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.TailSetFn");

                Variable.Variant from = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                yTreeSetInstance result = new yTreeSetInstance();
                result.data.addAll(ts.data.tailSet(from));

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "tailSet";
            }
        }

        TailSetFn tailSet = new TailSetFn();
        Variable tailSetVar = new Variable(new Variable.Variant(tailSet), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(tailSet.getFnName(), tailSetVar);


        // ts.union(other) -> new TreeSet = this ∪ other
        class UnionFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.union");

                Variable.Variant otherVariant = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof yTreeSetInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "union: argument must be a TreeSet."
                    );
                }

                yTreeSetInstance other  = (yTreeSetInstance) otherObj;
                yTreeSetInstance result = new yTreeSetInstance();

                result.data.addAll(ts.data);
                result.data.addAll(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "union";
            }
        }

        UnionFn union = new UnionFn();
        Variable unionVar = new Variable(new Variable.Variant(union), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(union.getFnName(), unionVar);


        // ts.intersection(other) -> new TreeSet = this ∩ other
        class IntersectionFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.intersection");

                Variable.Variant otherVariant = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof yTreeSetInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "intersection: argument must be a TreeSet."
                    );
                }

                yTreeSetInstance other  = (yTreeSetInstance) otherObj;
                yTreeSetInstance result = new yTreeSetInstance();

                for (Variable.Variant v : ts.data) {
                    if (other.data.contains(v)) {
                        result.data.add(v);
                    }
                }

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "intersection";
            }
        }

        IntersectionFn intersection = new IntersectionFn();
        Variable intersectionVar = new Variable(new Variable.Variant(intersection), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(intersection.getFnName(), intersectionVar);


        // ts.difference(other) -> new TreeSet = this \ other
        class DifferenceFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.difference");

                Variable.Variant otherVariant = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof yTreeSetInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "difference: argument must be a TreeSet."
                    );
                }

                yTreeSetInstance other  = (yTreeSetInstance) otherObj;
                yTreeSetInstance result = new yTreeSetInstance();

                for (Variable.Variant v : ts.data) {
                    if (!other.data.contains(v)) {
                        result.data.add(v);
                    }
                }

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "difference";
            }
        }

        DifferenceFn difference = new DifferenceFn();
        Variable differenceVar = new Variable(new Variable.Variant(difference), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(difference.getFnName(), differenceVar);


        // ts.isSubsetOf(other) -> true if all elements of this are in other
        class IsSubsetOfFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,1, "TreeSet.isSubsetOf");

                Variable.Variant otherVariant = arguments.get(0);
                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                RuntimeObject otherObj = otherVariant.asRuntimeObject();
                if (!(otherObj instanceof yTreeSetInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "isSubsetOf: argument must be a TreeSet."
                    );
                }

                yTreeSetInstance other = (yTreeSetInstance) otherObj;

                return new Variable.Variant(other.data.containsAll(ts.data));
            }

            @Override
            public String getFnName() {
                return "isSubsetOf";
            }
        }

        IsSubsetOfFn isSubsetOf = new IsSubsetOfFn();
        Variable isSubsetOfVar = new Variable(new Variable.Variant(isSubsetOf), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(isSubsetOf.getFnName(), isSubsetOfVar);


        // ts.toArray() -> sorted Y_ArrayObject
        class ToArrayFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.toArray");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                ArrayList<Variable.Variant> list = new ArrayList<>(ts.data);

                return new Variable.Variant(new yArray.yArrayInstance(list));
            }

            @Override
            public String getFnName() {
                return "toArray";
            }
        }

        ToArrayFn toArray = new ToArrayFn();
        Variable toArrayVar = new Variable(new Variable.Variant(toArray), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(toArray.getFnName(), toArrayVar);


        // ts.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.size");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                return new Variable.Variant(ts.data.size());
            }

            @Override
            public String getFnName() {
                return "size";
            }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(new Variable.Variant(size), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(size.getFnName(), sizeVar);


        // ts.isEmpty()
        class IsEmptyFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.isEmpty");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);

                return new Variable.Variant(ts.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(new Variable.Variant(isEmpty), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);


        // ts.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.clear");

                yTreeSetInstance ts = requireTreeSetThis(interpreter);
                ts.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "clear";
            }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(new Variable.Variant(clear), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(clear.getFnName(), clearVar);


        // ts.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                this.requireArity(arguments,0, "TreeSet.clone");

                yTreeSetInstance original = requireTreeSetThis(interpreter);
                yTreeSetInstance cloned   = new yTreeSetInstance();

                cloned.data.addAll(original.data);

                return new Variable.Variant(cloned);
            }

            @Override
            public String getFnName() {
                return "clone";
            }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(new Variable.Variant(clone), true, "function");
        yTreeSet.yTreeSet_Instance_Prototype.set(clone.getFnName(), cloneVar);

    }


    public static class yTreeSetInstance extends yClass.ClassObjectInstance {

        // Elements kept in sorted order — numeric keys sorted numerically, strings alphabetically
        final TreeSet<Variable.Variant> data;

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