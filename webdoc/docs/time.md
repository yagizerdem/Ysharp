---
sidebar_position: 41
---

The `Time` class provides utilities for retrieving the current time,
formatting epoch timestamps, parsing ISO-8601 strings and measuring
the elapsed time of a callable.

<h3>**All of the methods under the Time class are static**</h3>

## Reference

### Static methods

| Method               | Signature                                | Return Type | Description                                                                                   |
|----------------------|------------------------------------------|-------------|-----------------------------------------------------------------------------------------------|
| `now`                | `now()`                                  | `number`    | Returns the current time as Unix epoch seconds. Also available as `Time.seconds()`            |
| `seconds`            | `seconds()`                              | `number`    | Alias of `now()`. Returns the current time as Unix epoch seconds                              |
| `nowMillis`          | `nowMillis()`                            | `number`    | Returns the current time as Unix epoch milliseconds                                           |
| `nano`               | `nano()`                                 | `number`    | Returns the current value of the JVM high-resolution timer in nanoseconds                     |
| `minutes`            | `minutes()`                              | `number`    | Returns the current time as Unix epoch minutes (fractional)                                   |
| `hours`              | `hours()`                                | `number`    | Returns the current time as Unix epoch hours (fractional)                                     |
| `iso`                | `iso()`                                  | `string`    | Returns the current instant formatted as an ISO-8601 string (e.g. `2026-05-06T12:34:56.789Z`) |
| `formatEpochSeconds` | `formatEpochSeconds(timestamp : number)` | `string`    | Converts a Unix epoch seconds timestamp into an ISO-8601 string                               |
| `formatEpochMillis`  | `formatEpochMillis(timestamp : number)`  | `string`    | Converts a Unix epoch milliseconds timestamp into an ISO-8601 string                          |
| `parse`              | `parse(value : string)`                  | `number`    | Parses an ISO-8601 string and returns the corresponding Unix epoch seconds                    |
| `measure`            | `measure(fn : function)`                 | `number`    | Invokes the given callable with no arguments and returns the elapsed time in milliseconds     |

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
