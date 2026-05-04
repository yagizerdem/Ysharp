package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class SetDaemonFn extends Function.NativeFunction {

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

        boolean daemon = requireBoolean(arguments.getFirst(), getFnName(), 1);

        try {
            jt.setDaemon(daemon);
        } catch (IllegalThreadStateException e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "setDaemon must be called before the thread is started."
            );
        } catch (SecurityException e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Permission denied while setting daemon state."
            );
        }

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "setDaemon";
    }
}