package ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.ySemaphore;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TryAcquireTimeoutFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return -1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        if (arguments.size() != 2 && arguments.size() != 3) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "tryAcquireTimeout expects 2 or 3 arguments."
            );
        }

        ySemaphore.ySemaphoreInstance semaphore =
                ySemaphore.requireSemaphoreThis(interpreter);

        Semaphore lock = semaphore.getLock();

        int permits = 1;
        int timeout;
        String unitStr;

        if (arguments.size() == 2) {
            timeout = requireInt(arguments.getFirst(), getFnName(), 1);
            unitStr = requireString(arguments.get(1), getFnName(), 2);
        } else {
            permits = requireInt(arguments.getFirst(), getFnName(), 1);
            timeout = requireInt(arguments.get(1), getFnName(), 2);
            unitStr = requireString(arguments.get(2), getFnName(), 3);
        }

        if (permits < 0) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "tryAcquireTimeout permits must be >= 0."
            );
        }

        if (timeout < 0) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "tryAcquireTimeout timeout must be >= 0."
            );
        }

        TimeUnit unit;

        try {
            unit = TimeUnit.valueOf(unitStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Invalid TimeUnit: '" + unitStr + "'. Valid values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS."
            );
        }

        try {
            return new Variable.Variant(lock.tryAcquire(permits, timeout, unit));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Semaphore tryAcquireTimeout operation interrupted."
            );
        }
    }

    @Override
    public String getFnName() {
        return "tryAcquireTimeout";
    }
}