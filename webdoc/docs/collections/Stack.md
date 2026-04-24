---
sidebar_position: 4
---

# Stack

## General

`Stack` is a built-in collection class in Ysharp that implements a **last-in, first-out (LIFO)** data structure. Elements are added and removed from the top of the stack.

`Stack` extends the vector protocol, meaning it also supports iteration via `foreach`.

> **Note** <br/>
> Some methods have aliases for convenience. For example, `push` is an alias for `add`, `top` is an alias for `peek`, and `isEmpty` is an alias for `empty`.


```ysharp
var stack = new Stack();
```

Creates a new, empty `Stack` instance.

#### Example

```ysharp
var stack = new Stack();

stack.push(1);
stack.push(2);
stack.push(3);

println stack.peek();   // 3
println stack.pop();    // 3
println stack.size();   // 2
```

## Reference

### Instance methods

| Method                    | Signature                                    | Return Type     | Description                                                                                                                  |
|---------------------------|----------------------------------------------|-----------------|------------------------------------------------------------------------------------------------------------------------------|
| `add` / `push`            | `stack.add(value : any)`                     | `int`           | Pushes a value onto the stack and returns the new size                                                                       |
| `addAll`                  | `stack.addAll(collection : Collection)`      | `int`           | Adds all elements from the given collection (Stack, Array, Queue, Set, PriorityQueue) to this stack and returns the new size |
| `pop`                     | `stack.pop()`                                | `any`           | Removes and returns the top element; throws if empty                                                                         |
| `peek` / `top`            | `stack.peek()`                               | `any`           | Returns the top element without removing it; throws if empty                                                                 |
| `peekOrNull`              | `stack.peekOrNull()`                         | `any \| null`   | Returns the top element or null if the stack is empty                                                                        |
| `empty` / `isEmpty`       | `stack.empty()`                              | `bool`          | Returns true if the stack is empty                                                                                           |
| `size`                    | `stack.size()`                               | `int`           | Returns the number of elements in the stack                                                                                  |
| `contains`                | `stack.contains(value : any)`                | `bool`          | Returns true if the stack contains the given value                                                                           |
| `search`                  | `stack.search(value : any)`                  | `int`           | Returns 1-based position from the top, or -1 if not found                                                                    |
| `clear`                   | `stack.clear()`                              | `null`          | Removes all elements from the stack                                                                                          |
| `clone`                   | `stack.clone()`                              | `Stack`         | Returns a shallow copy of the stack                                                                                          |
| `reverse`                 | `stack.reverse()`                            | `Stack`         | Returns a new stack with elements in reversed order                                                                          |
| `toArray` / `toList`      | `stack.toArray()`                            | `Array`         | Returns an array containing all elements in stack order                                                                      |
| `asQueryable`             | `stack.asQueryable()`                        | `Queryable`     | Converts the stack into a queryable sequence (LINQ-style operations)                                                         |
| `toString`                | `stack.toString()`                           | `string`        | Returns a string representation of the stack                                                                                 |


#### `add(value : any) : int`
#### `push(value : any) : int`

- Pushes a value onto the stack and returns the new size

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);

println stack.size(); // 2
println stack.peek(); // 20
````


#### `addAll(collection : Collection) : int`

- Adds all elements from the given collection (Stack, Array, Queue, Set, PriorityQueue) to this stack and returns the new size

```ysharp
var stack = new Stack();
var arr = [1,2,3];

stack.addAll(arr);

println stack.size(); // 3
println stack.peek(); // 3
````

#### `pop() : any`

- Removes and returns the top element; throws if empty

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);

println stack.pop(); // 20
println stack.size(); // 1

// pop on empty
stack.pop();
try do
    stack.pop();
end
catch(e) do
    println e;
end
````

#### `peek() : any`
#### `top() : any`

- Returns the top element without removing it; throws if empty

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);

println stack.peek(); // 20
println stack.top();  // 20
println stack.size(); // 2 (unchanged)

// peek on empty
var emptyStack = new Stack();

try do
    emptyStack.peek();
end
catch(e) do
    println e;
end
````

#### `empty() : bool`
#### `isEmpty() : bool`

- Returns true if the stack is empty

```ysharp
var stack = new Stack();

println stack.empty();   // true
println stack.isEmpty(); // true

stack.push(10);

println stack.empty();   // false
println stack.isEmpty(); // false

stack.pop();

println stack.empty();   // true
````

#### `size() : int`

- Returns the number of elements in the stack

```ysharp
var stack = new Stack();

println stack.size(); // 0

stack.push(10);
stack.push(20);

println stack.size(); // 2

stack.pop();

println stack.size(); // 1
````


#### `search(value : any) : int`

- Returns 1-based position from the top, or -1 if not found

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);
stack.push(30); // top

println stack.search(30); // 1
println stack.search(20); // 2
println stack.search(10); // 3
println stack.search(99); // -1
````

#### `clear() : null`

- Removes all elements from the stack

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);

println stack.size(); // 2

stack.clear();

println stack.size();   // 0
println stack.isEmpty(); // true

// accessing after clear
println stack.peekOrNull(); // null
````


#### `clone() : Stack`

- Returns a shallow copy of the stack

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);
stack.push(30);

var cloned = stack.clone();

println cloned.size(); // 3
println cloned.peek(); // 30

// modify clone
cloned.pop();

println cloned.size(); // 2
println stack.size();  // 3 (original unchanged)
````

#### `reverse() : Stack`

- Returns a new stack with elements in reversed order

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);
stack.push(30); // top

var reversed = stack.reverse();

println reversed.pop(); // 10
println reversed.pop(); // 20
println reversed.pop(); // 30

// original unchanged
println stack.peek(); // 30
````

#### `toArray() : Array`
#### `toList() : Array`

- Returns an array containing all elements in stack order

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);
stack.push(30);

var arr = stack.toArray();

println arr.size();   // 3
println arr.get(0);   // 10
println arr.get(1);   // 20
println arr.get(2);   // 30
````

#### `asQueryable() : Queryable`

- Converts the stack into a queryable sequence (LINQ-style operations)

````
var stack = new Stack();

stack.push(1);
stack.push(2);
stack.push(3);

var q = stack.asQueryable();

// example query (assuming LINQ-style API)
var result = q
    .where((x, a, b) => x > 1)
    .select((x, a, b) => x * 10)
    .toArray();

println result.get(0); // 20
println result.get(1); // 30
````

#### `toString() : string`

- Returns a string representation of the stack

```ysharp
var stack = new Stack();

stack.push(10);
stack.push(20);
stack.push(30);

println stack.toString(); // [ 10, 20, 30 ]
````