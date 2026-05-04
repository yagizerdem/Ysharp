package ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.ySemaphore;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.concurrent.Semaphore;

public class AvailablePermitsFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        ySemaphore.ySemaphoreInstance semaphore = ySemaphore.requireSemaphoreThis(interpreter);
        Semaphore lock = semaphore.getLock();

        return new Variable.Variant(lock.availablePermits());
    }

    @Override
    public String getFnName() {
        return "availablePermits";
    }
}
