package ysharp.treewalk.evaluator.Native.Collections.Set.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Set.ySet;
import ysharp.treewalk.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ToStringFn extends Function.NativeFunction {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments)
            throws YsharpException {

        ySet.ySetInstance set = ySet.requireSetThis(interpreter);

        StringBuilder builder = new StringBuilder();
        builder.append("{ ");

        int i = 0;
        for (Variable.Variant element : set.data) {

            if (element.value instanceof RuntimeObject) {
                Variable toStringFn =
                        ((RuntimeObject) element.value).get("toString");

                if (toStringFn != null &&
                        toStringFn.value.isNativeFunction()) {

                    BoundNativeFunction bound =
                            new BoundNativeFunction(
                                    toStringFn.value.asNativeFunction(),
                                    element.asRuntimeObject(),
                                    "this");

                    builder.append(bound.call(
                            interpreter,
                            new ArrayList<>()));
                } else {
                    builder.append("<object>");
                }
            } else {
                builder.append(element.value);
            }

            if (i++ != set.data.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append(" }");
        return new Variable.Variant(builder.toString());
    }

    @Override
    public String getFnName() { return "toString"; }
}
