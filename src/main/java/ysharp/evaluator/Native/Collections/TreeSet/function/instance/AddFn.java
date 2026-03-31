package ysharp.evaluator.Native.Collections.TreeSet.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.TreeSet.yTreeSet;
import ysharp.evaluator.Variable;

import java.util.List;

public class AddFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        this.requireArity(arguments,1, "TreeSet.add");

        Variable.Variant value = arguments.getFirst();
        yTreeSet.yTreeSetInstance ts = yTreeSet.requireTreeSetThis(interpreter);

        return new Variable.Variant(ts.data.add(value));
    }

    @Override
    public String getFnName() {
        return "add";
    }
}