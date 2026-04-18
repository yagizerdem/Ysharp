---
sidebar_position: 16
---


## Closures

A closure is the combination of a function bundled together 
(enclosed) with references to its surrounding state (the lexical environment). 
In other words, a closure gives a function access to its outer scope. 
In Ysharp, closures are created every time a function is created, at function creation time.


### Lexical scoping

Consider the following example code:

````ysharp
function init() do
    var name = "Mozilla";

    function displayName() do
        println(name);
    end

    displayName();
end

init();
````

init() creates a local variable called name and a function called displayName(). 
The displayName() function is an inner function that is defined inside init()
and is available only within the body of the init() function.
Note that the displayName() function has no local variables of its own. 
However, since inner functions have access to the variables of outer scopes,
displayName() can access the variable name declared in the parent function, init().

If you run this code in your terminal, 
you can see that the println statement within the displayName() function successfully displays the value of the name variable,
which is declared in its parent function. 
This is an example of lexical scoping, which describes how a parser resolves 
variable names when functions are nested. 
The word lexical refers to the fact that lexical scoping uses the 
location where a variable is declared within the source code to
determine where that variable is available.
Nested functions have access to variables declared in their outer scope.


### Closure

Consider the following code example:

````ysharp
function makeFunc() do
    var name = "Mozilla";

    function displayName() do
        println(name);
    end

    return displayName;
end

var myFunc = makeFunc();
myFunc();
````


Running this code has exactly the same effect as the previous example of the init() function above. 
What's different (and interesting) is that the displayName() inner function is returned from the outer function before being executed.


At first glance, it might seem unintuitive that this code still works. In some programming languages, 
the local variables within a function exist for just the duration of that function's execution. 
Once makeFunc() finishes executing, you might expect that the name variable would no longer be accessible. 
However, because the code still works, this is obviously not the case in Ysharp.


The reason is that functions in Ysharp form closures. A closure is the combination of a 
function and the lexical environment within which that function was declared. 
This environment consists of any variables that were in-scope at the time the closure was created. In this case, 
myFunc is a reference to the instance of the function displayName that is created when makeFunc is run.
The instance of displayName maintains a reference to its lexical environment, 
within which the variable name exists. For this reason, when myFunc is invoked, 
the variable name remains available for use, and "Mozilla" is passed to println.


Here's a slightly more interesting example a makeAdder function: 

````ysharp
function makeAdder(x) do
    return (y) => do
        return x + y;
    end;
end

var add5 = makeAdder(5);
var add10 = makeAdder(10);

println(add5(2));   // 7
println(add10(2));  // 12
````

In this example, we have defined a function makeAdder(x),
that takes a single argument x, and returns a new function. 
The function it returns takes a single argument y, and returns the sum of x and y.

In essence, makeAdder is a function factory. It creates functions that can add a specific
value to their argument. In the above example, the function factory creates
two new functions one that adds five to its argument, and one that adds 10.

add5 and add10 both form closures. 
They share the same function body definition, but store different lexical environments.
In add5's lexical environment, x is 5, while in the lexical environment for add10, x is 10.


### Practical closures

Closures are useful because they let you associate data (the lexical environment) with a function that operates on that data. 
This has obvious parallels to object-oriented programming, 
where objects allow you to associate data (the object's properties) with one or more methods.

Consequently, you can use a closure anywhere that you might normally use an object with only a single method.

````ysharp
function createBankAccount(initialBalance) do
    var balance = initialBalance;

    return (amount) => do
        balance = balance + amount;
        return balance;
    end;
end

var account = createBankAccount(100);

println(account(50));   // 150
println(account(-20));  // 130
println(account(10));   // 140
````


### Emulating private methods with closures


Languages such as Java allow you to declare methods as private, meaning that they can be called only 
by other methods in the same class.

Ysharp, prior to classes, didn't have a native way of declaring private methods, 
but it was possible to emulate private methods using closures. 
Private methods aren't just useful for restricting access to code.
They also provide a powerful way of managing your global namespace.

````ysharp
function createCounter() do
    var privateCounter = 0;

    function changeBy(val) do
        privateCounter = privateCounter + val;
    end

    return {
        "increment" : () => do
            changeBy(1);
        end,

        "decrement" : () => do
            changeBy(-1);
        end,

        "value" : () => do
            return privateCounter;
        end
    };
end

var counter = createCounter();


println counter.get("value")() ; // 0

counter.get("increment")();
counter.get("increment")();
println(counter.get("value")()); // 2

counter.get("decrement")();
println(counter.get("value")()); // 1
````


In the previous examples, each closure had its own lexical environment.  
In this example, however, multiple functions share a single lexical environment.

This shared lexical environment is created inside a function (e.g., `createCounter`).  
When the function is called, it initializes its local variables and returns a set of functions.

The lexical environment contains private members:
- a variable `privateCounter`
- a helper function `changeBy`

These members cannot be accessed directly from outside the function.  
Instead, they are accessed indirectly through the returned functions.

The returned functions (`increment`, `decrement`, and `value`) are closures that all share the same lexical environment.  
Because of Ysharp’s lexical scoping rules, each of these functions can access and modify the same `privateCounter` variable and `changeBy` function.

### Closure scope chain

A nested function's access to the outer function's scope includes the enclosing scope of 
the outer function effectively creating a chain of function scopes. 
To demonstrate, consider the following example code.


- with statement lambda

````ysharp
var e = 10;

function sum(a) do
    return (b) => do
        return (c) => do
            return (d) => do
                return a + b + c + d + e;
            end;
        end;
    end;
end

println(sum(1)(2)(3)(4)); // 20
````

- same example with expression lambda

````ysharp
var e = 10;

function sum(a) do
    return (b) => (c) => (d) => a + b + c + d + e;
end

println(sum(1)(2)(3)(4)); // 20
````

In the example above, there's a series of nested functions,
all of which have access to the outer functions' scope. 
In this context, we can say that closures have access to all outer scopes.

### Performance considerations

As mentioned previously, each function instance manages its own scope and closure. 
Therefore, it is unwise to unnecessarily create functions within other functions if 
closures are not needed for a particular task, as it will negatively 
affect program performance both in terms of processing speed and memory consumption.


For instance, when creating a new object/class, 
methods should normally be associated to the object's prototype rather than defined into the object constructor. 
The reason is that whenever the constructor is called, 
the methods would get reassigned (that is, for every object creation).


````
function createMyObject(name : string, message : string) do
    var _name = name;
    var _message = message;

    return {
        "getName" : () => _name,
        "getMessage" : () => _message
    };
end

var obj = createMyObject("Ali", "Hello");

println(obj.get("getName")());    // Ali
println(obj.get("getMessage")()); // Hello
````


Because the previous code does not take advantage of the benefits of using closures in this particular instance,
we could instead rewrite it to avoid using closures as follows:

````ysharp
class MyObject {
    var name;
    var message;
    constructor(name : string, message : string) do
        this.name = name;
        this.message = message;
    end

    getName() do
        return this.name;
    end

    getMessage() do
        return this.message;
    end
}

var obj = new MyObject("Ali", "Hello");

println(obj.getName());    // Ali
println(obj.getMessage()); // Hello
````

- classes automatically wire prototype chain

## Resolver & Environment Internals 

**This part is related Ysharp environment management and resolver semantic analysis. Do not read if you are not interested.**


### Overview

The **Resolver** and **Environment** are two cooperating components in the Ysharp tree-walk interpreter. Together they handle variable scoping and name resolution   the Resolver performs a static semantic analysis pass before execution, and the Environment is the runtime store where variable values live.

---

### Why a Resolver Exists

In a naive tree-walk interpreter every variable lookup walks the enclosing scope chain at runtime   following a linked list of `Environment` objects until it finds the name or reaches the global scope. This is correct, but slow and brittle: the same chain walk repeats every time a variable is read inside a loop or a function.

The Resolver solves this with a **pre-execution static pass**. Before any statement is executed, the Resolver walks the entire AST and, for every variable reference, calculates exactly how many scope levels away the variable is declared. This integer   the **scope distance**   is handed to the Interpreter once, at resolve time. At runtime the Interpreter skips the chain walk entirely and jumps directly to the correct Environment level.

```
Without Resolver  →  O(d) per lookup  (d = scope depth)
With Resolver     →  O(1) per lookup
```

---

### How the Resolver Works

### Two-Phase Execution

1. **Resolve phase**   The Resolver visits every node in the AST before any code runs.
2. **Execute phase**   The Interpreter uses the pre-computed distances to look up variables instantly.

### Scope Tracking

The Resolver maintains its own lightweight scope stack (a `Stack<Map<String, Boolean>>`) that mirrors the block structure of the source code. This stack exists only during the resolve phase and is completely separate from the runtime Environment chain.

Each entry in the stack is a `Map<String, Boolean>` where:
- `false` means the variable has been **declared** but not yet **defined** (its initializer hasn't been resolved yet).
- `true` means the variable is fully **defined** and available for use.

This two-step process catches a specific class of error: reading a variable in its own initializer:

```ysharp
var x = x + 1;  // Error: Can't read local variable in its own initializer
```

### Distance Calculation

When the Resolver encounters a variable reference, it searches the scope stack from innermost to outermost. The number of levels it traverses becomes the **scope distance**, which is stored by the Interpreter via `interpreter.resolve(expr, distance)`.

```
scope stack (top = innermost):
  [ {z: true} ]          ← distance 0
  [ {y: true} ]          ← distance 1
  [ {x: true} ]          ← distance 2  ← variable found here
  [ globals ]
```

Variables resolved to distance `0` are in the current scope; global variables that don't appear in any tracked scope are looked up through the normal Environment chain as a fallback.

---

### How the Environment Works

### Structure

Each `Environment` holds a flat `Map<String, Variable>` for the current scope and a reference to its `enclosing` (parent) Environment. This forms the familiar linked-list scope chain.

```
Global Environment
  └── Function Environment
        └── Block Environment  ← current scope
```

### Direct Access via Distance

The key runtime optimization enabled by the Resolver is `getAt(distance, name)` and `assignAt(distance, name, value)`. Instead of walking the chain, these methods call `ancestor(distance)` which jumps exactly `distance` links up the chain and reads or writes directly.

```ysharp
// O(d) chain walk   used only for globals / unresolved names
env.getValue(name);

// O(1) direct access   used for all resolved local variables
env.getAt(distance, name);
env.assignAt(distance, name, value);
```

### Variable Lifecycle

| Operation | Method | Notes |
|-----------|--------|-------|
| Create a new variable | `define(name, var)` | Throws if name already exists in this scope   no shadowing within the same scope |
| Read a variable | `getValue(name)` / `getAt(distance, name)` | Throws if undefined |
| Update a variable | `assign(name, value)` / `assignAt(distance, name, value)` | Throws if undefined |
| Remove a variable | `remove(name)` / `removeAt(distance, name)` | Used for cleanup |

### Type Tracking

The Environment maintains a secondary `IdentityHashMap<Variable.Variant, String>` that maps a live value object to its declared type string. This allows type lookups by value identity (`getType(variant)`) in addition to the standard name-based lookup   useful during execution when the name is not in scope but the value object is at hand.

---

### Scoping Rules

These rules are enforced by the Resolver at analysis time, before execution:

- **Re-declaration in the same scope is an error.** Declaring two variables with the same name in the same block throws immediately.
- **Reading before definition is an error.** A variable cannot be referenced while its own initializer is being resolved.
- **Shadowing across scopes is allowed.** A variable in an inner scope may share a name with one in an outer scope; the inner one wins within its block.
- **Global variables are not tracked by the Resolver.** Only locals inside explicit scopes are pre-resolved. Globals fall back to the runtime chain walk.

---

### What the Resolver Covers

The Resolver visits every construct that introduces or references a name:

| Construct | Behavior |
|-----------|----------|
| `var` / `let` / `const` declarations | Declares and defines the identifier in the current scope |
| Function declarations | Defines the function name before resolving the body (allows recursion) |
| Class declarations | Defines the class name; resolves properties and method bodies in nested scopes |
| `for` / `for-in` / `for-each` loops | Opens a new scope for the loop variable |
| Block statements `{ }` | Opens and closes a scope |
| Lambda expressions | Opens a scope for parameters |
| Variable expressions | Resolves to a scope distance |
| Assignment expressions | Resolves both the target and the value |

---

### Error Reporting

The Resolver collects errors into a list rather than stopping at the first one, so multiple problems in the same file are reported together. After the resolve pass, check `resolver.hadErrors()` before proceeding to execution. Individual errors are available in `resolver.errors`.

Common resolver errors:

| Error | Cause |
|-------|-------|
| `Already a variable with this name in this scope` | Two declarations with the same name in one block |
| `Can't read local variable in its own initializer` | Self-referencing initializer |
| `Undefined variable '<name>'` | Name referenced but never declared in any reachable scope |

---

### Summary

| Concern | Handled By | When |
|---------|-----------|------|
| Scope distance calculation | Resolver | Before execution (static pass) |
| Variable storage & retrieval | Environment | At runtime |
| Fast local variable access | `getAt` / `assignAt` | At runtime, using pre-computed distance |
| Semantic error detection | Resolver | Before execution |
| Type-by-value lookup | Environment (`variantTypes`) | At runtime |