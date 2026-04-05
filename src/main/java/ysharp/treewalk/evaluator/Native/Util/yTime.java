package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.time.Instant;
import java.util.List;

public class yTime {

    public static RuntimeObject yTime_Instance_Prototype;

    static {}


    public static class yTime_Instance extends yClass.ClassObjectInstance {

        public yTime_Instance() {}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Time";
        }

        @Override
        public String toString() {
            return "<instance:Time>";
        }
    }


    public static class yTimeClass extends yClass.SealedClassObject {

        yTimeClass(){

            this.prototype = yClass.ClassPrototype;

            // Time.now()  -> seconds
            class NowFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    long now = System.currentTimeMillis() / 1000;

                    return new Variable.Variant((double) now);
                }

                @Override
                public String getFnName() {
                    return "now";
                }
            }

            NowFn now = new NowFn();

            Variable nowVar = new Variable(
                    new Variable.Variant(now),
                    true,
                    "function"
            );

            this.set(now.getFnName(), nowVar);


            // Time.nowMillis()
            class NowMillisFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    long now = System.currentTimeMillis();

                    return new Variable.Variant((double) now);
                }

                @Override
                public String getFnName() {
                    return "nowMillis";
                }
            }

            NowMillisFn nowMillis = new NowMillisFn();

            Variable nowMillisVar = new Variable(
                    new Variable.Variant(nowMillis),
                    true,
                    "function"
            );

            this.set(nowMillis.getFnName(), nowMillisVar);


            // Time.nano()
            class NanoFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    long nano = System.nanoTime();

                    return new Variable.Variant((double) nano);
                }

                @Override
                public String getFnName() {
                    return "nano";
                }
            }

            NanoFn nano = new NanoFn();

            Variable nanoVar = new Variable(
                    new Variable.Variant(nano),
                    true,
                    "function"
            );

            this.set(nano.getFnName(), nanoVar);


            // Time.sleep(ms)
            class SleepFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double ms = requireNumber(arguments.getFirst(), getClassName(), 1);

                    try {
                        Thread.sleep((long) ms);
                    }
                    catch (InterruptedException e) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "Sleep interrupted"
                        );
                    }

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "sleep";
                }
            }

            SleepFn sleep = new SleepFn();

            Variable sleepVar = new Variable(
                    new Variable.Variant(sleep),
                    true,
                    "function"
            );

            this.set(sleep.getFnName(), sleepVar);


            // Time.iso()
            class IsoFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    String iso = Instant.now().toString();

                    return new Variable.Variant(iso);
                }

                @Override
                public String getFnName() {
                    return "iso";
                }
            }

            IsoFn iso = new IsoFn();

            Variable isoVar = new Variable(
                    new Variable.Variant(iso),
                    true,
                    "function"
            );

            this.set(iso.getFnName(), isoVar);

            // Time.seconds()
            class SecondsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    double seconds = System.currentTimeMillis() / 1000.0;

                    return new Variable.Variant(seconds);
                }

                @Override
                public String getFnName() {
                    return "seconds";
                }
            }

            SecondsFn seconds = new SecondsFn();

            Variable secondsVar = new Variable(
                    new Variable.Variant(seconds),
                    true,
                    "function"
            );

            this.set(seconds.getFnName(), secondsVar);


            // Time.minutes()
            class MinutesFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    double minutes = System.currentTimeMillis() / 60000.0;

                    return new Variable.Variant(minutes);
                }

                @Override
                public String getFnName() {
                    return "minutes";
                }
            }

            MinutesFn minutes = new MinutesFn();

            Variable minutesVar = new Variable(
                    new Variable.Variant(minutes),
                    true,
                    "function"
            );

            this.set(minutes.getFnName(), minutesVar);

            // Time.format(timestamp)
            class FormatFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double timestamp = requireNumber(arguments.getFirst(), getClassName(), 1);

                    Instant instant = Instant.ofEpochSecond((long) timestamp);

                    return new Variable.Variant(instant.toString());
                }

                @Override
                public String getFnName() {
                    return "format";
                }
            }

            FormatFn format = new FormatFn();

            Variable formatVar = new Variable(
                    new Variable.Variant(format),
                    true,
                    "function"
            );

            this.set(format.getFnName(), formatVar);


            // Time.parse(iso)
            class ParseFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String value = requireString(arguments.getFirst(), getClassName(), 1);

                    Instant instant = Instant.parse(value);

                    return new Variable.Variant((double) instant.getEpochSecond());
                }

                @Override
                public String getFnName() {
                    return "parse";
                }
            }

            ParseFn parse = new ParseFn();

            Variable parseVar = new Variable(
                    new Variable.Variant(parse),
                    true,
                    "function"
            );

            this.set(parse.getFnName(), parseVar);


            // Time.measure(fn)
            class MeasureFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    Variable.Variant fnVar = arguments.getFirst();

                    if (!(fnVar.value instanceof Callable)) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "Time.measure expects callable"
                        );
                    }

                    Callable fn = (Callable) fnVar.value;

                    long start = System.nanoTime();

                    fn.call(interpreter, List.of());

                    long end = System.nanoTime();

                    double ms = (end - start) / 1_000_000.0;

                    return new Variable.Variant(ms);
                }

                @Override
                public String getFnName() {
                    return "measure";
                }
            }

            MeasureFn measure = new MeasureFn();

            Variable measureVar = new Variable(
                    new Variable.Variant(measure),
                    true,
                    "function"
            );

            this.set(measure.getFnName(), measureVar);
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            yTime_Instance instance = new yTime_Instance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "Time";
        }

        @Override
        public String getType() {
            return "Time";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yTimeClass ctor = new yTimeClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}