package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class yDateTime {

    static {}

    public static class yDateTimeInstance extends yClass.ClassObjectInstance {

        private ZonedDateTime internal;

        public yDateTimeInstance(ZonedDateTime dt) {
            this.internal = dt;
        }

        public ZonedDateTime getInternal() {
            return internal;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "DateTime";
        }

        @Override
        public String toString() {
            return internal.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }


    public static class yDateTimeClass extends yClass.SealedClassObject {

        yDateTimeClass() {
            this.prototype = yClass.ClassPrototype;

            // add static methods here

            // DateTime.now() -> DateTime  (current local time with system zone)
            class NowFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    yDateTimeInstance instance = new yDateTimeInstance(ZonedDateTime.now());

                    return new Variable.Variant(instance);
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
                    "function");
            this.set(now.getFnName(), nowVar);


            // DateTime.utcNow() -> DateTime  (current UTC time)
            class UtcNowFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    yDateTimeInstance instance = new yDateTimeInstance(ZonedDateTime.now(ZoneOffset.UTC));

                    return new Variable.Variant(instance);
                }

                @Override
                public String getFnName() {
                    return "utcNow";
                }
            }

            UtcNowFn utcNow = new UtcNowFn();
            Variable utcNowVar = new Variable(
                    new Variable.Variant(utcNow),
                    true,
                    "function");
            this.set(utcNow.getFnName(), utcNowVar);


            // DateTime.of(year, month, day, hour, minute, second) -> DateTime
            class OfFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 6;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 6, getClassName());

                    int year   = (int) requireInt(arguments.get(0), getClassName(), 1);
                    int month  = (int) requireInt(arguments.get(1), getClassName(), 2);
                    int day    = (int) requireInt(arguments.get(2), getClassName(), 3);
                    int hour   = (int) requireInt(arguments.get(3), getClassName(), 4);
                    int minute = (int) requireInt(arguments.get(4), getClassName(), 5);
                    int second = (int) requireInt(arguments.get(5), getClassName(), 6);

                    ZonedDateTime dt = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneOffset.UTC);
                    yDateTimeInstance instance = new yDateTimeInstance(dt);

                    return new Variable.Variant(instance);
                }

                @Override
                public String getFnName() {
                    return "of";
                }
            }

            OfFn of = new OfFn();
            Variable ofVar = new Variable(
                    new Variable.Variant(of),
                    true,
                    "function");
            this.set(of.getFnName(), ofVar);


            // DateTime.fromEpoch(epochSeconds: number) -> DateTime
            class FromEpochFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double epochSeconds = requireNumber(arguments.getFirst(), getClassName(), 1);
                    ZonedDateTime dt = ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond((long) epochSeconds),
                            ZoneOffset.UTC);
                    yDateTimeInstance instance = new yDateTimeInstance(dt);

                    return new Variable.Variant(instance);
                }

                @Override
                public String getFnName() {
                    return "fromEpoch";
                }
            }

            FromEpochFn fromEpoch = new FromEpochFn();
            Variable fromEpochVar = new Variable(
                    new Variable.Variant(fromEpoch),
                    true,
                    "function");
            this.set(fromEpoch.getFnName(), fromEpochVar);


            // DateTime.parse(isoString: string) -> DateTime
            class ParseFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String raw = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        ZonedDateTime dt = ZonedDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        yDateTimeInstance instance = new yDateTimeInstance(dt);
                        return new Variable.Variant(instance);
                    } catch (DateTimeParseException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "DateTime.parse: invalid ISO date-time string '" + raw + "'");
                    }
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
                    "function");
            this.set(parse.getFnName(), parseVar);


            // DateTime.year(dt: DateTime) -> number
            class YearFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getYear();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "year";
                }
            }

            YearFn year = new YearFn();
            Variable yearVar = new Variable(
                    new Variable.Variant(year),
                    true,
                    "function");
            this.set(year.getFnName(), yearVar);


            // DateTime.month(dt: DateTime) -> number  (1-12)
            class MonthFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getMonthValue();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "month";
                }
            }

            MonthFn month = new MonthFn();
            Variable monthVar = new Variable(
                    new Variable.Variant(month),
                    true,
                    "function");
            this.set(month.getFnName(), monthVar);


            // DateTime.day(dt: DateTime) -> number  (1-31)
            class DayFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getDayOfMonth();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "day";
                }
            }

            DayFn day = new DayFn();
            Variable dayVar = new Variable(
                    new Variable.Variant(day),
                    true,
                    "function");
            this.set(day.getFnName(), dayVar);


            // DateTime.hour(dt: DateTime) -> number  (0-23)
            class HourFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getHour();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "hour";
                }
            }

            HourFn hour = new HourFn();
            Variable hourVar = new Variable(
                    new Variable.Variant(hour),
                    true,
                    "function");
            this.set(hour.getFnName(), hourVar);


            // DateTime.minute(dt: DateTime) -> number  (0-59)
            class MinuteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getMinute();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "minute";
                }
            }

            MinuteFn minute = new MinuteFn();
            Variable minuteVar = new Variable(
                    new Variable.Variant(minute),
                    true,
                    "function");
            this.set(minute.getFnName(), minuteVar);


            // DateTime.second(dt: DateTime) -> number  (0-59)
            class SecondFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getSecond();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "second";
                }
            }

            SecondFn second = new SecondFn();
            Variable secondVar = new Variable(
                    new Variable.Variant(second),
                    true,
                    "function");
            this.set(second.getFnName(), secondVar);


            // DateTime.dayOfWeek(dt: DateTime) -> number  (1=Mon ... 7=Sun)
            class DayOfWeekFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getDayOfWeek().getValue();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "dayOfWeek";
                }
            }

            DayOfWeekFn dayOfWeek = new DayOfWeekFn();
            Variable dayOfWeekVar = new Variable(
                    new Variable.Variant(dayOfWeek),
                    true,
                    "function");
            this.set(dayOfWeek.getFnName(), dayOfWeekVar);


            // DateTime.dayOfYear(dt: DateTime) -> number  (1-366)
            class DayOfYearFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = dt.getInternal().getDayOfYear();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "dayOfYear";
                }
            }

            DayOfYearFn dayOfYear = new DayOfYearFn();
            Variable dayOfYearVar = new Variable(
                    new Variable.Variant(dayOfYear),
                    true,
                    "function");
            this.set(dayOfYear.getFnName(), dayOfYearVar);


            // DateTime.toEpoch(dt: DateTime) -> number  (seconds since Unix epoch)
            class ToEpochFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    double response = (double) dt.getInternal().toEpochSecond();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "toEpoch";
                }
            }

            ToEpochFn toEpoch = new ToEpochFn();
            Variable toEpochVar = new Variable(
                    new Variable.Variant(toEpoch),
                    true,
                    "function");
            this.set(toEpoch.getFnName(), toEpochVar);


            // DateTime.format(dt: DateTime, pattern: string) -> string
            class FormatFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt      = requireDateTime(arguments.get(0), getClassName(), 1);
                    String             pattern = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        String response = dt.getInternal().format(DateTimeFormatter.ofPattern(pattern));
                        return new Variable.Variant(response);
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "DateTime.format: invalid pattern '" + pattern + "'");
                    }
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
                    "function");
            this.set(format.getFnName(), formatVar);


            // DateTime.addDays(dt: DateTime, days: number) -> DateTime
            class AddDaysFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt   = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             days = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusDays((long) days));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addDays";
                }
            }

            AddDaysFn addDays = new AddDaysFn();
            Variable addDaysVar = new Variable(
                    new Variable.Variant(addDays),
                    true,
                    "function");
            this.set(addDays.getFnName(), addDaysVar);


            // DateTime.addHours(dt: DateTime, hours: number) -> DateTime
            class AddHoursFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt    = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             hours = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusHours((long) hours));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addHours";
                }
            }

            AddHoursFn addHours = new AddHoursFn();
            Variable addHoursVar = new Variable(
                    new Variable.Variant(addHours),
                    true,
                    "function");
            this.set(addHours.getFnName(), addHoursVar);


            // DateTime.addMinutes(dt: DateTime, minutes: number) -> DateTime
            class AddMinutesFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt      = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             minutes = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusMinutes((long) minutes));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addMinutes";
                }
            }

            AddMinutesFn addMinutes = new AddMinutesFn();
            Variable addMinutesVar = new Variable(
                    new Variable.Variant(addMinutes),
                    true,
                    "function");
            this.set(addMinutes.getFnName(), addMinutesVar);


            // DateTime.addSeconds(dt: DateTime, seconds: number) -> DateTime
            class AddSecondsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt      = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             seconds = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusSeconds((long) seconds));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addSeconds";
                }
            }

            AddSecondsFn addSeconds = new AddSecondsFn();
            Variable addSecondsVar = new Variable(
                    new Variable.Variant(addSeconds),
                    true,
                    "function");
            this.set(addSeconds.getFnName(), addSecondsVar);


            // DateTime.addMonths(dt: DateTime, months: number) -> DateTime
            class AddMonthsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt     = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             months = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusMonths((long) months));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addMonths";
                }
            }

            AddMonthsFn addMonths = new AddMonthsFn();
            Variable addMonthsVar = new Variable(
                    new Variable.Variant(addMonths),
                    true,
                    "function");
            this.set(addMonths.getFnName(), addMonthsVar);


            // DateTime.addYears(dt: DateTime, years: number) -> DateTime
            class AddYearsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt    = requireDateTime(arguments.get(0), getClassName(), 1);
                    double             years = requireNumber(arguments.get(1), getClassName(), 2);

                    yDateTimeInstance response = new yDateTimeInstance(dt.getInternal().plusYears((long) years));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "addYears";
                }
            }

            AddYearsFn addYears = new AddYearsFn();
            Variable addYearsVar = new Variable(
                    new Variable.Variant(addYears),
                    true,
                    "function");
            this.set(addYears.getFnName(), addYearsVar);


            // DateTime.diffSeconds(a: DateTime, b: DateTime) -> number  (a - b in seconds)
            class DiffSecondsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance a = requireDateTime(arguments.get(0), getClassName(), 1);
                    yDateTimeInstance b = requireDateTime(arguments.get(1), getClassName(), 2);

                    double response = (double) Duration.between(b.getInternal(), a.getInternal()).getSeconds();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "diffSeconds";
                }
            }

            DiffSecondsFn diffSeconds = new DiffSecondsFn();
            Variable diffSecondsVar = new Variable(
                    new Variable.Variant(diffSeconds),
                    true,
                    "function");
            this.set(diffSeconds.getFnName(), diffSecondsVar);


            // DateTime.diffDays(a: DateTime, b: DateTime) -> number  (a - b in days)
            class DiffDaysFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance a = requireDateTime(arguments.get(0), getClassName(), 1);
                    yDateTimeInstance b = requireDateTime(arguments.get(1), getClassName(), 2);

                    double response = (double) Duration.between(b.getInternal(), a.getInternal()).toDays();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "diffDays";
                }
            }

            DiffDaysFn diffDays = new DiffDaysFn();
            Variable diffDaysVar = new Variable(
                    new Variable.Variant(diffDays),
                    true,
                    "function");
            this.set(diffDays.getFnName(), diffDaysVar);


            // DateTime.isBefore(a: DateTime, b: DateTime) -> bool
            class IsBeforeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance a = requireDateTime(arguments.get(0), getClassName(), 1);
                    yDateTimeInstance b = requireDateTime(arguments.get(1), getClassName(), 2);

                    boolean response = a.getInternal().isBefore(b.getInternal());

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "isBefore";
                }
            }

            IsBeforeFn isBefore = new IsBeforeFn();
            Variable isBeforeVar = new Variable(
                    new Variable.Variant(isBefore),
                    true,
                    "function");
            this.set(isBefore.getFnName(), isBeforeVar);


            // DateTime.isAfter(a: DateTime, b: DateTime) -> bool
            class IsAfterFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance a = requireDateTime(arguments.get(0), getClassName(), 1);
                    yDateTimeInstance b = requireDateTime(arguments.get(1), getClassName(), 2);

                    boolean response = a.getInternal().isAfter(b.getInternal());

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "isAfter";
                }
            }

            IsAfterFn isAfter = new IsAfterFn();
            Variable isAfterVar = new Variable(
                    new Variable.Variant(isAfter),
                    true,
                    "function");
            this.set(isAfter.getFnName(), isAfterVar);


            // DateTime.isEqual(a: DateTime, b: DateTime) -> bool
            class IsEqualFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance a = requireDateTime(arguments.get(0), getClassName(), 1);
                    yDateTimeInstance b = requireDateTime(arguments.get(1), getClassName(), 2);

                    boolean response = a.getInternal().isEqual(b.getInternal());

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "isEqual";
                }
            }

            IsEqualFn isEqual = new IsEqualFn();
            Variable isEqualVar = new Variable(
                    new Variable.Variant(isEqual),
                    true,
                    "function");
            this.set(isEqual.getFnName(), isEqualVar);


            // DateTime.toUtc(dt: DateTime) -> DateTime
            class ToUtcFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    yDateTimeInstance response = new yDateTimeInstance(
                            dt.getInternal().withZoneSameInstant(ZoneOffset.UTC));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "toUtc";
                }
            }

            ToUtcFn toUtc = new ToUtcFn();
            Variable toUtcVar = new Variable(
                    new Variable.Variant(toUtc),
                    true,
                    "function");
            this.set(toUtc.getFnName(), toUtcVar);


            // DateTime.withZone(dt: DateTime, zoneId: string) -> DateTime
            class WithZoneFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    yDateTimeInstance dt     = requireDateTime(arguments.get(0), getClassName(), 1);
                    String             zoneId = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        ZoneId zone = ZoneId.of(zoneId);
                        yDateTimeInstance response = new yDateTimeInstance(
                                dt.getInternal().withZoneSameInstant(zone));
                        return new Variable.Variant(response);
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1 ,
                                "DateTime.withZone: unknown zone '" + zoneId + "'");
                    }
                }

                @Override
                public String getFnName() {
                    return "withZone";
                }
            }

            WithZoneFn withZone = new WithZoneFn();
            Variable withZoneVar = new Variable(
                    new Variable.Variant(withZone),
                    true,
                    "function");
            this.set(withZone.getFnName(), withZoneVar);


            // DateTime.toString(dt: DateTime) -> string  (ISO-8601)
            class ToStringFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    yDateTimeInstance dt = requireDateTime(arguments.getFirst(), getClassName(), 1);
                    String response = dt.toString();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "toString";
                }
            }

            ToStringFn toStringFn = new ToStringFn();
            Variable toStringVar = new Variable(
                    new Variable.Variant(toStringFn),
                    true,
                    "function");
            this.set(toStringFn.getFnName(), toStringVar);
        }



        @Override
        public int arity() {
            return 3;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            requireArity(arguments, 3, getClassName());

            int year  = (int) requireInt(arguments.get(0), getClassName(), 1);
            int month = (int) requireInt(arguments.get(1), getClassName(), 2);
            int day   = (int) requireInt(arguments.get(2), getClassName(), 3);

            ZonedDateTime dt = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
            yDateTimeInstance instance = new yDateTimeInstance(dt);

            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "DateTime";
        }

        @Override
        public String getType() {
            return "DateTime";
        }


        private yDateTimeInstance requireDateTime(Variable.Variant variant,
                                                   String className,
                                                   int argIndex) throws YsharpError {
            if (variant.value instanceof yDateTimeInstance dt) {
                return dt;
            }
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    className + ": argument " + argIndex + " must be a DateTime instance");
        }
    }


    public static void Register(Interpreter interpreter) throws Exception {

        yDateTimeClass ctor = new yDateTimeClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}