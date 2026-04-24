package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;

import java.util.List;

public class ResetFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        if (arguments.size() > 1) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "reset() accepts at most 1 argument: input (string)."
            );
        }

        yMatcher.yMatcherInstance matcher =
                yMatcher.requireMatcherThis(interpreter, getFnName());

        try {
            if (arguments.isEmpty()) {
                matcher.matcher.reset();
                return new Variable.Variant(matcher);
            } else {
                String input = requireString(arguments.getFirst(), getFnName(), 1);

                java.util.regex.Pattern pattern = matcher.matcher.pattern();
                java.util.regex.Matcher newMatcher = pattern.matcher(input);

                return new Variable.Variant(new yMatcher.yMatcherInstance(newMatcher));
            }

        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "reset() failed: " + e.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "reset";
    }
}