---
sidebar_position: 3
---

# LinkedList

## General

`LinkedList` is a built-in collection class in Ysharp that implements a singly linked list. It provides efficient insertion and removal at both ends, making it well suited for queue and stack style operations.

> **Note** <br/>
> Unlike `Array`, `LinkedList` does not provide constant-time random access. 
> Accessing or modifying an element by index requires traversing the list from the head.


```ysharp
var list = new LinkedList();
```

Creates a new, empty `LinkedList` instance.

## Reference

### Instance methods

| Method        | Signature                             | Return Type  | Description                                          |
|---------------|---------------------------------------|--------------|------------------------------------------------------|
| `addFirst`    | `list.addFirst(value)`                | `null`       | Inserts a value at the beginning of the list         |
| `addLast`     | `list.addLast(value)`                 | `null`       | Inserts a value at the end of the list               |
| `removeFirst` | `list.removeFirst()`                  | `any`        | Removes and returns the first element                |
| `removeLast`  | `list.removeLast()`                   | `any`        | Removes and returns the last element                 |
| `peekFirst`   | `list.peekFirst()`                    | `any\|null`  | Returns the first element without removing it        |
| `peekLast`    | `list.peekLast()`                     | `any\|null`  | Returns the last element without removing it         |
| `get`         | `list.get(index : int)`               | `any`        | Returns the element at the given index               |
| `set`         | `list.set(index : int, value)`        | `null`       | Replaces the element at the given index              |
| `contains`    | `list.contains(value)`                | `bool`       | Returns true if the list contains the given value    |
| `indexOf`     | `list.indexOf(value)`                 | `int`        | Returns the index of the first occurrence, or -1     |
| `size`        | `list.size()`                         | `int`        | Returns the number of elements in the list           |
| `isEmpty`     | `list.isEmpty()`                      | `bool`       | Returns true if the list contains no elements        |
| `clear`       | `list.clear()`                        | `null`       | Removes all elements from the list                   |
| `toArray`     | `list.toArray()`                      | `Array`      | Returns a new Array containing all elements in order |
| `clone`       | `list.clone()`                        | `LinkedList` | Returns a shallow copy of the list                   |
| `toString`    | `list.toString()`                     | `string`     | Returns a string representation of the list          |


## Examples


### `addFirst(value : any)`

Inserts a value at the beginning of the list.

````ysharp
var list = new LinkedList();

list.addFirst(10);   // [10]
list.addFirst(20);   // [20,10]
list.addFirst(30);   // [30,20,10]

println list.get(0);   // 30
println list.get(1);   // 20
println list.get(2);   // 10
````

````ysharp
var list = new LinkedList();

for var i = 1; i <= 5; i++ do
    list.addFirst(i);
end

// list: [5,4,3,2,1]
println list.get(0);   // 5
println list.get(4);   // 1
````

### `addLast(value : any)`

Inserts a value at the end of the list.

````ysharp
var list = new LinkedList();

list.addLast(10);   // [10]
list.addLast(20);   // [10,20]
list.addLast(30);   // [10,20,30]

println list.get(2);   // 30
````

### `clear()`

Removes all elements from the list, making it empty.

````ysharp
var list = new LinkedList();

list.addLast(1);
list.addLast(2);
list.addLast(3);

println list.size();   // 3

list.clear();

println list.size();     // 0
println list.isEmpty();  // true
````

### `contains(value : any)`

Returns `true` if the list contains the given value, otherwise `false`.

```ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

println list.contains(20);   // true
println list.contains(99);   // false
````

### `get(index : int)`

Returns the element at the specified index.

```ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

println list.get(0);   // 10
println list.get(1);   // 20
println list.get(2);   // 30
````

````
var list = new LinkedList();

list.addLast(1);
list.addLast(2);
list.addLast(3);

list.set(1, 99);

println list.get(1);   // 99
````

````ysharp
var list = new LinkedList();

list.addLast(1);

try do
    list.get(5);
end
catch(e) do
    println e;   // LinkedList index out of bounds
end
````


### `indexOf(value : any)`

Returns the index of the first occurrence of the given value, or `-1` if not found.

```ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

println list.indexOf(10);   // 0
println list.indexOf(20);   // 1
println list.indexOf(30);   // 2
println list.indexOf(99);   // -1
````

````ysharp
var list = new LinkedList();

list.addLast(1);
list.addLast(2);
list.addLast(3);

list.removeFirst();   // [2,3]

println list.indexOf(1);   // -1
println list.indexOf(2);   // 0
````

### `isEmpty()`

Returns `true` if the list contains no elements, otherwise `false`.


````ysharp
var list = new LinkedList();

println list.isEmpty();   // true

list.addLast(10);

println list.isEmpty();   // false
````

````ysharp
var list = new LinkedList();

list.addLast(1);
list.addLast(2);

list.clear();

println list.isEmpty();   // true
````

### `peekFirst()`

Returns the first element of the list without removing it, or `null` if the list is empty.


````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);

println list.peekFirst();   // 10
println list.size();        // 2 (unchanged)
````

````ysharp
var list = new LinkedList();

println list.peekFirst();   // null
````


### `peekLast()`

Returns the last element of the list without removing it, or `null` if the list is empty.

````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);

println list.peekLast();   // 20
println list.size();       // 2 (unchanged)
````

````ysharp
var list = new LinkedList();

println list.peekLast();   // null
````

### `removeFirst()`

Removes and returns the first element of the list, or `null` if the list is empty.

````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

var first = list.removeFirst();

println first;         // 10
println list.get(0);   // 20
println list.size();   // 2
````

````ysharp
var list = new LinkedList();

list.addLast(1);

println list.removeFirst();   // 1
println list.removeFirst();   // null
println list.isEmpty();       // true
````

### `removeLast()`

Removes and returns the last element of the list, or `null` if the list is empty.

````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

var last = list.removeLast();

println last;          // 30
println list.size();   // 2
println list.get(1);   // 20
````

````ysharp
var list = new LinkedList();

list.addLast(1);

println list.removeLast();   // 1
println list.removeLast();   // null
println list.isEmpty();      // true
````

### `set(index : int, value : any)`

Replaces the element at the specified index and returns the old value.

````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

var old = list.set(1, 99);

println old;           // 20
println list.get(1);   // 99
````

````ysharp
var list = new LinkedList();

list.addLast(1);
list.addLast(2);
list.addLast(3);

list.set(0, 100);
list.set(2, 300);

println list.get(0);   // 100
println list.get(2);   // 300
````

````ysharp
var list = new LinkedList();

list.addLast(1);

try do
    list.set(5, 10);
end
catch(e) do
    println e;   // LinkedList index out of bounds
end
````

### `size()`

Returns the number of elements in the list.

````ysharp
var list = new LinkedList();

println list.size();   // 0

list.addLast(10);
list.addLast(20);

println list.size();   // 2
````

````ysharp
var list = new LinkedList();

for var i = 0; i < 5; i++ do
    list.addLast(i);
end

println list.size();   // 5

list.removeFirst();
list.removeLast();

println list.size();   // 3
````

### `toArray()`

Returns a new `Array` containing all elements of the list in order.


````ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

var arr = list.toArray();

println arr.get(0);   // 10
println arr.get(1);   // 20
println arr.get(2);   // 30
````


````ysharp
var list = new LinkedList();

list.addLast(1);
list.addLast(2);
list.addLast(3);

list.removeFirst();   // [2,3]

var arr = list.toArray();

println arr.size();   // 2
println arr.get(0);   // 2
println arr.get(1);   // 
````

### `toString()`

Returns a string representation of the list in traversal order.

```ysharp
var list = new LinkedList();

list.addLast(10);
list.addLast(20);
list.addLast(30);

println list.toString();   // [10 -> 20 -> 30]
````

````ysharp
var list = new LinkedList();

println list.toString();   // []
````