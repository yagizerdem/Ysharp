package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class JoinTimeoutFn extends Function.NativeFunction {

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

        int timeoutMs = requireInt(arguments.getFirst(), getFnName(), 1);

        if (timeoutMs < 0) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "joinTimeout duration cannot be negative."
            );
        }

        try {
            jt.join(timeoutMs);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "joinTimeout interrupted."
            );
        }

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "joinTimeout";
    }
}