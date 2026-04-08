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
````
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
```
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

````
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
```
// variable declarations in same scope example
    let a = 10;
    let a = 20; // SyntaxError
```

<br/>

```
let a = 10;
do
    let a = 20; // shadows parent declaration
    println a; // pritns 20
end
println a; // prints 10
```

<br/>

- cannot redeclare let declarations with var.

```
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

```
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