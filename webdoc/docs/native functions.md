---
sidebar_position: 17
---

## Overview
Native functions are built-in functions implemented directly in the Ysharp runtime.

They are used for:
- Core language features
- Standard library implementations
- Class methods

> Note <br/>
> All class methods are internally converted into native functions.
> This may be unexpected to users, but i find implementing class methods as native functions easier


## Reference

| Function       | Signature                                | Return Type  | Arity    | Description                                                                                              |
|----------------|------------------------------------------|--------------|----------|----------------------------------------------------------------------------------------------------------|
| `__now`        | `__now()`                                | `int`        | 0        | Returns the current time as epoch milliseconds                                                           |
| `__sleep`      | `__sleep(ms : int)`                      | `null`       | 1        | Pauses execution for the given number of milliseconds                                                    |
| `__abs`        | `__abs(n : number)`                      | `number`     | 1        | Returns the absolute value of a number                                                                   |
| `__round`      | `__round(n : number)`                    | `number`     | 1        | Rounds a number to the nearest integer                                                                   |
| `__max`        | `__max(a, b, ...) \| max(array)`         | `number`     | variadic | Returns the maximum value from the given arguments or an array                                           |
| `__min`        | `__min(a, b, ...) \| min(array)`         | `number`     | variadic | Returns the minimum value from the given arguments or an array                                           |
| `__sum`        | `__sum(a, b, ...) \| sum(array)`         | `number`     | variadic | Returns the sum of the given arguments or all elements in an array                                       |
| `__bin`        | `__bin(n : int)`                         | `string`     | 1        | Returns the binary string representation of an integer, prefixed with `0b`                               |
| `__bool`       | `__bool(value)`                          | `bool`       | 1        | Returns the boolean (truthy/falsy) interpretation of a value                                             |
| `__int`        | `__int(value)`                           | `int`        | 1        | Converts a value to an integer; accepts number, string, and bool                                         |
| `__double`     | `__double(value)`                        | `double`     | 1        | Converts a value to a floating-point number; accepts number, string, and bool                            |
| `__str`        | `__str(value)`                           | `string`     | 1        | Converts a value to its string representation                                                            |
| `__chr`        | `__chr(codepoint : int)`                 | `string`     | 1        | Returns the character corresponding to the given Unicode code point (0..0x10FFFF)                        |
| `__len`        | `__len(collection)`                      | `int`        | 1        | Returns the number of elements in a collection (e.g. array)                                              |
| `__all`        | `__all(iterable)`                        | `bool`       | 1        | Returns `true` if all elements in the collection are truthy                                              |
| `__any`        | `__any(iterable)`                        | `bool`       | 1        | Returns `true` if at least one element in the collection is truthy                                       |
| `__callable`   | `__callable(value)`                      | `bool`       | 1        | Returns `true` if the value is callable (function or lambda)                                             |
| `__isInstance` | `__isInstance(value)`                    | `bool`       | 1        | Returns `true` if the value is an instance of a class                                                    |
| `__isClass`    | `__isClass(value)`                       | `bool`       | 1        | Returns `true` if the value is a class definition                                                        |
| `__hash`       | `__hash(value)`                          | `int`        | 1        | Returns the hash code of a value; null &rarr; 0, bool &rarr; 0/1, consistent hash for number/string/char |
| `__id`         | `__id(value)`                            | `int`        | 1        | Returns the identity hash code of an object (memory identity)                                            |
| `__input`      | `__input() \| input(prompt : string)`    | `string`     | variadic | Reads a line from standard input; optionally prints a prompt before reading                              |
| `__eval`       | `__eval(code : string)`                  | `any`        | 1        | Evaluates a single expression string and returns its result                                              |



### now

- Returns the current time as epoch milliseconds


````ysharp
// get current time

var currentTime = __now();

println("Current time (ms): " + currentTime);

// measure execution time
var start = __now();



// simulate some work
var sum = 0;
for var i = 0; i < 1000000; i = i + 1 do
    sum = sum + i;
end



var end_ = __now();

println("Execution time: " + (end_ - start) + " ms");

// simple timeout check
var timeout = 1000; // 1 second
var begin = __now();

while true do
    if __now() - begin > timeout then do
        println("Timeout reached!");
        break;
    end
end

````


### __sleep

- Pauses execution for the given number of milliseconds

````ysharp
// simple delay
println("Start");
__sleep(1000); // wait 1 second
println("After 1 second");

// countdown with delay
var count = 5;

while count > 0 do
    println("Countdown: " + count);
    __sleep(1000); // wait 1 second
    count = count - 1;
end

println("Done!");

// simulate loading
println("Loading...");
var i = 0;

while i < 3 do
    println("Processing step " + (i + 1));
    __sleep(500); // half second delay
    i = i + 1;
end

println("Finished");
````

### __abs

- Returns the absolute value of a number


````ysharp
// basic usage
var a = -10;
var b = 5;

println(__abs(a)); // 10
println(__abs(b)); // 5

// distance between two numbers
var x = -7;
var y = 3;

var distance = __abs(x - y);
println("Distance: " + distance); // 10

// normalize values (remove negative sign)
var values = [-3, -1, 0, 2, 5];

for var i = 0; i < values.length(); i = i + 1 do
    var v = values.get(i);
    println("abs(" + v + ") = " + __abs(v));
end

// check if number is effectively zero (tolerance)
var num = -0.0001;

if __abs(num) < 0.001 then do
    println("Approximately zero");
end else do
    println("Not zero");
end
````

### __round

- Rounds a number to the nearest integer


````ysharp
// basic usage
var a = 3.2;
var b = 3.7;
var c = -2.4;
var d = -2.6;

println(__round(a)); // 3
println(__round(b)); // 4
println(__round(c)); // -2
println(__round(d)); // -3

// rounding user input values
var values = [1.2, 2.5, 3.8, 4.49];

var i = 0;
while i < values.length() do
    var v = values.get(i);
    println("Original: " + v + " → Rounded: " + __round(v));
    i = i + 1;
end

// calculating average and rounding result
var nums = [10, 15, 20];

var sum = 0;
var i2 = 0;

while i2 < nums.length() do
    sum = sum + nums.get(i2);
    i2 = i2 + 1;
end

var avg = sum / nums.length();
println("Average (rounded): " + __round(avg));

// rounding for display (e.g. scores)
var score = 89.6;
println("Final score: " + __round(score));
````

### __max

- Returns the maximum value from the given arguments or an array

````ysharp
// basic usage with arguments
println(__max(3, 7));          // 7
println(__max(10, 2, 8, 6));   // 10

// negative numbers
println(__max(-5, -2, -9));    // -2

// mixed values
var a = 12;
var b = 25;
var c = 7;

println(__max(a, b, c));       // 25

// using array
var numbers = [4, 9, 1, 20, 6];

println(__max(numbers));       // 20

// finding max manually (comparison)
var values = [3, 15, 8, 22, 5];

var currentMax = values.get(0);
var i = 1;

while i < values.length() do
    var v = values.get(i);
    currentMax = __max(currentMax, v);
    i = i + 1;
end

println("Max value: " + currentMax);

// real-world: highest score
var scores = [78, 92, 85, 88];

var best = __max(scores);
println("Highest score: " + best);
````

### __min

- Returns the minimum value from the given arguments or an array

````ysharp
// basic usage with arguments
println(__min(3, 7));          // 3
println(__min(10, 2, 8, 6));   // 2

// negative numbers
println(__min(-5, -2, -9));    // -9

// mixed values
var a = 12;
var b = 25;
var c = 7;

println(__min(a, b, c));       // 7

// using array
var numbers = [4, 9, 1, 20, 6];

println(__min(numbers));       // 1

// finding min manually (comparison)
var values = [3, 15, 8, 22, 5];

var currentMin = values.get(0);
var i = 1;

while i < values.length() do
    var v = values.get(i);
    currentMin = __min(currentMin, v);
    i = i + 1;
end

println("Min value: " + currentMin);

// real-world: lowest score
var scores = [78, 92, 85, 88];

var worst = __min(scores);
println("Lowest score: " + worst);
````


### __sum

- Returns the sum of the given arguments or all elements in an array

````ysharp
// basic usage with arguments
println(__sum(1, 2, 3));        // 6
println(__sum(10, 20, 30, 40)); // 100

// negative and mixed numbers
println(__sum(-5, 3, 2));       // 0

// using array
var numbers = [4, 9, 1, 20, 6];

println(__sum(numbers));        // 40

// manual accumulation comparison
var values = [3, 15, 8, 22, 5];

var total = 0;
var i = 0;

while i < values.length() do
    total = total + values.get(i);
    i = i + 1;
end

println("Manual sum: " + total);
println("Built-in sum: " + __sum(values));

// real-world: total score
var scores = [78, 92, 85, 88];

var totalScore = __sum(scores);
println("Total score: " + totalScore);

// combining values dynamically
var a = 5;
var b = 10;
var c = 15;

println(__sum(a, b, c)); // 30
````

### __bin

- Returns the binary string representation of an integer, prefixed with `0b`

````ysharp
// basic usage
var n = 10;
println(__bin(n)); // 0b1010

// different numbers
println(__bin(1));   // 0b1
println(__bin(5));   // 0b101
println(__bin(16));  // 0b10000

// using in loop
var i = 0;

while i < 5 do
    println(i + " -> " + __bin(i));
    i = i + 1;
end

// checking bit patterns
var value = 13; // 1101
var binStr = __bin(value);

println("Value: " + value);
println("Binary: " + binStr);

// debugging bitwise operations
var a = 6;  // 110
var b = 3;  // 011

var result = a & b;

println("a: " + __bin(a));
println("b: " + __bin(b));
println("a & b: " + __bin(result));

// convert list of integers to binary
var nums = [2, 4, 7, 8];

var idx = 0;
while idx < nums.length() do
    var v = nums.get(idx);
    println(v + " in binary = " + __bin(v));
    idx = idx + 1;
end
````

### __bool

- Returns the boolean (truthy/falsy) interpretation of a value

````ysharp
// basic usage
println(__bool(true));   // true
println(__bool(false));  // false

// numbers
println(__bool(1));      // true
println(__bool(0));      // false
println(__bool(-5));     // true

// strings
println(__bool("hello")); // true
println(__bool(""));      // false (empty string)

// null
var x = null;
println(__bool(x));       // false

// objects / arrays
println(__bool([1,2,3])); // true
println(__bool([]));      // true (empty array is still truthy)

// conditional usage
var value = 0;

if __bool(value) then do
    println("Value is truthy");
end else do
    println("Value is falsy");
end

// filtering truthy values
var list = [0, 1, null, 5, "", 10];

var i = 0;
while i < list.length() do
    var v = list.get(i);

    if __bool(v) then do
        println("Truthy: " + v);
    end

    i = i + 1;
end

// double negation pattern
var data = "text";
var isValid = __bool(data);

println("Is valid: " + Type.Converter.toString(isValid));
````

### __int

- Converts a value to an integer; accepts number, string, and bool

````ysharp
// basic usage
println(__int(10));      // 10
println(__int(3.7));     // 3

// string conversion
println(__int("42"));    // 42
println(__int("7"));     // 7

// boolean conversion
println(__int(true));    // 1
println(__int(false));   // 0

// mixed values
var a = "100";
var b = 25.9;
var c = true;

println(__int(a) + __int(b) + __int(c)); // 100 + 25 + 1 = 126

// using in loop (string to int)
var nums = ["1", "2", "3", "4"];

var i = 0;
var sum = 0;

while i < nums.length() do
    sum = sum + __int(nums.get(i));
    i = i + 1;
end

println("Sum: " + sum);

// safe numeric operations
var x = "50";
var y = "20";

var result = __int(x) - __int(y);
println("Result: " + result); // 30

// parsing user-like input
var input = "123";

var parsed = __int(input);
println("Parsed value: " + parsed);
````

### __all

- Returns true if all elements in the collection are truthy

````ysharp
// basic usage
var arr1 = [1, 2, 3];
println(__all(arr1)); // true

var arr2 = [1, 0, 3];
println(__all(arr2)); // false

// with different types
var arr3 = ["a", "b", "c"];
println(__all(arr3)); // true

var arr4 = ["a", "", "c"];
println(__all(arr4)); // false

// null values
var arr5 = [1, null, 3];
println(__all(arr5)); // false

// boolean values
var flags = [true, true, true];
println(__all(flags)); // true

var flags2 = [true, false, true];
println(__all(flags2)); // false

// validation example
var usernames = ["ali", "mehmet", "yagiz"];

if __all(usernames) then do
    println("All usernames are valid (non-empty)");
end else do
    println("Some usernames are invalid");
end

// manual comparison
var list = [5, 10, 15];

var i = 0;
var allTrue = true;

while i < list.length() do
    if !__bool(list.get(i)) then do
        allTrue = false;
        break;
    end
    i = i + 1;
end

println("Manual all: " + __str(allTrue));
println("Built-in all: " + __str(__all(list)));
````

### __chr

- Returns the character corresponding to the given Unicode code point (0..0x10FFFF)

````ysharp
// basic usage
println(__chr(65));   // A
println(__chr(97));   // a
println(__chr(48));   // 0

// common symbols
println(__chr(33));   // !
println(__chr(64));   // @
println(__chr(35));   // #

// build string from codepoints
var codes = [72, 101, 108, 108, 111]; // "Hello"

var result = "";
var i = 0;

while i < codes.length() do
    result = result + __chr(codes.get(i));
    i = i + 1;
end

println(result); // Hello

// alphabet generation
var i2 = 65;

while i2 <= 90 do
    println(__chr(i2)); // A-Z
    i2 = i2 + 1;
end

// unicode example
println(__chr(9731)); // ☃ (snowman)
println(__chr(10084)); // ❤

// simple Caesar cipher (shift characters)
var text = "abc";
var shift = 1;

var i3 = 0;
var encrypted = "";

while i3 < text.length() do
    var code = text.charCodeAt(i3);
    encrypted = encrypted + __chr(code + shift);
    i3 = i3 + 1;
end

println("Encrypted: " + encrypted); // bcd
````

### __len

- Returns the number of elements in a collection (e.g. array)

````ysharp
// basic usage
var arr = [1, 2, 3, 4];

println(__len(arr)); // 4

// empty collection
var empty = [];

println(__len(empty)); // 0

// dynamic usage
var items = ["a", "b", "c"];

if __len(items) > 0 then do
    println("Collection is not empty");
end else do
    println("Collection is empty");
end

// loop using __len
var nums = [10, 20, 30, 40];

var i = 0;
while i < __len(nums) do
    println(nums.get(i));
    i = i + 1;
end

// compare sizes
var a = [1,2,3];
var b = [5,6];

if __len(a) > __len(b) then do
    println("a is larger");
end else do
    println("b is larger or equal");
end

// nested collections
var matrix = [
    [1,2],
    [3,4],
    [5,6]
];

println("Row count: " + __len(matrix)); // 3
````

### __any

- Returns true if at least one element in the collection is truthy

````ysharp
// basic usage
var values1 = [0, 0, 1];
println(__any(values1)); // true

var values2 = [0, 0, 0];
println(__any(values2)); // false

// with strings
var words = ["", "", "hello"];
println(__any(words)); // true

var words2 = ["", "", ""];
println(__any(words2)); // false

// with null values
var list = [null, null, 5];
println(__any(list)); // true

var list2 = [null, null, null];
println(__any(list2)); // false

// boolean list
var flags = [false, false, true];
println(__any(flags)); // true

var flags2 = [false, false, false];
println(__any(flags2)); // false

// validation example
var inputs = ["", "user2", ""];

if __any(inputs) then do
    println("At least one valid input exists");
end else do
    println("No valid inputs");
end

// manual equivalent
var arr = [0, 0, 10];

var i = 0;
var result = false;

while i < __len(arr) do
    if __bool(arr.get(i)) then do
        result = true;
        break;
    end
    i = i + 1;
end

println("Manual: " + __str(result));
println("Built-in: " + __str(__any(arr)));
````

### __callable

- Returns true if the value is callable (function or lambda)


````ysharp
// basic usage
var f = (x) => x + 1;

println(__callable(f));      // true
println(__callable(10));     // false
println(__callable("text")); // false

// function example
function add(a, b) do
    return a + b;
end

println(__callable(add)); // true

// lambda vs non-callable
var x = 5;
var y = (n) => n * 2;

println(__callable(x)); // false
println(__callable(y)); // true

// safe call pattern
var maybeFn = (n) => n * n;

if __callable(maybeFn) then do
    println(maybeFn(4)); // 16
end else do
    println("Not callable");
end

// dynamic execution
var items = [
    (x) => x + 1,
    42,
    (x) => x * 2
];

var i = 0;

while i < __len(items) do
    var v = items.get(i);

    if __callable(v) then do
        println("Result: " + v(5));
    end
    else do
        println("Not callable: " + v);
    end

    i = i + 1;
end

// validation example
var action = null;

if __callable(action) then do
    action();
end else do
    println("No valid function to call");
end
````


### __isInstance

- Returns true if the value is an instance of a class

````ysharp
class Person { }

var p = new Person();

println(__isInstance(p));     // true
println(__isInstance(Person)); // false (class itself is not instance)
println(__isInstance(10));     // false
println(__isInstance("text")); // false

// multiple values
var values = [p, 42, "hello", new Person()];

var i = 0;

while i < __len(values) do
    var v = values.get(i);

    if __isInstance(v) then do
        println("Instance: " + __str(v));
    end
    else do
        println("Not instance: " + __str(v));
    end

    i = i + 1;
end

// safe property access
var obj = new Person();

if __isInstance(obj) then do
    println("Object type: " + obj.getType());
end

// validation example
var maybeObj = null;

if __isInstance(maybeObj) then do
    println("Valid instance");
end else do
    println("Not a class instance");
end

// working with collections
class Box { }

var items = [new Box(), new Box(), 5];

var i2 = 0;

while i2 < __len(items) do
    var v = items.get(i2);

    if __isInstance(v) then do
        println("Box instance detected");
    end

    i2 = i2 + 1;
end
````

### __isClass

- Returns true if the value is a class definition

````ysharp
// basic usage
class Person {}

var p = new Person();

println(__isClass(Person)); // true
println(__isClass(p));      // false
println(__isClass(10));     // false
println(__isClass("text")); // false

// working with mixed values
var values = [Person, p, 42, "hello"];

var i = 0;

while i < __len(values) do
    var v = values.get(i);

    if __isClass(v) then do
        println("Class detected: " + __str(v));
    end
    else do
        println("Not a class: " + __str(v));
    end

    i = i + 1;
end

// safe instantiation
var maybeClass = Person;

if __isClass(maybeClass) then do
    var obj = new maybeClass();
    println("Instance created: " + __str(obj));
end else do
    println("Not a class");
end

// validation example
var input = null;

if __isClass(input) then do
    println("Valid class");
end else do
    println("Invalid class");
end

// class registry example
class A {}
class B {}

var registry = [A, B, 123];

var j = 0;

while j < __len(registry) do
    var v = registry.get(j);

    if __isClass(v) then do
        println("Registered class: " + __str(v));
    end

    j = j + 1;
end
````

### __hash

- Returns the hash code of a value
- `null &rarr; 0`, `bool &rarr; 0/1`, consistent hash for `number`, `string`, and `char`

````ysharp
// basic usage
println(__hash(null));   // 0
println(__hash(true));   // 1
println(__hash(false));  // 0

println(__hash(123));    // hash of number
println(__hash("abc"));  // hash of string
println(__hash('A'));    // hash of char

// same value → same hash
var a = "hello";
var b = "hello";

println(__hash(a)); // same
println(__hash(b)); // same

// different values → likely different hashes
println(__hash("a"));
println(__hash("b"));

// using hash for grouping
var values = ["apple", "banana", "apple", "orange"];

var i = 0;

while i < __len(values) do
    var v = values.get(i);
    println(v + " → hash: " + __hash(v));
    i = i + 1;
end

// simple duplicate detection using hash
var seen = [];

var items = ["x", "y", "x", "z"];

var j = 0;

while j < __len(items) do
    var h = __hash(items.get(j));

    var k = 0;
    var exists = false;

    while k < __len(seen) do
        if seen.get(k) == h then do
            exists = true;
            break;
        end
        k = k + 1;
    end

    if exists then do
        println("Duplicate detected: " + items.get(j));
    end else do
        seen.add(h);
    end

    j = j + 1;
end

// debug hashing behavior
var val = "test";
println("Value: " + val);
println("Hash: " + __hash(val));
````

### __id

- Returns the identity hash code of an object (memory identity)

````ysharp
// basic usage
var a = [1, 2, 3];
var b = [1, 2, 3];

println(__id(a)); // e.g. 12345678
println(__id(b)); // different value

// same reference → same id
var c = a;

println(__id(a)); // same as c
println(__id(c));

// primitives may not behave like objects
println(__id(10));
println(__id("text"));

// comparing identity vs equality
var x = [1,2];
var y = [1,2];

println(x == y);        // may be false (different objects)
println(__id(x));       // different ids
println(__id(y));

// aliasing example
var obj1 = ["data"];
var obj2 = obj1;

println("obj1 id: " + __id(obj1));
println("obj2 id: " + __id(obj2)); // same id

// detecting shared references
var items = [];

var shared = [42];

items.add(shared);
items.add(shared);
items.add([42]);

var i = 0;

while i < __len(items) do
    println("Item " + i + " id: " + __id(items.get(i)));
    i = i + 1;
end

// debug object identity
class Box { }

var b1 = new Box();
var b2 = new Box();

println("b1 id: " + __id(b1));
println("b2 id: " + __id(b2));
````

### __input

- Reads a line from standard input; optionally prints a prompt before reading


````ysharp
// basic usage (no prompt)
var name = __input();

println("Hello, " + name);

// with prompt
var username = __input("Enter your username: ");
println("Username: " + username);

// numeric input (convert manually)
var ageStr = __input("Enter your age: ");
var age = __int(ageStr);

println("You are " + age + " years old");

// simple loop until valid input
var inputValue;

while true do
    inputValue = __input("Enter a number greater than 10: ");

    var num = __int(inputValue);

    if num > 10 then do
        println("Valid input: " + num);
        break;
    end else do
        println("Invalid, try again.");
    end
end

// collecting multiple inputs
var users = [];

var i = 0;

while i < 3 do
    var u = __input("Enter username " + (i + 1) + ": ");
    users.add(u);
    i = i + 1;
end

println("Users: " + users);

// simple menu
println("1. Say Hello");
println("2. Exit");

var choice = __input("Choose option: ");

if choice == "1" then do
    var n = __input("Enter name: ");
    println("Hello " + n);
end else do
    println("Exiting...");
end
````


### __eval

- Evaluates a single expression string in an isolated context and returns its result
- Does NOT have access to variables from the current scope

````ysharp
println __eval("10 + 4 * 3 / (10 >> 2) + 24 * 10"); // 256
````