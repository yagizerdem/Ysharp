---
sidebar_position: 1
---


The Array object, as with arrays in other programming 
languages, enables storing a collection 
of multiple items under a single variable name,
and has members for performing common array operations.


## Description
In Ysharp, arrays aren't primitives 
but are instead Array objects with the following core characteristics:

- Ysharp arrays are resizable and can contain a mix of different data types. 
(When those characteristics are undesirable, use typed arrays instead.)

- Ysharp arrays are not associative arrays and so,
array elements cannot be accessed using arbitrary strings as indexes,
but must be accessed using nonnegative integers 
(or their respective string form) as indexes.

- Ysharp arrays are zero-indexed: the first element of an array is at index 0, 
the second is at index 1, and so on and the last element is 
at the value of the array's length property minus 1.

- Ysharp array-copy operations create shallow copies. (All standard built-in copy operations with any 
Yshapr collection create shallow copies, rather than deep copies).

## Array indices

Ysharp arrays are **not associative** and do not support arbitrary keys.

- Elements can only be accessed using **non-negative integer indexes**
- Any non-integer index (such as a string) is **invalid**

```ysharp
var arr = [10, 20, 30];

println arr.get(0); // 10
println arr.get(1); // 20
````

Array elements are object properties in the same way that toString is a property
(to be specific, however, toString() is a method).
Nevertheless, trying to access an element of an array as follows 
throws a syntax error because the property name is not valid:

`arr.0; // a syntax error`

---

#### Relationship between length and numerical properties

A Ysharp array's length property and numerical 
properties are connected. Several of the
built-in array methods (e.g., get(), set(), slice(), indexOf() etc.)
take into account the value of an array's length property when they're called.
Other methods (e.g., push(), shift(), etc.)
also result in updates to an array's length property.

````ysharp
const fruits = [];
fruits.push("banana");
fruits.push("apple");
fruits.push("peach");
println fruits.size(); // 3
````

## Reference

### Instance methods

| Method                | Signature                                             | Return Type         | Description                                                                                                                           |
|-----------------------|-------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `add` / `push`        | `arr.add(value : any)`                                | `int`               | Adds a value to the end of the array and returns the new size of the array                                                            |
| `asQueryable`         | `arr.asQueryable()`                                   | `Queryable`         | Converts the array into a Queryable sequence for LINQ-style operations without copying the underlying data                            |
| `average`             | `arr.average()`                                       | `double`            | Returns the average of all numeric values in the array; non-numeric values are ignored, returns 0 if no valid numbers exist           |
| `clear`               | `arr.clear()`                                         | `int`               | Removes all elements from the array and returns 0                                                                                     |
| `clone`               | `arr.clone()`                                         | `Array`             | Returns a shallow copy of the array; elements are copied by reference                                                                 |
| `concat`              | `arr.concat(other : Array)`                           | `Array`             | Returns a new array by merging the current array with another array (shallow copy)                                                    |
| `contains`            | `arr.contains(value : any)`                           | `bool`              | Returns true if the array contains the specified value (uses equality comparison)                                                     |
| `count`               | `arr.count(value : any)`                              | `int`               | Returns the number of occurrences of the specified value in the array                                                                 |
| `ensureCapacity`      | `arr.ensureCapacity(minCapacity : int)`               | `int`               | Ensures the internal capacity is at least the specified value without changing the current size; returns the current size             |
| `every`               | `arr.every(callback : function)`                      | `bool`              | Returns true if all elements satisfy the given predicate function; callback receives (element, index?, array?)                        |
| `fill`                | `arr.fill(value : any)`                               | `Array`             | Replaces all elements in the array with the given value and returns the array itself                                                  |
| `filter`              | `arr.filter(callback : function)`                     | `Array`             | Returns a new array containing elements that satisfy the predicate; callback receives (element, index?, array?)                       |
| `find`                | `arr.find(callback : function)`                       | `any`               | Returns the first element that satisfies the predicate, or null if none match; callback receives (element, index?, array?)            |
| `findIndex`           | `arr.findIndex(callback : function)`                  | `int`               | Returns the index of the first element that satisfies the predicate, or -1 if none match; callback receives (element, index?, array?) |
| `flat`                | `arr.flat(depth : int)`                               | `Array`             | Returns a new array with nested arrays flattened up to the specified depth                                                            |
| `forEach`             | `arr.forEach(callback : function)`                    | `null`              | Executes the given function for each element in the array; callback receives (element, index?, array?)                                |
| `get`                 | `arr.get(index : int)`                                | `any`               | Returns the element at the specified index; throws an error if the index is out of bounds                                             |
| `indexOf`             | `arr.indexOf(value : any)`                            | `int`               | Returns the index of the first occurrence of the specified value, or -1 if not found                                                  |
| `insert`              | `arr.insert(index : int, value : any)`                | `int`               | Inserts a value at the specified index, shifts elements to the right, and returns the new size                                        |
| `isEmpty`             | `arr.isEmpty()`                                       | `bool`              | Returns true if the array contains no elements                                                                                        |
| `join`                | `arr.join(separator : string)`                        | `string`            | Joins all elements into a string using the given separator; uses element's toString if available                                      |
| `lastIndexOf`         | `arr.lastIndexOf(value : any)`                        | `int`               | Returns the index of the last occurrence of the specified value, or -1 if not found                                                   |
| `map`                 | `arr.map(callback : function)`                        | `Array`             | Returns a new array with the results of applying the callback to each element; callback receives (element, index?, array?)            |
| `max`                 | `arr.max()`                                           | `any`               | Returns the maximum numeric value in the array; throws an error if the array is empty and ignores non-numeric values                  |
| `pop`                 | `arr.pop()`                                           | `any`               | Removes and returns the last element of the array; throws an error if the array is empty                                              |
| `reduce`              | `arr.reduce(callback : function, initialValue : any)` | `any`               | Reduces the array to a single value by applying the callback; callback receives (accumulator, element, index?, array?)                |
| `remove`              | `arr.remove(index : int)`                             | `any`               | Removes and returns the element at the specified index; shifts remaining elements to the left                                         |
| `reverse`             | `arr.reverse()`                                       | `Array`             | Reverses the elements of the array in place and returns the array itself                                                              |
| `set`                 | `arr.set(index : int, value : any)`                   | `any`               | Replaces the element at the specified index with a new value and returns the previous element                                         |
| `shift`               | `arr.shift()`                                         | `any`               | Removes and returns the first element of the array; shifts remaining elements to the left and throws an error if the array is empty   |
| `shuffle`             | `arr.shuffle()`                                       | `Array`             | Randomly shuffles the elements of the array in place and returns the array itself                                                     |
| `size` / `length`     | `arr.size()`                                          | `int`               | Returns the number of elements in the array                                                                                           |
| `skip`                | `arr.skip(n : int)`                                   | `Array`             | Returns a new array excluding the first n elements (clamped within bounds)                                                            |
| `slice`               | `arr.slice(start : int, end : int)`                   | `Array`             | Returns a shallow copy of a portion of the array from start (inclusive) to end (exclusive); supports negative indices                 |
| `some`                | `arr.some(callback : function)`                       | `bool`              | Returns true if at least one element satisfies the predicate; callback receives (element, index?, array?)                             |
| `sort`                | `arr.sort(callback? : function)`                      | `Array`             | Sorts the array in place; uses default numeric/string comparison or a custom comparator callback and returns the array                |
| `sum`                 | `arr.sum()`                                           | `double`            | Returns the sum of all numeric values in the array; non-numeric values are ignored                                                    |
| `take`                | `arr.take(n : int)`                                   | `Array`             | Returns a new array containing the first n elements (clamped within bounds)                                                           |
| `toNativeArray`       | `arr.toNativeArray()`                                 | `Object[]`          | Converts the array into a native Java array containing underlying values                                                              |
| `toNativeArrayList`   | `arr.toNativeArrayList()`                             | `ArrayList<Object>` | Converts the array into a native Java ArrayList containing underlying values                                                          |
| `toString`            | `arr.toString()`                                      | `string`            | Returns a string representation of the array by converting each element using its toString method if available                        |
| `unique` / `distinct` | `arr.unique()`                                        | `Array`             | Returns a new array with duplicate elements removed using equality comparison                                                         |
| `unshift`             | `arr.unshift(value : any)`                            | `int`               | Adds an element to the beginning of the array, shifts existing elements to the right, and returns the new size                        |


### Static methods


| Method     | Signature                                    | Return Type | Description                                                                    |
|------------|----------------------------------------------|-------------|--------------------------------------------------------------------------------|
| `isArray`  | `Array.isArray(value : any)`                 | `bool`      | Returns true if the given value is an array instance                           |
| `of`       | `Array.of(...values : any)`                  | `Array`     | Creates a new array from the given arguments                                   |
| `range`    | `Array.range(start : int, end : int, step?)` | `Array`     | Creates a new array containing a sequence of numbers from start to end         |


## Examples / instance methods

### add

- Adds a value to the end of the array and returns the new size of the array

````ysharp
let arr = [1, 2, 3];

arr.add(4);        // [1, 2, 3, 4]
arr.push(5);       // [1, 2, 3, 4, 5]

let size = arr.add(6);
println(size);       // 6

arr.add("hello");
arr.add(null);

println arr.toString();
````

### asQueryable

- Converts the array into a Queryable sequence for LINQ-style operations without copying the underlying data

````ysharp
var arr = [1, 2, 3, 4, 5];

// convert to queryable
var q = arr.asQueryable();

// filtering (assuming LINQ-style API exists)
var result = q
    .where((x) => x > 2)
    .select((x) => x * 10)
    .toArray();

println result.toString(); // [30, 40, 50]

// original array is NOT modified
println arr.toString(); // [1, 2, 3, 4, 5]
````

### average

- Returns the average of all numeric values in the array
- Non-numeric values are ignored
- Returns `0` if no valid numeric values exist  

````ysharp
var arr = [1, 2, 3, 4];

println arr.average(); // 2.5
var arr = [1, "hello", 3, true];

println arr.average(); // 2.0  (only 1 and 3 are counted)
var arr = ["a", "b", null];

println arr.average(); // 0
````

### clear

- Removes all elements from the array and returns `0`

````
var arr = [1, 2, 3, 4];

println arr.size(); // 4

var result = arr.clear();

println result;     // 0
println arr.size(); // 0
println arr.toString(); // [ ]
var arr = [];

var result = arr.clear();

println result;     // 0
println arr.size(); // 0
````


### clone

- Returns a shallow copy of the array
- Elements are copied **by reference**, not deeply cloned  

````ysharp
var arr = [1, 2, 3];

var cloned = arr.clone();

println arr.toString();    // [ 1 , 2 , 3 ]
println cloned.toString(); // [ 1 , 2 , 3 ]
var obj = { "value": 10 };

var arr = [obj];
var cloned = arr.clone();

obj.put("value", 99);

// both arrays see the same object reference
println arr.get(0).get("value");    // 99
println cloned.get(0).get("value"); // 99
````

### concat

- Returns a new array by merging the current array with another array
- Performs a **shallow copy** (elements are copied by reference)

````ysharp
var arr1 = [1, 2, 3];
var arr2 = [4, 5];

var result = arr1.concat(arr2);

println result.toString(); // [ 1 , 2 , 3 , 4 , 5 ]
var obj = { "value": 10 };

var arr1 = [obj];
var arr2 = [];

var result = arr1.concat(arr2);

obj.put("value", 99);

// shallow copy → same reference
println result.get(0).get("value"); // 99
````
- The original arrays are not modified
- The returned array is a new instance

### contains

- Returns `true` if the array contains the specified value
- Uses equality comparison (primitives by value, objects by reference)


````ysharp
var arr = [1, 2, 3];

println arr.contains(2); // true
println arr.contains(5); // false
var arr = ["hello", "world"];

println arr.contains("hello"); // true
println arr.contains("test");  // false
var obj = { "value": 10 };
var arr = [obj];

// same reference
println arr.contains(obj); // true

// different object with same content
var other = { "value": 10 };
println arr.contains(other); // false
var arr = [null, 1];

println arr.contains(null); // true
````

### count

- Returns the number of occurrences of the specified value in the array
- Uses equality comparison (primitives by value, objects by reference)

````ysharp
var arr = [1, 2, 2, 3, 2];

println arr.count(2); // 3
println arr.count(5); // 0
var arr = ["a", "b", "a", "c"];

println arr.count("a"); // 2
println arr.count("x"); // 0
var obj = { "value": 10 };
var arr = [obj, obj];

// same reference
println arr.count(obj); // 2

// different object with same content
var other = { "value": 10 };
println arr.count(other); // 0
var arr = [null, 1, null, 2];

println arr.count(null); // 2
````


### ensureCapacity

- Ensures the internal capacity is at least the specified value
- Does **not change the array size**
- Returns the current size  

````ysharp
var arr = [1, 2, 3];

println arr.size(); // 3

var result = arr.ensureCapacity(100);

println result;     // 3
println arr.size(); // 3
var arr = [];

arr.ensureCapacity(1000);

// no elements added, only capacity increased internally
println arr.size(); // 0
````

### every

- Returns `true` if **all elements** satisfy the given predicate function
- Stops early if any element fails the condition
- Callback receives `(element, index?, array?)`

````
var arr = [2, 4, 6];

println arr.every((x) => x % 2 == 0); // true
var arr = [2, 3, 6];

println arr.every((x) => x % 2 == 0); // false
var arr = [1, 2, 3];

println arr.every((x, i) => x > i); // true
````


### fill

- Replaces all elements in the array with the given value
- Returns the array itself (in-place modification)

````ysharp
var arr = [1, 2, 3, 4];

arr.fill(0);

println arr.toString(); // [ 0 , 0 , 0 , 0 ]
var arr = ["a", "b", "c"];

var result = arr.fill("x");

println result.toString(); // [ x , x , x ]
println arr.toString();    // [ x , x , x ] (same array)
var obj = { "value": 10 };

var arr = [1, 2, 3];

arr.fill(obj);

obj.put("value", 99);

// same reference in all positions
println arr.get(0).get("value"); // 99
println arr.get(1).get("value"); // 99
````

### filter

- Returns a new array containing elements that satisfy the predicate
- Does not modify the original array
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3, 4];

var result = arr.filter((x) => x % 2 == 0);

println result.toString(); // [ 2 , 4 ]
println arr.toString();    // [ 1 , 2 , 3 , 4 ]
var arr = ["a", "bb", "ccc"];

var result = arr.filter((x) => x.length() > 1);

println result.toString(); // [ bb , ccc ]
var arr = [10, 20, 30];

var result = arr.filter((x, i) => i % 2 == 0);

println result.toString(); // [ 10 , 30 ]
var arr = [];

var result = arr.filter((x) => x > 0);

println result.toString(); // [ ]
````

### find

- Returns the first element that satisfies the predicate
- Returns `null` if no element matches
- Stops searching after the first match
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3, 4];

var result = arr.find((x) => x > 2);

println result; // 3
var arr = [1, 2, 3];

var result = arr.find((x) => x > 10);

println result; // null
var arr = ["a", "bb", "ccc"];

var result = arr.find((x) => x.length() == 2);

println result; // bb
var arr = [10, 20, 30];

var result = arr.find((x, i) => i == 1);

println result; // 20
````


### findIndex

- Returns the index of the first element that satisfies the predicate
- Returns `-1` if no element matches
- Stops searching after the first match
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3, 4];

var index = arr.findIndex((x) => x > 2);

println index; // 2
var arr = [1, 2, 3];

var index = arr.findIndex((x) => x > 10);

println index; // -1
var arr = ["a", "bb", "ccc"];

var index = arr.findIndex((x) => x.length() == 2);

println index; // 1
var arr = [10, 20, 30];

var index = arr.findIndex((x, i) => i == 1);

println index; // 1
````


### flat

- Returns a new array with nested arrays flattened up to the specified depth
- Does not modify the original array  


````ysharp
var arr = [1, [2, 3], [4, 5]];

var result = arr.flat(1);

println result.toString(); // [ 1 , 2 , 3 , 4 , 5 ]
var arr = [1, [2, [3, [4]]]];

var result = arr.flat(2);

println result.toString(); // [ 1 , 2 , 3 , [4] ]
var arr = [1, [2, [3, [4]]]];

var result = arr.flat(10);

println result.toString(); // [ 1 , 2 , 3 , 4 ]
var arr = [1, 2, 3];

var result = arr.flat(1);

println result.toString(); // [ 1 , 2 , 3 ]
````

### forEach

- Executes the given function for each element in the array
- Returns `null`
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3];

arr.forEach((x) => do
    println x;
end);

// 1
// 2
// 3
var arr = ["a", "b", "c"];

arr.forEach((x, i) => do
    println i + " -> " + x;
end);

// 0 -> a
// 1 -> b
// 2 -> c
var arr = [10, 20, 30];

arr.forEach((x, i, a) => do
    a.set(i, x * 2);
end);

println arr.toString(); // [ 20 , 40 , 60 ]
````

### get

- Returns the element at the specified index
- Throws an error if the index is out of bounds  

````ysharp
var arr = [10, 20, 30];

println arr.get(0); // 10
println arr.get(2); // 30
var arr = ["a", "b", "c"];

var value = arr.get(1);
println value; // b
var arr = [1, 2, 3];

try do
    arr.get(5);
    println "should not reach here";
end
catch(e) do
    println "index out of bounds";
end
````


### indexOf

- Returns the index of the first occurrence of the specified value
- Returns `-1` if the value is not found
- Uses equality comparison (primitives by value, objects by reference)


````ysharp
var arr = [1, 2, 3, 2];

println arr.indexOf(2); // 1
println arr.indexOf(5); // -1
var arr = ["a", "b", "c"];

println arr.indexOf("b"); // 1
println arr.indexOf("x"); // -1
var obj = { "value": 10 };
var arr = [obj];

println arr.indexOf(obj); // 0

var other = { "value": 10 };
println arr.indexOf(other); // -1
````

### insert

- Inserts a value at the specified index
- Shifts existing elements to the right
- Returns the new size of the array  

````ysharp
var arr = [1, 2, 3];

arr.insert(1, 99);

println arr.toString(); // [ 1 , 99 , 2 , 3 ]
println arr.size();     // 4
var arr = [10, 20];

arr.insert(0, 5);   // beginning
arr.insert(arr.size(), 30); // end

println arr.toString(); // [ 5 , 10 , 20 , 30 ]
var arr = [];

arr.insert(0, "hello");

println arr.toString(); // [ hello ]
var arr = [1, 2, 3];

try do
    arr.insert(10, 5);
end
catch(e) do
    println "index out of bounds";
end
````


### isEmpty

- Returns `true` if the array contains no elements
- Returns `false` otherwise  

````ysharp
var arr = [];

println arr.isEmpty(); // true
var arr = [1, 2, 3];

println arr.isEmpty(); // false
var arr = [];

arr.add(10);

println arr.isEmpty(); // false

arr.clear();

println arr.isEmpty(); // true
````

### join

- Joins all elements into a string using the given separator
- Uses each element’s `toString` method if available  

````ysharp
var arr = [1, 2, 3];

var result = arr.join(", ");

println result; // 1, 2, 3
var arr = ["a", "b", "c"];

println arr.join("-"); // a-b-c
var obj = { "value": 10 };

var arr = [obj];

println arr.join(", "); // uses object's toString
var arr = [1, null, 3];

println arr.join(" | "); // 1 | null | 3
````

### lastIndexOf

- Returns the index of the **last occurrence** of the specified value
- Returns `-1` if the value is not found
- Uses equality comparison (primitives by value, objects by reference)

````ysharp
var arr = [1, 2, 3, 2];

println arr.lastIndexOf(2); // 3
println arr.lastIndexOf(5); // -1
var arr = ["a", "b", "a", "c"];

println arr.lastIndexOf("a"); // 2
println arr.lastIndexOf("x"); // -1
var obj = { "value": 10 };
var arr = [obj, obj];

println arr.lastIndexOf(obj); // 1

var other = { "value": 10 };
println arr.lastIndexOf(other); // -1
````

### map

- Returns a new array with the results of applying the callback to each element
- Does not modify the original array
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3];

var result = arr.map((x) => x * 2);

println result.toString(); // [ 2 , 4 , 6 ]
println arr.toString();    // [ 1 , 2 , 3 ]
var arr = ["a", "bb", "ccc"];

var result = arr.map((x) => x.length());

println result.toString(); // [ 1 , 2 , 3 ]
var arr = [10, 20, 30];

var result = arr.map((x, i) => x + i);

println result.toString(); // [ 10 , 21 , 32 ]
var arr = [];

var result = arr.map((x) => x * 2);

println result.toString(); // [ ]
````

### max

- Returns the maximum numeric value in the array
- Throws an error if the array is empty
- Ignores non-numeric values  

````ysharp
var arr = [1, 5, 3];

println arr.max(); // 5
var arr = [1, "hello", 10, true];

println arr.max(); // 10 (non-numeric values ignored)
var arr = [-5, -2, -10];

println arr.max(); // -2
var arr = [];

try do
    arr.max();
end
catch(e) do
    println "cannot call max on empty array";
end
````

### pop

- Removes and returns the last element of the array
- Throws an error if the array is empty  

````ysharp
var arr = [1, 2, 3];

var value = arr.pop();

println value;           // 3
println arr.toString();  // [ 1 , 2 ]
var arr = ["a", "b"];

println arr.pop(); // b
println arr.pop(); // a
var arr = [];

try do
    arr.pop();
end
catch(e) do
    println "cannot pop from empty array";
end
````


### reduce

- Reduces the array to a single value by applying the callback
- Callback receives `(accumulator, element, index?, array?)`
- Uses `initialValue` as the starting accumulator if provided  


````ysharp
var arr = [1, 2, 3, 4];

var sum = arr.reduce((acc, x) => acc + x, 0);

println sum; // 10
var arr = [1, 2, 3];

var result = arr.reduce((acc, x) => acc * x, 1);

println result; // 6
var arr = ["a", "b", "c"];

var result = arr.reduce((acc, x) => acc + x, "");

println result; // abc
var arr = [10, 20, 30];

var result = arr.reduce((acc, x, i) => acc + (x * i), 0);

println result; // 80  (0 + 20*1 + 30*2)
````


### remove

- Removes and returns the element at the specified index
- Shifts remaining elements to the left  

````ysharp
var arr = [1, 2, 3, 4];

var value = arr.remove(1);

println value;          // 2
println arr.toString(); // [ 1 , 3 , 4 ]
var arr = ["a", "b", "c"];

println arr.remove(0);  // a
println arr.toString(); // [ b , c ]
var arr = [10, 20, 30];

println arr.remove(arr.size() - 1); // 30
println arr.toString();             // [ 10 , 20 ]
var arr = [1, 2, 3];

try do
    arr.remove(10);
end
catch(e) do
    println "index out of bounds";
end
````


### reverse

- Reverses the elements of the array in place
- Returns the array itself  


````ysharp
var arr = [1, 2, 3, 4];

arr.reverse();

println arr.toString(); // [ 4 , 3 , 2 , 1 ]
var arr = ["a", "b", "c"];

var result = arr.reverse();

println result.toString(); // [ c , b , a ]
println arr.toString();    // [ c , b , a ] (same array)
var arr = [1];

arr.reverse();

println arr.toString(); // [ 1 ]
````


### set

- Replaces the element at the specified index with a new value
- Returns the previous element  


````ysharp
var arr = [1, 2, 3];

var old = arr.set(1, 99);

println old;             // 2
println arr.toString();  // [ 1 , 99 , 3 ]
var arr = ["a", "b", "c"];

println arr.set(0, "z"); // a
println arr.toString();  // [ z , b , c ]
var obj = { "value": 10 };

var arr = [obj];

var newObj = { "value": 20 };

arr.set(0, newObj);

println arr.get(0).get("value"); // 20
var arr = [1, 2, 3];

try do
    arr.set(10, 5);
end
catch(e) do
    println "index out of bounds";
end
````


### shift

- Removes and returns the first element of the array
- Shifts remaining elements to the left
- Throws an error if the array is empty  

````ysharp
var arr = [1, 2, 3];

var value = arr.shift();

println value;           // 1
println arr.toString();  // [ 2 , 3 ]
var arr = ["a", "b", "c"];

println arr.shift();     // a
println arr.toString();  // [ b , c ]
var arr = [10];

println arr.shift();     // 10
println arr.toString();  // [ ]
var arr = [];

try do
    arr.shift();
end
catch(e) do
    println "cannot shift from empty array";
end
````

### shuffle

- Randomly shuffles the elements of the array in place
- Returns the array itself  

````ysharp
var arr = [1, 2, 3, 4, 5];

arr.shuffle();

println arr.toString(); // example: [ 3 , 1 , 5 , 2 , 4 ]
var arr = ["a", "b", "c"];

var result = arr.shuffle();

println result.toString(); // random order
println arr.toString();    // same array (modified)
var arr = [1];

arr.shuffle();

println arr.toString(); // [ 1 ]
````


### size / length

- Returns the number of elements in the array  

````ysharp
var arr = [1, 2, 3];

println arr.size();   // 3
println arr.length(); // 3
var arr = [];

println arr.size(); // 0
var arr = [1, 2];

arr.add(3);
arr.add(4);

println arr.size(); // 4
````


### skip

- Returns a new array excluding the first `n` elements
- `n` is clamped within bounds (`0 <= n <= size`)
- Does not modify the original array  


````ysharp
var arr = [1, 2, 3, 4, 5];

var result = arr.skip(2);

println result.toString(); // [ 3 , 4 , 5 ]
println arr.toString();    // [ 1 , 2 , 3 , 4 , 5 ]
var arr = [1, 2, 3];

println arr.skip(0).toString(); // [ 1 , 2 , 3 ]
println arr.skip(3).toString(); // [ ]
println arr.skip(10).toString(); // [ ]
var arr = [];

println arr.skip(5).toString(); // [ ]
````

### slice

- Returns a shallow copy of a portion of the array from `start` (inclusive) to `end` (exclusive)
- Supports negative indices
- Does not modify the original array  

````ysharp
var arr = [1, 2, 3, 4, 5];

var result = arr.slice(1, 4);

println result.toString(); // [ 2 , 3 , 4 ]
println arr.toString();    // [ 1 , 2 , 3 , 4 , 5 ]
var arr = [1, 2, 3, 4, 5];

var result = arr.slice(-3, -1);

println result.toString(); // [ 3 , 4 ]
var arr = [1, 2, 3];

println arr.slice(0, 3).toString(); // [ 1 , 2 , 3 ]
println arr.slice(1, 10).toString(); // [ 2 , 3 ]
var obj = { "value": 10 };

var arr = [obj];

var sliced = arr.slice(0, 1);

obj.put("value", 99);

// shallow copy → same reference
println sliced.get(0).get("value"); // 99
````


### some

- Returns `true` if at least one element satisfies the predicate
- Stops early when a match is found
- Callback receives `(element, index?, array?)`

````ysharp
var arr = [1, 2, 3, 4];

println arr.some((x) => x > 3); // true
var arr = [1, 2, 3];

println arr.some((x) => x > 10); // false
var arr = ["a", "bb", "ccc"];

println arr.some((x) => x.length() == 2); // true
var arr = [10, 20, 30];

println arr.some((x, i) => i == 2); // true
var arr = [];

println arr.some((x) => x > 0); // false
````

### sort

- Sorts the array in place
- Uses default numeric/string comparison or a custom comparator
- Returns the array itself  

````ysharp
var arr = [3, 1, 2];

arr.sort();

println arr.toString(); // [ 1 , 2 , 3 ]
var arr = ["b", "a", "c"];

arr.sort();

println arr.toString(); // [ a , b , c ]
var arr = [3, 1, 2];

arr.sort((a, b) => a - b);

println arr.toString(); // [ 1 , 2 , 3 ]
var arr = [3, 1, 2];

arr.sort((a, b) => b - a);

println arr.toString(); // [ 3 , 2 , 1 ]
var arr = [1, "b", 2, "a"];

arr.sort();

println arr.toString(); // mixed comparison (numbers first, then strings)
````

### sum

- Returns the sum of all numeric values in the array
- Non-numeric values are ignored  

````ysharp
var arr = [1, 2, 3, 4];

println arr.sum(); // 10.0
var arr = [1, "hello", 3, true];

println arr.sum(); // 4  (only 1 and 3 are counted)
var arr = ["a", "b", null];

println arr.sum(); // 0.0
var arr = [-5, 10, -2];

println arr.sum(); // 3.0
````


### take

- Returns a new array containing the first `n` elements
- `n` is clamped within bounds (`0 <= n <= size`)
- Does not modify the original array  

````ysharp
var arr = [1, 2, 3, 4, 5];

var result = arr.take(3);

println result.toString(); // [ 1 , 2 , 3 ]
println arr.toString();    // [ 1 , 2 , 3 , 4 , 5 ]
var arr = [1, 2, 3];

println arr.take(0).toString();  // [ ]
println arr.take(3).toString();  // [ 1 , 2 , 3 ]
println arr.take(10).toString(); // [ 1 , 2 , 3 ]
````


### toNativeArray

- Converts the array into a native Java `Object[]`
- Elements are converted to their underlying Java values

````ysharp
var arr = [1, 2, 3];

var nativeArr = arr.toNativeArray();

// Java side representation:
// Object[] {1, 2, 3}
var arr = ["a", 10, true];

var nativeArr = arr.toNativeArray();

// Java side:
// Object[] {"a", 10, true}
var obj = { "value": 10 };

var arr = [obj];

var nativeArr = arr.toNativeArray();

// underlying object reference is preserved
````


### toNativeArrayList

- Converts the array into a native Java `ArrayList<Object>`
- Elements are converted to their underlying Java values  

````ysharp
var arr = [1, 2, 3];

var list = arr.toNativeArrayList();

// Java side representation
// list => ArrayList [1, 2, 3]
var arr = ["a", 10, true];

var list = arr.toNativeArrayList();

// Java side:
// ["a", 10, true]
var obj = { "value": 10 };

var arr = [obj];

var list = arr.toNativeArrayList();

// underlying object reference is preserved
````

### toString

- Returns a string representation of the array
- Each element is converted using its `toString` method if available  

````ysharp
var arr = [1, 2, 3];

println arr.toString(); // [ 1 , 2 , 3 ]
var arr = ["a", "b", "c"];

println arr.toString(); // [ a , b , c ]
var obj = { "value": 10 };

var arr = [obj];

println arr.toString(); // uses object's toString
var arr = [1, null, 3];

println arr.toString(); // [ 1 , null , 3 ]
````


### unique / distinct

- Returns a new array with duplicate elements removed
- Uses equality comparison (primitives by value, objects by reference)
- Preserves the original order of first occurrences  

````ysharp
var arr = [1, 2, 2, 3, 1];

var result = arr.unique();

println result.toString(); // [ 1 , 2 , 3 ]
var arr = ["a", "b", "a", "c"];

println arr.unique().toString(); // [ a , b , c ]
var obj = { "value": 10 };

var arr = [obj, obj];

println arr.unique().toString(); // [ {value:10} ]
var obj1 = { "value": 10 };
var obj2 = { "value": 10 };

var arr = [obj1, obj2];

println arr.unique().toString(); // both kept (different references)
var arr = [null, 1, null, 2];

println arr.unique().toString(); // [ null , 1 , 2 ]
````


### unshift

- Adds an element to the beginning of the array
- Shifts existing elements to the right
- Returns the new size of the array  

````ysharp
var arr = [2, 3];

var size = arr.unshift(1);

println size;           // 3
println arr.toString(); // [ 1 , 2 , 3 ]
var arr = [];

arr.unshift("hello");

println arr.toString(); // [ hello ]
println arr.size();     // 1
var arr = [10, 20];

arr.unshift(5);
arr.unshift(0);

println arr.toString(); // [ 0 , 5 , 10 , 20 ]
````

---

## Examples / static methods

### isArray

- Returns `true` if the given value is an array
- Returns `false` otherwise  

````ysharp
var arr = [1, 2, 3];

println Array.isArray(arr); // true
println Array.isArray(10);      // false
println Array.isArray("hello"); // false
println Array.isArray(null);    // false
````


### of
- Creates a new array from the given arguments
- Accepts variable number of values

````ysharp
var arr = Array.of(1, 2, 3);

println arr.toString(); // [ 1 , 2 , 3 ]
var arr = Array.of("a", "b", "c");

println arr.toString(); // [ a , b , c ]
var arr = Array.of();

println arr.toString(); // [ ]
var obj = { "value": 10 };

var arr = Array.of(obj, obj);

println arr.toString(); // [ {value:10} , {value:10} ]
````

### range

- Creates a sequence of numbers from start to end (exclusive)
- Optional step parameter (default = 1)
- Supports negative step


````ysharp
var arr = Array.range(0, 5);

println arr.toString(); // [ 0 , 1 , 2 , 3 , 4 ]
var arr = Array.range(1, 10, 2);

println arr.toString(); // [ 1 , 3 , 5 , 7 , 9 ]
var arr = Array.range(5, 0, -1);

println arr.toString(); // [ 5 , 4 , 3 , 2 , 1 ]
try do
    Array.range(0, 10, 0);
end
catch(e) do
    println "step cannot be zero";
end
````

