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

public class MatcherFn extends Function.NativeFunction implements Callable  {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        String charSequance = requireString(arguments.getFirst(), getFnName(),1 );
        yPattern.yPatternInstance instance = yPattern.requirePatternThis(interpreter, getFnName());
        Matcher matcher = instance.pattern.matcher(charSequance);
        return new Variable.Variant(new yMatcher.yMatcherInstance(matcher));
    }

    @Override
    public String getFnName() {
        return "matcher";
    }
}
