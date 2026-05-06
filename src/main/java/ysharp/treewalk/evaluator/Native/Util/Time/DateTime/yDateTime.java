package ysharp.treewalk.evaluator.Native.Util.Time.DateTime;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Time.DateTime.function.statix.*;

import java.util.List;

public class yDateTime {

    public static class yDateTimeClass extends yClass.SealedClassObject {

        public yDateTimeClass() {
            this.prototype = yClass.ClassPrototype;

            // DateTime.getMonth();
            this.RegisterNativeFn(new GetMonthFn());
            // DateTime.getYear();
            this.RegisterNativeFn(new GetYearFn());
            // DateTime.getDayOfMonth();
            this.RegisterNativeFn(new GetDayOfMonthFn());
            // DateTime.getDateTime();
            this.RegisterNativeFn(new GetDateTimeFn());
            // DateTime.getDate();
            this.RegisterNativeFn(new GetDateFn());
            // DateTime.getTime();
            this.RegisterNativeFn(new GetTimeFn());
            // DateTime.getDayOfWeek();
            this.RegisterNativeFn(new GetDayOfWeekFn());
            // DateTime.getHour();
            this.RegisterNativeFn(new GetHourFn());
            // DateTime.getMinute();
            this.RegisterNativeFn(new GetMinuteFn());
            // DateTime.getSecond();
            this.RegisterNativeFn(new GetSecondFn());

            // DateTime.parse();
            this.RegisterNativeFn(new ParseFn());

            // DateTime.format();
            this.RegisterNativeFn(new FormatFn());

            this.RegisterNativeFn(new PlusDaysFn());

            this.RegisterNativeFn(new MinusDaysFn());

            this.RegisterNativeFn(new IsBeforeFn());

            this.RegisterNativeFn(new DiffDaysFn());
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
                    "cannot take instance of static DateTime class"
            );
        }

        @Override
        public String getClassName() {
            return "DateTime";
        }

        @Override
        public String getType() {
            return "_DateTime_";
        }

        @Override
        public String toString() {
            return "<class:DateTime>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yDateTimeClass ctor = new yDateTimeClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}