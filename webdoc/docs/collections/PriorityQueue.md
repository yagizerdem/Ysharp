---
sidebar_position: 8
---


## General

An unbounded priority queue based on a priority heap. The elements of the priority queue
are ordered according to their natural ordering, or by a Comparator provided at queue construction time,
depending on which constructor is used. 
A priority queue does not permit null elements.
The head of this queue is the least element with respect to the specified ordering. 
If multiple elements are tied for least value, 
the head is one of those elements -- ties are broken arbitrarily. 
The queue retrieval operations poll, remove, peek,
and element access the element at the head of the queue.

A priority queue is unbounded, but has an internal capacity governing the
size of an array used to store the elements on the queue.
It is always at least as large as the queue size. As elements are added to a priority queue,
its capacity grows automatically. The details of the growth policy are not specified.


`PriorityQueue` extends the vector protocol, meaning it also supports iteration via `foreach`.

```ysharp
var pq = new PriorityQueue();
````

Creates a new, empty PriorityQueue instance.


## Reference

### Instance methods

| Method                 | Signature                                              | Return Type     | Description                                                                                   |
|------------------------|--------------------------------------------------------|-----------------|-----------------------------------------------------------------------------------------------|
| `enqueue` / `add`      | `pq.enqueue(value : any, priority : number)`           | `void`          | Inserts a value with a given priority (lower number = higher priority)                        |
| `dequeue`              | `pq.dequeue()`                                         | `any`           | Removes and returns the element with highest priority; throws if empty                        |
| `peek`                 | `pq.peek()`                                            | `any`           | Returns the highest priority element without removing it; throws if empty                     |
| `peekPriority`         | `pq.peekPriority()`                                    | `number`        | Returns the priority of the top element                                                       |
| `contains`             | `pq.contains(value : any)`                             | `bool`          | Returns true if the queue contains the given value                                            |
| `changePriority`       | `pq.changePriority(value : any, newPriority : number)` | `bool`          | Updates the priority of the given value; returns true if found                                |
| `remove`               | `pq.remove(value : any)`                               | `bool`          | Removes the first occurrence of the given value; returns true if found                        |
| `size`                 | `pq.size()`                                            | `int`           | Returns the number of elements in the queue                                                   |
| `isEmpty`              | `pq.isEmpty()`                                         | `bool`          | Returns true if the queue is empty                                                            |
| `clear`                | `pq.clear()`                                           | `void`          | Removes all elements from the queue                                                           |
| `toArray`              | `pq.toArray()`                                         | `Array`         | Returns elements in heap order (NOT sorted)                                                   |
| `drainSorted`          | `pq.drainSorted()`                                     | `Array`         | Removes all elements and returns them sorted by priority                                      |
| `clone`                | `pq.clone()`                                           | `PriorityQueue` | Returns a shallow copy of the queue                                                           |
| `toString`             | `pq.toString()`                                        | `string`        | Returns a string representation of the queue                                                  |


### enqueue / add
#### `enqueue(value : any, priority : number) : void`

- Inserts a value with a given priority (lower number = higher priority)

```ysharp
var pq = new PriorityQueue();

// basic insert
pq.enqueue(10, 5);
println pq.peek(); // 10

// multiple inserts (min-heap behavior)
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.peek(); // 20 (highest priority: lowest number)

// removing elements (priority order)
println pq.dequeue(); // 20
println pq.dequeue(); // 10
println pq.dequeue(); // 30

// duplicates allowed with different priorities
pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

println pq.dequeue(); // 5 (priority 1)
println pq.dequeue(); // 5 (priority 5)
println pq.dequeue(); // 5 (priority 10)

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.dequeue(); // "hello"
println pq.dequeue(); // true
println pq.dequeue(); // 10

// same priority (order not guaranteed)
pq.enqueue(1, 5);
pq.enqueue(2, 5);
pq.enqueue(3, 5);

println pq.size(); // 3
println pq.dequeue(); // one of them
println pq.dequeue();
println pq.dequeue();
````


### dequeue
#### `dequeue() : any`

- Removes and returns the element with highest priority; throws if empty

```ysharp
var pq = new PriorityQueue();

// empty case (should throw)
try do
    pq.dequeue();
    Assert.fail("dequeue on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.dequeue(); // 20 (priority 3)
println pq.dequeue(); // 10 (priority 5)
println pq.dequeue(); // 30 (priority 7)

// queue should be empty now
println pq.isEmpty(); // true

// duplicates with different priorities
pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

println pq.dequeue(); // 5 (priority 1)
println pq.dequeue(); // 5 (priority 5)
println pq.dequeue(); // 5 (priority 10)

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.dequeue(); // "hello"
println pq.dequeue(); // true
println pq.dequeue(); // 10

// same priority (order not guaranteed)
pq.enqueue(1, 5);
pq.enqueue(2, 5);
pq.enqueue(3, 5);

println pq.dequeue(); // one of them
println pq.dequeue();
println pq.dequeue();
````


### peek
#### `peek() : any`

- Returns the highest priority element without removing it; throws if empty

```ysharp
var pq = new PriorityQueue();

// empty case (should throw)
try do
    pq.peek();
    Assert.fail("peek on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.peek(); // 20 (priority 3)

// no removal effect
println pq.peek(); // 20
println pq.size(); // 3

// after dequeue
pq.dequeue();
println pq.peek(); // 10 (next highest priority)

// duplicates with different priorities
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

println pq.peek(); // 5 (priority 1)

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.peek(); // "hello"

// same priority (order not guaranteed)
pq.clear();

pq.enqueue(1, 5);
pq.enqueue(2, 5);
pq.enqueue(3, 5);

println pq.peek(); // one of them
````

### peekPriority
#### `peekPriority() : number`

- Returns the priority of the top element

```ysharp
var pq = new PriorityQueue();

// empty case (should throw)
try do
    pq.peekPriority();
    Assert.fail("peekPriority on empty should throw");
end
catch(e) do
    println "caught"; // expected
end

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.peekPriority(); // 3

// no removal effect
println pq.peekPriority(); // 3
println pq.size();         // 3

// after dequeue
pq.dequeue();
println pq.peekPriority(); // 5

// duplicates with different priorities
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

println pq.peekPriority(); // 1

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.peekPriority(); // 2

// same priority
pq.clear();

pq.enqueue(1, 5);
pq.enqueue(2, 5);
pq.enqueue(3, 5);

println pq.peekPriority(); // 5
````


### contains
#### `contains(value : any) : bool`

- Returns true if the queue contains the given value

```ysharp
var pq = new PriorityQueue();

// empty
println pq.contains(10); // false

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.contains(10); // true
println pq.contains(20); // true
println pq.contains(999); // false

// order irrelevant
println pq.contains(30); // true

// after dequeue
pq.dequeue(); // removes 20
println pq.contains(20); // false

// duplicates with different priorities
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

println pq.contains(5); // true

pq.dequeue(); // remove one occurrence
println pq.contains(5); // still true

pq.dequeue();
pq.dequeue();

println pq.contains(5); // false

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.contains(10);      // true
println pq.contains("hello"); // true
println pq.contains(true);    // true
println pq.contains(false);   // false

// no side effect
var before = pq.size();
pq.contains(10);
println pq.size() == before; // true
````


### changePriority
#### `changePriority(value : any, newPriority : number) : bool`

- Updates the priority of the given value; returns true if found

```ysharp
var pq = new PriorityQueue();

// empty
println pq.changePriority(10, 1); // false

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

// change existing
println pq.changePriority(10, 1); // true
println pq.peek();                // 10 (now highest priority)

// change non-existing
println pq.changePriority(999, 0); // false

// order after multiple updates
pq.enqueue(40, 6);

println pq.peek(); // 10

pq.changePriority(30, 0);
println pq.peek(); // 30

// duplicates (only first occurrence should be affected)
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 5);
pq.enqueue(5, 1);

println pq.changePriority(5, 100); // true

// only one element updated, others remain
println pq.dequeue(); // 5 (priority 1 or 5 depending on which matched first)

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.changePriority("hello", 10); // true

println pq.peek(); // true (priority 3 now highest)

// no side effect when not found
var before = pq.size();

println pq.changePriority("world", 1); // false
println pq.size() == before;           // true
````


### remove
#### `remove(value : any) : bool`

- Removes the first occurrence of the given value; returns true if found

```ysharp
var pq = new PriorityQueue();

// empty
println pq.remove(10); // false

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.remove(20); // true
println pq.contains(20); // false
println pq.size(); // 2

// remove non-existing
println pq.remove(999); // false

// remove head element
pq.clear();

pq.enqueue(1, 1);
pq.enqueue(2, 2);
pq.enqueue(3, 3);

println pq.remove(1); // true
println pq.peek();    // 2

// remove tail element
println pq.remove(3); // true
println pq.size();    // 1

// duplicates (only first occurrence removed)
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 5);
pq.enqueue(5, 1);

println pq.remove(5); // true

println pq.size(); // 2
println pq.contains(5); // true

// remove remaining
pq.remove(5);
pq.remove(5);

println pq.contains(5); // false
println pq.isEmpty();   // true

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.remove("hello"); // true
println pq.contains("hello"); // false
println pq.size(); // 2

// no side effect when not found
var before = pq.size();

println pq.remove("world"); // false
println pq.size() == before; // true
````

### size
#### `size() : int`

- Returns the number of elements in the queue

```ysharp
var pq = new PriorityQueue();

// empty
println pq.size(); // 0

// single insert
pq.enqueue(10, 5);
println pq.size(); // 1

// multiple inserts
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.size(); // 3

// after dequeue
pq.dequeue();
println pq.size(); // 2

// after remove
pq.remove(10);
println pq.size(); // 1

// duplicates
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 5);
pq.enqueue(5, 1);

println pq.size(); // 3

pq.dequeue();
println pq.size(); // 2

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.size(); // 3

// after clear
pq.clear();
println pq.size(); // 0

// no side effect
var before = pq.size();
pq.size();
println pq.size() == before; // true
````

### isEmpty
#### `isEmpty() : bool`

- Returns true if the queue is empty

```ysharp
var pq = new PriorityQueue();

// empty
println pq.isEmpty(); // true

// after insert
pq.enqueue(10, 5);
println pq.isEmpty(); // false

// after dequeue
pq.dequeue();
println pq.isEmpty(); // true

// multiple elements
pq.enqueue(1, 3);
pq.enqueue(2, 2);
pq.enqueue(3, 1);

println pq.isEmpty(); // false

// remove all
pq.dequeue();
pq.dequeue();
pq.dequeue();

println pq.isEmpty(); // true

// after clear
pq.enqueue(10, 5);
pq.enqueue(20, 3);

pq.clear();
println pq.isEmpty(); // true

// no side effect
var before = pq.isEmpty();
pq.isEmpty();
println pq.isEmpty() == before; // true
````


### clear
#### `clear() : void`

- Removes all elements from the queue

```ysharp
var pq = new PriorityQueue();

// clear on empty
pq.clear();
println pq.isEmpty(); // true
println pq.size();    // 0

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.size(); // 3

// clear all
pq.clear();

println pq.isEmpty(); // true
println pq.size();    // 0

// reuse after clear
pq.enqueue(100, 1);
println pq.peek(); // 100
println pq.size(); // 1

// multiple clears
pq.clear();
pq.clear();

println pq.isEmpty(); // true

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.size(); // 3

pq.clear();

println pq.isEmpty(); // true
println pq.size();    // 0

// no side effect
var before = pq.size();
pq.clear();
println pq.size() == before; // true
````


### toArray
#### `toArray() : Array`

- Returns elements in heap order (NOT sorted)

```ysharp
var pq = new PriorityQueue();

// empty
var arr = pq.toArray();
println arr.size(); // 0

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

var arr = pq.toArray();

println arr.size(); // 3

// NOT sorted, heap order
println arr.get(0); // likely 20 (root)
println arr.get(1); // depends on heap
println arr.get(2);

// verify all values exist (order irrelevant)
println arr.contains(10); // true
println arr.contains(20); // true
println arr.contains(30); // true

// after dequeue
pq.dequeue(); // removes highest priority (20)

var arr = pq.toArray();

println arr.size(); // 2
println arr.contains(20); // false

// duplicates
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 5);
pq.enqueue(5, 1);

var arr = pq.toArray();

println arr.size(); // 3
println arr.contains(5); // true

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

var arr = pq.toArray();

println arr.size(); // 3
println arr.contains("hello"); // true

// independence check
var arr = pq.toArray();

pq.enqueue(999, 0);

println arr.size(); // should not change
````

### drainSorted
#### `drainSorted() : Array`

- Removes all elements and returns them sorted by priority

```ysharp
var pq = new PriorityQueue();

// empty
var arr = pq.drainSorted();
println arr.size(); // 0

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

var arr = pq.drainSorted();

println arr.size();  // 3
println arr.get(0);  // 20 (priority 3)
println arr.get(1);  // 10 (priority 5)
println arr.get(2);  // 30 (priority 7)

// queue should be empty after drain
println pq.isEmpty(); // true

// duplicates with different priorities
pq.enqueue(5, 10);
pq.enqueue(5, 1);
pq.enqueue(5, 5);

var arr = pq.drainSorted();

println arr.get(0); // 5 (priority 1)
println arr.get(1); // 5 (priority 5)
println arr.get(2); // 5 (priority 10)

// mixed types
pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

var arr = pq.drainSorted();

println arr.get(0); // "hello"
println arr.get(1); // true
println arr.get(2); // 10

// same priority (order not guaranteed among equals)
pq.enqueue(1, 5);
pq.enqueue(2, 5);
pq.enqueue(3, 5);

var arr = pq.drainSorted();

println arr.size(); // 3
println arr.get(0); // one of them
println arr.get(1);
println arr.get(2);

// queue empty again
println pq.isEmpty(); // true
````

### clone
#### `clone() : PriorityQueue`

- Returns a shallow copy of the queue

```ysharp
var pq1 = new PriorityQueue();

// empty clone
var pq2 = pq1.clone();

println pq2.isEmpty(); // true
println pq2.size();    // 0

// basic usage
pq1.enqueue(10, 5);
pq1.enqueue(20, 3);
pq1.enqueue(30, 7);

var pq2 = pq1.clone();

println pq1.size(); // 3
println pq2.size(); // 3

println pq2.peek(); // 20

// independence
pq1.enqueue(40, 1);

println pq1.peek(); // 40
println pq2.peek(); // still 20

// modify clone
pq2.enqueue(5, 0);

println pq2.peek(); // 5
println pq1.peek(); // 40

// order preserved in clone
println pq2.dequeue(); // 5
println pq2.dequeue(); // 20
println pq2.dequeue(); // 10
println pq2.dequeue(); // 30

println pq2.isEmpty(); // true

// duplicates
pq1.clear();

pq1.enqueue(5, 10);
pq1.enqueue(5, 5);
pq1.enqueue(5, 1);

var pq2 = pq1.clone();

println pq2.size(); // 3

println pq2.dequeue(); // 5 (priority 1)
println pq2.dequeue(); // 5 (priority 5)
println pq2.dequeue(); // 5 (priority 10)

// mixed types
pq1.clear();

pq1.enqueue(10, 5);
pq1.enqueue("hello", 2);
pq1.enqueue(true, 3);

var pq2 = pq1.clone();

println pq2.dequeue(); // "hello"
println pq2.dequeue(); // true
println pq2.dequeue(); // 10

println pq2.isEmpty(); // true
````


### toString
#### `toString() : string`

- Returns a string representation of the queue

```ysharp
var pq = new PriorityQueue();

// empty
println pq.toString(); // []

// basic usage
pq.enqueue(10, 5);
pq.enqueue(20, 3);
pq.enqueue(30, 7);

println pq.toString(); // heap order representation

// add more elements
pq.enqueue(40, 1);

println pq.toString(); // root should reflect highest priority

// after dequeue
pq.dequeue();

println pq.toString();

// duplicates
pq.clear();

pq.enqueue(5, 10);
pq.enqueue(5, 5);
pq.enqueue(5, 1);

println pq.toString();

// mixed types
pq.clear();

pq.enqueue(10, 5);
pq.enqueue("hello", 2);
pq.enqueue(true, 3);

println pq.toString();

// after clear
pq.clear();

println pq.toString(); // []

// no side effect
var before = pq.toString();
pq.toString();
println pq.toString() == before; // true
````