package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;

import java.util.List;

public class UseTransparentBoundsFn extends Function.NativeFunction implements Callable {

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

        boolean flag = requireBoolean(arguments.getFirst(), getFnName(), 1);

        matcher.matcher.useTransparentBounds(flag);

        return new Variable.Variant(matcher);
    }

    @Override
    public String getFnName() {
        return "useTransparentBounds";
    }
}