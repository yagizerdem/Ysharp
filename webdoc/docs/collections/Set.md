---
sidebar_position: 6
---


## General


A collection that contains no duplicate elements. More formally ,
sets contain no pair of elements e1 and e2 such that e1.equals(e2), 
and at most one null element. As implied by its name, this class models the mathematical set abstraction.


```ysharp
var set = new Set();
````

Creates a new, empty Set instance.


## Reference

### Instance methods

| Method              | Signature                            | Return Type | Description                                                           |
|---------------------|--------------------------------------|-------------|-----------------------------------------------------------------------|
| `add / set`         | `set.add(value : any)`               | `bool`      | Adds a value to the set if not already present; returns size if added |
| `remove`            | `set.remove(value : any)`            | `bool`      | Removes the specified value; returns true if the value existed        |
| `contains`          | `set.contains(value : any)`          | `bool`      | Returns true if the value exists in the set                           |
| `clear`             | `set.clear()`                        | `void`      | Removes all elements from the set                                     |
| `size`              | `set.size()`                         | `int`       | Returns the number of elements in the set                             |
| `clone`             | `set.clone()`                        | `Set`       | Returns a shallow copy of the set                                     |
| `union`             | `set.union(other : Set)`             | `Set`       | Returns a new set containing all elements from both sets              |
| `intersection`      | `set.intersection(other : Set)`      | `Set`       | Returns a new set with elements common to both sets                   |
| `difference`        | `set.difference(other : Set)`        | `Set`       | Returns a new set with elements in this set but not in the other      |
| `isSubsetOf`        | `set.isSubsetOf(other : Set)`        | `bool`      | Returns true if this set is a subset of the other                     |
| `isSupersetOf`      | `set.isSupersetOf(other : Set)`      | `bool`      | Returns true if this set is a superset of the other                   |
| `equals`            | `set.equals(other : Set)`            | `bool`      | Returns true if both sets contain exactly the same elements           |
| `isEmpty` / `empty` | `set.isEmpty()`                      | `bool`      | Returns true if the set is empty                                      |
| `toString`          | `set.toString()`                     | `string`    | Returns a string representation of the set                            |


## Examples

### add
#### `add(value : any) : bool` 

- Adds a value to the set if not already present; returns size if added

````ysharp
var set = new Set();

// basic insert
println set.add(10);   // 1
println set.add(20);   // 2

// duplicate insert
println set.add(10);   // 2

// mixed types
set.add("hello");
set.add(true);

println set.size();    // 4

// chaining behavior test (not actual chaining)
var result = set.add(30);
println result;        // 5
````

### remove
#### `remove(value : any) : bool`

- 	Removes the specified value; returns true if the value existed

````ysharp
### Examples

### `remove(value : any) : bool`

```ysharp
var set = new Set();

// initial data
set.add(10);
set.add(20);
set.add(30);

// remove existing element
println set.remove(20);   // true
println set.size();       // 2
println set.contains(20); // false

// remove non-existing element
println set.remove(999);  // false

// remove remaining elements
set.remove(10);
set.remove(30);

println set.size();       // 0
println set.empty();      // true

// remove from empty set
println set.remove(1);    // false
````

### contains
#### `contains(value : any) : bool`

- Returns true if the value exists in the set

```ysharp
var set = new Set();

// initial data
set.add(10);
set.add(20);
set.add(30);

// existing values
println set.contains(10);  // true
println set.contains(20);  // true

// non-existing value
println set.contains(999); // false

// mixed types
set.add("hello");
println set.contains("hello"); // true
println set.contains("world"); // false

// after removal
set.remove(10);
println set.contains(10); // false
````


### clear
#### `clear() : void`

- 	Removes all elements from the set

```ysharp
var set = new Set();

// initial data
set.add(10);
set.add(20);
set.add(30);

println set.size(); // 3

// clear all elements
set.clear();

println set.size();  // 0
println set.empty(); // true

// operations after clear
println set.contains(10); // false

// add again after clear
set.add(100);
println set.size();  // 1
println set.contains(100); // true
````


### size
#### `size() : int`

- Returns the number of elements in the set

```ysharp
var set = new Set();

// empty set
println set.size(); // 0

// add elements
set.add(10);
set.add(20);
set.add(30);

println set.size(); // 3

// duplicate insert (should not change size)
set.add(20);
println set.size(); // 3

// remove element
set.remove(10);
println set.size(); // 2

// clear set
set.clear();
println set.size(); // 0
````

### clone
#### `clone() : Set`

- Returns a shallow copy of the set

```ysharp
var set1 = new Set();

// initial data
set1.add(10);
set1.add(20);
set1.add(30);

// clone
var set2 = set1.clone();

println set1.size(); // 3
println set2.size(); // 3

// verify contents
println set2.contains(10); // true
println set2.contains(20); // true
println set2.contains(30); // true

// modify original
set1.add(40);
set1.remove(10);

// cloned set should NOT change
println set1.size(); // 3
println set2.size(); // 3

println set2.contains(10); // true
println set2.contains(40); // false

// modify clone
set2.add(100);

// original should NOT change
println set1.contains(100); // false
println set2.contains(100); // true
````

### union
#### `union(other : Set) : Set`

- Returns a new set containing all elements from both sets

```ysharp
var a = new Set();
a.add(1);
a.add(2);
a.add(3);

var b = new Set();
b.add(3);
b.add(4);
b.add(5);

// union
var u = a.union(b);

// result should contain all unique elements
println u.size(); // 5

println u.contains(1); // true
println u.contains(2); // true
println u.contains(3); // true
println u.contains(4); // true
println u.contains(5); // true

// original sets should NOT change
println a.size(); // 3
println b.size(); // 3

// union with empty set
var empty = new Set();

var u2 = a.union(empty);

println u2.size(); // 3
println u2.contains(1); // true
println u2.contains(2); // true
println u2.contains(3); // true
````

### intersection
#### `intersection(other : Set) : Set`

- Returns a new set with elements common to both sets

```ysharp
var a = new Set();
a.add(1);
a.add(2);
a.add(3);

var b = new Set();
b.add(2);
b.add(3);
b.add(4);

// intersection
var i = a.intersection(b);

// should contain only common elements
println i.size(); // 2

println i.contains(2); // true
println i.contains(3); // true

// non-common elements
println i.contains(1); // false
println i.contains(4); // false

// original sets should NOT change
println a.size(); // 3
println b.size(); // 3

// intersection with empty set
var empty = new Set();

var i2 = a.intersection(empty);

println i2.size(); // 0
println i2.empty(); // true
````

### difference
#### `difference(other : Set) : Set`

- Returns a new set with elements in this set but not in the other

```ysharp
var a = new Set();
a.add(1);
a.add(2);
a.add(3);

var b = new Set();
b.add(2);
b.add(4);

// difference (a - b)
var d = a.difference(b);

// should contain elements in a but not in b
println d.size(); // 2

println d.contains(1); // true
println d.contains(3); // true

// excluded elements
println d.contains(2); // false
println d.contains(4); // false

// original sets should NOT change
println a.size(); // 3
println b.size(); // 2

// difference with empty set
var empty = new Set();

var d2 = a.difference(empty);

println d2.size(); // 3
println d2.contains(1); // true
println d2.contains(2); // true
println d2.contains(3); // true

// reverse difference (empty - a)
var d3 = empty.difference(a);

println d3.size(); // 0
println d3.empty(); // true
````

### isSubsetOf
#### `isSubsetOf(other : Set) : bool`

- Returns true if this set is a subset of the other

```ysharp
var a = new Set();
a.add(1);
a.add(2);

var b = new Set();
b.add(1);
b.add(2);
b.add(3);

// a ⊆ b
println a.isSubsetOf(b); // true

// b ⊆ a
println b.isSubsetOf(a); // false

// identical sets
var c = new Set();
c.add(1);
c.add(2);

println a.isSubsetOf(c); // true
println c.isSubsetOf(a); // true

// subset with missing element
c.add(5);
println a.isSubsetOf(c); // true
println c.isSubsetOf(a); // false

// empty set is subset of any set
var empty = new Set();

println empty.isSubsetOf(a); // true
println empty.isSubsetOf(empty); // true

// non-empty set is not subset of empty set
println a.isSubsetOf(empty); // false
````

### isSupersetOf
#### `isSupersetOf(other : Set) : bool`

- Returns true if this set is a superset of the other

```ysharp
var a = new Set();
a.add(1);
a.add(2);
a.add(3);

var b = new Set();
b.add(1);
b.add(2);

// a ⊇ b
println a.isSupersetOf(b); // true

// b ⊇ a
println b.isSupersetOf(a); // false

// identical sets
var c = new Set();
c.add(1);
c.add(2);
c.add(3);

println a.isSupersetOf(c); // true
println c.isSupersetOf(a); // true

// superset with extra element
a.add(4);
println a.isSupersetOf(c); // true

// empty set cases
var empty = new Set();

println a.isSupersetOf(empty); // true
println empty.isSupersetOf(a); // false
println empty.isSupersetOf(empty); // true
````

### equals
#### `equals(other : Set) : bool`

- Returns true if both sets contain exactly the same elements

```ysharp
var a = new Set();
a.add(1);
a.add(2);
a.add(3);

var b = new Set();
b.add(3);
b.add(2);
b.add(1);

// same elements, different order
println a.equals(b); // true

// different elements
b.add(4);
println a.equals(b); // false

// identical sets
var c = new Set();
c.add(1);
c.add(2);
c.add(3);

println a.equals(c); // true

// missing element
c.remove(3);
println a.equals(c); // false

// empty sets
var empty1 = new Set();
var empty2 = new Set();

println empty1.equals(empty2); // true

// mixed types
var d = new Set();
d.add(10);
d.add("hello");

var e = new Set();
e.add("hello");
e.add(10);

println d.equals(e); // true
````

### isEmpty / empty
#### `isEmpty() : bool`

- Returns true if the set is empty

```ysharp
var set = new Set();

// empty set
println set.isEmpty(); // true
println set.empty();   // true

// add element
set.add(10);

println set.isEmpty(); // false
println set.empty();   // false

// remove element
set.remove(10);

println set.isEmpty(); // true
println set.empty();   // true

// multiple operations
set.add(1);
set.add(2);
set.clear();

println set.isEmpty(); // true
println set.empty();   // true
````

### toString
#### `toString() : string`

- Returns a string representation of the set

```ysharp
var set = new Set();

// empty set
println set.toString(); // []

// add elements
set.add(10);
set.add(20);
set.add(30);

println set.toString(); // [10, 20, 30] (order may vary)

// duplicate insert (should not change)
set.add(20);
println set.toString(); // [10, 20, 30]

// mixed types
set.add("hello");
set.add(true);

println set.toString(); // [10, 20, 30, "hello", true] (order may vary)

// after removal
set.remove(20);
println set.toString(); // [10, 30, "hello", true] (order may vary)

// after clear
set.clear();
println set.toString(); // []
````
