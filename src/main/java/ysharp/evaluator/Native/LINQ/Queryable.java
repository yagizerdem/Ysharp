package ysharp.evaluator.Native.LINQ;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queryable {

    // helper
    private static Queryable.QueryableInstance requireQueryableThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Queryable.QueryableInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'queryble' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (Queryable.QueryableInstance) obj;
    }

    private static class Operation {
        public static enum Type {
            WHERE,
            TAKE,
            SKIP,
            TAKE_WHILE,
            SKIP_WHILE,
            SELECT,
            SELECT_MANY,
            ORDER_BY,
            ORDER_BY_DESC,
            THEN_BY,
            THEN_BY_DESC,
            //
            COUNT,
            SUM,
            AVG,
            MIN,
            MAX,
            FIRST,
            FIRST_OR_DEFAULT,
            LAST,
            LAST_OR_DEFAULT,
            SINGLE,
            SINGLE_OR_DEFAULT,
            ANY,
            ALL,
            DISTINCT,
            UNION,
            INTERSECT,
            EXCEPT,
            JOIN,
            GROUP_JOIN,
            GROUP_BY,
            TO_ARRAY,
            REVERSE
        }
        public final Type opType;
        public final List<Variable.Variant> args;

        public Operation(Type opType, List<Variable.Variant> args) {
            this.opType = opType;
            this.args = args;
        }
    }

    public static RuntimeObject Queryable_Instance_Prototype;

    static {
        Queryable_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Queryable__";
            }

            @Override
            public String toString() {
                return "<prototype:Queryable>";
            }
        };
        Queryable_Instance_Prototype.prototype = yClass.ClassPrototype;

        // queryable.where(callback)
        class WhereFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
                queryable.operations.add(new Operation(Operation.Type.WHERE, Arrays.asList(new Variable.Variant(callback))));

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "where";
            }
        }

        WhereFn where = new WhereFn();
        Variable whereVar = new Variable(
                new Variable.Variant(where),
                true,
                "function");
        Queryable_Instance_Prototype.set(where.getFnName(), whereVar);


        // queryable.take(n)
        class TakeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                int n = requireInt(arguments.getFirst(), getFnName(), 1);
                queryable.operations.add(new Operation(Operation.Type.TAKE, Arrays.asList(new Variable.Variant(n))));

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "take";
            }
        }

        TakeFn take = new TakeFn();
        Variable takeVar = new Variable(
                new Variable.Variant(take),
                true,
                "function");
        Queryable_Instance_Prototype.set(take.getFnName(), takeVar);


        // queryable.skip(n)
        class SkipFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                int n = requireInt(arguments.getFirst(), getFnName(), 1);
                queryable.operations.add(
                        new Operation(
                                Operation.Type.SKIP,
                                Arrays.asList(new Variable.Variant(n))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "skip";
            }
        }

        SkipFn skip = new SkipFn();
        Variable skipVar = new Variable(
                new Variable.Variant(skip),
                true,
                "function");
        Queryable_Instance_Prototype.set(skip.getFnName(), skipVar);


        // queryable.takeWhile(callback)
        class TakeWhileFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.TAKE_WHILE,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "takeWhile";
            }
        }

        TakeWhileFn takeWhile = new TakeWhileFn();
        Variable takeWhileVar = new Variable(
                new Variable.Variant(takeWhile),
                true,
                "function");
        Queryable_Instance_Prototype.set(takeWhile.getFnName(), takeWhileVar);


        // queryable.skipWhile(callback)
        class SkipWhileFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.SKIP_WHILE,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "skipWhile";
            }
        }

        SkipWhileFn skipWhile = new SkipWhileFn();
        Variable skipWhileVar = new Variable(
                new Variable.Variant(skipWhile),
                true,
                "function");
        Queryable_Instance_Prototype.set(skipWhile.getFnName(), skipWhileVar);


        // queryable.select(callback)
        class SelectFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.SELECT,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "select";
            }
        }

        SelectFn select = new SelectFn();
        Variable selectVar = new Variable(
                new Variable.Variant(select),
                true,
                "function");
        Queryable_Instance_Prototype.set(select.getFnName(), selectVar);


        // queryable.selectMany(callback)
        class SelectManyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.SELECT_MANY,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "selectMany";
            }
        }

        SelectManyFn selectMany = new SelectManyFn();
        Variable selectManyVar = new Variable(
                new Variable.Variant(selectMany),
                true,
                "function");
        Queryable_Instance_Prototype.set(selectMany.getFnName(), selectManyVar);


        // queryable.orderBy(callback)
        class OrderByFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.ORDER_BY,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "orderBy";
            }
        }

        OrderByFn orderBy = new OrderByFn();
        Variable orderByVar = new Variable(
                new Variable.Variant(orderBy),
                true,
                "function");
        Queryable_Instance_Prototype.set(orderBy.getFnName(), orderByVar);


        // queryable.orderByDesc(callback)
        class OrderByDescFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.ORDER_BY_DESC,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "orderByDesc";
            }
        }

        OrderByDescFn orderByDesc = new OrderByDescFn();
        Variable orderByDescVar = new Variable(
                new Variable.Variant(orderByDesc),
                true,
                "function");
        Queryable_Instance_Prototype.set(orderByDesc.getFnName(), orderByDescVar);


        // queryable.thenBy(callback)
        class ThenByFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.THEN_BY,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "thenBy";
            }
        }

        ThenByFn thenBy = new ThenByFn();
        Variable thenByVar = new Variable(
                new Variable.Variant(thenBy),
                true,
                "function");
        Queryable_Instance_Prototype.set(thenBy.getFnName(), thenByVar);


        // queryable.thenByDesc(callback)
        class ThenByDescFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);

                queryable.operations.add(
                        new Operation(
                                Operation.Type.THEN_BY_DESC,
                                Arrays.asList(new Variable.Variant(callback))
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "thenByDesc";
            }
        }

        ThenByDescFn thenByDesc = new ThenByDescFn();
        Variable thenByDescVar = new Variable(
                new Variable.Variant(thenByDesc),
                true,
                "function");
        Queryable_Instance_Prototype.set(thenByDesc.getFnName(), thenByDescVar);


        // queryable.reverse()
        class ReverseFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(
                        new Operation(
                                Operation.Type.REVERSE,
                                new ArrayList<>()
                        )
                );

                return new Variable.Variant(queryable);
            }

            @Override
            public String getFnName() {
                return "reverse";
            }
        }

        ReverseFn reverse = new ReverseFn();
        Variable reverseVar = new Variable(
                new Variable.Variant(reverse),
                true,
                "function");

        Queryable_Instance_Prototype.set(reverse.getFnName(), reverseVar);

        // queryable.count() -> Terminal
        class CountFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(new Operation(Operation.Type.COUNT, new ArrayList<>()));
                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() { return "count"; }
        }

        CountFn count = new CountFn();
        Variable countVar = new Variable(new Variable.Variant(count),
                true,
                "function");
        Queryable_Instance_Prototype.set(count.getFnName(), countVar);


        // queryable.sum() -> Terminal
        class SumFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(new Operation(Operation.Type.SUM, new ArrayList<>()));
                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() { return "sum"; }
        }

        SumFn sum = new SumFn();
        Variable sumVar = new Variable(new Variable.Variant(sum),
                true,
                "function");
        Queryable_Instance_Prototype.set(sum.getFnName(), sumVar);


        // queryable.avg() -> Terminal
        class AvgFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(new Operation(Operation.Type.AVG, new ArrayList<>()));
                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() { return "avg"; }
        }

        AvgFn avg = new AvgFn();
        Variable avgVar = new Variable(new Variable.Variant(avg),
                true,
                "function");
        Queryable_Instance_Prototype.set(avg.getFnName(), avgVar);


        // queryable.min() -> Terminal
        class MinFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(new Operation(Operation.Type.MIN, new ArrayList<>()));
                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() { return "min"; }
        }

        MinFn min = new MinFn();
        Variable minVar = new Variable(new Variable.Variant(min),
                true,
                "function");
        Queryable_Instance_Prototype.set(min.getFnName(), minVar);


        // queryable.max() -> Terminal
        class MaxFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(new Operation(Operation.Type.MAX, new ArrayList<>()));
                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() { return "max"; }
        }

        MaxFn max = new MaxFn();
        Variable maxVar = new Variable(new Variable.Variant(max),
                true,
                "function");
        Queryable_Instance_Prototype.set(max.getFnName(), maxVar);


        // queryable.toArray() -> Terminal / Collector
        class ToArrayFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                QueryableInstance queryable = requireQueryableThis(interpreter, getFnName());

                queryable.operations.add(
                        new Operation(
                                Operation.Type.TO_ARRAY,
                                new ArrayList<>()
                        )
                );

                return queryable.execute(interpreter);
            }

            @Override
            public String getFnName() {
                return "toArray";
            }
        }

        ToArrayFn toArray = new ToArrayFn();
        Variable toArrayVar = new Variable(
                new Variable.Variant(toArray),
                true,
                "function");

        Queryable_Instance_Prototype.set(toArray.getFnName(), toArrayVar);
        Queryable_Instance_Prototype.set("toList", toArrayVar);

    }

    public static class QueryableInstance extends yClass.ClassObjectInstance {
        public final List<Variable.Variant> data;
        public List<Operation> operations;

        public QueryableInstance() {
            this.data = new ArrayList<>();
            this.prototype = Queryable_Instance_Prototype;
            this.operations = new ArrayList<>();
        }

        public QueryableInstance(List<Variable.Variant> data) {
            this.data = data;
            this.prototype = Queryable_Instance_Prototype;
            this.operations = new ArrayList<>();
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Queryable";
        }

        @Override
        public String toString() {
            return "<instance:Queryable>";
        }


        public Variable.Variant execute(Interpreter interpreter) throws YsharpError {

            List<Variable.Variant> result = new ArrayList<>(this.data);

            for (Operation op : this.operations) {

                switch (op.opType) {

                    case WHERE -> {
                        Callable fn = op.args.get(0).asCallable();
                        List<Variable.Variant> filtered = new ArrayList<>();

                        for (int i = 0; i < result.size(); i++) {
                            Variable.Variant el = result.get(i);

                            List<Variable.Variant> args = new ArrayList<>();
                            args.add(el);
                            args.add(new Variable.Variant(i));
                            args.add(new Variable.Variant(this));

                            Variable.Variant res = fn.call(interpreter, args);

                            if (res.isTruthy()) {
                                filtered.add(el);
                            }
                        }

                        result = filtered;
                    }

                    case SELECT -> {
                        Callable fn = op.args.get(0).asCallable();
                        List<Variable.Variant> mapped = new ArrayList<>();

                        for (int i = 0; i < result.size(); i++) {
                            Variable.Variant el = result.get(i);

                            List<Variable.Variant> args = new ArrayList<>();
                            args.add(el);
                            args.add(new Variable.Variant(i));
                            args.add(new Variable.Variant(this));

                            mapped.add(fn.call(interpreter, args));
                        }

                        result = mapped;
                    }

                    case TAKE -> {
                        int n = op.args.get(0).asInt();
                        int limit = Math.max(0, Math.min(n, result.size()));
                        result = new ArrayList<>(result.subList(0, limit));
                    }

                    case SKIP -> {
                        int n = op.args.get(0).asInt();
                        int start = Math.max(0, Math.min(n, result.size()));
                        result = new ArrayList<>(result.subList(start, result.size()));
                    }

                    case TAKE_WHILE -> {
                        Callable fn = op.args.get(0).asCallable();
                        List<Variable.Variant> temp = new ArrayList<>();

                        for (int i = 0; i < result.size(); i++) {
                            Variable.Variant el = result.get(i);

                            List<Variable.Variant> args = new ArrayList<>();
                            args.add(el);
                            args.add(new Variable.Variant(i));
                            args.add(new Variable.Variant(this));

                            if (!fn.call(interpreter, args).isTruthy()) break;

                            temp.add(el);
                        }

                        result = temp;
                    }

                    case SKIP_WHILE -> {
                        Callable fn = op.args.get(0).asCallable();
                        List<Variable.Variant> temp = new ArrayList<>();

                        boolean skipping = true;

                        for (int i = 0; i < result.size(); i++) {
                            Variable.Variant el = result.get(i);

                            if (skipping) {
                                List<Variable.Variant> args = new ArrayList<>();
                                args.add(el);
                                args.add(new Variable.Variant(i));
                                args.add(new Variable.Variant(this));

                                if (fn.call(interpreter, args).isTruthy()) {
                                    continue;
                                } else {
                                    skipping = false;
                                }
                            }

                            temp.add(el);
                        }

                        result = temp;
                    }

                    case SELECT_MANY -> {
                        Callable fn = op.args.get(0).asCallable();
                        List<Variable.Variant> flat = new ArrayList<>();

                        for (int i = 0; i < result.size(); i++) {
                            Variable.Variant el = result.get(i);

                            List<Variable.Variant> args = new ArrayList<>();
                            args.add(el);
                            args.add(new Variable.Variant(i));
                            args.add(new Variable.Variant(this));

                            Variable.Variant res = fn.call(interpreter, args);

                            if (res.value instanceof QueryableInstance q) {
                                flat.addAll(q.data);
                            } else if (res.value instanceof ysharp.evaluator.Native.Collections.yArray.yArrayInstance arr) {
                                flat.addAll(arr.data);
                            } else {
                                flat.add(res);
                            }
                        }

                        result = flat;
                    }

                    case REVERSE -> {
                        java.util.Collections.reverse(result);
                    }

                    case DISTINCT -> {
                        List<Variable.Variant> unique = new ArrayList<>();

                        for (Variable.Variant el : result) {
                            boolean exists = false;

                            for (Variable.Variant ex : unique) {
                                if ((el == null && ex == null) ||
                                        (el != null && el.equals(ex))) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) unique.add(el);
                        }

                        result = unique;
                    }

                    case TO_ARRAY -> {
                        ysharp.evaluator.Native.Collections.yArray.yArrayInstance arr =
                                new ysharp.evaluator.Native.Collections.yArray.yArrayInstance(
                                        new ArrayList<>(result)
                                );

                        return new Variable.Variant(arr);
                    }

                    case COUNT -> {
                        return new Variable.Variant((double) result.size());
                    }

                    case SUM -> {
                        double sum = 0;
                        for (Variable.Variant el : result) {
                            if (el != null && el.canImplicitlyConvertNumber()) {
                                sum += el.implicitlyConvertNumber();
                            }
                        }
                        return new Variable.Variant(sum);
                    }

                    case AVG -> {
                        if (result.isEmpty()) return new Variable.Variant(0.0);
                        double sum = 0;
                        int count = 0;
                        for (Variable.Variant el : result) {
                            if (el != null && el.canImplicitlyConvertNumber()) {
                                sum += el.implicitlyConvertNumber();
                                count++;
                            }
                        }
                        return new Variable.Variant(count == 0 ? 0.0 : sum / count);
                    }

                    case MIN -> {
                        if (result.isEmpty()) return new Variable.Variant(null);
                        double min = Double.MAX_VALUE;
                        Variable.Variant minVar = new Variable.Variant(null);
                        boolean found = false;

                        for (Variable.Variant el : result) {
                            if (el != null && el.canImplicitlyConvertNumber()) {
                                double val = el.implicitlyConvertNumber();
                                if (val < min) {
                                    min = val;
                                    minVar = el;
                                    found = true;
                                }
                            }
                        }
                        return found ? minVar : new Variable.Variant(null);
                    }

                    case MAX -> {
                        if (result.isEmpty()) return new Variable.Variant(null);
                        double max = Double.NEGATIVE_INFINITY;
                        Variable.Variant maxVar = new Variable.Variant(null);
                        boolean found = false;

                        for (Variable.Variant el : result) {
                            if (el != null && el.canImplicitlyConvertNumber()) {
                                double val = el.implicitlyConvertNumber();
                                if (val > max) {
                                    max = val;
                                    maxVar = el;
                                    found = true;
                                }
                            }
                        }
                        return found ? maxVar : new Variable.Variant(null);
                    }

                    default -> {
                    }
                }
            }

            ysharp.evaluator.Native.Collections.yArray.yArrayInstance arr =
                    new ysharp.evaluator.Native.Collections.yArray.yArrayInstance(
                            new ArrayList<>(result)
                    );

            return new Variable.Variant(arr);
        }

    }

    public static class QueryableClass extends yClass.SealedClassObject {
        @Override
        public int arity() {
            return 0;
        }

        public QueryableClass(){
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            ArrayList<Variable.Variant> value = new ArrayList<>();
            QueryableInstance newQueryable = new QueryableInstance(value);

            return new Variable.Variant(newQueryable);
        }

        @Override
        public String getClassName() {
            return "Queryable";
        }

        @Override
        public String getType() {
            return "Queryable";
        }

        @Override
        public String toString() {
            return "<class:Queryable>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        Queryable.QueryableClass querybleClassCtor = new Queryable.QueryableClass();
        Variable.Variant variant = new Variable.Variant(querybleClassCtor);
        Variable var = new Variable(variant, false, querybleClassCtor.getType());
        interpreter.defineGlobal(querybleClassCtor.getClassName(), var);
    }

}
