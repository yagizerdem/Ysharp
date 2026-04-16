package ysharp.treewalk.evaluator.Native.Collections.Array.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class RangeFn extends Function.NativeFunction implements Callable {
    @Override
    public int arity() { return -1; } // 2 or 3 arguments

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        int size = arguments.size();
        if (size < 2 || size > 3) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'range' expects 2 or 3 arguments (start, end, [step])."
            );
        }

        int start = requireInt(arguments.get(0), getFnName(), 1);
        int end = requireInt(arguments.get(1), getFnName(), 2);
        int step = size == 3 ? requireInt(arguments.get(2), getFnName(), 3) : 1;

        if (step == 0) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'step' cannot be zero in 'range'."
            );
        }

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();

        if (step > 0) {
            for (int i = start; i < end; i += step) {
                newArray.data.add(new Variable.Variant(i));
            }
        } else {
            for (int i = start; i > end; i += step) {
                newArray.data.add(new Variable.Variant(i));
            }
        }

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() { return "range"; }
}
