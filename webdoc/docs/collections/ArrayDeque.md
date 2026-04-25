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
