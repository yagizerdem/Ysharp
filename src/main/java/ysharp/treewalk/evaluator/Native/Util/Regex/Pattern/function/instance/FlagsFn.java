package ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.yPattern;

import java.util.List;
import java.util.regex.Matcher;

public class FlagsFn extends Function.NativeFunction implements Callable  {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yPattern.yPatternInstance instance = yPattern.requirePatternThis(interpreter, getFnName());
        int flags = instance.pattern.flags();
        return new Variable.Variant(flags);
    }

    @Override
    public String getFnName() {
        return "flags";
    }
}
