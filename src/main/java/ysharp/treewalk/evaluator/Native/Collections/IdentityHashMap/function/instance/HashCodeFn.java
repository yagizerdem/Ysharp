package ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.IdentityHashMap.yIdentityHashMap;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class HashCodeFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        this.requireArity(arguments, 0, "IdentityHashMap.hashCode");

        yIdentityHashMap.yIdentityHashMapInstance ihm = yIdentityHashMap.requireIdentityHashMapThis(interpreter);

        return new Variable.Variant(ihm.data.hashCode());
    }

    @Override
    public String getFnName() {
        return "hashCode";
    }
}

