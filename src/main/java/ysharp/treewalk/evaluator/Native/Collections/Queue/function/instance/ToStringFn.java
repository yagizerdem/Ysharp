package ysharp.treewalk.evaluator.Native.Collections.Queue.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Queue.yQueue;
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
        yQueue.yQueueInstance queue = yQueue.requireQueueThis(interpreter, getFnName());

        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        int counter = 0;
        for (Variable.Variant var : queue.data) {

            if (var.value instanceof RuntimeObject) {

                Variable toStringFn =
                        ((RuntimeObject) var.value).get("toString");

                if (toStringFn != null &&
                        toStringFn.value.isNativeFunction()) {

                    BoundNativeFunction bound =
                            new BoundNativeFunction(
                                    toStringFn.value.asNativeFunction(),
                                    var.asRuntimeObject(),
                                    "this"
                            );

                    builder.append(
                            bound.call(interpreter, new ArrayList<>())
                    );
                }
                else {
                    builder.append("<class>");
                }
            }
            else {
                builder.append(var.value.toString());
            }

            if(counter < queue.data.size() -1) builder.append(",");
            builder.append(" ");
            counter++;
        }
        builder.append("]");

        return new Variable.Variant(builder.toString());
    }

    @Override
    public String getFnName() {
        return "toString";
    }
}