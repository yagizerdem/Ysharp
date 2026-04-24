package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class StartFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        yThread.yThreadInstance thread = yThread.requireThreadThis(interpreter);

        Thread jt = thread.getJavaThread();

        if (jt == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Thread is not properly initialized."
            );
        }

        if (jt.getState() != Thread.State.NEW) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Thread has already been started."
            );
        }

        jt.start();

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "start";
    }
}
