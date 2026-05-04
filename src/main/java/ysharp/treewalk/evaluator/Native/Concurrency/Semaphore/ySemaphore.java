package ysharp.treewalk.evaluator.Native.Concurrency.Semaphore;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Concurrency.Semaphore.function.instance.*;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ySemaphore {

    public static ySemaphore.ySemaphoreInstance requireSemaphoreThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof ySemaphoreInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Semaphore objects."
            );
        }

        return (ySemaphoreInstance) obj;
    }

    public static RuntimeObject ySemaphore_Instance_Prototype;

    static {
        ySemaphore_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "semaphore_prototype";
            }
        };
        ySemaphore_Instance_Prototype.prototype = yClass.ClassPrototype;

        // semaphore.acquire();
        // semaphore.acquire(n : int);
        ySemaphore_Instance_Prototype.RegisterNativeFn(new AcquireFn());

        // semaphore.acquireUninterruptibly();
        // semaphore.acquire(n : int);
        ySemaphore_Instance_Prototype.RegisterNativeFn(new AcquireUninterruptiblyFn());

        // semaphore.release();
        ySemaphore_Instance_Prototype.RegisterNativeFn(new ReleaseFn());

        // semaphore.availablePermits() -> int
        ySemaphore_Instance_Prototype.RegisterNativeFn(new AvailablePermitsFn());
        // semaphore.drainPermits() -> int
        ySemaphore_Instance_Prototype.RegisterNativeFn(new DrainPermitsFn());
        // semaphore.getQueueLength() -> int
        ySemaphore_Instance_Prototype.RegisterNativeFn(new GetQueueLengthFn());
        // semaphore.hasQueuedThreads() -> bool
        ySemaphore_Instance_Prototype.RegisterNativeFn(new HasQueuedThreadsFn());
        // semaphore.isFair() -> bool
        ySemaphore_Instance_Prototype.RegisterNativeFn(new IsFairFn());
        // semaphore.tryAcquire() -> bool
        // semaphore.tryAcquire(n : int) -> bool
        ySemaphore_Instance_Prototype.RegisterNativeFn(new TryAcquireFn());
        // semaphore.tryAcquireTimeout(long timeout, String unit) -> bool
        // unit: "NANOSECONDS", "MICROSECONDS", "MILLISECONDS", "SECONDS", "MINUTES", "HOURS", "DAYS"
        ySemaphore_Instance_Prototype.RegisterNativeFn(new TryAcquireTimeoutFn());

    }

    public static class ySemaphoreInstance extends yClass.ClassObjectInstance {

        private final Semaphore lock;

        public ySemaphoreInstance(int permits, boolean fair) {
            this.lock = new Semaphore(permits, fair);
            this.prototype = ySemaphore_Instance_Prototype;
        }

        public Semaphore getLock() {
            return this.lock;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Semaphore";
        }

        @Override
        public String toString() {
            return "<class:Semaphore>";
        }
    }

    public static class ySemaphoreClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {
            requireArity(arguments, arity(), getClassName());
            int permits = requireInt(arguments.getFirst(), getClassName(), 1);
            boolean fair = requireBoolean(arguments.get(1), getClassName(), 2);

            if(permits < 0) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "Semaphore permits must be >= 0."
                );
            }

            ySemaphoreInstance semaphore = new ySemaphoreInstance(permits, fair);
            return new Variable.Variant(semaphore);
        }

        @Override
        public String getClassName() {
            return "Semaphore";
        }

        @Override
        public String getType() {
            return "_Semaphore_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        ySemaphoreClass ctor = new ySemaphoreClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}