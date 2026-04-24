---
sidebar_position: 2
---

## General

A HashMap is a part of Ysharp’s Collection Framework. 
It stores elements in key-value pairs, where, Keys are unique. and Values can be duplicated.

- Internally uses Hashing, hence allows efficient key-based retrieval, insertion, and removal with an average of O(1) time.
- HashMap is not thread-safe.
- Insertion order is not preserved in HashMap. To preserve the insertion order, LinkedHashMap is used and to maintain sorted order, TreeMap is used.
- HashMap allows one null key and multiple null values. If a null key is added multiple times, it overwrites the previous value.

## Reference

### Instance methods

| Method             | Signature                                                  | Return Type | Description                                                                                                                                                                                                                                   |
|--------------------|------------------------------------------------------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `clear`            | `map.clear()`                                              | `null`      | Removes all key-value pairs from the map                                                                                                                                                                                                      |
| `clone`            | `map.clone()`                                              | `HashMap`   | Returns a shallow copy of the map; key-value pairs are copied by reference                                                                                                                                                                    |
| `compute`          | `map.compute(key : any, callback : function)`              | `any`       | Computes a new value for the given key using the callback `(key?, oldValue?)`; updates or removes the entry based on the result, if key not exists new key value pair is created; callback accepts parameters `(key? : any, oldValue? : any)` |
| `computeIfAbsent`  | `map.computeIfAbsent(key : any, callback : function)`      | `any`       | Computes and inserts a value using the callback `(key?)` only if the key is not already present; returns the existing or computed value; callback accepts parameters `(key? : any)`                                                           |
| `computeIfPresent` | `map.computeIfPresent(key : any, callback : function)`     | `any`       | Computes a new value using `(key?, existingValue?)` only if the key exists; updates or removes the entry based on the result; callback accepts parameters `(key? : any, existingValue? : any)`                                                |
| `containsKey`      | `map.containsKey(key : any)`                               | `bool`      | Returns true if the map contains the specified key                                                                                                                                                                                            |
| `containsValue`    | `map.containsValue(value : any)`                           | `bool`      | Returns true if the map contains the specified value                                                                                                                                                                                          |
| `entries`          | `map.entries()`                                            | `Array`     | Returns an array of `[key, value]` pairs representing all entries in the map                                                                                                                                                                  |
| `get`              | `map.get(key : any)`                                       | `any`       | Returns the value associated with the given key, or null if the key does not exist                                                                                                                                                            |
| `getOrDefault`     | `map.getOrDefault(key : any, defaultValue : any)`          | `any`       | Returns the value for the given key, or the provided default value if the key does not exist                                                                                                                                                  |
| `isEmpty`          | `map.isEmpty()`                                            | `bool`      | Returns true if the map contains no key-value pairs                                                                                                                                                                                           |
| `keys`             | `map.keys()`                                               | `Array`     | Returns an array containing all keys in the map                                                                                                                                                                                               |
| `merge`            | `map.merge(key : any, value : any, callback : function)`   | `any`       | Merges the given value with the existing value using `(existing?, value?)`; inserts, updates, or removes the entry based on the result; callback accepts parameters `(existingValue? : any, newValue? : any)`                                 |
| `put / add`        | `map.put(key : any, value : any)`                          | `any`       | Associates the given value with the key and returns the previous value, or null if none existed                                                                                                                                               |
| `putIfAbsent`      | `map.putIfAbsent(key : any, value : any)`                  | `any`       | Inserts the value only if the key is not already present; returns the existing value or null if inserted                                                                                                                                      |
| `remove`           | `map.remove(key : any)`                                    | `any`       | Removes the entry associated with the key and returns its value, or null if the key does not exist                                                                                                                                            |
| `replace`          | `map.replace(key : any, value : any)`                      | `any`       | Replaces the value for the given key if it exists and returns the previous value, or null if the key does not exist                                                                                                                           |
| `size`             | `map.size()`                                               | `int`       | Returns the number of key-value pairs in the map                                                                                                                                                                                              |
| `toString`         | `map.toString()`                                           | `string`    | Returns a string representation of the map in `{key=value}` format                                                                                                                                                                            |
| `values`           | `map.values()`                                             | `Array`     | Returns an array containing all values in the map                                                                                                                                                                                             |



### Examples

### clear

- Removes all key-value pairs from the map

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);

println map.size(); // 2

// clear all entries
map.clear();

println map.size();   // 0
println map.isEmpty(); // true

// accessing removed keys
println map.get("a"); // null
````

### clone

- Returns a shallow copy of the map; key-value pairs are copied by reference

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);

// clone the map
var cloned = map.clone();

println cloned.get("a"); // 10
println cloned.get("b"); // 20

// modify cloned map
cloned.put("a", 999);

// original map is NOT affected (structure-wise)
println map.get("a");    // 10
println cloned.get("a"); // 999
````

### compute

- Computes a new value for the given key using the callback `(key, oldValue)`; updates or removes the entry based on the result; if the key does not exist, a new key-value pair is created

````ysharp
var map = {};

map.put("a", 10);

// update existing value
map.compute("a", (key, oldValue) => oldValue + 5);

println map.get("a"); // 15

// create new key (oldValue = null)
map.compute("b", (key, oldValue) => 100);

println map.get("b"); // 100

// remove entry (return null)
map.compute("a", (key, oldValue) => null);

println map.get("a"); // null
`````

### computeIfAbsent

- Computes and inserts a value using the callback `(key)` only if the key is not already present; returns the existing or computed value; if callback returns null, entry is NOT inserted

````ysharp
var map = {};

// key does not exist   value is computed and inserted
var value1 = map.computeIfAbsent("a", (key) => 10);

println value1;        // 10
println map.get("a"); // 10


// key exists   callback is NOT executed
var value2 = map.computeIfAbsent("a", (key) => 999);

println value2;        // 10
println map.get("a"); // 10


// computing value based on key
var value3 = map.computeIfAbsent("hello", (key) => key.length());

println value3;            // 5
println map.get("hello"); // 5


// lazy evaluation (only runs once)
var v1 = map.computeIfAbsent("x", (key) => do
    println "computing...";
    return 42;
end);

var v2 = map.computeIfAbsent("x", (key) => do
    println "SHOULD NOT RUN";
    return 999;
end);

println v1; // 42
println v2; // 42


// if callback returns null   NOT inserted into map
var v3 = map.computeIfAbsent("y", (key) => null);

println v3;        // null
println map.get("y"); // null


// callback can also have no parameters
map.computeIfAbsent("z", () => 100);

println map.get("z"); // 100
````


### computeIfPresent

- Computes a new value using `(key, existingValue)` only if the key exists; updates or removes the entry based on the result; if the callback returns null, the entry is removed


````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   value is updated
var value1 = map.computeIfPresent("a", (key, value) => value + 5);

println value1;        // 15
println map.get("a"); // 15


// key does NOT exist   callback is NOT executed
var value2 = map.computeIfPresent("c", (key, value) => 999);

println value2;        // null
println map.get("c"); // null


// computing using both key and existing value
var value3 = map.computeIfPresent("b", (key, value) => key.length() + value);

println value3;        // 21  ("b".length() = 1   1 + 20)
println map.get("b"); // 21


// if callback returns null   entry is REMOVED
var value4 = map.computeIfPresent("a", (key, value) => null);

println value4;        // null
println map.get("a"); // null


// lazy behavior (only runs if key exists)
var v1 = map.computeIfPresent("b", (key, value) => do
    println "updating...";
    return value * 2;
end);

var v2 = map.computeIfPresent("x", (key, value) => do
    println "SHOULD NOT RUN";
    return 999;
end);

println v1; // 42
println v2; // null
````

### containsKey

- Returns true if the map contains the specified key

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   returns true
println map.containsKey("a"); // true
println map.containsKey("b"); // true


// key does NOT exist   returns false
println map.containsKey("c"); // false


// after removing a key
map.remove("a");

println map.containsKey("a"); // false


// null key support
map.put(null, 100);

println map.containsKey(null); // true


// value can be null but key still exists
map.put("x", null);

println map.containsKey("x"); // true
````

### containsValue

- Returns true if the map contains the specified value

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);
map.put("c", 10);


// value exists   returns true
println map.containsValue(10); // true
println map.containsValue(20); // true


// duplicate values are allowed
println map.containsValue(10); // true


// value does NOT exist   returns false
println map.containsValue(999); // false


// null value support
map.put("x", null);

println map.containsValue(null); // true


// after removing value
map.remove("b");

println map.containsValue(20); // false
````

### entries

- Returns an array of `[key, value]` pairs representing all entries in the map

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);
map.put("c", 30);


// get all entries
var entries = map.entries();

println entries;
// example output: [ ["a", 10], ["b", 20], ["c", 30] ]


// iterate over entries
foreach var entry in entries do
    var key = entry.get(0);
    var value = entry.get(1);

    println key + " -> " + value;
end


// empty map   returns empty array
var emptyMap = {};

println emptyMap.entries(); // []


// null values are preserved
map.put("x", null);

var e2 = map.entries();

foreach var entry in e2 do
    println entry.get(0) + " -> " + entry.get(1);
end
// includes: "x -> null"
````

### get

- Returns the value associated with the given key, or null if the key does not exist

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   returns value
println map.get("a"); // 10
println map.get("b"); // 20


// key does NOT exist   returns null
println map.get("c"); // null


// null key support
map.put(null, 100);

println map.get(null); // 100


// value can be null
map.put("x", null);

println map.get("x"); // null


// after removing key
map.remove("a");

println map.get("a"); // null
````

### getOrDefault

- Returns the value for the given key, or the provided default value if the key does not exist

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   returns actual value
println map.getOrDefault("a", 999); // 10
println map.getOrDefault("b", 999); // 20


// key does NOT exist   returns default value
println map.getOrDefault("c", 999); // 999


// default value is NOT inserted into the map
println map.get("c"); // null


// null key support
map.put(null, 100);

println map.getOrDefault(null, 999); // 100


// value can be null   still returned as null (default is NOT used)
map.put("x", null);

println map.getOrDefault("x", 999); // null


// after removing key   default is used
map.remove("a");

println map.getOrDefault("a", 555); // 555
````

### isEmpty

- Returns true if the map contains no key-value pairs

````ysharp
var map = {};


// empty map   true
println map.isEmpty(); // true


// after adding elements   false
map.put("a", 10);

println map.isEmpty(); // false


// adding more elements
map.put("b", 20);

println map.isEmpty(); // false


// after removing one element   still not empty
map.remove("a");

println map.isEmpty(); // false


// after clearing all elements   true
map.clear();

println map.isEmpty(); // true
````

### keys

- Returns an array containing all keys in the map


````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);
map.put("c", 30);


// get all keys
var keys = map.keys();

println keys;
// example output: ["a", "b", "c"]


// iterate over keys
foreach var key in keys do
    println key;
end


// empty map   returns empty array
var emptyMap = {};

println emptyMap.keys().toString(); // []


// null key support
map.put(null, 100);

var keys2 = map.keys();

println keys2.toString(); // includes null


// removing a key
map.remove("a");

println map.keys().toString(); // ["b", "c", null] (order not guaranteed)
````


### merge

- Merges the given value with the existing value using `(existingValue, newValue)`; inserts, updates, or removes the entry based on the result; if the callback returns null, the entry is removed


````ysharp
var map = {};


// key does NOT exist   value is inserted directly
var value1 = map.merge("a", 10, (oldVal, newVal) => oldVal + newVal);

println value1;        // 10
println map.get("a"); // 10


// key exists   callback is executed
var value2 = map.merge("a", 5, (oldVal, newVal) => oldVal + newVal);

println value2;        // 15
println map.get("a"); // 15


// overwrite using new value
var value3 = map.merge("a", 100, (oldVal, newVal) => newVal);

println value3;        // 100
println map.get("a"); // 100


// custom merge logic
map.put("b", 20);

var value4 = map.merge("b", 5, (oldVal, newVal) => oldVal * newVal);

println value4;        // 100
println map.get("b"); // 100


// if callback returns null   entry is REMOVED
var value5 = map.merge("b", 10, (oldVal, newVal) => null);

println value5;        // null
println map.get("b"); // null


// lazy behavior (callback only runs if key exists)
var v1 = map.merge("x", 50, (oldVal, newVal) => do
    println "merging...";
    return oldVal + newVal;
end);

var v2 = map.merge("x", 25, (oldVal, newVal) => do
    println "merging again...";
    return oldVal + newVal;
end);

println v1; // 50  (inserted directly, no callback)
println v2; // 75  (callback executed)
````


### put

- Associates the given value with the key and returns the previous value, or null if none existed

````ysharp
var map = {};


// inserting new key   returns null
var prev1 = map.put("a", 10);

println prev1;        // null
println map.get("a"); // 10


// updating existing key   returns previous value
var prev2 = map.put("a", 20);

println prev2;        // 10
println map.get("a"); // 20


// inserting multiple keys
map.put("b", 30);
map.put("c", 40);

println map.get("b"); // 30
println map.get("c"); // 40


// null key support
map.put(null, 100);

println map.get(null); // 100


// value can be null
var prev3 = map.put("x", null);

println prev3;        // null
println map.get("x"); // null


// overwriting null value
var prev4 = map.put("x", 999);

println prev4;        // null
println map.get("x"); // 999
````


### putIfAbsent

- Inserts the value only if the key is not already present; returns the existing value or null if inserted

````ysharp
var map = {};


// key does not exist   value is inserted, returns null
var prev1 = map.putIfAbsent("a", 10);

println prev1;        // null
println map.get("a"); // 10


// key exists   value is NOT replaced, returns existing value
var prev2 = map.putIfAbsent("a", 999);

println prev2;        // 10
println map.get("a"); // 10


// inserting multiple keys
map.putIfAbsent("b", 20);
map.putIfAbsent("c", 30);

println map.get("b"); // 20
println map.get("c"); // 30


// null key support
map.putIfAbsent(null, 100);

println map.get(null); // 100


// value can be null
var prev3 = map.putIfAbsent("x", null);

println prev3;        // null
println map.get("x"); // null


// existing null value   still considered "present"
var prev4 = map.putIfAbsent("x", 999);

println prev4;        // null
println map.get("x"); // null
````


### remove

- Removes the entry associated with the key and returns its value, or null if the key does not exist


````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   removed and value returned
var val1 = map.remove("a");

println val1;        // 10
println map.get("a"); // null


// key does NOT exist   returns null
var val2 = map.remove("c");

println val2; // null


// removing another key
var val3 = map.remove("b");

println val3;        // 20
println map.get("b"); // null


// null key support
map.put(null, 100);

var val4 = map.remove(null);

println val4;        // 100
println map.get(null); // null


// value can be null
map.put("x", null);

var val5 = map.remove("x");

println val5;        // null
println map.get("x"); // null
````

### replace

- Replaces the value for the given key if it exists and returns the previous value, or null if the key does not exist


````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);


// key exists   value is replaced, previous value returned
var prev1 = map.replace("a", 100);

println prev1;        // 10
println map.get("a"); // 100


// key does NOT exist   nothing happens, returns null
var prev2 = map.replace("c", 999);

println prev2;        // null
println map.get("c"); // null


// replacing another key
var prev3 = map.replace("b", 200);

println prev3;        // 20
println map.get("b"); // 200


// null key support
map.put(null, 50);

var prev4 = map.replace(null, 500);

println prev4;        // 50
println map.get(null); // 500


// value can be null
map.put("x", null);

var prev5 = map.replace("x", 999);

println prev5;        // null
println map.get("x"); // 999
````

### size

- Returns the number of key-value pairs in the map

````ysharp
var map = {};


// empty map   size is 0
println map.size(); // 0


// adding elements
map.put("a", 10);
map.put("b", 20);

println map.size(); // 2


// updating existing key   size does NOT change
map.put("a", 100);

println map.size(); // 2


// adding more keys
map.put("c", 30);

println map.size(); // 3


// removing a key   size decreases
map.remove("b");

println map.size(); // 2


// clearing map   size becomes 0
map.clear();

println map.size(); // 0
````

### toString

- Returns a string representation of the map in `{key=value}` format


````ysharp
var map = {};


// empty map
println map.toString(); // {}


// adding elements
map.put("a", 10);
map.put("b", 20);

println map.toString(); // {a=10, b=20}


// updating value
map.put("a", 100);

println map.toString(); // {a=100, b=20}


// null key and value support
map.put(null, 50);
map.put("x", null);

println map.toString(); // {a=100, b=20, null=50, x=null}


// removing elements
map.remove("b");

println map.toString(); // {a=100, null=50, x=null}


// order is not guaranteed
// output order may vary depending on hashing
````

### values

- Returns an array containing all values in the map

````ysharp
var map = {};

map.put("a", 10);
map.put("b", 20);
map.put("c", 30);


// get all values
var values = map.values();

println values;
// example output: [10, 20, 30]


// iterate over values
foreach var value in values do
    println value;
end


// empty map   returns empty array
var emptyMap = {};

println emptyMap.values().toString(); // []


// null values are included
map.put("x", null);

var values2 = map.values();

println values2.toString(); // includes null


// removing a value
map.remove("a");

println map.values().toString(); // [20, 30, null] (order not guaranteed)
````