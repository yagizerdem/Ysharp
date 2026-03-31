package ysharp.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class GetOrDefaultFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant key          = arguments.getFirst();
        Variable.Variant defaultValue = arguments.get(1);
        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        Variable.Variant value = tm.data.get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public String getFnName() {
        return "getOrDefault";
    }
}

