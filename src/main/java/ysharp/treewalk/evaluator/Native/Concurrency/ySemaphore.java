package ysharp.treewalk.evaluator.Native.Concurrency;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ySemaphore {

    private static ySemaphore.ySemaphoreInstance requireSemaphoreThis(Interpreter interpreter) {
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
        class AcquireFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                try {
                    lock.acquire();
                } catch (InterruptedException ex) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,  "Semaphore operation interrupted.");
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "acquire";
            }
        }

        AcquireFn acquire = new AcquireFn();
        Variable acquireVar = new Variable(new Variable.Variant(acquire), true, "function");
        ySemaphore_Instance_Prototype.set(acquire.getFnName(), acquireVar);


        // semaphore.acquireN(int permits);
        class AcquireNFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();


                int permits = requireInt(arguments.getFirst(), getFnName(), 1);

                try {
                    lock.acquire(permits);
                } catch (InterruptedException ex) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,  "Semaphore operation interrupted.");
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "acquireN";
            }
        }

        AcquireNFn acquireN = new AcquireNFn();
        Variable acquireNVar = new Variable(new Variable.Variant(acquireN), true, "function");
        ySemaphore_Instance_Prototype.set(acquireN.getFnName(), acquireNVar);


        // semaphore.acquireUninterruptibly();
        class AcquireUninterruptiblyFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                lock.acquireUninterruptibly();
                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "acquireUninterruptibly";
            }
        }

        AcquireUninterruptiblyFn acquireUninterruptibly = new AcquireUninterruptiblyFn();
        Variable acquireUninterruptiblyVar = new Variable(new Variable.Variant(acquireUninterruptibly), true, "function");
        ySemaphore_Instance_Prototype.set(acquireUninterruptibly.getFnName(), acquireUninterruptiblyVar);


        // semaphore.acquireUninterruptiblyN(int permits);
        class AcquireUninterruptiblyNFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                int permits = requireInt(arguments.getFirst(), getFnName(), 1);
                lock.acquireUninterruptibly(permits);
                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "acquireUninterruptiblyN";
            }
        }

        AcquireUninterruptiblyNFn acquireUninterruptiblyN = new AcquireUninterruptiblyNFn();
        Variable acquireUninterruptiblyNVar = new Variable(new Variable.Variant(acquireUninterruptiblyN), true, "function");
        ySemaphore_Instance_Prototype.set(acquireUninterruptiblyN.getFnName(), acquireUninterruptiblyNVar);


        // semaphore.release();
        class ReleaseFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                lock.release();
                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "release";
            }
        }

        ReleaseFn release = new ReleaseFn();
        Variable releaseVar = new Variable(new Variable.Variant(release), true, "function");
        ySemaphore_Instance_Prototype.set(release.getFnName(), releaseVar);


        // semaphore.releaseN(int permits);
        class ReleaseNFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                int permits = requireInt(arguments.getFirst(), getFnName(), 1);
                lock.release(permits);
                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "releaseN";
            }
        }

        ReleaseNFn releaseN = new ReleaseNFn();
        Variable releaseNVar = new Variable(new Variable.Variant(releaseN), true, "function");
        ySemaphore_Instance_Prototype.set(releaseN.getFnName(), releaseNVar);


        // semaphore.availablePermits() -> int
        class AvailablePermitsFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.availablePermits());
            }

            @Override
            public String getFnName() {
                return "availablePermits";
            }
        }

        AvailablePermitsFn availablePermits = new AvailablePermitsFn();
        Variable availablePermitsVar = new Variable(new Variable.Variant(availablePermits), true, "function");
        ySemaphore_Instance_Prototype.set(availablePermits.getFnName(), availablePermitsVar);


        // semaphore.drainPermits() -> int
        class DrainPermitsFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.drainPermits());
            }

            @Override
            public String getFnName() {
                return "drainPermits";
            }
        }

        DrainPermitsFn drainPermits = new DrainPermitsFn();
        Variable drainPermitsVar = new Variable(new Variable.Variant(drainPermits), true, "function");
        ySemaphore_Instance_Prototype.set(drainPermits.getFnName(), drainPermitsVar);


        // semaphore.getQueueLength() -> int
        class GetQueueLengthFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.getQueueLength());
            }

            @Override
            public String getFnName() {
                return "getQueueLength";
            }
        }

        GetQueueLengthFn getQueueLength = new GetQueueLengthFn();
        Variable getQueueLengthVar = new Variable(new Variable.Variant(getQueueLength), true, "function");
        ySemaphore_Instance_Prototype.set(getQueueLength.getFnName(), getQueueLengthVar);


        // semaphore.hasQueuedThreads() -> bool
        class HasQueuedThreadsFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.hasQueuedThreads());
            }

            @Override
            public String getFnName() {
                return "hasQueuedThreads";
            }
        }

        HasQueuedThreadsFn hasQueuedThreads = new HasQueuedThreadsFn();
        Variable hasQueuedThreadsVar = new Variable(new Variable.Variant(hasQueuedThreads), true, "function");
        ySemaphore_Instance_Prototype.set(hasQueuedThreads.getFnName(), hasQueuedThreadsVar);


        // semaphore.isFair() -> bool
        class IsFairFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.isFair());
            }

            @Override
            public String getFnName() {
                return "isFair";
            }
        }

        IsFairFn isFair = new IsFairFn();
        Variable isFairVar = new Variable(new Variable.Variant(isFair), true, "function");
        ySemaphore_Instance_Prototype.set(isFair.getFnName(), isFairVar);


        // semaphore.tryAcquire() -> bool
        class TryAcquireFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                return new Variable.Variant(lock.tryAcquire());
            }

            @Override
            public String getFnName() {
                return "tryAcquire";
            }
        }

        TryAcquireFn tryAcquire = new TryAcquireFn();
        Variable tryAcquireVar = new Variable(new Variable.Variant(tryAcquire), true, "function");
        ySemaphore_Instance_Prototype.set(tryAcquire.getFnName(), tryAcquireVar);


        // semaphore.tryAcquireN(int permits) -> bool
        class TryAcquireNFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                int permits = requireInt(arguments.getFirst(), getFnName(), 1);
                return new Variable.Variant(lock.tryAcquire(permits));
            }

            @Override
            public String getFnName() {
                return "tryAcquireN";
            }
        }

        TryAcquireNFn tryAcquireN = new TryAcquireNFn();
        Variable tryAcquireNVar = new Variable(new Variable.Variant(tryAcquireN), true, "function");
        ySemaphore_Instance_Prototype.set(tryAcquireN.getFnName(), tryAcquireNVar);


        // semaphore.tryAcquireTimeout(long timeout, String unit) -> bool
        // unit: "NANOSECONDS", "MICROSECONDS", "MILLISECONDS", "SECONDS", "MINUTES", "HOURS", "DAYS"
        class TryAcquireTimeoutFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();

                double timeout = requireDouble(arguments.getFirst(), getFnName(), 1);
                String unitStr = requireString(arguments.get(1), getFnName(), 2);

                TimeUnit unit;
                try {
                    unit = TimeUnit.valueOf(unitStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                            "Invalid TimeUnit: '" + unitStr + "'. Valid values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS");
                }

                try {
                    return new Variable.Variant(lock.tryAcquire((long)timeout, unit));
                } catch (InterruptedException ex) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,  "Semaphore operation interrupted.");
                }
            }

            @Override
            public String getFnName() {
                return "tryAcquireTimeout";
            }
        }

        TryAcquireTimeoutFn tryAcquireTimeout = new TryAcquireTimeoutFn();
        Variable tryAcquireTimeoutVar = new Variable(new Variable.Variant(tryAcquireTimeout), true, "function");
        ySemaphore_Instance_Prototype.set(tryAcquireTimeout.getFnName(), tryAcquireTimeoutVar);


        // semaphore.tryAcquireNTimeout(int permits, long timeout, String unit) -> bool
        class TryAcquireNTimeoutFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 3;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
                ySemaphoreInstance semaphore = requireSemaphoreThis(interpreter);
                Semaphore lock = semaphore.getLock();


                int permits = requireInt(arguments.getFirst(), getFnName(), 1);
                double timeout = requireDouble(arguments.get(1), getFnName(), 2);
                String unitStr = requireString(arguments.get(2), getFnName(), 3);

                TimeUnit unit;
                try {
                    unit = TimeUnit.valueOf(unitStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                            "Invalid TimeUnit: '" + unitStr + "'. Valid values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS");
                }

                try {
                    return new Variable.Variant(lock.tryAcquire(permits, (long)timeout, unit));
                } catch (InterruptedException ex) {
                    throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,  "Semaphore operation interrupted.");
                }
            }

            @Override
            public String getFnName() {
                return "tryAcquireNTimeout";
            }
        }

        TryAcquireNTimeoutFn tryAcquireNTimeout = new TryAcquireNTimeoutFn();
        Variable tryAcquireNTimeoutVar = new Variable(new Variable.Variant(tryAcquireNTimeout), true, "function");
        ySemaphore_Instance_Prototype.set(tryAcquireNTimeout.getFnName(), tryAcquireNTimeoutVar);

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
            return "Semaphore";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        ySemaphoreClass ctor = new ySemaphoreClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}