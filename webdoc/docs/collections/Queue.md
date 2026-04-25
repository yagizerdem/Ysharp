---
sidebar_position: 5
---

## General

`Queue` is a built-in collection class in Ysharp that implements a **first-in, first-out (FIFO)** data structure. Elements are added to the end of the queue and removed from the front.

`Queue` extends the vector protocol, meaning it also supports iteration via `foreach`.

> **Note** <br/>
> Some methods have aliases for convenience. For example, `enqueue` is an alias for `add`, `dequeue` is an alias for `remove`, and `isEmpty` is an alias for `empty`.


```ysharp
var queue = new Queue();
````

Creates a new, empty Queue instance.

````ysharp
var queue = new Queue();

queue.enqueue(1);
queue.enqueue(2);
queue.enqueue(3);

println queue.peek();    // 1
println queue.dequeue(); // 1
println queue.size();    // 2
````


## Reference

### Instance methods

| Method               | Signature                       | Return Type     | Description                                                          |
|----------------------|---------------------------------|-----------------|----------------------------------------------------------------------|
| `add` / `enqueue`    | `queue.add(value : any)`        | `bool`          | Inserts a value at the end of the queue                              |
| `remove` / `dequeue` | `queue.remove()`                | `any`           | Removes and returns the front element; throws if empty               |
| `poll`               | `queue.poll()`                  | `any \| null`   | Removes and returns the front element, or null if the queue is empty |
| `peek`               | `queue.peek()`                  | `any`           | Returns the front element without removing it; throws if empty       |
| `size`               | `queue.size()`                  | `int`           | Returns the number of elements in the queue                          |
| `isEmpty` / `empty`  | `queue.isEmpty()`               | `bool`          | Returns true if the queue is empty                                   |
| `toString`           | `queue.toString()`              | `string`        | Returns a string representation of the queue                         |


#### `add(value : any) : bool`
#### `enqueue(value : any) : bool`

- Inserts a value at the end of the queue

```ysharp
var queue = new Queue();

queue.enqueue(10);
queue.enqueue(20);
queue.enqueue(30);

println queue.size(); // 3
println queue.peek(); // 10
````

#### `remove() : any`
#### `dequeue() : any`

- Removes and returns the front element; throws if empty

```ysharp
var queue = new Queue();

queue.enqueue(10);
queue.enqueue(20);

println queue.dequeue(); // 10
println queue.size();    // 1

// remove on empty
var emptyQueue = new Queue();

try do
    emptyQueue.remove();
end
catch(e) do
    println e;
end
````

#### `poll() : any | null`

- Removes and returns the front element, or null if the queue is empty

```ysharp
var queue = new Queue();

println queue.poll(); // null

queue.enqueue(10);
queue.enqueue(20);

println queue.poll(); // 10
println queue.poll(); // 20
println queue.poll(); // null
````

#### `peek() : any`

- Returns the front element without removing it; throws if empty

```ysharp
var queue = new Queue();

queue.enqueue(10);
queue.enqueue(20);

println queue.peek(); // 10
println queue.size(); // 2 (unchanged)

// peek on empty
var emptyQueue = new Queue();

try do
    emptyQueue.peek();
end
catch(e) do
    println e;
end
````


#### `size() : int`

- Returns the number of elements in the queue

```ysharp
var queue = new Queue();

println queue.size(); // 0

queue.enqueue(10);
queue.enqueue(20);

println queue.size(); // 2

queue.dequeue();

println queue.size(); // 1
````

#### `isEmpty() : bool`
#### `empty() : bool`

- Returns true if the queue is empty

```ysharp
var queue = new Queue();

println queue.isEmpty(); // true
println queue.empty();   // true

queue.enqueue(10);

println queue.isEmpty(); // false
println queue.empty();   // false

queue.dequeue();

println queue.isEmpty(); // true
````


#### `toString() : string`

- Returns a string representation of the queue

```ysharp
var queue = new Queue();

queue.enqueue(10);
queue.enqueue(20);
queue.enqueue(30);

println queue.toString(); // [ 10, 20, 30 ]
````