---
sidebar_position: 8
---

### General

A mutable sequence of characters. This class provides an API compatible with StringBuffer, 
but with no guarantee of synchronization. 
This class is designed for use as a drop-in replacement for 
StringBuffer in places where the string 
buffer was being used by a single thread (as is generally the case). 
Where possible, it is recommended that this class be used in preference 
to StringBuffer as it will be faster under most implementations.
The principal operations on a StringBuilder are the append and insert
methods, which are overloaded so as to accept data of any type.
Each effectively converts a given datum to a string and then appends or
inserts the characters of that string to the string builder. The append method
always adds these characters at the end of the builder;
the insert method adds the characters at a specified point.

For example, if z refers to a string builder object whose current contents are "start", 
then the method call z.append("le") would cause the string builder to contain "startle", 
whereas z.insert(4, "le") would alter the string builder to contain "starlet".

In general, if sb refers to an instance of a StringBuilder, 
then sb.append(x) has the same effect as sb.insert(sb.length(), x).

Every string builder has a capacity. As long as the length of the character sequence contained in the string builder does not exceed the capacity, 
it is not necessary to allocate a new internal buffer.
If the internal buffer overflows, it is automatically made larger.

Instances of StringBuilder are not safe for use by multiple threads. 
If such synchronization is required then it is recommended that StringBuffer be used.

### Reference

| Method        | Signature                                                      | Return Type           | Description                                                                 |
|---------------|----------------------------------------------------------------|-----------------------|-----------------------------------------------------------------------------|
| `append`      | `sb.append(value : any)`                                       | `StringBuilder`       | Appends the string representation of the given value to the builder         |
| `append`      | `sb.append(arr : array, offset : int, len : int)`              | `StringBuilder`       | Appends a subarray of characters from the given array                       |
| `appendLine`  | `sb.appendLine(value : any)`                                   | `StringBuilder`       | Appends the value followed by a newline character (`\n`)                    |
| `appendLine`  | `sb.appendLine(arr : array, offset : int, len : int)`          | `StringBuilder`       | Appends characters from array, each followed by a newline                   |
| `insert`      | `sb.insert(index : int, value : any)`                          | `StringBuilder`       | Inserts the string representation of value at the given index               |
| `insert`      | `sb.insert(index : int, arr : array)`                          | `StringBuilder`       | Inserts all characters from the given char array at the specified index     |
| `insert`      | `sb.insert(index : int, arr : array, offset : int, len : int)` | `StringBuilder`       | Inserts a subarray of characters at the specified index                     |
| `toString`    | `sb.toString()`                                                | `string`              | Returns the built string                                                    |
| `clear`       | `sb.clear()`                                                   | `StringBuilder`       | Clears the internal buffer (sets length to 0)                               |
| `length`      | `sb.length()`                                                  | `int`                 | Returns the current length of the builder                                   |


## Samples

### append

- Appends the string representation of the given value to the builder
- Appends a subarray of characters from the given array

````ysharp
// build a log message dynamically
var sb = new StringBuilder();

sb.append("User: ");
sb.append("yagiz");
sb.append(" | Score: ");
sb.append(150);

println(sb.toString());
// Output: User: yagiz | Score: 150.0


// chaining usage (fluent style)
var sb = new StringBuilder();
var message = sb
    .append("Server started at ")
    .append("10:30")
    .append(" on port ")
    .append(8080);

println(message.toString());
// Output: Server started at 10:30 on port 8080.0


// building CSV line
var csv = new StringBuilder();

var name = "Ali";
var age = 25;
var city = "Izmir";

csv.append(name)
   .append(",")
   .append(age)
   .append(",")
   .append(city);

println(csv.toString());
// Output: Ali,25.0,Izmir


// building string inside loop
var numbers = [1,2,3,4,5];

var result = new StringBuilder();

for var i = 0; i < numbers.length(); i = i + 1 do
    result.append(numbers.get(i));

    if i != numbers.length() - 1 then do
        result.append("-");
    end
end

println(result.toString());
// Output: 1.0-2.0-3.0-4.0-5.0


// handling null values safely
var sb2 = new StringBuilder();

var value = null;

sb2.append("Value is: ");
sb2.append(value);

println(sb2.toString());
// Output: Value is: null


// append char array
var chars = ['H','e','l','l','o'];

var sb3 = new StringBuilder();
sb3.append(chars, 0, 5);

println(sb3.toString());
// Output: Hello


// append partial char array
var data = ['A','B','C','D','E'];

var sb4 = new StringBuilder();
sb4.append(data, 1, 3);

println(sb4.toString());
// Output: BCD
````


### insert

- Inserts the string representation of value at the given index
- Inserts all characters from the given char array at the specified index

````ysharp
// basic insert
var sb = new StringBuilder();
sb.append("yagiz");

sb.insert(1, "X");

println(sb.toString());
// Output: yXagiz


// insert at beginning
var sb2 = new StringBuilder();
sb2.append("world");

sb2.insert(0, "Hello ");

println(sb2.toString());
// Output: Hello world


// insert at end
var sb3 = new StringBuilder();
sb3.append("test");

sb3.insert(sb3.length(), "!");

println(sb3.toString());
// Output: test!


// insert number
var sb4 = new StringBuilder();
sb4.append("Score: ");

sb4.insert(7, 100);

println(sb4.toString());
// Output: Score: 100.0


// insert boolean
var sb5 = new StringBuilder();
sb5.append("Status: ");

sb5.insert(8, true);

println(sb5.toString());
// Output: Status: true


// insert null
var sb6 = new StringBuilder();
sb6.append("Value: ");

sb6.insert(7, null);

println(sb6.toString());
// Output: Value: null


// insert char array (full)
var chars = ['a','b','c'];

var sb7 = new StringBuilder();
sb7.append("XYZ");

sb7.insert(1, chars);

println(sb7.toString());
// Output: XabcYZ


// insert partial char array
var data = ['A','B','C','D'];

var sb8 = new StringBuilder();
sb8.append("hello");

sb8.insert(1, data, 1, 2);

println(sb8.toString());
// Output: hBCello


// insert inside loop (dynamic building)
var sb9 = new StringBuilder();
sb9.append("----");

for var i = 0; i < 4; i = i + 1 do
    sb9.insert(i, i);
end

println(sb9.toString());
// Output: 0.01.02.03.0----


// building formatted string using insert
var name = "Ali";
var age = 20;

var sb10 = new StringBuilder();
sb10.append("Name:  Age: ");

sb10.insert(6, name);
sb10.insert(sb10.length(), age);

println(sb10.toString());
// Output: Name: Ali Age: 20.0


// insert multiple times same index (order important)
var sb11 = new StringBuilder();
sb11.append("abc");

sb11.insert(1, "X");
sb11.insert(1, "Y");

println(sb11.toString());
// Output: aYXbc
````

### toString

- 	Returns the built string

````ysharp
// basic usage
var sb = new StringBuilder();

sb.append("Hello ");
sb.append("World");

var result = sb.toString();

println(result);
// Output: Hello World


// using toString in expressions
var sb2 = new StringBuilder();

sb2.append("Value: ");
sb2.append(42);

println("Result -> " + sb2.toString());
// Output: Result -> Value: 42.0


// converting builder to string before reuse
var sb3 = new StringBuilder();

sb3.append("abc");

var str = sb3.toString();

println(str);
// Output: abc

// builder is still mutable
sb3.append("123");

println(sb3.toString());
// Output: abc123
````


### clear

- Clears the internal buffer (sets length to 0)


````
// basic clear
var sb = new StringBuilder();

sb.append("hello");

sb.clear();

println(sb.toString());
// Output: ""


// reuse after clear
var sb2 = new StringBuilder();

sb2.append("data");
sb2.clear();
sb2.append("new");

println(sb2.toString());
// Output: new


// clear inside loop
var sb3 = new StringBuilder();

var words = ["a", "bb", "ccc"];

for var i = 0; i < words.length(); i = i + 1 do
    sb3.clear();
    sb3.append(words.get(i));

    println(sb3.toString());
end

// Output:
// a
// bb
// ccc


// chaining after clear
var sb4 = new StringBuilder();

sb4.append("test")
   .clear()
   .append("ok");

println(sb4.toString());
// Output: ok
````

### length

- Returns the current length of the builder

````
// basic length
var sb = new StringBuilder();

println(sb.length());
// Output: 0


// after append
var sb2 = new StringBuilder();

sb2.append("abc");

println(sb2.length());
// Output: 3


// length with numbers
var sb3 = new StringBuilder();

sb3.append(123);

println(sb3.length());
// Output: 5   (because "123.0")


// length after appendLine
var sb4 = new StringBuilder();

sb4.appendLine("a");

println(sb4.length());
// Output: 2   ("a\n")


// dynamic length tracking
var sb5 = new StringBuilder();

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
var sb6 = new StringBuilder();

sb6.append("hello");

println(sb6.length()); // 5

sb6.clear();

println(sb6.length());
// Output: 0
````
