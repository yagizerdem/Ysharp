---
sidebar_position: 7
---

### General

Strings are collection of unicode characters to store text. 
A String variable contains a collection of characters surrounded by double quotes (""):

In Ysharp strings are immutable object type. meaning 
their values cannot be changed once created. 
If you try to modify a string like concat or replace, 
a new string object is created instead of altering the original one. 

Standard library provide a lot of string utility functions. <br/> 
Full api list below :

### Reference

| Method                   | Signature                                              | Return Type | Description                                                                                 |
|--------------------------|--------------------------------------------------------|-------------|---------------------------------------------------------------------------------------------|
| `length` / `size`        | `str.length()`                                         | `int`       | Returns the length of the string                                                            |
| `toUpper`                | `str.toUpper()`                                        | `string`    | Returns a new string with all characters in uppercase                                       |
| `toLower`                | `str.toLower()`                                        | `string`    | Returns a new string with all characters in lowercase                                       |
| `charAt`                 | `str.charAt(index : int)`                              | `char`      | Returns the character at the specified index                                                |
| `substring`              | `str.substring(start : int, end: int)`                 | `string`    | Returns a substring from start index (inclusive) to end index (exclusive)                   |
| `equals`                 | `str.equals(other : string)`                           | `bool`      | Returns true if the given string is equal to this string                                    |
| `indexOf`                | `str.indexOf(other : string)`                          | `int`       | Returns the index of the first occurrence of the given substring, or -1 if not found        |
| `contains`               | `str.contains(other : string)`                         | `bool`      | Returns true if the given substring exists in the string                                    |
| `trim`                   | `str.trim()`                                           | `string`    | Returns a new string with leading and trailing whitespace removed                           |
| `trimLeft` / `trimStart` | `str.trimLeft()`                                       | `string`    | Returns a new string with leading whitespace removed                                        |
| `trimRight` / `trimEnd`  | `str.trimRight()`                                      | `string`    | Returns a new string with trailing whitespace removed                                       |
| `repeat`                 | `str.repeat(n : int)`                                  | `string`    | Returns a new string repeated n times                                                       |
| `startsWith`             | `str.startsWith(other : string)`                       | `bool`      | Returns true if the string starts with the given substring                                  |
| `endsWith`               | `str.endsWith(other : string)`                         | `bool`      | Returns true if the string ends with the given substring                                    |
| `replace`                | `str.replace(old : string, new : string)`              | `string`    | Returns a new string with all occurrences of old substring replaced by new substring        |
| `isEmpty`                | `str.isEmpty()`                                        | `bool`      | Returns true if the string is empty                                                         |
| `reverse`                | `str.reverse()`                                        | `string`    | Returns a new string with characters in reverse order                                       |
| `padLeft`                | `str.padLeft(len : int, padStr : string)`              | `string`    | Returns a new string padded on the left to the specified length using the given pad string  |
| `padRight`               | `str.padRight(len : int, padStr : string)`             | `string`    | Returns a new string padded on the right to the specified length using the given pad string |
| `compareTo`              | `str.compareTo(other : string)`                        | `int`       | Compares two strings lexicographically and returns a negative, zero, or positive integer    |
| `capitalize`             | `str.capitalize()`                                     | `string`    | Returns a new string with the first character converted to uppercase                        |
| `split`                  | `str.split(string : string)`                           | `array`     | Splits the string using the given delimiter and returns an array of substrings              |
| `replaceAll`             | `str.replaceAll(regex : string, replacement : string)` | `string`    | Returns a new string with all matches of the regex replaced by the replacement string       |
| `join`                   | `str.join(array : Array)`                              | `string`    | Joins array elements into a string using this string as the separator                       |
| `format`                 | `str.format(...)`                                      | `string`    | Formats the string using the given arguments and returns the formatted result               |
| `toCharArray`            | `str.toCharArray()`                                    | `array`     | Returns an array containing the characters of the string                                    |
| `matches`                | `str.matches(regex : string)`                          | `bool`      | Returns true if the string matches the given regular expression                             |
| `replaceFirst`           | `str.replaceFirst(regex : string, repl : string)`      | `string`    | Returns a new string with the first match of the regex replaced by the given replacement    |
| `lastIndexOf`            | `str.lastIndexOf(str : string)`                        | `int`       | Returns the index of the last occurrence of the given substring, or -1 if not found         |
| `count`                  | `str.count(substr : string)`                           | `int`       | Returns the number of occurrences of the given substring in the string                      |
| `charCodeAt`             | `str.charCodeAt(index : int)`                          | `int`       | Returns the character code at the specified index                                           |
| `slice`                  | `str.slice(start : int, end? : int)`                   | `string`    | Returns a substring from start to end (end optional, supports negative indices)             |


## Samples

### length 

- Returns the length of the string

````ysharp
var password = "abc123";

// check if password meets minimum length requirement
var minLength = 8;

if password.length() < minLength then do
    println("Password is too short. Minimum length is " + minLength);
end
else do
    println("Password length is valid");
end

// simulate user input list
var usernames = ["ali", "mehmet123", "x", "yagizerdem"];

// filter valid usernames based on length
for var i = 0; i < usernames.length(); i = i + 1 do
    var u = usernames.get(i);

    if u.length() >= 3 && u.length() <= 12 then do
        println("Valid username: " + u);
    end else do
        println("Invalid username: " + u);
    end
end
````

### toUpper

- Returns a new string with all characters in uppercase  

````ysharp
var message = "Hello Ysharp";

// convert to uppercase
var upperMessage = message.toUpper();

println("Original: " + message);
println("Uppercase: " + upperMessage);

// practical usage: case-insensitive comparison
var input = "admin";

if input.toUpper() == "ADMIN" then do
    println("Access granted");
end else do
    println("Access denied");
end
````


### toLower

- Returns a new string with all characters in lowercase

````ysharp
var email = "User@Example.COM";

// normalize email (common real-world usage)
var normalizedEmail = email.toLower();

println("Original: " + email);
println("Normalized: " + normalizedEmail);

// case-insensitive comparison
var input = "ADMIN";

if input.toLower() == "admin" then do
    println("Access granted");
end else do
    println("Access denied");
end
````

### charAt

- Returns the character at the specified index

````ysharp
var text = "Ysharp";

// get characters by index
var first = text.charAt(0);
var second = text.charAt(1);

println("First char: " + first);
println("Second char: " + second);

// iterate over string using charAt
for var i = 0; i < text.length(); i = i + 1 do
    var ch = text.charAt(i);
    println("Char at " + i + ": " + ch);
end

// simple check: count vowels
var vowels = "aeiou";
var count = 0;

for var i = 0; i < text.length(); i = i + 1 do
    var ch = Type.Converter.toString(text.charAt(i)).toLower();
    if vowels.contains(ch) then do
        count = count + 1;
    end
end

println("Vowel count: " + count);
````


### substring 

- Returns a substring from start index (inclusive) to end index (exclusive)

````ysharp
var text = "Order123Price45Tax5";

// this will hold extracted numbers as strings
var numbers = [];

var start = -1;

// scan character by character
for var i = 0; i < text.length(); i = i + 1 do
    var ch = text.charAt(i);

    // check if digit
    if ch >= '0' && ch <= '9' then do
        // start of number
        if start == -1 then do
            start = i;
        end
    end else do
        // end of number
        if start != -1 then do
            var numStr = text.substring(start, i);
            numbers.add(numStr);
            start = -1;
        end
    end
end

// handle last number (if string ends with digit)
if start != -1 then do
    var numStr = text.substring(start, text.length());
    numbers.add(numStr);
end

// print extracted numbers
println("Extracted numbers:");
for var i = 0; i < numbers.length(); i = i + 1 do
    println(numbers.get(i));
end

// sum them (assuming implicit conversion to number exists)
var sum = 0;
for var i = 0; i < numbers.length(); i = i + 1 do
    sum = sum + Type.Converter.toInt(numbers.get(i));
end

println("Sum: " + sum);
````

#### Explanation

This example extracts all numbers from a mixed string.

It scans the string character by character using `charAt`.  
When a digit is found, it marks the start of a number.  
When a non digit is encountered, it extracts the number using `substring` and stores it.

At the end, all extracted values are converted to integers and summed.

> This demonstrates basic string parsing and the foundation of how lexers/tokenizers work.

---
<br/>

- some one liner examples

````ysharp
var text = "HelloWorld";

// first 5 characters
println(text.substring(0, 5));      // Hello

// last 5 characters
println(text.substring(5, 10));     // World

// middle part
println(text.substring(2, 7));      // lloWo

// remove first character
println(text.substring(1, text.length()));  // elloWorld

// get first character as string
println(text.substring(0, 1));      // H
````

### equals

- Returns true if the given string is equal to this string

```ysharp
var a = "hello";
var b = "hello";
var c = "Hello";

// exact match
println(a.equals(b));   // true

// case-sensitive comparison
println(a.equals(c));   // false

// comparison after normalization
println(a.toLower().equals(c.toLower()));  // true

// use in condition
if a.equals("hello") then do
    println("Matched");
end
````


### indexOf

Returns the index of the first occurrence of a substring.  
If the substring is not found, it returns `-1`.

```ysharp
var text = "Hello Ysharp World";

// find substring
println(text.indexOf("Ysharp"));   // 6

// not found case
println(text.indexOf("Java"));     // -1

// use in condition
var email = "user@example.com";

var atIndex = email.indexOf("@");

if atIndex != -1 then do
    println("Valid email");
end else do
    println("Invalid email");
end

// extract using indexOf + substring
var domain = email.substring(atIndex + 1, email.length());
println(domain);   // example.com
```

### contains

Returns `true` if the given substring exists in the string.

```ysharp
var text = "Learn Ysharp Programming";

// basic usage
println(text.contains("Ysharp"));   // true
println(text.contains("Java"));     // false

// use with normalization (case-insensitive search)
var input = "ysharp";

println(text.toLower().contains(input.toLower()));  // true

// practical usage: filter list
var sentences = [
    "I love programming",
    "Ysharp is powerful",
    "Hello world",
    "Learning Ysharp is fun"
];

for var i = 0; i < sentences.length(); i = i + 1 do
    var s = sentences.get(i);

    if s.contains("Ysharp") then do
        println("Matched: " + s);
    end
end

// simple validation
var url = "https://example.com";

println(url.contains("https://"));  // true
```


### trim

Returns a new string with leading and trailing whitespace removed.  
The original string is not modified.

```ysharp
var text = "   Hello Ysharp   ";

// remove whitespace
var clean = text.trim();

println("Original: '" + text + "'");
println("Trimmed: '" + clean + "'");

// useful for input normalization
var username = "   yagiz   ";
var normalized = username.trim();

println(normalized);   // yagiz

// check empty input after trim
var input = "     ";

if input.trim().length() == 0 then do
    println("Input is empty");
end
```

### trimLeft / trimStart

Returns a new string with leading whitespace removed.  
The original string is not modified.

```ysharp
var text = "   Hello Ysharp   ";

// remove leading whitespace
var result = text.trimLeft();

println("Original: '" + text + "'");
println("Result: '" + result + "'");

// alias usage (same behavior)
println(text.trimStart());

// practical usage: parsing indented text
var line = "    function test()";

// remove indentation
var cleanLine = line.trimLeft();

println(cleanLine);   // function test()

// combine with other methods
var input = "   admin";
var normalized = input.trimLeft().toLower();

println(normalized);  // admin
```

### trimRight / trimEnd

Returns a new string with trailing whitespace removed.  
The original string is not modified.

```ysharp
var text = "   Hello Ysharp   ";

// remove trailing whitespace
var result = text.trimRight();

println("Original: '" + text + "'");
println("Result: '" + result + "'");

// alias usage (same behavior)
println(text.trimEnd());

// practical usage: cleaning file input
var line = "command    ";
var cleanLine = line.trimRight();

println(cleanLine);   // command

// combine with other methods
var input = "user   ";
var normalized = input.trimRight().toLower();

println(normalized);  // user
```

### repeat

Returns a new string repeated `n` times.  
The original string is not modified.

```ysharp
var text = "ha";

// repeat string
println(text.repeat(3));   // hahaha

// build separators
var line = "-".repeat(10);
println(line);   // ----------

// simple formatting
var title = "Ysharp";
println("=".repeat(5) + " " + title + " " + "=".repeat(5));

// generate padding
var indent = " ".repeat(4);
println(indent + "indented text");

// edge case
println("x".repeat(0));   // ""
```

### isEmpty

Returns `true` if the string is empty (`""`), otherwise `false`.

```ysharp
var text = "";

// basic check
println(text.isEmpty());   // true

// non-empty string
var msg = "hello";
println(msg.isEmpty());    // false

// common usage with trim
var input = "   ";

if input.trim().isEmpty() then do
    println("Input is empty");
end else do
    println("Input has content");
end

// filtering list
var items = ["hello", "", "world", ""];

for var i = 0; i < items.length(); i = i + 1 do
    var item = items.get(i);

    if !item.isEmpty() then do
        println("Valid: " + item);
    end
end
```

### reverse

Returns a new string with characters in reverse order.  
The original string is not modified.

```ysharp
var text = "Ysharp";

// basic usage
var reversed = text.reverse();

println("Original: " + text);
println("Reversed: " + reversed);   // prahsY

// palindrome check
var word = "level";

if word.equals(word.reverse()) then do
    println("Palindrome");
end else do
    println("Not palindrome");
end

// combine with other methods
var input = "  Hello  ";
var result = input.trim().reverse();

println(result);   // olleH
```

### padLeft

Returns a new string padded on the left to reach the specified total length using the given pad string.  
If the string is already equal to or longer than the target length, it is returned unchanged.

```ysharp
var text = "42";

// basic padding
println(text.padLeft(5, "0"));   // 00042

// align numbers (table-like output)
var nums = ["1", "23", "456"];

for var i = 0; i < nums.length(); i = i + 1 do
    println(nums.get(i).padLeft(5, " "));
end

// custom pad string
println("A".padLeft(6, "*"));    // *****A

// combine with other methods
var input = "7";
var formatted = input.padLeft(3, "0");

println(formatted);   // 007
```

### padRight

Returns a new string padded on the right to reach the specified total length using the given pad string.  
If the string is already equal to or longer than the target length, it is returned unchanged.

```ysharp
var text = "42";

// basic padding
println(text.padRight(5, "0"));   // 42000

// align text (table-like output)
var names = ["Ali", "Mehmet", "Ayşe"];

for var i = 0; i < names.length(); i = i + 1 do
    println(names.get(i).padRight(10, " "));
end

// custom pad string
println("A".padRight(6, "*"));   // A*****

// combine with other methods
var input = "ID";
var formatted = input.padRight(5, "_");

println(formatted);   // ID___
```

### compareTo

Compares two strings lexicographically (dictionary order).

- Returns `0` &rarr; if both strings are equal
- Returns a negative number &rarr; if this string is smaller
- Returns a positive number &rarr; if this string is greater

```ysharp
var a = "apple";
var b = "banana";
var c = "apple";

// basic comparisons
println(a.compareTo(b));   // negative
println(b.compareTo(a));   // positive
println(a.compareTo(c));   // 0

// use in condition
if a.compareTo(b) < 0 then do
    println(a + " comes before " + b);
end

// sorting example (simple)
var words = ["dog", "cat", "apple"];

for var i = 0; i < words.length(); i = i + 1 do
    for var j = i + 1; j < words.length(); j = j + 1 do
        if words.get(i).compareTo(words.get(j)) > 0 then do
            var temp = words.get(i);
            words.set(i, words.get(j));
            words.set(j, temp);
        end
    end
end

println("Sorted:");
for var i = 0; i < words.length(); i = i + 1 do
    println(words.get(i));
end
```

### capitalize

Returns a new string with the first character converted to uppercase.  
The rest of the string remains unchanged.

```ysharp
var text = "ysharp";

// basic usage
println(text.capitalize());   // Ysharp

// already capitalized
var name = "Ysharp";
println(name.capitalize());   // Ysharp

// combine with other methods
var input = "   hello world";
var result = input.trim().capitalize();

println(result);   // Hello world

// simple title formatting (first letter only)
var username = "admin";
println("User: " + username.capitalize());   // User: Admin
```

### split

Splits the string using the given delimiter and returns an array of substrings.

```ysharp
var text = "apple,banana,orange";

// basic usage
var parts = text.split(",");

for var i = 0; i < parts.length(); i = i + 1 do
    println(parts.get(i));
end

// split sentence into words
var sentence = "Learn Ysharp step by step";
var words = sentence.split(" ");

println("Word count: " + words.length());

// access specific part
println(words.get(0));   // Learn
println(words.get(1));   // Ysharp

// practical usage: parsing CSV-like data
var data = "id:1|name:Ali|age:20";
var fields = data.split("|");

for var i = 0; i < fields.length(); i = i + 1 do
    println(fields.get(i));
end
```

### replaceAll

Returns a new string where all substrings matching the given regex are replaced with the replacement string.

```ysharp
var text = "Hello 123 World 456";

// replace all digits
var result = text.replaceAll("[0-9]+", "#");

println(result);   // Hello # World #

// normalize spaces (multiple -> single)
var messy = "Hello     Ysharp     World";
var clean = messy.replaceAll("\\s+", " ");

println(clean);   // Hello Ysharp World

// remove non-letter characters
var input = "User@123!";
var lettersOnly = input.replaceAll("[^a-zA-Z]", "");

println(lettersOnly);   // User

// practical usage: mask sensitive data
var phone = "555-123-4567";
var masked = phone.replaceAll("[0-9]", "*");

println(masked);   // ***-***-****
````

### join

Joins array elements into a single string using this string as the separator.

```ysharp
var items = ["apple", "banana", "orange"];

// basic usage
var result = ",".join(items);
println(result);   // apple,banana,orange

// different separator
println(" - ".join(items));   // apple - banana - orange

// join numbers (after conversion if needed)
var nums = ["1", "2", "3"];
println("|".join(nums));   // 1|2|3

// practical usage: build sentence
var words = ["Ysharp", "is", "powerful"];
var sentence = " ".join(words);

println(sentence);   // Ysharp is powerful

// edge case: empty array
var empty = [];
println(",".join(empty));   // ""
```


### format

Formats the string using the given arguments and returns the formatted result.

```ysharp
var template = "Hello, %1$s!";

// basic usage
var result = template.format("Ysharp");
println(result);   // Hello, Ysharp!

// multiple arguments
var t = "%1$s is %2$s years old";
println(t.format("Ali", 25));   // Ali is 25 years old

// reusing arguments
var t2 = "%1$s loves %1$s";
println(t2.format("Ysharp"));   // Ysharp loves Ysharp

// practical usage: logging
var user = "admin";
var action = "login";

var log = "[INFO] User %1$s performed %2$s";
println(log.format(user, action));

// combining with other methods
var name = "yagiz";
println("User: %1$s".format(name.capitalize()));   // User: Yagiz
```

### toCharArray

Returns an array containing the characters of the string.

```ysharp
var text = "Ysharp";

// convert to char array
var chars = text.toCharArray();

// access elements
println(chars.get(0));   // Y
println(chars.get(1));   // s

// iterate over characters
for var i = 0; i < chars.length(); i = i + 1 do
    println(chars.get(i));
end

// practical usage: count vowels
var vowels = "aeiou";
var count = 0;

for var i = 0; i < chars.length(); i = i + 1 do
    var ch = Type.Converter.toString(chars.get(i)).toLower();

    if vowels.contains(ch) then do
        count = count + 1;
    end
end

println("Vowel count: " + count);
```


### matches

Returns `true` if the entire string matches the given regular expression.

```ysharp
var text = "12345";

// basic usage
println(text.matches("[0-9]+"));   // true
println(text.matches("[a-z]+"));   // false

// email validation (simple)
var email = "user@example.com";

println(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+"));  // true

// username validation (only letters and numbers, 3–10 chars)
var username = "user123";

println(username.matches("[a-zA-Z0-9]{3,10}"));   // true

// practical usage in condition
var input = "ABC123";

if input.matches("[A-Z0-9]+") then do
    println("Valid code");
end else do
    println("Invalid code");
end
```

### replaceFirst

Returns a new string where only the first substring matching the given regex is replaced with the replacement string.

```ysharp
var text = "123-456-789";

// replace first number group
var result = text.replaceFirst("[0-9]+", "#");
println(result);   // #-456-789

// replace first word
var sentence = "Hello World Hello";
println(sentence.replaceFirst("Hello", "Hi"));   // Hi World Hello

// normalize first space only
var messy = "Hello     Ysharp     World";
println(messy.replaceFirst("\\s+", " "));   // Hello Ysharp     World

// practical usage: mask first occurrence
var email = "user@example.com";
var masked = email.replaceFirst("[a-zA-Z]", "*");

println(masked);   // *ser@example.com
```

### lastIndexOf

Returns the index of the last occurrence of a substring.  
If the substring is not found, it returns `-1`.

```ysharp
var text = "one two one three";

// basic usage
println(text.lastIndexOf("one"));   // last occurrence index
println(text.lastIndexOf("two"));   // index of "two"
println(text.lastIndexOf("java"));  // -1

// use with condition
var file = "archive.tar.gz";

var dotIndex = file.lastIndexOf(".");

if dotIndex != -1 then do
    var extension = file.substring(dotIndex + 1, file.length());
    println("Extension: " + extension);   // gz
end

// practical usage: get last part
var path = "C:/users/yagiz/docs/file.txt";

var slashIndex = path.lastIndexOf("/");

var fileName = path.substring(slashIndex + 1, path.length());
println(fileName);   // file.txt
```

### count

Returns the number of occurrences of the given substring in the string.

```ysharp
var text = "banana";

// basic usage
println(text.count("a"));   // 3
println(text.count("na"));  // 2
println(text.count("x"));   // 0

// case-sensitive behavior
var s = "Hello hello";
println(s.count("hello"));   // 1
println(s.toLower().count("hello"));   // 2

// practical usage: validation
var password = "Pass123!!";

// count special characters
var specialCount = password.count("!");

println("Special chars: " + specialCount);

// simple frequency check
var sentence = "ysharp is sharp";
println(sentence.count("sharp"));   // 2
```

### charCodeAt

Returns the numeric character code (e.g., Unicode/ASCII) at the specified index.

```ysharp
var text = "ABC";

// basic usage
println(text.charCodeAt(0));   // 65
println(text.charCodeAt(1));   // 66
println(text.charCodeAt(2));   // 67

// compare characters using codes
var a = "a";
var b = "b";

println(a.charCodeAt(0) < b.charCodeAt(0));   // true

// practical usage: check if digit
var ch = "7";

var code = ch.charCodeAt(0);

if code >= 48 && code <= 57 then do
    println("Digit");
end

// iterate and print codes
var word = "Hi";

for var i = 0; i < word.length(); i = i + 1 do
    println(word.charCodeAt(i));
end
```

### slice

Returns a substring from `start` to `end` (end is optional).  
Supports negative indices (counting from the end of the string).

```ysharp
var text = "HelloYsharp";

// basic usage
println(text.slice(0, 5));    // Hello
println(text.slice(5));       // Ysharp

// negative indices
println(text.slice(-6));      // Ysharp
println(text.slice(-6, -1));  // Yshar

// middle extraction
println(text.slice(2, 8));    // lloYsh

// practical: get file extension
var file = "archive.tar.gz";

var ext = file.slice(file.lastIndexOf(".") + 1);
println(ext);   // gz

// practical: get last N characters
var id = "USER_123456";
println(id.slice(-6));   // 123456

// combine with other methods
var input = "   hello world   ";
var result = input.trim().slice(0, 5).toUpper();

println(result);   // HELLO

// edge cases
println(text.slice(0));        // HelloYsharp
println(text.slice(100));      // "" (out of range)
println(text.slice(-100));     // HelloYsharp (clamped)
```