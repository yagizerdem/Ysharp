---
sidebar_position: 7
---

## General

Resizable-array implementation. Array deques have no capacity restrictions; they grow as necessary to 
support usage. They are not thread-safe; in the absence of external synchronization, 
they do not support concurrent access by multiple threads. 
Null elements are prohibited. This class is likely to be faster than Stack when
used as a stack, and faster than LinkedList when used as a queue.
Most ArrayDeque operations run in amortized constant time. 
Exceptions include remove, removeFirstOccurrence, removeLastOccurrence, contains, and the bulk operations,
all of which run in linear time.


`ArrayDeque` extends the vector protocol, meaning it also supports iteration via `foreach`.

```ysharp
var queue = new ArrayDeque();
````

Creates a new, empty ArrayDeque instance.


## Reference

### Instance methods

| Method                             | Signature                                        | Return Type   | Description                                                            |
|------------------------------------|--------------------------------------------------|---------------|------------------------------------------------------------------------|
| `addFirst`                         | `deque.addFirst(value : any)`                    | `void`        | Inserts a value at the front of the deque                              |
| `addLast`                          | `deque.addLast(value : any)`                     | `void`        | Inserts a value at the end of the deque                                |
| `removeFirst`                      | `deque.removeFirst()`                            | `any`         | Removes and returns the first element; throws if empty                 |
| `removeLast`                       | `deque.removeLast()`                             | `any`         | Removes and returns the last element; throws if empty                  |
| `peekFirst`                        | `deque.peekFirst()`                              | `any`         | Returns the first element without removing it; throws if empty         |
| `peekLast`                         | `deque.peekLast()`                               | `any`         | Returns the last element without removing it; throws if empty          |
| `contains`                         | `deque.contains(value : any)`                    | `bool`        | Returns true if the deque contains the given value                     |
| `removeFirstOccurrence`            | `deque.removeFirstOccurrence(value : any)`       | `bool`        | Removes the first occurrence of the given value; returns true if found |
| `removeLastOccurrence`             | `deque.removeLastOccurrence(value : any)`        | `bool`        | Removes the last occurrence of the given value; returns true if found  |
| `size`                             | `deque.size()`                                   | `int`         | Returns the number of elements in the deque                            |
| `isEmpty`                          | `deque.isEmpty()`                                | `bool`        | Returns true if the deque is empty                                     |
| `clear`                            | `deque.clear()`                                  | `void`        | Removes all elements from the deque                                    |
| `toArray`                          | `deque.toArray()`                                | `Array`       | Returns an array containing all elements from front to back            |
| `clone`                            | `deque.clone()`                                  | `ArrayDeque`  | Returns a shallow copy of the deque                                    |
| `toString`                         | `deque.toString()`                               | `string`      | Returns a string representation of the deque                           |


## Examples

### addFirst
#### `addFirst(value : any) : void`

- Inserts a value at the front of the deque

```ysharp
var d = new ArrayDeque();

// basic insert
d.addFirst(10);
println d.toString(); // [10]

// multiple inserts (LIFO at front)
d.addFirst(20);
d.addFirst(30);

println d.toString(); // [30, 20, 10]

// mix with addLast
d.addLast(40);
println d.toString(); // [30, 20, 10, 40]

// duplicates allowed
d.addFirst(30);
println d.toString(); // [30, 30, 20, 10, 40]

// mixed types
d.addFirst("hello");
d.addFirst(true);

println d.toString(); // [true, "hello", 30, 30, 20, 10, 40]

// verify front element
println d.peekFirst(); // true
````

### addLast
#### `addLast(value : any) : void`

- Inserts a value at the end of the deque

```ysharp
var d = new ArrayDeque();

// basic insert
d.addLast(10);
println d.toString(); // [10]

// multiple inserts (FIFO at end)
d.addLast(20);
d.addLast(30);

println d.toString(); // [10, 20, 30]

// mix with addFirst
d.addFirst(5);
println d.toString(); // [5, 10, 20, 30]

// duplicates allowed
d.addLast(30);
println d.toString(); // [5, 10, 20, 30, 30]

// mixed types
d.addLast("hello");
d.addLast(true);

println d.toString(); // [5, 10, 20, 30, 30, "hello", true]

// verify last element
println d.peekLast(); // true
````

### removeFirst
#### `removeFirst() : any`

- Removes and returns the first element; throws if empty

```ysharp
var d = new ArrayDeque();

// empty case (should throw)
try do
    d.removeFirst();
    Assert.fail("removeFirst on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
d.addLast(10);
d.addLast(20);
d.addLast(30);

println d.removeFirst(); // 10
println d.toString();    // [20, 30]

// multiple removals
println d.removeFirst(); // 20
println d.removeFirst(); // 30

println d.toString();    // []

// mix with addFirst
d.addFirst(1);
d.addFirst(2);
d.addLast(3);

println d.removeFirst(); // 2
println d.removeFirst(); // 1
println d.removeFirst(); // 3

// duplicates
d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.removeFirst(); // 5
println d.removeFirst(); // 5
println d.removeFirst(); // 5
````

### removeLast
#### `removeLast() : any`

- Removes and returns the last element; throws if empty

```ysharp
var d = new ArrayDeque();

// empty case (should throw)
try do
    d.removeLast();
    Assert.fail("removeLast on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
d.addLast(10);
d.addLast(20);
d.addLast(30);

println d.removeLast(); // 30
println d.toString();   // [10, 20]

// multiple removals
println d.removeLast(); // 20
println d.removeLast(); // 10

println d.toString();   // []

// mix with addFirst
d.addFirst(1);
d.addFirst(2);
d.addLast(3);

println d.removeLast(); // 3
println d.removeLast(); // 1
println d.removeLast(); // 2

// duplicates
d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.removeLast(); // 5
println d.removeLast(); // 5
println d.removeLast(); // 5
````

### peekFirst
#### `peekFirst() : any`

- Returns the first element without removing it; throws if empty

```ysharp
var d = new ArrayDeque();

// empty case (should throw)
try do
    d.peekFirst();
    Assert.fail("peekFirst on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
d.addLast(10);
d.addLast(20);
d.addLast(30);

println d.peekFirst(); // 10
println d.toString();  // [10, 20, 30]

// no removal effect
println d.peekFirst(); // 10
println d.size();      // 3

// mix with addFirst
d.addFirst(5);
println d.peekFirst(); // 5

// duplicates
d.clear();

d.addLast(7);
d.addLast(7);
d.addLast(7);

println d.peekFirst(); // 7

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.peekFirst(); // 10
````

### peekLast
#### `peekLast() : any`

- Returns the last element without removing it; throws if empty

```ysharp
var d = new ArrayDeque();

// empty case (should throw)
try do
    d.peekLast();
    Assert.fail("peekLast on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
d.addLast(10);
d.addLast(20);
d.addLast(30);

println d.peekLast(); // 30
println d.toString(); // [10, 20, 30]

// no removal effect
println d.peekLast(); // 30
println d.size();     // 3

// mix with addFirst
d.addFirst(5);
println d.peekLast(); // 30

// duplicates
d.clear();

d.addLast(7);
d.addLast(7);
d.addLast(7);

println d.peekLast(); // 7

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.peekLast(); // true
````

### contains
#### `contains(value : any) : bool`

- Returns true if the deque contains the given value

```ysharp
var d = new ArrayDeque();

// empty
println d.contains(10); // false

// basic usage
d.addLast(10);
d.addLast(20);
d.addLast(30);

println d.contains(10); // true
println d.contains(20); // true
println d.contains(999); // false

// order does not matter
println d.contains(30); // true

// after removal
d.removeFirst(); // removes 10
println d.contains(10); // false

// duplicates
d.clear();

d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.contains(5); // true

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.contains(10);      // true
println d.contains("hello"); // true
println d.contains(true);    // true
println d.contains(false);   // false
````

### removeFirstOccurrence
#### `removeFirstOccurrence(value : any) : bool`

- Removes the first occurrence of the given value; returns true if found

```ysharp
var d = new ArrayDeque();

// empty
println d.removeFirstOccurrence(10); // false

// basic usage
d.addLast(1);
d.addLast(2);
d.addLast(3);
d.addLast(2);
d.addLast(4);

println d.toString(); // [1, 2, 3, 2, 4]

// remove first occurrence of 2
println d.removeFirstOccurrence(2); // true
println d.toString();               // [1, 3, 2, 4]

// remove non-existing
println d.removeFirstOccurrence(999); // false

// remove head
println d.removeFirstOccurrence(1); // true
println d.toString();               // [3, 2, 4]

// remove tail
println d.removeFirstOccurrence(4); // true
println d.toString();               // [3, 2]

// duplicates
d.clear();

d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.removeFirstOccurrence(5); // true
println d.toString();               // [5, 5]

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.removeFirstOccurrence("hello"); // true
println d.toString();                    // [10, true]
````

### removeLastOccurrence
#### `removeLastOccurrence(value : any) : bool`

- Removes the last occurrence of the given value; returns true if found

```ysharp
var d = new ArrayDeque();

// empty
println d.removeLastOccurrence(10); // false

// basic usage
d.addLast(1);
d.addLast(2);
d.addLast(3);
d.addLast(2);
d.addLast(4);

println d.toString(); // [1, 2, 3, 2, 4]

// remove last occurrence of 2
println d.removeLastOccurrence(2); // true
println d.toString();              // [1, 2, 3, 4]

// remove non-existing
println d.removeLastOccurrence(999); // false

// remove tail
println d.removeLastOccurrence(4); // true
println d.toString();              // [1, 2, 3]

// remove head
println d.removeLastOccurrence(1); // true
println d.toString();              // [2, 3]

// duplicates
d.clear();

d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.removeLastOccurrence(5); // true
println d.toString();              // [5, 5]

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.removeLastOccurrence("hello"); // true
println d.toString();                   // [10, true]
````


### size
#### `size() : int`

- Returns the number of elements in the deque

```ysharp
var d = new ArrayDeque();

// empty
println d.size(); // 0

// basic usage
d.addLast(10);
println d.size(); // 1

d.addFirst(20);
println d.size(); // 2

d.addLast(30);
println d.size(); // 3

// after remove
d.removeFirst();
println d.size(); // 2

d.removeLast();
println d.size(); // 1

// duplicates
d.clear();

d.addLast(5);
d.addLast(5);
d.addLast(5);

println d.size(); // 3

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.size(); // 3

// after clear
d.clear();
println d.size(); // 0
````


### isEmpty
#### `isEmpty() : bool`

- Returns true if the deque is empty

```ysharp
var d = new ArrayDeque();

// empty
println d.isEmpty(); // true

// after add
d.addLast(10);
println d.isEmpty(); // false

// after remove
d.removeFirst();
println d.isEmpty(); // true

// multiple elements
d.addLast(1);
d.addLast(2);
d.addFirst(3);

println d.isEmpty(); // false

// remove all
d.removeFirst();
d.removeLast();
d.removeFirst();

println d.isEmpty(); // true

// clear
d.addLast(100);
d.addLast(200);

d.clear();
println d.isEmpty(); // true
````


### clear
#### `clear() : void`

- Removes all elements from the deque

```ysharp
var d = new ArrayDeque();

// empty clear
d.clear();
println d.isEmpty(); // true

// basic usage
d.addLast(10);
d.addLast(20);
d.addFirst(5);

println d.toString(); // [5, 10, 20]

// clear all
d.clear();

println d.isEmpty(); // true
println d.size();    // 0
println d.toString(); // []

// reuse after clear
d.addLast(100);
println d.toString(); // [100]

// multiple clears
d.clear();
d.clear();

println d.isEmpty(); // true

// mixed types
d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.toString(); // [10, "hello", true]

d.clear();

println d.isEmpty(); // true
println d.size();    // 0
````


### toArray
#### `toArray() : Array`

- Returns an array containing all elements from front to back

```ysharp
var d = new ArrayDeque();

// empty
var arr = d.toArray();
println arr.size(); // 0

// basic usage
d.addLast(1);
d.addLast(2);
d.addLast(3);

var arr = d.toArray();

println arr.size();  // 3
println arr.get(0);  // 1
println arr.get(1);  // 2
println arr.get(2);  // 3

// addFirst effect
d.clear();

d.addLast(10);
d.addLast(20);
d.addFirst(5);

var arr = d.toArray();

println arr.get(0); // 5
println arr.get(1); // 10
println arr.get(2); // 20

// after remove
d.removeFirst();

var arr = d.toArray();

println arr.get(0); // 10
println arr.get(1); // 20

// duplicates
d.clear();

d.addLast(7);
d.addLast(7);
d.addLast(7);

var arr = d.toArray();

println arr.size(); // 3

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

var arr = d.toArray();

println arr.get(0); // 10
println arr.get(1); // "hello"
println arr.get(2); // true

// independence check
var arr = d.toArray();

d.addLast(999);

println arr.size(); // 3 (should not change)
````

### clone
#### `clone() : ArrayDeque`

- Returns a shallow copy of the deque

```ysharp
var d1 = new ArrayDeque();

// empty clone
var d2 = d1.clone();

println d2.isEmpty(); // true
println d2.size();    // 0

// basic usage
d1.addLast(1);
d1.addLast(2);
d1.addLast(3);

var d2 = d1.clone();

println d1.toString(); // [1, 2, 3]
println d2.toString(); // [1, 2, 3]

// independence
d1.addLast(4);

println d1.toString(); // [1, 2, 3, 4]
println d2.toString(); // [1, 2, 3]

// modify clone
d2.addFirst(0);

println d2.toString(); // [0, 1, 2, 3]
println d1.toString(); // [1, 2, 3, 4]

// order preserved
println d2.removeFirst(); // 0
println d2.removeFirst(); // 1
println d2.removeFirst(); // 2
println d2.removeFirst(); // 3

println d2.isEmpty(); // true

// duplicates
d1.clear();

d1.addLast(5);
d1.addLast(5);
d1.addLast(5);

var d2 = d1.clone();

println d2.toString(); // [5, 5, 5]

// mixed types
d1.clear();

d1.addLast(10);
d1.addLast("hello");
d1.addLast(true);

var d2 = d1.clone();

println d2.toString(); // [10, "hello", true]
````

### toString
#### `toString() : string`

- Returns a string representation of the deque

```ysharp
var d = new ArrayDeque();

// empty
println d.toString(); // []

// basic usage
d.addLast(1);
d.addLast(2);
d.addLast(3);

println d.toString(); // [1, 2, 3]

// addFirst effect
d.addFirst(0);
println d.toString(); // [0, 1, 2, 3]

// after remove
d.removeLast();
println d.toString(); // [0, 1, 2]

// duplicates
d.clear();

d.addLast(7);
d.addLast(7);
d.addLast(7);

println d.toString(); // [7, 7, 7]

// mixed types
d.clear();

d.addLast(10);
d.addLast("hello");
d.addLast(true);

println d.toString(); // [10, "hello", true]

// clear
d.clear();
println d.toString(); // []
````

