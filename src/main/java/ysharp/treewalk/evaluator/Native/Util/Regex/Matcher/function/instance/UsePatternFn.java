package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;
import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.yPattern;

import java.util.List;

public class UsePatternFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        yMatcher.yMatcherInstance matcher =
                yMatcher.requireMatcherThis(interpreter, getFnName());

        Variable.Variant arg = arguments.getFirst();

        if (!(arg.value instanceof RuntimeObject obj) ||
                !(obj instanceof yPattern.yPatternInstance patternInstance)) {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "usePattern() expects a Pattern instance."
            );
        }

        matcher.matcher.usePattern(patternInstance.pattern);

        return new Variable.Variant(matcher);
    }

    @Override
    public String getFnName() {
        return "usePattern";
    }
}