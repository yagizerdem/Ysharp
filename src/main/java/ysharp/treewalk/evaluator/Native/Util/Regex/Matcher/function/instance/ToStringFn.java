package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;

import java.util.List;

public class ToStringFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        yMatcher.yMatcherInstance matcher =
                yMatcher.requireMatcherThis(interpreter, getFnName());

        String result = matcher.matcher.toString();

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}