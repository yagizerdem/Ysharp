---
sidebar_position: 41
---

The `Time` class provides utilities for retrieving the current time,
formatting epoch timestamps, parsing ISO-8601 strings and measuring
the elapsed time of a callable.

<h3>**All of the methods under the Time class are static**</h3>

## Reference

### Static methods

| Method               | Signature                                | Return Type  | Description                                                                                   |
|----------------------|------------------------------------------|--------------|-----------------------------------------------------------------------------------------------|
| `now`                | `now()`                                  | `number`     | Returns the current time as Unix epoch seconds. Also available as `Time.seconds()`            |
| `seconds`            | `seconds()`                              | `number`     | Alias of `now()`. Returns the current time as Unix epoch seconds                              |
| `nowMillis`          | `nowMillis()`                            | `number`     | Returns the current time as Unix epoch milliseconds                                           |
| `nano`               | `nano()`                                 | `number`     | Returns the current value of the JVM high-resolution timer in nanoseconds                     |
| `minutes`            | `minutes()`                              | `number`     | Returns the current time as Unix epoch minutes (fractional)                                   |
| `hours`              | `hours()`                                | `number`     | Returns the current time as Unix epoch hours (fractional)                                     |
| `iso`                | `iso()`                                  | `string`     | Returns the current instant formatted as an ISO-8601 string (e.g. `2026-05-06T12:34:56.789Z`) |
| `formatEpochSeconds` | `formatEpochSeconds(timestamp : number)` | `string`     | Converts a Unix epoch seconds timestamp into an ISO-8601 string                               |
| `formatEpochMillis`  | `formatEpochMillis(timestamp : number)`  | `string`     | Converts a Unix epoch milliseconds timestamp into an ISO-8601 string                          |
| `parse`              | `parse(value : string)`                  | `number`     | Parses an ISO-8601 string and returns the corresponding Unix epoch seconds                    |
| `measure`            | `measure(fn : function)`                 | `number`     | Invokes the given callable with no arguments and returns the elapsed time in milliseconds     |

## Examples

### Getting the current time

```ys
println Time.now();        // e.g. 1746528000
println Time.nowMillis();  // e.g. 1746528000000
println Time.iso();        // e.g. "2026-05-06T12:34:56.789Z"
```

### Formatting epoch timestamps

```ys
let ts = Time.now();
println Time.formatEpochSeconds(ts);  // ISO-8601 representation

let tsMs = Time.nowMillis();
println Time.formatEpochMillis(tsMs); // ISO-8601 representation
```

### Parsing an ISO-8601 string

```ys
let seconds = Time.parse("2026-05-06T12:34:56Z");
print seconds; // Unix epoch seconds
```

### Measuring elapsed time

```ys
let elapsedMs = Time.measure(() => do
    // some work...
    let sum = 0;
    for var i = 0; i < 1000000; i = i + 1 do
        sum = sum + i;
    end
end);

println "took " + elapsedMs + " ms";
```

## Notes

- `Time` is a sealed static class it cannot be instantiated.
- `now()`, `seconds()`, `nowMillis()`, `minutes()` and `hours()` are all
  derived from the system wall clock (`System.currentTimeMillis()`).
- `nano()` is based on the JVM high-resolution timer and is only meaningful
  for measuring elapsed time, not for representing wall-clock time.
- `measure(fn)` calls `fn` with no arguments; pass a zero-arity function
  or a closure that captures everything it needs.

---

## DateTime

The `DateTime` class provides utilities for working with calendar dates
and wall-clock times based on the system's local time zone. It exposes
accessors for individual components of the current date and time, plus
helpers to parse, format, shift and compare ISO-8601 datetime strings.

<h3>**All of the methods under the DateTime class are static**</h3>

### Reference

#### Static methods

| Method          | Signature                                  | Return Type | Description                                                                                                  |
|-----------------|--------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------|
| `getYear`       | `getYear()`                                | `number`    | Returns the current year (e.g. `2026`)                                                                       |
| `getMonth`      | `getMonth()`                               | `number`    | Returns the current month as a number in the range `1..12`                                                   |
| `getDayOfMonth` | `getDayOfMonth()`                          | `number`    | Returns the current day of the month in the range `1..31`                                                    |
| `getDayOfWeek`  | `getDayOfWeek()`                           | `string`    | Returns the current day of the week as an upper-case name (e.g. `"WEDNESDAY"`)                               |
| `getHour`       | `getHour()`                                | `number`    | Returns the current hour of day in the range `0..23`                                                         |
| `getMinute`     | `getMinute()`                              | `number`    | Returns the current minute of the hour in the range `0..59`                                                  |
| `getSecond`     | `getSecond()`                              | `number`    | Returns the current second of the minute in the range `0..59`                                                |
| `getDate`       | `getDate()`                                | `string`    | Returns the current local date as an ISO-8601 string (e.g. `"2026-05-06"`)                                   |
| `getTime`       | `getTime()`                                | `string`    | Returns the current local time formatted as `HH:mm:ss` (e.g. `"12:34:56"`)                                   |
| `getDateTime`   | `getDateTime()`                            | `string`    | Returns the current local date-time as an ISO-8601 string (e.g. `"2026-05-06T12:34:56.789"`)                 |
| `parse`         | `parse(value : string)`                    | `string`    | Parses an ISO-8601 local date-time string and returns its normalized string form. Throws on invalid input    |
| `format`        | `format(value : string, pattern : string)` | `string`    | Parses an ISO-8601 local date-time string and reformats it using the given pattern (e.g. `"dd/MM/yyyy"`)     |
| `plusDays`      | `plusDays(value : string, days : number)`  | `string`    | Returns a new ISO-8601 local date-time string shifted forward by the given number of days                    |
| `minusDays`     | `minusDays(value : string, days : number)` | `string`    | Returns a new ISO-8601 local date-time string shifted backward by the given number of days                   |
| `isBefore`      | `isBefore(left : string, right : string)`  | `boolean`   | Returns `true` if `left` is strictly before `right`. Both arguments must be ISO-8601 local date-time strings |
| `diffDays`      | `diffDays(left : string, right : string)`  | `number`    | Returns the number of whole days between `left` and `right` (`right - left`). May be negative                |

### Examples

#### Reading the current date and time

```ys
println Time.DateTime.getYear();        // e.g. 2026
println Time.DateTime.getMonth();       // e.g. 5
println Time.DateTime.getDayOfMonth();  // e.g. 6
println Time.DateTime.getDayOfWeek();   // e.g. "WEDNESDAY"

println Time.DateTime.getHour();        // e.g. 12
println Time.DateTime.getMinute();      // e.g. 34
println Time.DateTime.getSecond();      // e.g. 56

println Time.DateTime.getDate();        // e.g. "2026-05-06"
println Time.DateTime.getTime();        // e.g. "12:34:56"
println Time.DateTime.getDateTime();    // e.g. "2026-05-06T12:34:56.789"
```

#### Parsing and formatting

```ys
let dt = Time.DateTime.parse("2026-05-06T12:34:56");
println dt; // "2026-05-06T12:34:56"

println Time.DateTime.format("2026-05-06T12:34:56", "dd/MM/yyyy HH:mm");
// "06/05/2026 12:34"
```

#### Shifting dates

```ys
let now      = Time.DateTime.getDateTime();
let tomorrow = Time.DateTime.plusDays(now, 1);
let lastWeek = Time.DateTime.minusDays(now, 7);

println tomorrow;
println lastWeek;
```

#### Comparing dates

```ys
let a = "2026-05-06T12:00:00";
let b = "2026-05-10T12:00:00";

println Time.DateTime.isBefore(a, b); // true
println Time.DateTime.diffDays(a, b); // 4
println Time.DateTime.diffDays(b, a); // -4
```

### Notes

- `DateTime` is a sealed static class; it cannot be instantiated.
- All accessors (`getYear`, `getMonth`, `getDayOfMonth`, `getDayOfWeek`,
  `getHour`, `getMinute`, `getSecond`, `getDate`, `getTime`,
  `getDateTime`) read the system's **local** date and time, not UTC.
- `parse`, `format`, `plusDays`, `minusDays`, `isBefore` and `diffDays`
  all expect ISO-8601 **local** date-time strings (the same shape
  produced by `getDateTime()`, e.g. `2026-05-06T12:34:56`). Passing a
  zoned or offset string (e.g. ending in `Z` or `+02:00`) will raise an
  error.
- `format` uses Java's `DateTimeFormatter` pattern syntax (e.g. `yyyy`,
  `MM`, `dd`, `HH`, `mm`, `ss`). Invalid patterns raise an error.
- `diffDays(left, right)` returns whole days only; partial days are
  truncated toward zero.
