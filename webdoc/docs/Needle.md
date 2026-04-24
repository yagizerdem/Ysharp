---
sidebar_position: 24
---

# Needle

`Needle` is a built-in static utility class in Ysharp that provides regular expression support through two sub-classes: `Pattern` and `Matcher`.

- `Needle.Pattern` &rarr; compiles a regex pattern with optional flags
- `Needle.Matcher` &rarr; applies a compiled pattern to an input string

> **Note** <br/>
> `Needle` itself cannot be instantiated. It serves as a namespace to access `Pattern` and `Matcher`.

---

## Needle.Pattern

`Needle.Pattern` compiles a regular expression string into a reusable pattern object.

### Constructor

```
new Needle.Pattern(pattern : string)
new Needle.Pattern(pattern : string, flags : int)
```

| Parameter | Type   | Required | Description                                 |
|-----------|--------|----------|---------------------------------------------|
| `pattern` | string | Yes      | The regular expression string               |
| `flags`   | int    | No       | One or more flag constants combined with `  |` |

#### Example

```ysharp
var p = new Needle.Pattern("\\d+");
var pInsensitive = new Needle.Pattern("hello", Needle.Pattern.CASE_INSENSITIVE);
```

### Pattern Flags

Flag constants are accessed as static fields on `Needle.Pattern`.

| Flag                | Description                                             |
|---------------------|---------------------------------------------------------|
| `CASE_INSENSITIVE`  | Case-insensitive matching                               |
| `MULTILINE`         | `^` and `$` match start/end of each line                |
| `DOTALL`            | `.` matches any character including line terminators    |
| `UNICODE_CASE`      | Unicode-aware case folding (use with `CASE_INSENSITIVE`)|
| `COMMENTS`          | Whitespace and `#` comments are ignored in pattern      |
| `UNIX_LINES`        | Only `\n` is recognized as a line terminator            |
| `LITERAL`           | Pattern is treated as a literal string                  |
| `CANON_EQ`          | Canonical Unicode equivalence matching                  |

Multiple flags can be combined using `|`:

```ysharp
var p = new Needle.Pattern("foo", Needle.Pattern.CASE_INSENSITIVE | Needle.Pattern.MULTILINE);
```

### Instance Methods

#### Reference

| Method     | Signature                                     | Return Type | Description                                                                                                                    |
|------------|-----------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------|
| `flags`    | `pattern.flags()`                             | `int`       | Returns the bitmask flags of the compiled regex pattern (e.g., CASE_INSENSITIVE, MULTILINE, etc.)                              |
| `matcher`  | `pattern.matcher(input : string)`             | `Matcher`   | Creates a matcher for the given input string using this pattern and returns a matcher instance for performing match operations |
| `split`    | `pattern.split(input : string, limit? : int)` | `array`     | Splits the input string using this regex pattern; optionally limits the number of resulting substrings                         |
| `toString` | `pattern.toString()`                          | `string`    | Returns the string representation of the regex pattern                                                                         |


#### `matcher(input : string) : Matcher`

Creates a `Matcher` for the given input string against this pattern.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 123 def");
```

#### `split(input : string) : Array`
#### `split(input : string, limit : int) : Array`

Splits the input string around matches of this pattern. If `limit` is provided, the array will contain at most `limit` entries; the last entry contains the remainder of the string.

```ysharp
var p = new Needle.Pattern(",");
var parts = p.split("a,b,c,d");
println parts.toString();   // [a, b, c, d]

var limited = p.split("a,b,c,d", 2);
println limited.toString();  // [a, b,c,d]
```

#### `flags() : int`

Returns the integer value of the flags that were used when this pattern was compiled.

```ysharp
var p = new Needle.Pattern("foo", Needle.Pattern.CASE_INSENSITIVE);
println p.flags();   // 2
```

#### `toString() : string`

Returns the original regex string used to compile this pattern.

```ysharp
var p = new Needle.Pattern("\\d+");
println p.toString();   // \d+
```

---

## Needle.Matcher

A `Matcher` is created from a `Pattern` and applies the pattern against an input string. It maintains internal state, including the current position and the last match found.

> **Note** <br/>
> `Needle.Matcher` cannot be instantiated directly. Use `pattern.matcher(input)` to obtain a `Matcher` instance.

### Instance Methods

#### Reference

| Method                 | Signature                                   | Return Type | Description                                                                                     |
|------------------------|---------------------------------------------|-------------|-------------------------------------------------------------------------------------------------|
| `_end`                 | `matcher._end(group? : int \| string)`      | `int`       | Returns the end index of the last match or the specified capturing group (by index or name)     |
| `find`                 | `matcher.find(start? : int)`                | `bool`      | Attempts to find the next match; optionally starts searching from the given index               |
| `groupCount`           | `matcher.groupCount()`                      | `int`       | Returns the number of capturing groups in the pattern                                           |
| `group`                | `matcher.group(group? : int \| string)`     | `string`    | Returns the matched substring or the specified capturing group (by index or name)               |
| `hasAnchoringBounds`   | `matcher.hasAnchoringBounds()`              | `bool`      | Returns true if anchoring bounds are enabled for this matcher                                   |
| `hasTransparentBounds` | `matcher.hasTransparentBounds()`            | `bool`      | Returns true if transparent bounds are enabled for this matcher                                 |
| `hitEnd`               | `matcher.hitEnd()`                          | `bool`      | Returns true if the last match attempt reached the end of the input                             |
| `lookingAt`            | `matcher.lookingAt()`                       | `bool`      | Attempts to match the input sequence starting from the beginning                                |
| `matches`              | `matcher.matches()`                         | `bool`      | Attempts to match the entire input sequence against the pattern                                 |
| `pattern`              | `matcher.pattern()`                         | `Pattern`   | Returns the regex pattern associated with this matcher                                          |
| `regionEnd`            | `matcher.regionEnd()`                       | `int`       | Returns the end index of the current matching region                                            |
| `region`               | `matcher.region(start : int, end : int)`    | `Matcher`   | Sets the region for this matcher to the specified start and end indices and returns the matcher |
| `regionStart`          | `matcher.regionStart()`                     | `int`       | Returns the start index of the current matching region                                          |
| `replaceAll`           | `matcher.replaceAll(replacement : string)`  | `string`    | Replaces all matches in the input with the given replacement string and returns the result      |
| `requireEnd`           | `matcher.requireEnd()`                      | `bool`      | Returns true if more input could change the result of the last match                            |
| `reset`                | `matcher.reset(input? : string)`            | `Matcher`   | Resets this matcher; optionally creates a new matcher with the given input                      |
| `start`                | `matcher.start(group? : int \| string)`     | `int`       | Returns the start index of the last match or the specified capturing group (by index or name)   |
| `toString`             | `matcher.toString()`                        | `string`    | Returns the string representation of this matcher                                               |
| `useAnchoringBounds`   | `matcher.useAnchoringBounds(flag : bool)`   | `Matcher`   | Enables or disables anchoring bounds for this matcher and returns the matcher                   |
| `usePattern`           | `matcher.usePattern(pattern : Pattern)`     | `Matcher`   | Sets a new pattern for this matcher and returns the matcher                                     |
| `useTransparentBounds` | `matcher.useTransparentBounds(flag : bool)` | `Matcher`   | Enables or disables transparent bounds for this matcher and returns the matcher                 |


#### `end(group? : int | string) : int`

Returns the index after the last character of the most recent match. With a group index or named group, returns the end index of that capturing group instead.

```ysharp
var p = new Needle.Pattern("(\\d+)-(\\d+)");
var m = p.matcher("range: 10-99");
if m.find() then do
    println m._end();    // 12  (end of full match "10-99")
    println m._end(1);   // 9   (end of group 1 "10")
    println m._end(2);   // 12  (end of group 2 "99")
end
```

```ysharp
var p = new Needle.Pattern("(?<from>\\d+)-(?<to>\\d+)");
var m = p.matcher("range: 10-99");
if m.find() then do
    println m._end("from");   // 9
    println m._end("to");     // 12
end
```

---

#### `find(start? : int) : bool`

Searches for the next subsequence that matches the pattern. Each call advances the internal cursor. If `start` is provided, the search begins from that character index and any prior match state is cleared. Returns `true` if a match is found.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42 def 99");

while m.find() do
    println m.group();   // 42
                         // 99
end
```

```ysharp
// start from index 7, skipping "42"
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42 def 99");
m.find(7);
println m.group();   // 99
```

---

#### `groupCount() : int`

Returns the number of capturing groups defined in the pattern. Group 0 (the full match) is not counted.

```ysharp
var p = new Needle.Pattern("(\\w+)@(\\w+)\\.(\\w+)");
var m = p.matcher("user@example.com");
println m.groupCount();   // 3
```

---

#### `group(group? : int | string) : string`

Returns the substring captured by the last match. With no argument, returns the entire match (group 0). With an integer, returns the capturing group at that index. With a string, returns the named capturing group.

```ysharp
var p = new Needle.Pattern("(\\w+)@(\\w+)");
var m = p.matcher("user@example");
if m.find() then do
    println m.group();    // user@example
    println m.group(0);   // user@example
    println m.group(1);   // user
    println m.group(2);   // example
end
```

```ysharp
var p = new Needle.Pattern("(?<user>\\w+)@(?<domain>\\w+)");
var m = p.matcher("admin@host");
if m.find() then do
    println m.group("user");    // admin
    println m.group("domain");  // host
end
```

---

#### `hasAnchoringBounds() : bool`

Returns `true` if anchoring bounds are currently enabled. When active, `^` and `$` anchors match the boundaries of the current region rather than the full input. Anchoring bounds are enabled by default.

```ysharp
var p = new Needle.Pattern("^\\d+");
var m = p.matcher("123 abc");
m.region(4, 7);

println m.hasAnchoringBounds();   // true
println m.lookingAt();            // false  ("abc" does not start with digits within region)

m.useAnchoringBounds(false);
println m.hasAnchoringBounds();   // false
```

---

#### `hasTransparentBounds() : bool`

Returns `true` if transparent bounds are currently enabled. When active, lookahead and lookbehind assertions can see characters outside the current region. Transparent bounds are disabled by default.

```ysharp
var p = new Needle.Pattern("(?<=\\d)abc");
var m = p.matcher("1abc");
m.region(1, 4);

println m.hasTransparentBounds();   // false
println m.lookingAt();              // false  (lookbehind cannot see "1" outside region)

m.useTransparentBounds(true);
println m.hasTransparentBounds();   // true
println m.lookingAt();              // true
```

---

#### `hitEnd() : bool`

Returns `true` if the last match attempt reached the end of the input. Useful for detecting whether additional input could affect the result.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("99");
m.find();
println m.hitEnd();   // true

var m2 = p.matcher("99 abc");
m2.find();
println m2.hitEnd();   // false
```

---

#### `lookingAt() : bool`

Attempts to match the pattern starting at the **beginning** of the input (or the region start). Unlike `matches()`, the pattern does not need to cover the entire input.

```ysharp
var p = new Needle.Pattern("\\d+");

var m = p.matcher("123abc");
println m.lookingAt();   // true

var m2 = p.matcher("abc123");
println m2.lookingAt();  // false
```

---

#### `matches() : bool`

Attempts to match the **entire** input against the pattern. Returns `true` only if the whole input string is consumed by the match.

```ysharp
var p = new Needle.Pattern("\\d+");

var m = p.matcher("12345");
println m.matches();   // true

var m2 = p.matcher("123abc");
println m2.matches();  // false
```

---

#### `pattern() : Pattern`

Returns the `Pattern` object this matcher was created from.

```ysharp
var p = new Needle.Pattern("\\w+");
var m = p.matcher("hello");
println m.pattern().toString();   // \w+
```

---

#### `regionEnd() : int`

Returns the end index of the current search region. By default this equals the length of the input string.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42 xyz");
println m.regionEnd();   // 10

m.region(0, 6);
println m.regionEnd();   // 6
```

---

#### `region(start : int, end : int) : Matcher`

Restricts the search to a substring of the input without modifying the original string. Only characters between `start` (inclusive) and `end` (exclusive) are visible to the matcher. Returns the same `Matcher` instance for chaining.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42 def 99 xyz");
m.region(4, 13);   // visible: "42 def 99"

while m.find() do
    println m.group();   // 42
                         // 99
end
```

---

#### `regionStart() : int`

Returns the start index of the current search region. By default this is `0`.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42 xyz");
println m.regionStart();   // 0

m.region(4, 9);
println m.regionStart();   // 4
```

---

#### `replaceAll(replacement : string) : string`

Replaces every subsequence of the input that matches the pattern with the given replacement string and returns the resulting string. Use `$1`, `$2`, … in the replacement to back-reference capturing groups.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("a1b22c333");
println m.replaceAll("X");   // aXbXcX
```

```ysharp
// back-reference: wrap each match in brackets
var p = new Needle.Pattern("(\\w+)@(\\w+)");
var m = p.matcher("foo@bar and baz@qux");
println m.replaceAll("[$1]");   // [foo] and [baz]
```

---

#### `requireEnd() : bool`

Returns `true` if more input appended to the current string could change the result of the last match. Useful for incremental or streaming matching scenarios.

```ysharp
var p = new Needle.Pattern("\\d+$");
var m = p.matcher("123");
m.find();
println m.requireEnd();   // true  (appending more chars could break the $ anchor)
```

---

#### `reset(input? : string) : Matcher`

Resets the matcher to the beginning of the input, clearing all match state. If `input` is provided, the matcher's input is replaced with the new string. Returns the same `Matcher` instance for chaining.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("1 2 3");
m.find();
println m.group();   // 1

m.reset();
m.find();
println m.group();   // 1  (cursor reset to start)
```

```ysharp
// reset with new input
var p = new Needle.Pattern("\\d+");
var m = p.matcher("aaa");
m.reset("99 bbb");
m.find();
println m.group();   // 99
```

---

#### `start(group? : int | string) : int`

Returns the start index of the most recent match. With a group index or named group, returns the start index of that capturing group.

```ysharp
var p = new Needle.Pattern("(\\d+)-(\\d+)");
var m = p.matcher("range: 10-99");
if m.find() then do
    println m.start();    // 7   (start of full match "10-99")
    println m.start(1);   // 7   (start of group 1 "10")
    println m.start(2);   // 10  (start of group 2 "99")
end
```

```ysharp
var p = new Needle.Pattern("(?<from>\\d+)-(?<to>\\d+)");
var m = p.matcher("range: 10-99");
if m.find() then do
    println m.start("from");   // 7
    println m.start("to");     // 10
end
```

---

#### `toString() : string`

Returns a string representation of the matcher, including pattern and current position information.

```ysharp
var p = new Needle.Pattern("\\d+");
var m = p.matcher("abc 42");
println m.toString();
```

---

#### `useAnchoringBounds(flag : bool) : Matcher`

Enables or disables anchoring bounds for this matcher. Returns the same `Matcher` instance for chaining. See `hasAnchoringBounds()` for a full explanation.

```ysharp
var p = new Needle.Pattern("^\\d+");
var m = p.matcher("42 abc");
m.region(0, 2);

m.useAnchoringBounds(true);
println m.lookingAt();   // true   (^ matches region start)

m.useAnchoringBounds(false);
println m.lookingAt();   // false  (^ only matches absolute input start, region is not the start)
```

---

#### `usePattern(pattern : Pattern) : Matcher`

Replaces the pattern used by this matcher. The current position in the input is preserved; only the pattern changes. Returns the same `Matcher` instance for chaining.

```ysharp
var p1 = new Needle.Pattern("\\d+");
var p2 = new Needle.Pattern("[a-z]+");
var m = p1.matcher("abc 123 def");

m.find();
println m.group();   // 123

m.reset();
m.usePattern(p2);
m.find();
println m.group();   // abc
```

---

#### `useTransparentBounds(flag : bool) : Matcher`

Enables or disables transparent bounds for this matcher. Returns the same `Matcher` instance for chaining. See `hasTransparentBounds()` for a full explanation.

```ysharp
var p = new Needle.Pattern("(?<=\\d)abc");
var m = p.matcher("1abc");
m.region(1, 4);

m.useTransparentBounds(true);
println m.lookingAt();   // true   (lookbehind sees "1" outside region)

m.useTransparentBounds(false);
println m.lookingAt();   // false
```

---

### Static Method

#### `Needle.Matcher.quoteReplacement(s : string) : string`

Returns a literal replacement string safe to use in `replaceAll` or `replaceFirst`. Any `$` and `\` characters in `s` are escaped so they are treated as plain characters instead of back-references or escape sequences.

```ysharp
var literal = Needle.Matcher.quoteReplacement("$100 off");
var p = new Needle.Pattern("PRICE");
var m = p.matcher("Get PRICE today!");
println m.replaceAll(literal);   // Get $100 off today!
```

---

## Full Example

```ysharp
// Extract all numbers from a string
var p = new Needle.Pattern("\\d+");
var m = p.matcher("I have 3 cats and 12 dogs");

var numbers = [];
while m.find() do
    numbers.push(m.group());
end
println numbers.toString();   // [3, 12]
```

```ysharp
// Named groups for parsing a date
var p = new Needle.Pattern("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
var m = p.matcher("Date: 2025-04-24");

if m.find() then do
    println m.group("year");    // 2025
    println m.group("month");   // 04
    println m.group("day");     // 24
end
```

```ysharp
// Case-insensitive split
var p = new Needle.Pattern("[,;\\s]+");
var parts = p.split("one, two;  three four");
println parts.toString();   // [one, two, three, four]
```
