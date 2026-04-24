package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.regex.Matcher;

public class QuoteReplacementFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String input = requireString(arguments.getFirst(), getFnName(), 1);

        String result = Matcher.quoteReplacement(input);

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "quoteReplacement";
    }
}