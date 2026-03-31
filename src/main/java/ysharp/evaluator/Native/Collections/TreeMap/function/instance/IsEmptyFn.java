package ysharp.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class IsEmptyFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        return new Variable.Variant(tm.data.isEmpty());
    }

    @Override
    public String getFnName() {
        return "isEmpty";
    }
}