package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class SortFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        if (arguments.size() > 1) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "'sort' expects 0 or 1 argument."
            );
        }

        if (arguments.isEmpty()) {
            array.data.sort((a, b) -> {
                if (a == null || a.value == null) return 1;
                if (b == null || b.value == null) return -1;

                if (a.canImplicitlyConvertNumber() && b.canImplicitlyConvertNumber()) {
                    double numA = a.implicitlyConvertNumber();
                    double numB = b.implicitlyConvertNumber();
                    return Double.compare(numA, numB);
                }

                String strA = a.value.toString();
                String strB = b.value.toString();
                return strA.compareTo(strB);
            });
        } else {
            Callable callback = requireCallable(arguments.getFirst(), getFnName(), 1);
            final YsharpError[] sortError = new YsharpError[1];

            try {
                array.data.sort((cur, other) -> {
                    List<Variable.Variant> args = new ArrayList<>();
                    args.add(cur);
                    args.add(other);

                    try {
                        Variable.Variant result = callback.call(interpreter, args);

                        if (result.canImplicitlyConvertNumber()) {
                            double val = result.implicitlyConvertNumber();
                            if (val > 0) return 1;
                            if (val < 0) return -1;
                            return 0;
                        }

                        return result.isTruthy() ? 1 : -1;

                    } catch (YsharpError e) {
                        sortError[0] = e;
                        return 0;
                    }
                });
            } catch (IllegalArgumentException e) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        0,
                        "Comparison method violates its general contract in 'sort'. Ensure consistent return values."
                );
            }

            if (sortError[0] != null) {
                throw sortError[0];
            }
        }

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "sort";
    }
}
