package ysharp.treewalk.evaluator.Native.Util.Time;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Time.DateTime.yDateTime;
import ysharp.treewalk.evaluator.Native.Util.Time.function.statix.*;

import java.text.Format;
import java.time.Instant;
import java.util.List;

public class yTime {

    public static class yTimeClass extends yClass.SealedClassObject {

        yTimeClass(){

            this.prototype = yClass.ClassPrototype;

            // Time.now()  -> seconds
            this.RegisterNativeFn(new NowFn(), List.of("seconds"));
            // Time.nowMillis()
            this.RegisterNativeFn(new NowMillisFn());
            // Time.nano()
            this.RegisterNativeFn(new NanoFn());
            // Time.iso()
            this.RegisterNativeFn(new IsoFn());
            // Time.minutes()
            this.RegisterNativeFn(new MinutesFn());
            // Time.hours()
            this.RegisterNativeFn(new HoursFn());
            // Time.formatEpochSeconds(timestamp)
            this.RegisterNativeFn(new FormatEpochSecondsFn());
            // Time.formatEpochMillis(timestamp)
            this.RegisterNativeFn(new FormatEpochMillisFn());
            // Time.parse(iso)
            this.RegisterNativeFn(new ParseFn());
            // Time.measure(fn)
            this.RegisterNativeFn(new MeasureFn());

            // DateTime static class
            this.RegisterClass(new yDateTime.yDateTimeClass());
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of static Time class");
        }

        @Override
        public String getClassName() {
            return "Time";
        }

        @Override
        public String getType() {
            return "_Time_";
        }

        @Override
        public String toString() {
            return "<class:Time>";
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