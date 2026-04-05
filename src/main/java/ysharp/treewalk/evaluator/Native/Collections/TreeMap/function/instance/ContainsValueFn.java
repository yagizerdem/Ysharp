package ysharp.treewalk.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class ContainsValueFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        Variable.Variant value = arguments.getFirst();
        yTreeMap.yTreeMapInstance tm = yTreeMap.requireTreeMapThis(interpreter);

        return new Variable.Variant(tm.data.containsValue(value));
    }

    @Override
    public String getFnName() {
        return "containsValue";
    }
}

