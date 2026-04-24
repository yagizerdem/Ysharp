package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;

import java.util.List;

public class RegionFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        yMatcher.yMatcherInstance matcher =
                yMatcher.requireMatcherThis(interpreter, getFnName());

        int start = requireInt(arguments.get(0), getFnName(), 1);
        int end   = requireInt(arguments.get(1), getFnName(), 2);

        try {
            matcher.matcher.region(start, end);
        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Invalid region bounds: " + e.getMessage()
            );
        }

        return new Variable.Variant(matcher);
    }

    @Override
    public String getFnName() {
        return "region";
    }
}