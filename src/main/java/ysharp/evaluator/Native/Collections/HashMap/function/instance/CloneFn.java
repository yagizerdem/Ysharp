package ysharp.evaluator.Native.Collections.HashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
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

        yHashMap.yHashMapInstance original = yHashMap.requireHashMapThis(interpreter);
        yHashMap.yHashMapInstance cloned   = new yHashMap.yHashMapInstance();

        cloned.data.putAll(original.data);

        return new Variable.Variant(cloned);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}
