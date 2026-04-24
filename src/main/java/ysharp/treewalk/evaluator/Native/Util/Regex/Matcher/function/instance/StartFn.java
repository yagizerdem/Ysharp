package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.yMatcher;

import java.util.List;

public class StartFn extends Function.NativeFunction {

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
                    "start() accepts at most 1 argument: group (int or string)."
            );
        }

        yMatcher.yMatcherInstance matcher =
                yMatcher.requireMatcherThis(interpreter, getFnName());

        int result;

        try {
            if (arguments.isEmpty()) {
                // start()
                result = matcher.matcher.start();
            } else {
                Variable.Variant arg = arguments.getFirst();

                if (arg.isNumber()) {
                    // start(int group)
                    int index = requireInt(arg, getFnName(), 1);
                    result = matcher.matcher.start(index);
                } else if (arg.isString()) {
                    // start(String name)
                    String name = requireString(arg, getFnName(), 1);
                    result = matcher.matcher.start(name);
                } else {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "start() argument must be int (group index) or string (group name)."
                    );
                }
            }

        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Invalid start() call: " + e.getMessage()
            );
        }

        return new Variable.Variant(result);
    }

    @Override
    public String getFnName() {
        return "start";
    }
}