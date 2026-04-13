---
sidebar_position: 3
---

## Basics
This chapter discusses Ysharp's basic grammar, variable declarations, data types and literals.

Ysharp borrows most of its expression grammer from C language. All of the C expression grammer is available on top of that 
Ysharp suppoprts modern expressions like ternary operator,  nullish coalescing operator, pipes ex...

Ysharp is **case sensitive** and uses the Unicode character set. 
For example, the word Bihaber (which means "unaware" in Turkish) could be used as a variable name.
``var Bihaber = "<some string data here>"``

But Bihaber is not same with bihaber because Ysharp is case sensitive.

In Ysharp, instructions are called statements and are separated by semicolons (;).

> **NOTE** :  <br/>
> Statement and expression parse trees are distinct in Ysharp, the main conjunction point of these separate parse trees are 
> expression statements which is a special kind of statement that binds expressions into statement abstract syntax tree. 
> This also mens that there is 2 different evaluators in Ysharp interpreter that one evaluates only expression grammar and 
> other evaluates statements.

## Comments
The syntax of comments is the same as in C++ and in many other languages:
````ysharp
// a one line comment

/* this is a longer,
 * multi-line comment
 */
````

You can't nest block comments. This often happens when you accidentally include a */ 
sequence in your comment, which will terminate the comment. <br/>
``
/* You can't, however, /* nest comments */ SyntaxError */
``

In this case, you need to break up the */ pattern. For example, by inserting a backslash: <br/>
``
/* You can /* nest comments *\/ by escaping slashes */
``

- Comments behave like whitespace, and are discarded after Ysharp preprocessing phase.

## Declarations
Ysharp has three kinds of variable declarations.

<u>**var**</u> <br/>
Declares block scoped variable, optionally initializing it to a value. Variables declared with var declaration
can be redeclared in same block, or nested block that shadows parents declaration.
For example : <br/> 
```ysharp
// variable declarations in same scope example
var a = 10;
println(a); // prints 10
var b = 20;
var b = 30; // overrides b variable in same block
println(b); // prints 30
var c = 40;
var c;
println(c); // prints null
var d;
println(d); // prints null
```

<br/> 

````ysharp
// variable declarations in nested scopes
var a = 10;
do
    var a = 20;
    println(a); // prints 20
end
println(a); // prints 10
````

<u>**let**</u> <br/>
Declares block scoped variable, optionally initializing it to a value. Variables declared with let declaration
**<b>cannot be redeclared in same block</b>** . <br/>
For example : <br/> 
```ysharp
// variable declarations in same scope example
let a = 10;
let a = 20; // SyntaxError
```

<br/>

```ysharp
let a = 10;
do
    let a = 20; // shadows parent declaration
    println a; // pritns 20
end
println a; // prints 10
```

<br/>

- cannot redeclare let declarations with var.

```ysharp
// cannot redeclare let declarations with var
let a = 10;
var a = 20; // SyntaxError

/* cannot redeclare even var declerations with let, 
*  program must use var declarations to redeclare 
*/ 
var b = 10;
let a = 20; // SyntaxError

// this is OK 
var c = 50;
var c = 60;
```

<u>**const**</u> <br/>
Declares block scoped variable with **<b>Must have variable initializers</b>** . 
Const values are runtime constant that cannot be reassigned , or redeclared in same block.
<br/>
For example : <br/> 

```ysharp
// example 1
const a; // SyntaxError, constants must have initializer

// example 2
const a = 10; // this is OK

// example 3
const b = 20;
b = 30; // SyntaxError, constants cannot reassigned

// example 4
const c = 40;
do
    const c = 50;
    println c; // prints 50
end
println c; // prints 40

// example 5
const d = 60;
const d = 70; // SyntaxError, cannot redeclare constants
```

## Scopes
**<b>Scope = Visibility</b>** <br/>
Scope determines the accessibility (visibility) of variables.
Ysharp variables have 4 types of scope:
- Global scope
- Function scope
- Block scope
- Object scope

---

### Block Scope

> 
> **(`do ... end`)**
> 
> A code block or block statement is a group of statements enclosed within do end keywords.
> 
> Code blocks are important for controlling the flow of execution and defining variable scope within a Ysharp program.

- block statements create a new environment and linked to parent environment so that variables 
declared in block does not overwrite globals.

```ysharp
// global environment
do
    // block scope
    var x = 10;
    println x;   // 10
    
    /*
    * you can still access 
    * global environment in block
    */
    
    /*
    * Random object comes from 
    * globals so that you can access
    */
    println Random.nextInt(0, 10);
end

println x;       // SyntaxError x is not defined
```

---

### Nested Block Scopes

Block scopes can be nested inside one another. Each inner block creates its own environment
and is linked to its parent, which in turn is linked to the global environment. This forms a
**scope chain**.

When a variable is declared in an inner block with the same name as one in an outer block,
it **shadows** the outer declaration. The outer variable is not modified it is simply
hidden while the inner block is active.

```ysharp
var x = 10; // global scope

do
    var x = 20; // outer block shadows global x
    println x;  // 20

    do
        var x = 30; // inner block shadows outer x
        println x;  // 30
    end

    println x; // 20 inner block ended, outer x is restored
end

println x; // 10 outer block ended, global x is restored
```

#### How the scope chain works

When a variable is accessed, Ysharp searches for it starting from the **current scope** and
walks outward toward the global scope until it finds a matching declaration. This is called
**lexical scoping**.

1. Is the variable declared in the current block? &#8594; Use it.
2. If not, check the parent block.
3. If not found anywhere, a `SyntaxError` is thrown (`x is not defined`).

#### Variable declarations in nested scopes

| Declaration | Can shadow in nested block? | Can redeclare in same block? |
|---|-----------------------------|----------------------------|
| `var` | &#10003; Yes| &#10003; Yes|
| `let` | &#10003; Yes| &#10005; No (`SyntaxError`)|
| `const` | &#10003; Yes| &#10005; No (`SyntaxError`)|

Example with `let` and `const`:

```ysharp
let a = 1;
do
    let a = 2;   // OK shadows parent, not a redeclaration
    println a;   // 2
end
println a;       // 1

const b = 100;
do
    const b = 200; // OK shadows parent constant
    println b;     // 200
end
println b;         // 100
```

> **NOTE:**  
> Shadowing does not mutate the outer variable. Once the inner block ends,
> the outer variable is accessible again with its original value unchanged.

---

### Function Scope

Each Ysharp function have their own scope.
Variables declared with var, let and const are quite similar when declared inside a function.

- They all have <b>**Function Scope**</b> :
```ysharp
function myFunction1() do
  var carName = "Volvo";  // Function Scope
end

function myFunction2() do
  let carName = "Volvo";  // Function Scope
end

function myFunction3() do
  const carName = "Volvo";  // Function Scope
end
```

#### Functions do not share scope

Each function call creates a **new, independent environment**. Variables declared in one
function are invisible to other functions, even if they share the same name.

```ysharp
function foo() do
  var result = 42;
  println result; // 42
end

function bar() do
  println result; // SyntaxError result is not defined
end
```

#### Functions can access the global scope

A function can read and write variables declared in the global scope, as long as
no local variable shadows them.

```ysharp
var appName = "Ysharp";

function greet() do
  println appName; // "Ysharp" read from global scope
end

greet();
```

If a local variable shadows a global one, the global is unaffected:

```ysharp
var appName = "Ysharp";

function greet() do
  var appName = "SomethingElse"; // shadows global
  println appName; // "SomethingElse"
end

greet();
println appName; // "Ysharp" global unchanged
```

#### Block scope inside functions

Functions can contain block statements (`do ... end`). Blocks inside a function follow
the same nesting rules as anywhere else inner blocks shadow outer ones.

```ysharp
function calculate() do
  var total = 0;

  do
    var bonus = 100;
    total = total + bonus; // can access function scope 'total'
    println total; // 100
  end

  println bonus; // SyntaxError bonus is not defined outside the inner block
end
```

### Object Scope
Classes are template of creating runtime objects. Infact classes are prototype based objects similar to
javascript's class syntax, but there is no explicit api for creating objects other than class syntax.

Other scope rules are applied for class object scope as well.

-  Class objects also link their current environment with parents environment.

```ysharp
var global_a = 10;
class Human {
    var f_name = "yagiz";
    var l_name = "erdem";
    
    fullName() do
        // access the instance members
        println this.f_name + " " + this.l_name; // "yagiz erdem"
    end
    
    accessGlobal() do
        /* can access parents environment which 
        * is global env. in this example
        */
        println global_a; // 10
    end
}

var h = new Human();

h.fullName();
h.accessGlobal();
```

- For more examples and explanations, see the classes guide.

---

## Literals
Literals represent values in Ysharp.
These are fixed values not variables that you literally provide in your script.
This section describes the following types of literals:

- Boolean literals
- Numeric literals
- String literals
- Array literals
- HashMap literals

### Boolean Literals
The Boolean type has two literal values: true and false.

### Numeric Literals

Ysharp supports two types of numeric literals, 
based on their underlying representation: integers and floating point numbers.

- Integers: Whole numbers without a fractional component. (Handled as Java int internally).
- Floating point: Numbers containing a decimal point or an exponent. (Handled as Java double internally).

#### Integer Literals
- Integers can be written in decimal (base 10).
```ysharp
var count = 42;
var negative = -100;
var zero = 0;
```

#### Floating-point Literals
A floating-point literal includes a decimal point.
```ysharp
var pi = 3.14159;
var price = 99.90;
```

> **NOTE** <br/>
> Unlike some languages, Ysharp automatically treats a number as a 
> floating point if it contains a decimal point. 
> Otherwise, it is treated as an integer.

### String Literals

Strings are not primitive types but can be used like primitive's with string 
initialization shortcut, or you can also use new expression to take instance from 
base string class.

For example :
```
const f_name = "yagiz";
const l_name = new String("erdem");
const full_name = f_name + " " + l_name;
print full_name; // "yagiz erdem"
```

Unlike C/C++ strings are very powerful data type in Ysharp and there is rich set 
of String API to work with strings. You can visit String section to review full
API documentation. 

### Array Literals

Arrays represent ordered collections of values.  
They can store elements of any type and maintain insertion order.

An array literal is defined using square brackets `[]`.

```ysharp
var numbers = [1, 2, 3, 4, 5];
var mixed = [1, "hello", true, 3.14];
println mixed; // <instance:Array>
println  numbers.toString(); // "[1, 2, 3, 4, 5]"
```

You can visit Collections section to review full API documentation.

### HashMap Literals

HashMaps represent unordered collections of key-value pairs.  
They allow efficient lookup, insertion, and deletion based on keys.

A HashMap literal is defined using curly braces `{}`.

```ysharp
var person = {
    "name": "yagiz",
    "age": 23,
    "isStudent": true
};
```

<b>**Key Restrictions**</b> <br/>
In Ysharp, **only string keys** are allowed in HashMap literals.

```ysharp
var valid = {
    "a": 1,
    "b": 2
};

var invalid = {
    1: "one",      // SyntaxError
    true: "yes"    // SyntaxError
};
```

- If programmer want to use other type of keys rather than string, use put method 
under built-in HashMap collection class rather than shortcut `curly brace {}` syntax.

> **NOTE**  <br/>
> This `array literal []`, `hash-map literal {}` and `"string data"`  are actually shorthand 
> for creating **arrays**, **hash-map** and **string** rather than **`new Array()`**, **`new HashMap()`** 
> **`new String("string data")`** syntax. I intentionally embed shorthands to parser for 
> making Ysharp feel's like scripting language. I try to make Ysharp syntax less 
> verbose as much as possible. I did not add other shorthands like Stack, Queue, LinkedLists
> or other data structures because 95% percent of the time developer's only use arrays and hash-maps.
> Who the fuck uses Stack or Queue to create Desktop Applications ?

## Binary Expressions

Binary expressions combine two operands with an operator and always produce a value.
Ysharp inherits all binary operators from C, and adds a few modern additions such as the
range operator (`..`) and the nullish coalescing operator (`??`).

---

### Arithmetic Operators

Arithmetic operators perform mathematical calculations on numeric values.

| Operator | Description | Example |
|----------|-------------|---------|
| `+` | Addition | `3 + 5` &#8594; `8` |
| `-` | Subtraction | `10 - 4` &#8594; `6` |
| `*` | Multiplication | `3 * 7` &#8594; `21` |
| `/` | Division | `10 / 4` &#8594; `2` |
| `%` | Remainder (modulo) | `10 % 3` &#8594; `1` |

> **NOTE**  
> Division between two **integers** performs **integer division**  the fractional part
> is discarded. If you need a floating-point result, at least one operand must be a `double`.

```ysharp
var a = 10 / 3;    // 3      integer division
var b = 10.0 / 3;  // 3.333  double division
var c = 10 / 3.0;  // 3.333  double division

println a;
println b;
println c;
```

---

### Comparison Operators

Comparison operators compare two values and return a `bool` (`true` or `false`).

| Operator | Description | Example                 |
|----------|-------------|-------------------------|
| `==` | Equal to | `5 == 5` &#8594; `true` |
| `!=` | Not equal to | `5 != 3` &#8594; `true` |
| `>` | Greater than | `7 > 3` &#8594; `true`  |
| `>=` | Greater than or equal to | `5 >= 5` &#8594; `true` |
| `<` | Less than | `2 < 8` &#8594; `true`  |
| `<=` | Less than or equal to | `3 <= 3` &#8594; `true`        |

```ysharp
var x = 10;
var y = 20;

println x == y;   // false
println x != y;   // true
println x < y;    // true
println x >= 10;  // true
```

---

### Logical Operators

Logical operators work on `bool` values and return a `bool`.

| Operator | Description |
|----------|-------------|
| `&&` | Logical AND  `true` only if **both** operands are `true` |
| `\|\|` | Logical OR  `true` if **at least one** operand is `true` |

Both operators use **short-circuit evaluation**:

- `&&`  if the left operand evaluates to `false`, the right operand is **not evaluated**.
- `||`  if the left operand evaluates to `true`, the right operand is **not evaluated**.

```ysharp
var a = false;
var b = true;

println a && b; // false
println a || b; // true

// someFunc() is never called because the left side is already false
println false && someFunc();

// someFunc() is never called because the left side is already true
println true || someFunc();
```

---

### Bitwise Operators

Bitwise operators work on the binary representation of **integer** values.

| Operator | Description | Example              |
|----------|-------------|----------------------|
| `&` | Bitwise AND | `6 & 3` &#8594; `2`  |
| `\|` | Bitwise OR | `6 \| 3` &#8594; `7` |
| `^` | Bitwise XOR | `6 ^ 3` &#8594; `5`  |
| `<<` | Left shift | `1 << 3` &#8594; `8` |
| `>>` | Right shift | `8 >> 2` &#8594; `2` |


> **NOTE**  
> Applying bitwise operators to `double` values causes a runtime error.
> Always ensure both operands are integers when using bitwise operators.

---

### Range Operator

The `..` operator constructs a **range** value from two integer bounds. Ranges are
primarily used in `for` and `foreach` loops and collection operations.

```ysharp
// Iterate 1 through 10 (inclusive)
for var i in 1..10 do
    print i + " ";
end
// Output: 1 2 3 4 5 6 7 8 9 10

// Store a range in a variable
var r = 1..5;
for var n in r do
    println n;
end
```

---

### Nullish Coalescing Operator

The `??` operator returns the left-hand operand if it is **not null**, otherwise it
evaluates and returns the right-hand operand.

```ysharp
var name = null;
var displayName = name ?? "Anonymous";
println displayName;  // "Anonymous"

var count = 0;
println count ?? 99;  // 0 count is not null, so it is returned as-is
```

This is particularly useful for providing default values without verbose `if` checks.

```ysharp
// Without ??
var value = null;
var label;
if value == null then do
    label = "N/A";
end else do
    label = value;
end

// With ??
var label = value ?? "N/A";
print label;
```

---

### Ternary Conditional Operator

The `? :` operator evaluates a condition and returns one of two values depending on
whether the condition is `true` or `false`.

```ysharp
var age = 20;
var status = age >= 18 ? "adult" : "minor";
println status;  // "adult"
```

Unlike an `if` statement, the ternary operator is an **expression**  it produces a value
and can be used anywhere an expression is expected.

```ysharp
var x = 10;
println x > 0 ? "positive" : "non-positive";

// Inside string concatenation
var n = 3;
var msg = "Result: " + (n % 2 == 0 ? "even" : "odd");
println msg;
```

The false branch can itself be another ternary expression, allowing multiple conditions
to be chained.

```ysharp
var score = 72;
var grade = score >= 90 ? "A"
          : score >= 75 ? "B"
          : score >= 60 ? "C"
          : "F";
println grade;  // "C"
```

---

### Operator Precedence

When multiple operators appear in the same expression, Ysharp evaluates them in a
fixed order from **highest** to **lowest** precedence. Operators with higher precedence
bind more tightly to their operands.

| Precedence | Operator(s) | Category |
|-----|-------------|----------|
| 1 (highest) | `*`  `/`  `%` | Multiplicative |
| 2 | `+`  `-` | Additive |
| 3 | `..` | Range |
| 4 | `<<`  `>>` | Bitwise shift |
| 5 | `<`  `<=`  `>`  `>=` | Comparison |
| 6 | `==`  `!=` | Equality |
| 7 | `&` | Bitwise AND |
| 8 | `^` | Bitwise XOR |
| 9 | `\|` | Bitwise OR |
| 10 | `&&` | Logical AND |
| 11 | `\|\|` | Logical OR |
| 12 | `??` | Null coalescing |
| 13 (lowest) | `? :` | Ternary conditional |

```ysharp
// Precedence in action
println 2 + 3 * 4;     // 14  * before +
println (2 + 3) * 4;   // 20  parentheses override precedence

println true || false && false;   // true  && before ||
println (true || false) && false; // false

println null ?? 1 + 2;  // 3 + before ??
```

Use parentheses whenever the intended evaluation order may not be obvious to the reader.
Explicit grouping improves clarity and prevents subtle bugs.

---

### Associativity

All binary operators in Ysharp are **left-associative**: when the same operator appears
multiple times in a row, it is evaluated from left to right.

```ysharp
println 10 - 3 - 2;   // (10 - 3) - 2 = 5, not 10 - (3 - 2) = 9
println 2 * 3 * 4;    // (2 * 3) * 4 = 24
println 1 ?? 2 ?? 3;  // (1 ?? 2) ?? 3 = 1
```

---

### Type Rules

Ysharp enforces the following type rules on binary expressions at runtime.

| Operator group | Allowed operand types |
|----------------|-----------------------|
| `+` `-` `*` `/` `%` | `int`, `double` (mixed operands produce `double`) |
| `&` `\|` `^` `<<` `>>` | `int` only |
| `==` `!=` | Any type |
| `<` `<=` `>` `>=` | `int`, `double` |
| `&&` `\|\|` | `bool` only |
| `..` | `int` only |
| `??` | Any type |

The `+` operator is additionally overloaded for **string concatenation**. If either
operand is a `string`, Ysharp converts the other operand to its string representation
and concatenates them.

```ysharp
println "Score: " + 100;    // "Score: 100"
println 3.14 + " is pi";    // "3.14 is pi"
println "x = " + true;      // "x = true"
```

---

## Unary Expressions

Unary operators take a single operand and produce a new value. Ysharp supports both
**prefix** operators (placed before the operand) and **postfix** operators (placed after
the operand).

### Arithmetic Unary Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `-` | Negation flips the sign of a number | `-x` |
| `+` | Unary plus no effect, returns the value as-is | `+x` |

```ysharp
var x = 10;
println -x;   // -10
println +x;   //  10

var y = -3.5;
println -y;   // 3.5
```

---

### Logical NOT Operator

The `!` operator inverts a `bool` value.

```ysharp
println !true;   // false
println !false;  // true

var isLoggedIn = false;
if !isLoggedIn then do
    println "Please log in.";
end
```

---

### Bitwise NOT Operator

The `~` operator flips all bits of an **integer** value (ones' complement).

```ysharp
println ~0;   // -1
println ~1;   // -2
println ~7;   // -8
```

> **NOTE**  
> `~` only works on `int` values. Applying it to a `double` causes a runtime error.

---

### Increment and Decrement Operators

Ysharp supports both prefix and postfix forms of `++` and `--`.

| Operator | Form | Description |
|----------|------|-------------|
| `++` | Prefix `++x` | Increments `x` by 1, returns the **new** value |
| `++` | Postfix `x++` | Increments `x` by 1, returns the **original** value |
| `--` | Prefix `--x` | Decrements `x` by 1, returns the **new** value |
| `--` | Postfix `x--` | Decrements `x` by 1, returns the **original** value |

```ysharp
var a = 5;
println ++a;  // 6 a is now 6
println a++;  // 6 returns original value, a is now 7
println a;    // 7

var b = 10;
println --b;  // 9  b is now 9
println b--;  // 9  returns original value, b is now 8
println b;    // 8
```

> **NOTE**  
> Increment and decrement operators can only be applied to **variables** (lvalues).
> Applying them to a literal or expression like `5++` causes a `SyntaxError`.

---

### Operator Precedence

Unary operators have higher precedence than all binary operators. When a unary and a
binary operator appear together, the unary binds first.

```ysharp
println -2 + 3;    // 1  (-2) + 3
println !true || false;  // false (!true) || false
```

Postfix operators (`x++`, `x--`) bind more tightly than prefix operators.

```ysharp
var x = 3;
println -x++;  // -3 reads as -(x++), returns -(original x), then x becomes 4
println x;     // 4
```
