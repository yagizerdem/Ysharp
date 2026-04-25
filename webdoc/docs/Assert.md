---
sidebar_position: 25
---

he Assert object provides a set of utility methods used for testing and validation.

It is designed to simplify writing test cases by offering common assertion functions
such as equality checks, boolean validation, and failure signaling.

## Description

In Ysharp, Assert is a built-in utility class with the following characteristics:

- All methods are **static** and are accessed directly via the `Assert` class.
- Used primarily in **unit tests** and **debugging scenarios**.
- Throws errors when an assertion fails, allowing early detection of issues.
- Supports validation of:
    - equality / inequality
    - boolean conditions
    - null checks
    - forced failures

- Does not maintain any internal state; purely functional.

---

## Reference

### Static methods

| Method          | Signature                            | Return Type | Description                                                      |
|-----------------|--------------------------------------|-------------|------------------------------------------------------------------|
| `equals`        | `Assert.equals(a : any, b : any)`    | `void`      | Asserts that two values are equal                                |
| `notEquals`     | `Assert.notEquals(a : any, b : any)` | `void`      | Asserts that two values are not equal                            |
| `isTrue`        | `Assert.isTrue(value : bool)`        | `void`      | Asserts that the given value is true                             |
| `isFalse`       | `Assert.isFalse(value : bool)`       | `void`      | Asserts that the given value is false                            |
| `null`          | `Assert.null(value : any)`           | `void`      | Asserts that the given value is null                             |
| `notNull`       | `Assert.notNull(value : any)`        | `void`      | Asserts that the given value is not null                         |
| `fail`          | `Assert.fail(message? : string)`     | `void`      | Forces a failure with an optional message                        |


### equals
#### `equals(a : any, b : any) : void`

- Asserts that two values are equal

```ysharp
// basic equality
Assert.equals(10, 10);

Assert.equals("hello", "hello");

Assert.equals(true, true);

// numeric equivalence
Assert.equals(12.0, 12);

// complex expressions
Assert.equals(5 + 5, 10);
Assert.equals("a".repeat(3), "aaa");

// null equality
var a = null;
var b = null;

Assert.equals(a, b);

// objects (same reference)
var obj = { "value": 10 };

Assert.equals(obj, obj);

// collections
var arr = [1, 2, 3];
var arr2 = arr;

Assert.equals(arr, arr2);

// after operations
var pq = new PriorityQueue();

pq.enqueue(10, 5);
pq.enqueue(20, 3);

Assert.equals(pq.peek(), 20);

// no return value, only validation
Assert.equals(1, 1);
````

### notEquals
#### `notEquals(a : any, b : any) : void`

- Asserts that two values are not equal

```ysharp
// basic inequality
Assert.notEquals(10, 20);

Assert.notEquals("hello", "world");

Assert.notEquals(true, false);

// numeric difference
Assert.notEquals(12, 13);

// complex expressions
Assert.notEquals(5 + 5, 11);
Assert.notEquals("a".repeat(2), "aaa");

// null vs non-null
var a = null;
var b = 10;

Assert.notEquals(a, b);

// objects (different references)
var obj1 = { "value": 10 };
var obj2 = { "value": 10 };

Assert.notEquals(obj1, obj2);

// collections (different instances)
var arr1 = [1, 2, 3];
var arr2 = [1, 2, 3];

Assert.notEquals(arr1, arr2);

// after operations
var pq = new PriorityQueue();

pq.enqueue(10, 5);
pq.enqueue(20, 3);

Assert.notEquals(pq.peek(), 10);

// no return value, only validation
Assert.notEquals(1, 2);
````

### isTrue
#### `isTrue(value : bool) : void`

- Asserts that the given value is true

```ysharp
// basic true
Assert.isTrue(true);

// expressions
Assert.isTrue(5 > 2);
Assert.isTrue(10 == 10);

// logical operations
Assert.isTrue(true && true);
Assert.isTrue(!false);

// string checks
Assert.isTrue("hello".length() == 5);

// collection checks
var arr = [1, 2, 3];
Assert.isTrue(arr.size() == 3);

// after operations
var pq = new PriorityQueue();

pq.enqueue(10, 5);
pq.enqueue(20, 3);

Assert.isTrue(pq.peek() == 20);

// boolean variables
var flag = true;
Assert.isTrue(flag);

// no return value, only validation
Assert.isTrue(1 < 2);
````

### isFalse
#### `isFalse(value : bool) : void`

- Asserts that the given value is false

```ysharp
// basic false
Assert.isFalse(false);

// expressions
Assert.isFalse(5 < 2);
Assert.isFalse(10 != 10);

// logical operations
Assert.isFalse(true && false);
Assert.isFalse(!true);

// string checks
Assert.isFalse("hello".length() == 4);

// collection checks
var arr = [1, 2, 3];
Assert.isFalse(arr.size() == 0);

// after operations
var pq = new PriorityQueue();

pq.enqueue(10, 5);
pq.enqueue(20, 3);

Assert.isFalse(pq.peek() == 10);

// boolean variables
var flag = false;
Assert.isFalse(flag);

// no return value, only validation
Assert.isFalse(1 > 2);
````

### null
#### `null(value : any) : void`

- Asserts that the given value is null

```ysharp
// basic null
var a = null;

Assert.null(a);

// function returning null
var result = null;
Assert.null(result);

// after removal operations
var d = new ArrayDeque();

Assert.null(d.poll()); // poll returns null when empty

// object fields
var obj = { "value": null };

Assert.null(obj.get("value"));

// collections
var arr = [null, 1, 2];

Assert.null(arr.get(0));

// after clear-like logic
var pq = new PriorityQueue();

var x = null;
Assert.null(x);

// no return value, only validation
Assert.null(null);
````

### notNull
#### `notNull(value : any) : void`

- Asserts that the given value is not null

```ysharp
// basic non-null values
Assert.notNull(10);
Assert.notNull("hello");
Assert.notNull(true);

// objects
var obj = { "value": 10 };
Assert.notNull(obj);

// collections
var arr = [1, 2, 3];
Assert.notNull(arr);

// after operations
var pq = new PriorityQueue();

pq.enqueue(10, 5);
pq.enqueue(20, 3);

Assert.notNull(pq.peek());

// ArrayDeque example
var d = new ArrayDeque();

d.addLast(10);
Assert.notNull(d.peekFirst());

// function result
var result = "data";
Assert.notNull(result);

// no return value, only validation
Assert.notNull(0);
````

### fail
#### `fail(message? : string) : void`

- Forces a failure with an optional message

```ysharp
// basic failure
try do
    Assert.fail("this should fail");
end
catch(e) do
    println "caught"; // expected
end

// without message
try do
    Assert.fail();
end
catch(e) do
    println "caught"; // expected
end

// conditional failure
var value = 10;

try do
    if(value != 20) do
        Assert.fail("unexpected value");
    end
end
catch(e) do
    println "caught";
end

// inside test logic
var pq = new PriorityQueue();

try do
    if(pq.isEmpty()) do
        Assert.fail("queue should not be empty here");
    end
end
catch(e) do
    println "caught";
end
````