package ysharp.treewalk.evaluator.Native.function.core;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Variable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Clock extends Function.NativeFunction {

    // now() -> epoch milliseconds
    public static class Now extends Clock {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 0, getFnName());
            return new Variable.Variant(System.currentTimeMillis());
        }

        @Override public int arity() { return 0; }
        @Override public String getFnName() { return "now"; }
    }

    // time() -> epoch seconds
    public static class Time extends Clock {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 0, getFnName());
            return new Variable.Variant(System.currentTimeMillis() / 1000);
        }

        @Override public int arity() { return 0; }
        @Override public String getFnName() { return "time"; }
    }

    // nanoTime() -> high resolution timer
    public static class NanoTime extends Clock {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 0, getFnName());
            return new Variable.Variant(System.nanoTime());
        }

        @Override public int arity() { return 0; }
        @Override public String getFnName() { return "nanoTime"; }
    }

    // sleep(ms)
    public static class Sleep extends Clock {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());
            long ms = (long) requireNumber(args.get(0), getFnName(), 1);

            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "sleep interrupted"
                );
            }

            return new Variable.Variant(null);
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "sleep"; }
    }

    // formatTime(timestamp)
    public static class FormatTime extends Clock {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            long ts = (long) requireNumber(args.get(0), getFnName(), 1);

            LocalDateTime dt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(ts),
                    ZoneId.systemDefault()
            );

            String formatted = dt.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );

            return new Variable.Variant(formatted);
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "formatTime"; }
    }

    public static void  Register(Interpreter interpreter) {
        Now nowFn = new Now();
        interpreter.global.define(nowFn.getFnName(),
                new Variable(new Variable.Variant(nowFn), true, nowFn.getType()));
    }
}