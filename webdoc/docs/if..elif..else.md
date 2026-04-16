---
sidebar_position: 10
---

### if...else Statement

The if...else statement executes a statement 
if a specified condition is truthy. If the condition is falsy, 
another statement in the optional else clause will be executed.

### Syntax

```ysharp
if condition do
    // code executed if condition is truthy
end else do
    // code executed if condition is falsy
end
```

You can also chain multiple conditions using elif:

```ysharp
if condition1 do
    // executes if condition1 is true
end 
elif condition2 do
    // executes if condition2 is true
end 
else do
    // executes if all conditions are false
end
```


### Using an assignment as a condition

You should almost never have an if...else with an assignment like x = y as a condition:

````ysharp
var x;

// BAD
if ((x = y)) do
  // …
end

// OK
x = y;
if x do
  // …
end
````

### Truthy and Falsy Values

In Ysharp, any value used in a conditional context (such as an `if` statement) is automatically converted to a boolean.

---

### Falsy Values

The following values are considered **falsy**, meaning they evaluate to `false`:

- `false`
- `null`
- `0`
- `""` (empty string)

### Example

```ysharp
if 0 then do
    print("will not run");
end

if "" then do
    print("will not run");
end
```

### Notes

- Only one branch will execute.
- Conditions are evaluated from top to bottom.
- Use elif to avoid deeply nested if statements.