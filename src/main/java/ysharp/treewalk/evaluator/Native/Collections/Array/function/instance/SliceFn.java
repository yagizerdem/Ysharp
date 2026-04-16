package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SliceFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());

        Variable.Variant startVar = arguments.getFirst();
        Variable.Variant endVar = arguments.get(1);

        if (!startVar.canImplicitlyConvertNumber() || !endVar.canImplicitlyConvertNumber()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "'slice' arguments must be numbers."
            );
        }

        int start = (int) startVar.implicitlyConvertNumber();
        int end = (int) endVar.implicitlyConvertNumber();

        if (start < 0) start = array.data.size() + start;
        if (end < 0) end = array.data.size() + end;

        start = Math.max(0, Math.min(start, array.data.size()));
        end = Math.max(start, Math.min(end, array.data.size()));

        yArray.yArrayInstance newArray = new yArray.yArrayInstance();
        for (Variable.Variant v : array.data.subList(start, end)) {
            newArray.data.add(new Variable.Variant(v.value));
        }

        return new Variable.Variant(newArray);
    }

    @Override
    public String getFnName() {
        return "slice";
    }
}
