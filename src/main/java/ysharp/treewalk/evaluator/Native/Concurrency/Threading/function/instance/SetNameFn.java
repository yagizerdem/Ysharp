package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SetNameFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        yThread.yThreadInstance thread = yThread.requireThreadThis(interpreter);

        Thread jt = thread.getJavaThread();

        if (jt == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Thread is not initialized."
            );
        }

        String name = requireString(arguments.getFirst(), getFnName(), 1);

        jt.setName(name);

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "setName";
    }
}