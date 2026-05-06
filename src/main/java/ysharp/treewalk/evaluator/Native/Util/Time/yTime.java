package ysharp.treewalk.evaluator.Native.Util.Time;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Time.function.statix.*;

import java.text.Format;
import java.time.Instant;
import java.util.List;

public class yTime {

    public static class yTimeClass extends yClass.SealedClassObject {

        yTimeClass(){

            this.prototype = yClass.ClassPrototype;

            // Time.now()  -> seconds
            this.RegisterNativeFn(new NowFn());
            // Time.nowMillis()
            this.RegisterNativeFn(new NowMillisFn());
            // Time.nano()
            this.RegisterNativeFn(new NanoFn());
            // Time.iso()
            this.RegisterNativeFn(new IsoFn());
            // Time.seconds()
            this.RegisterNativeFn(new SecondsFn());
            // Time.minutes()
            this.RegisterNativeFn(new MinutesFn());
            // Time.format(timestamp)
            this.RegisterNativeFn(new FormatFn());
            // Time.parse(iso)
            this.RegisterNativeFn(new ParseFn());
            // Time.measure(fn)
            this.RegisterNativeFn(new MeasureFn());
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