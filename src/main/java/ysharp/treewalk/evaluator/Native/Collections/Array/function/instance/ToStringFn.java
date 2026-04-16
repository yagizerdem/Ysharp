package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Native.function.binding.BoundNativeFunction;

import java.util.ArrayList;
import java.util.List;

public class ToStringFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for(int i = 0; i < array.data.size(); i++) {
            Variable.Variant var = array.data.get(i);
            if(var.value instanceof RuntimeObject) {
                Variable toStringFn = ((RuntimeObject) var.value).get("toString");
                if(toStringFn != null && toStringFn.value.isNativeFunction()) {
                    BoundNativeFunction bound = new BoundNativeFunction(toStringFn.value.asNativeFunction(), var.asRuntimeObject(), "this");
                    List<Variable.Variant> args = new ArrayList<>();
                    builder.append(bound.call(interpreter, args));
                }
                else {
                    builder.append(var.value.toString());
                }
            }
            else {
                builder.append(var.value.toString());
            }

            builder.append(" ");
            if(i != array.data.size() -1) {
                builder.append(", ");
            }
        }
        builder.append("]");

        return new Variable.Variant(new yString.yStringInstance(builder.toString()));
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}