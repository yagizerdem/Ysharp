package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.Variable;

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

        this.requireArity(arguments, 0, "IdentityHashMap.clone");

        yIdentityHashMap.yIdentityHashMapInstance original = yIdentityHashMap.requireIdentityHashMapThis(interpreter);
        yIdentityHashMap.yIdentityHashMapInstance cloned   = new yIdentityHashMap.yIdentityHashMapInstance();

        cloned.data.putAll(original.data);

        return new Variable.Variant(cloned);
    }

    @Override
    public String getFnName() {
        return "clone";
    }
}
