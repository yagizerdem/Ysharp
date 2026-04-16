---
sidebar_position: 9
---

### General

A thread-safe, mutable sequence of characters. A string buffer is like a String,
but can be modified. At any point in time it contains some particular sequence of characters ,
but the length and content of the sequence can be changed through certain method calls.
String buffers are safe for use by multiple threads. 
The methods are synchronized where necessary so that all the operations on any particular 
instance behave as if they occur in some serial order that is consistent with the order
of the method calls made by each of the individual threads involved.

The principal operations on a StringBuffer are the append and insert methods, which are overloaded so as to accept data of any type.
Each effectively converts a given datum to a string and then appends or inserts the characters of that string to the string buffer.
The append method always adds these characters at the end of the buffer; the insert method adds the characters at a specified point.

For example, if z refers to a string buffer object whose current contents are "start", 
then the method call z.append("le") would cause the string buffer to contain "startle",
whereas z.insert(4, "le") would alter the string buffer to contain "starlet".

In general, if sb refers to an instance of a StringBuffer, then sb.append(x) 
has the same effect as sb.insert(sb.length(), x).

Whenever an operation occurs involving a source sequence (such as appending or inserting from a source sequence), 
this class synchronizes only on the string buffer performing the operation, not on the source. 
Note that while StringBuffer is designed to be safe to use concurrently from multiple threads, 
if the constructor or the append or insert operation is passed a source sequence that is shared across threads, 
the calling code must ensure that the operation has a consistent and unchanging view of the source 
sequence for the duration of the operation. This could be satisfied by the caller holding a lock during the operation's call,
by using an immutable source sequence, or by not sharing the source sequence across threads.

Every string buffer has a capacity. As long as the length of the character sequence contained in the 
string buffer does not exceed the capacity, it is not necessary to allocate a new internal 
buffer array. If the internal buffer overflows, it is automatically made larger.


### Reference


| Method        | Signature                                                      | Return Type      | Description                                                                 |
|---------------|----------------------------------------------------------------|------------------|-----------------------------------------------------------------------------|
| `append`      | `sb.append(value : any)`                                       | `StringBuffer`   | Appends the string representation of the given value                        |
| `append`      | `sb.append(arr : array, offset : int, len : int)`              | `StringBuffer`   | Appends a subarray of characters from the given array                       |
| `appendLine`  | `sb.appendLine(value : any)`                                   | `StringBuffer`   | Appends the value followed by a newline (`\n`)                              |
| `appendLine`  | `sb.appendLine(arr : array, offset : int, len : int)`          | `StringBuffer`   | Appends characters from array, each followed by a newline                   |
| `insert`      | `sb.insert(index : int, value : any)`                          | `StringBuffer`   | Inserts the string representation of value at the given index               |
| `insert`      | `sb.insert(index : int, arr : array)`                          | `StringBuffer`   | Inserts all characters from the given char array at the specified index     |
| `insert`      | `sb.insert(index : int, arr : array, offset : int, len : int)` | `StringBuffer`   | Inserts a subarray of characters at the specified index                     |
| `toString`    | `sb.toString()`                                                | `string`         | Returns the built string                                                    |
| `clear`       | `sb.clear()`                                                   | `StringBuffer`   | Clears the internal buffer (sets length to 0)                               |
| `length`      | `sb.length()`                                                  | `int`            | Returns the current length of the buffer                                    |


## Samples

### append

- Appends the string representation of the given value


````ysharp
// basic usage
var sb = new StringBuffer();

sb.append("Hello ");
sb.append("World");

println(sb.toString());
// Output: Hello World


// chaining (method chaining)
var sb2 = new StringBuffer();

sb2.append("User: ")
   .append("yagiz")
   .append(" | Score: ")
   .append(100);

println(sb2.toString());
// Output: User: yagiz | Score: 100.0


// append different types
var sb3 = new StringBuffer();

sb3.append("Value: ");
sb3.append(42);
sb3.append(", Status: ");
sb3.append(true);

println(sb3.toString());
// Output: Value: 42.0, Status: true


// append null safely
var sb4 = new StringBuffer();

sb4.append("Result: ");
sb4.append(null);

println(sb4.toString());
// Output: Result: null


// append char array
var chars = ['H','e','l','l','o'];

var sb5 = new StringBuffer();
sb5.append(chars, 0, 5);

println(sb5.toString());
// Output: Hello


// append partial char array
var data = ['A','B','C','D','E'];

var sb6 = new StringBuffer();
sb6.append(data, 1, 3);

println(sb6.toString());
// Output: BCD


// build string inside loop
var numbers = [1,2,3];

var sb7 = new StringBuffer();

for var i = 0; i < numbers.length(); i = i + 1 do
    sb7.append(numbers.get(i));

    if i != numbers.length() - 1 then do
        sb7.append("-");
    end
end

println(sb7.toString());
// Output: 1.0-2.0-3.0
````

### insert

- Inserts the string representation of the given value at the specified index


````ysharp
// basic insert
var sb = new StringBuffer();

sb.append("yagiz");
sb.insert(1, "X");

println(sb.toString());
// Output: yXagiz


// insert at beginning
var sb2 = new StringBuffer();

sb2.append("world");
sb2.insert(0, "Hello ");

println(sb2.toString());
// Output: Hello world


// insert at end
var sb3 = new StringBuffer();

sb3.append("test");
sb3.insert(sb3.length(), "!");

println(sb3.toString());
// Output: test!


// insert number
var sb4 = new StringBuffer();

sb4.append("Score: ");
sb4.insert(7, 100);

println(sb4.toString());
// Output: Score: 100.0


// insert boolean
var sb5 = new StringBuffer();

sb5.append("Status: ");
sb5.insert(8, true);

println(sb5.toString());
// Output: Status: true


// insert null
var sb6 = new StringBuffer();

sb6.append("Value: ");
sb6.insert(7, null);

println(sb6.toString());
// Output: Value: null


// insert char array (full)
var chars = ['a','b','c'];

var sb7 = new StringBuffer();
sb7.append("XYZ");

sb7.insert(1, chars);

println(sb7.toString());
// Output: XabcYZ


// insert partial char array
var data = ['A','B','C','D'];

var sb8 = new StringBuffer();
sb8.append("hello");

sb8.insert(1, data, 1, 2);

println(sb8.toString());
// Output: hBCello


// insert multiple times same index (order important)
var sb9 = new StringBuffer();

sb9.append("abc");

sb9.insert(1, "X");
sb9.insert(1, "Y");

println(sb9.toString());
// Output: aYXbc


// building string using insert positions
var sb10 = new StringBuffer();

sb10.append("Name:  Age: ");

sb10.insert(6, "Ali");
sb10.insert(sb10.length(), 20);

println(sb10.toString());
// Output: Name: Ali Age: 20.0


// insert inside loop (dynamic shifting)
var sb11 = new StringBuffer();

sb11.append("----");

for var i = 0; i < 4; i = i + 1 do
    sb11.insert(i, i);
end

println(sb11.toString());
// Output: 0.01.02.03.0----
````


### toString

- Returns the built string

````ysharp
// basic usage
var sb = new StringBuffer();

sb.append("Hello ");
sb.append("World");

var result = sb.toString();

println(result);
// Output: Hello World


// using toString in expression
var sb2 = new StringBuffer();

sb2.append("Score: ");
sb2.append(99);

println("Result -> " + sb2.toString());
// Output: Result -> Score: 99.0


// builder remains mutable after toString
var sb3 = new StringBuffer();

sb3.append("abc");

var str = sb3.toString();
println(str);
// Output: abc

sb3.append("123");
println(sb3.toString());
// Output: abc123
````


### clear

- Clears the internal buffer (sets length to 0)

````ysharp
// basic clear
var sb = new StringBuffer();

sb.append("hello");
sb.clear();

println(sb.toString());
// Output: ""


// reuse after clear
var sb2 = new StringBuffer();

sb2.append("data");
sb2.clear();
sb2.append("new");

println(sb2.toString());
// Output: new


// clear inside loop
var sb3 = new StringBuffer();

var values = ["a", "bb", "ccc"];

for var i = 0; i < values.length(); i = i + 1 do
    sb3.clear();
    sb3.append(values.get(i));

    println(sb3.toString());
end

// Output:
// a
// bb
// ccc


// chaining after clear
var sb4 = new StringBuffer();

sb4.append("test")
   .clear()
   .append("ok");

println(sb4.toString());
// Output: ok
````

### length

- Returns the current length of the buffer

````ysharp
// basic length
var sb = new StringBuffer();

println(sb.length());
// Output: 0


// after append
var sb2 = new StringBuffer();

sb2.append("abc");

println(sb2.length());
// Output: 3


// length with number
var sb3 = new StringBuffer();

sb3.append(123);

println(sb3.length());
// Output: 5   ("123.0")


// length after appendLine
var sb4 = new StringBuffer();

sb4.appendLine("a");

println(sb4.length());
// Output: 2   ("a\n")


// dynamic length tracking
var sb5 = new StringBuffer();

var input = ["x","yy","zzz"];

for var i = 0; i < input.length(); i = i + 1 do
    sb5.append(input.get(i));
    println("Current length: " + sb5.length());
end

// Output:
// 1
// 3
// 6


// length after clear
var sb6 = new StringBuffer();

sb6.append("hello");

println(sb6.length()); // 5

sb6.clear();

println(sb6.length());
// Output: 0
````