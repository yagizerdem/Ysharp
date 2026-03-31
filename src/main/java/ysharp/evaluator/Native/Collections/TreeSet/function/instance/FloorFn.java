package ysharp.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.evaluator.Variable;

import java.util.List;

public class FloorFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,1, "TreeSet.floor");

        Variable.Variant value = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        Variable.Variant result = ts.data.floor(value);

        return result != null ? result : new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "floor";
    }
}

