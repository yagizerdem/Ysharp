package ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.yPattern;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;

import java.util.List;

public class SplitFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        if (arguments.isEmpty()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "split() requires at least 1 argument: input (string)."
            );
        }

        if (arguments.size() > 2) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "split() accepts at most 2 arguments: input (string), limit (int)."
            );
        }

        yPattern.yPatternInstance instance =
                yPattern.requirePatternThis(interpreter, getFnName());

        String input = requireString(arguments.getFirst(), getFnName(), 1);

        String[] result;

        try {
            if (arguments.size() == 1) {
                result = instance.pattern.split(input);
            } else {
                int limit = requireInt(arguments.get(1), getFnName(), 2);
                result = instance.pattern.split(input, limit);
            }
        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "split() failed: " + e.getMessage()
            );
        }

        yArray.yArrayInstance arr = new yArray.yArrayInstance();
        for (String s : result) {
            arr.data.add(new Variable.Variant(s));
        }

        return new Variable.Variant(arr);
    }

    @Override
    public String getFnName() {
        return "split";
    }
}