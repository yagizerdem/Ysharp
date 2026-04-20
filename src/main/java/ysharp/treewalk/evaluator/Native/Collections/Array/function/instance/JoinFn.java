package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.function.binding.BoundNativeFunction;

import java.util.ArrayList;
import java.util.List;

public class JoinFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {
        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        String separator = arguments.getFirst().value.toString();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < array.data.size(); i++) {
            Variable.Variant element = array.data.get(i);

            if (element.value instanceof RuntimeObject) {
                Variable toStringFn = ((RuntimeObject) element.value).get("toString");
                if (toStringFn != null && toStringFn.value.isNativeFunction()) {
                    BoundNativeFunction bound = new BoundNativeFunction(
                            toStringFn.value.asNativeFunction(),
                            element.asRuntimeObject(),
                            "this"
                    );
                    List<Variable.Variant> args = new ArrayList<>();
                    builder.append(bound.call(interpreter, args));
                } else {
                    if(element.value == null) builder.append("null");
                    else builder.append(element.value.toString());
                }
            } else {
                if(element.value == null) builder.append("null");
                else builder.append(element.value.toString());
            }

            if (i < array.data.size() - 1) {
                builder.append(separator);
            }
        }

        return new Variable.Variant(builder.toString());
    }

    @Override
    public String getFnName() {
        return "join";
    }
}