package ysharp.evaluator.Native.Collections.TreeMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeMap.yTreeMap;
import ysharp.evaluator.Variable;

import java.util.List;

public class CloneFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yTreeMap.yTreeMapInstance original = yTreeMap.requireTreeMapThis(interpreter);
        yTreeMap.yTreeMapInstance cloned   = new yTreeMap.yTreeMapInstance();

        cloned.data.putAll(original.data);

        return new Variable.Variant(cloned);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}
