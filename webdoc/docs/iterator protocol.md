---
sidebar_position: 19
---

Ysharp provides a built-in iterator protocol that allows custom objects to be used in `foreach` loops. Any object that implements this protocol becomes iterable.

---

## Overview

The iterator protocol consists of two layers:

1. **The Iterable** &rarr; an object that exposes an `iter()` method returning an iterator.
2. **The Iterator** &rarr; an object that exposes a `getNext()` method returning successive values, and `null` when exhausted.

---

## Protocol Contract

### Iterable

An iterable object must implement:

| Method   | Signature  | Return Type | Description                                      |
|----------|------------|-------------|--------------------------------------------------|
| `iter`   | `iter()`   | `object`    | Returns a new iterator instance for this object  |

### Iterator

The iterator object returned by `iter()` must implement:

| Method     | Signature      | Return Type          | Description                                                      |
|------------|----------------|----------------------|------------------------------------------------------------------|
| `getNext`  | `getNext()`    | `any\null`           | Returns the next value in the sequence, or `null` when exhausted |

> **Note:** Returning `null` from `getNext()` signals the end of iteration. The `foreach` loop will stop immediately when `null` is received.

---

## Built-in Iterator Support

All built-in collection types that implement `IVector` (such as `Array`) come with iterator support out of the box via the `Vector_Instance_Prototype`. They expose `iter()` which returns a `VectorIterator` instance.

The `VectorIterator` maintains an internal cursor. When all elements are consumed, the cursor resets to `0` and `getNext()` returns `null`.

---

## Using `foreach`

The `foreach` loop automatically invokes the iterator protocol on the iterable expression.

```ysharp
foreach var item in myCollection do
    println(item);
end
```

**What happens internally:**

1. The interpreter calls `myCollection.iter()` to get an iterator.
2. On each iteration, it calls `iterator.getNext()`.
3. If `getNext()` returns `null`, the loop terminates.
4. Otherwise, the returned value is assigned to the loop variable and the body executes.

---

## Type Checking in `foreach`

The loop variable can be optionally typed. If a type annotation is provided, the interpreter validates each value returned by `getNext()` against the declared type at runtime.

```ysharp
foreach var item in numbers do
    println item;
end
```

If the type does not match, a runtime error is thrown:

```
Type mismatch. Cannot assign value of type 'string' to variable 'item' of type 'int'.
```

---

## Implementing a Custom Iterator

You can make any class iterable by implementing `iter()` and a companion iterator class with `getNext()`.

```ysharp
class Range {
    var start = 0;
    var _end = 0;

    constructor(start, _end) do
        this.start = start;
        this._end = _end;
    end

    iter() do
        return new RangeIterator(this.start, this._end);
    end
}

class RangeIterator {
    var current = 0;
    var _end = 0;

    constructor(start, _end) do
        this.current = start;
        this._end = _end;
    end

    getNext() do
        if this.current > this._end then do
            return null;
        end
        var value = this.current;
        this.current++;
        return value;
    end
}

var r = new Range(1, 5);

foreach var n in r do
    println(n); // 1 2 3 4 5
end
```

---

## Error Cases

| Situation                                      | Error Message                                                                           |
|------------------------------------------------|-----------------------------------------------------------------------------------------|
| Iterable does not have `iter` method           | `iter should be function that returns iterator object to use foreach loop`              |
| `iter()` does not return an object             | `iterator should be class`                                                              |
| Iterator does not have `getNext` method        | `getNext should be function that returns iterator object to use foreach loop`           |
| `foreach` used on a non-class value            | `iterable should be class`                                                              |
| Loop variable type mismatch                    | `Type mismatch. Cannot assign value of type '<T>' to variable '<name>' of type '<T2>'.` |

---

## Summary

| Concept        | Required Method | Returns              |
|----------------|-----------------|----------------------|
| Iterable       | `iter()`        | Iterator object      |
| Iterator       | `getNext()`     | Next value or `null` |