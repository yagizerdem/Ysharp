package ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.concurrent.Semaphore;
import ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.ySemaphore;


public class AcquireFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        if (arguments.size() > 1) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "acquire expects 0 or 1 arguments."
            );
        }

        ySemaphore.ySemaphoreInstance semaphore = ySemaphore.requireSemaphoreThis(interpreter);
        Semaphore lock = semaphore.getLock();

        int permits = 1;

        if (!arguments.isEmpty()) {
            permits = requireInt(arguments.getFirst(), getFnName(), 1);

            if (permits < 0) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "acquire permits must be >= 0."
                );
            }
        }

        try {
            lock.acquire(permits);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Semaphore acquire operation interrupted."
            );
        }

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "acquire";
    }
}