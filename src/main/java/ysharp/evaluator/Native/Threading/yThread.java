package ysharp.evaluator.Native.Threading;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.ArrayList;
import java.util.List;

public class yThread {

    private static yThreadInstance requireThreadThis(Interpreter interpreter) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yThreadInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
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
        class StartFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                yThreadInstance thread = requireThreadThis(interpreter);

                Thread jt = thread.getJavaThread();

                if (jt == null) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Thread is not properly initialized."
                    );
                }

                if (jt.getState() != Thread.State.NEW) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
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

        StartFn start = new StartFn();
        Variable startVar = new Variable(
                new Variable.Variant(start),
                true,
                "function");
        yThread_Instance_Prototype.set(start.getFnName(), startVar);

        // thread.join();
        class JoinFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                try {
                    yThreadInstance thread = requireThreadThis(interpreter);
                    thread.getJavaThread().join();
                }catch (InterruptedException interruptedException) {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            interruptedException.getMessage());
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "join";
            }
        }

        JoinFn join = new JoinFn();
        Variable joinVar = new Variable(
                new Variable.Variant(join),
                true,
                "function");
        yThread_Instance_Prototype.set(join.getFnName(), joinVar);


        // thread.isAlive;
        class IsAliveFn extends Function.NativeFunction {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                yThreadInstance thread = requireThreadThis(interpreter);
                boolean flag = thread.getJavaThread().isAlive();

                return new Variable.Variant(flag);
            }

            @Override
            public String getFnName() {
                return "isAlive";
            }
        }

        IsAliveFn isAlive = new IsAliveFn();
        Variable isAliveVar = new Variable(
                new Variable.Variant(isAlive),
                true,
                "function");
        yThread_Instance_Prototype.set(isAlive.getFnName(), isAliveVar);

        // thread.interrupt()
        class InterruptFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yThreadInstance thread = requireThreadThis(interpreter);

                Thread jt = thread.getJavaThread();

                if (jt == null) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            -1,
                            "Thread is not initialized."
                    );
                }

                jt.interrupt();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "interrupt";
            }
        }

        InterruptFn interrupt = new InterruptFn();
        Variable interruptVar = new Variable(
                new Variable.Variant(interrupt),
                true,
                "function");
        yThread_Instance_Prototype.set(interrupt.getFnName(), interruptVar);

    }


    public static class yThreadInstance extends yClass.ClassObjectInstance {

        private Thread javaThread;

        private Callable callable;

        public yThreadInstance(Callable callable) {
            this.callable = callable;
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

        @Override
        public int arity() {
            return 1; // expect callable for argument
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            if(arguments.isEmpty()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
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
             newInstance.curEnv = interpreter.curEnv;
             thread.setJavaThread(new ThreadWrapper(newInstance, fn, fnArgs));

            return new Variable.Variant(thread);
        }

        @Override
        public String getClassName() {
            return "Thread";
        }

        @Override
        public String getType() {
            return "Thread";
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
            fn.call(instance, args);
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yThreadClass ctor = new yThreadClass();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");
        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}