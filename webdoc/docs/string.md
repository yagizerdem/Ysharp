---
sidebar_position: 7
---

Strings are collection of unicode characters to store text. 
A String variable contains a collection of characters surrounded by double quotes (""):

In Ysharp strings are immutable object type. meaning 
their values cannot be changed once created. 
If you try to modify a string like concat or replace, 
a new string object is created instead of altering the original one. 

Standard library provide a lot of string utility functions. <br/> 
Full list api list below :


| Method | Signature | Return Type | Description |
|--------|----------|------------|-------------|
| `length` / `size` | `str.length()` | `int` | Returns the length of the string |
| `isEmpty` | `str.isEmpty()` | `boolean` | Checks if the string is empty |
| `toUpper` | `str.toUpper()` | `string` | Converts string to uppercase |
| `toLower` | `str.toLower()` | `string` | Converts string to lowercase |
| `reverse` | `str.reverse()` | `string` | Reverses the string |
| `capitalize` | `str.capitalize()` | `string` | Capitalizes first character |
| `charAt` | `str.charAt(index)` | `char` | Returns character at index |
| `charCodeAt` | `str.charCodeAt(index)` | `int` | Returns Unicode code of character |
| `substring` | `str.substring(start, end)` | `string` | Extracts substring `[start, end)` |
| `slice` | `str.slice(start, end?)` | `string` | Supports negative indices |
| `equals` | `str.equals(other)` | `boolean` | Checks equality |
| `compareTo` | `str.compareTo(other)` | `int` | Lexicographic comparison |
| `indexOf` | `str.indexOf(substr)` | `int` | First occurrence index |
| `lastIndexOf` | `str.lastIndexOf(substr)` | `int` | Last occurrence index |
| `contains` | `str.contains(substr)` | `boolean` | Checks if substring exists |
| `startsWith` | `str.startsWith(prefix)` | `boolean` | Checks prefix |
| `endsWith` | `str.endsWith(suffix)` | `boolean` | Checks suffix |
| `count` | `str.count(substr)` | `int` | Counts occurrences |
| `replace` | `str.replace(old, new)` | `string` | Replaces substring |
| `replaceAll` | `str.replaceAll(regex, repl)` | `string` | Replaces all regex matches |
| `replaceFirst` | `str.replaceFirst(regex, repl)` | `string` | Replaces first regex match |
| `repeat` | `str.repeat(n)` | `string` | Repeats string n times |
| `trim` | `str.trim()` | `string` | Trims both sides |
| `trimLeft` / `trimStart` | `str.trimLeft()` | `string` | Trims left side |
| `trimRight` / `trimEnd` | `str.trimRight()` | `string` | Trims right side |
| `padLeft` | `str.padLeft(len, padStr)` | `string` | Pads from left |
| `padRight` | `str.padRight(len, padStr)` | `string` | Pads from right |
| `split` | `str.split(regex)` | `array<string>` | Splits using regex |
| `join` | `str.join(array)` | `string` | Joins array using string as separator |
| `toCharArray` | `str.toCharArray()` | `array<char>` | Converts to char array |
| `matches` | `str.matches(regex)` | `boolean` | Full regex match |
| `format` | `str.format(...args)` | `string` | Formats string |


