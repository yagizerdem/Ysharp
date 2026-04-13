---
sidebar_position: 5
---

## Primitive & Objects

Ysharp is a dynamic typed language with support of optional type tags attributes. 
Type system works like Type Script but it checks type compatibility in runtime instead 
of compile time.

In Ysharp, every variable is **NOT** derived from object type. 
There are primitives and object types.

There are 5 primitive types in Ysharp
- int
- double
- char
- bool
- null

There are 3 object  types in Ysharp
- function
- class
- string

> <b>**Note**</b> <br/>
> Strings are categorized under object but parser has built in support for
> creating strings so you can use shorthand for creating string just like primitive types. 
> I believe using string as primitive is more natural to most of the programmers so i added this feature.
> 
> For example : 
>```ysharp
> var f_name = "yagiz"; // shorthand for creating string
> var l_name = new String("erdem"); // explicit object syntax
> println f_name + " " + l_name; // yagiz erdem
> ```

**Under string section you can access full api documentation .**

## Naming Conventions

Always use the same coding conventions for all your Ysharp projects.

Coding conventions are style guidelines for programming. They typically cover:
- Naming and declaration rules for variables and functions.
- Rules for the use of white space, indentation, and comments.
- Programming practices and principles.

Coding conventions secure quality:
- Improve code readability
- Make code maintenance easier

Coding conventions can be documented rules for teams to follow, or just be your individual coding practice.

Always use the same naming convention for all your code. For example:

- Variable and function names written as camelCase
- Global variables written in UPPERCASE (We don't, but it's quite common)
- Constants (like PI) written in UPPERCASE

<b>**Underscores:**</b><br/>
Many programmers prefer to use underscores (date_of_birth), especially in SQL databases.
Underscores are often used in PHP documentation.

<b>**PascalCase:**</b> <br/>
PascalCase is often preferred by C programmers.


<b>**camelCase:**</b> <br/>
camelCase is used by Ysharp itself.

> <b>**Note**</b> <br/>
> All characters in identifier must be alfa-numeric and identifier cannot start with numeric 
> character .
> 
> For example:
> ```ysharp
> var 1a = 40; // Syntax error
> var a1 = 40; // OK
> var _a = 40; // OK variables can start with underscore
> ```

## Primitives
In Ysharp, a primitive (primitive value, primitive data type) is data that is not an object and has no methods or properties.
Most of the time, a primitive value is represented directly at the lowest level of the language implementation.

All primitives are immutable, they cannot be altered. 
It is important not to confuse a primitive itself with 
a variable assigned a primitive value. 
The variable may be reassigned to a new value, 
but the existing value can not be changed in the
ways that objects, arrays, and functions can be altered.
The language does not offer utilities to
mutate primitive values.
```ysharp
// int
var age = 21;
println age; // 21

// double
var pi = 3.14;
println pi; // 3.14

// char
var letter = 'A';
println letter; // A

// bool
var isActive = true;
println isActive; // true

// null
var data = null;
println data; // null
```

## Objects

Objects are runtime structures that has prototype, instance/object
level method/properties.
Objects are specialized under 3 categories which are strings, functions, classes.

### Functions & Classes

In Ysharp, functions and classes are categorized into subtypes based on how they are defined and used.

#### Function Types
Ysharp supports three types of functions:

- **User defined functions** <br/>
  Functions declared by the user using the function keyword.


- **Lambda expressions** <br/>
  Anonymous functions that can be defined inline.
  They are similar to lambda (arrow) functions in JavaScript.


- **Native functions** <br/>
  Built in functions provided by the Ysharp runtime and standard library.

#### Class Types

Ysharp supports two types of classes:

- **User defined classes** <br/>
  Classes created by the user using the class keyword.


- **Native classes** <br/>
  Built in classes provided by the Ysharp runtime and standard library.

You can visit class, function and string api documentation to 
get more details, theese topics are very deep and i dont want to
go over every single api in this section.