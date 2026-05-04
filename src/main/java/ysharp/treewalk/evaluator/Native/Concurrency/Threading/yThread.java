package ysharp.treewalk.evaluator.Native.Concurrency.Threading;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance.*;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.statix.CurrentFn;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.statix.SleepFn;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.statix.YieldFn;

import java.util.ArrayList;
import java.util.List;

public class yThread {

    public static yThreadInstance requireThreadThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yThreadInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Thread objects."
            );
        }

        return (yThreadInstance) obj;
    }


    public static RuntimeObject yThread_Instance_Prototype;

    static {
        yThread_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "thread_prototype";
            }
        };
        yThread_Instance_Prototype.prototype = yClass.ClassPrototype;

        // thread.start();
        yThread_Instance_Prototype.RegisterNativeFn(new StartFn());
        // thread.join();
        yThread_Instance_Prototype.RegisterNativeFn(new JoinFn());
        // thread.isAlive;
        yThread_Instance_Prototype.RegisterNativeFn(new IsAliveFn());
        // thread.interrupt()
        yThread_Instance_Prototype.RegisterNativeFn(new InterruptFn());
        // thread.isInterrupted();
        yThread_Instance_Prototype.RegisterNativeFn(new IsInterruptedFn());
        // thread.joinTimeout(ms)
        yThread_Instance_Prototype.RegisterNativeFn(new JoinTimeoutFn());
        // thread.getName(str)
        yThread_Instance_Prototype.RegisterNativeFn(new GetNameFn());
        // thread.setName(str)
        yThread_Instance_Prototype.RegisterNativeFn(new SetNameFn());
        // thread.getId()
        yThread_Instance_Prototype.RegisterNativeFn(new GetIdFn());
        // thread.isDaemon()
        yThread_Instance_Prototype.RegisterNativeFn(new IsDaemonFn());
        // thread.setDaemon()
        yThread_Instance_Prototype.RegisterNativeFn(new SetDaemonFn());
        // thread.getState()
        yThread_Instance_Prototype.RegisterNativeFn(new GetStateFn());
    }


    public static class yThreadInstance extends yClass.ClassObjectInstance {

        private Thread javaThread;

        private Callable callable;

        public yThreadInstance(Callable callable) {
            this.callable = callable;
            this.prototype = yThread_Instance_Prototype;
        }

        public yThreadInstance() {
            this.prototype = yThread_Instance_Prototype;
        }

        public Thread getJavaThread() {
            return javaThread;
        }

        public void setJavaThread(Thread javaThread) {
            this.javaThread = javaThread;
        }

        public Callable getCallable() {
            return callable;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Thread";
        }

        @Override
        public String toString() {
            return "<class:Thread>";
        }
    }

    public static class yThreadClass extends yClass.SealedClassObject {

        public yThreadClass(){
            this.prototype = yClass.ClassPrototype;
            // Thread.current()
            this.RegisterNativeFn(new CurrentFn());
            // Thread.yield()
            this.RegisterNativeFn(new YieldFn());
            // Thread.sleep(ms)
            this.RegisterNativeFn(new SleepFn());
        }

        @Override
        public int arity() {
            return 1; // expect callable for argument
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            if(arguments.isEmpty()) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "Thread constructor requires a function or lambda as its first argument.");
            }

            Callable fn = requireCallable(arguments.getFirst(), getClassName(), 1);

            List<Variable.Variant> fnArgs = new ArrayList<>();

            for(int i = 1; i < arguments.size(); i++) {
                fnArgs.add(arguments.get(i));
            }

             yThreadInstance thread = new yThreadInstance(fn);
             Interpreter newInstance = new Interpreter();
             newInstance.global = interpreter.global;
             newInstance.curEnv = new Environment(interpreter.curEnv);
             thread.setJavaThread(new ThreadWrapper(newInstance, fn, fnArgs));

            return new Variable.Variant(thread);
        }

        @Override
        public String getClassName() {
            return "Thread";
        }

        @Override
        public String getType() {
            return "_Thread_";
        }
    }

    static class  ThreadWrapper extends Thread {

        private final Interpreter instance;
        private final Callable fn;
        private final List<Variable.Variant> args;

        public ThreadWrapper(Interpreter instance,
                             Callable fn,
                             List<Variable.Variant> args){
            this.instance = instance;
            this.fn = fn;
            this.args = args;
        }

        @Override
        public void run() {
            try {
                fn.call(instance, args);
            } catch (Exception e) {
                e.printStackTrace();
                throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                        -1 ,
                        e.getLocalizedMessage());
            }
            finally {
                Thread.yield();
            }
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yThreadClass ctor = new yThreadClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}