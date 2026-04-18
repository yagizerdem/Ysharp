### General

Ysharp functions are blocks of code that perform a specific task.
Functions allows us to reuse code, improving both efficiency and organization.

- Functions are reusable code blocks designed to perform a particular task. (write once, run many times)
- Functions are executed when they are called or invoked.
- Functions organize code into smaller parts
- Functions make code easier to read and maintain

A function can be created with the function keyword, a name, and block statement.
The code to run is written inside block statement.

#### Example

````ysharp
function sayHello() do 
    return "Hello World"; 
end
````

> **Note** <br/> 
> The function above does not do anything. It has to be called first.


````ysharp
function sayHello() do
  return "Hello World";
end

let message = sayHello();
println message;
````


> **Note** <br/>
> () means execute now.

## Function Syntax

- Functions are defined with the function keyword:
- followed by parentheses ( )
- followed by do..end block

A function name must follow the same naming rules as variables.

Parameters are optional and are defined inside parentheses:
`(p1, p2, ...)`

The function body is defined inside a `do ... end` block.

Functions may optionally return a value to the caller using the `return` statement.
If no value is returned, the function implicitly returns `null`.


>  Note <br/>
> Function parameters and return type support type tags. You can optionally use type tags to
> explicitly declare types.

#### Example

````ysharp
function sum2Num(a, b) do
    return a + b;
end

// parameter can take type tags
function sum3Num(a : number, b:number, c : number) do
    return a + b + c;
end

// paramters and retun value can take type tags
function sum4Num(a : number, b : number, c : number , d : number) : number do
    return a + b + c + d;
end

println sum2Num(1 ,2);
println sum3Num(1 ,2, 3);
println sum4Num(1 ,2, 3, 4);
````


Functions can pass parameter as other functions :

- This example demonstrate quicksort algorithm that accepts comparator 
from outside as function parameter , and performs not in-place quick sort algorithm.

````ysharp
function quickSort(arr : Array, cmp : function ) : Array do
    if arr.size() <= 1 then do
        return arr;
    end

    var pivot = arr.get(0);

    var L = arr.filter((x) => cmp(x, pivot) < 0);
    var eq = arr.filter((x) => cmp(x, pivot) == 0);
    var R = arr.filter((x) => cmp(x, pivot) > 0);

    L = quickSort(L, cmp);
    R = quickSort(R, cmp);

    var result = [];
    result = result.concat(L);
    result = result.concat(eq);
    result = result.concat(R);

    return result;
end

var arr = [];

for var i in 1..10 do
    arr.push(Random.nextInt(0,100));
end


function comparator(a : number, b : number) do
   return a > b ? 1 : (a == b ? 0 : -1);
end

arr = quickSort(arr, comparator);

// sorted in ascending order
println arr.toString();
````

## Function overload

Function Overloading is a feature found in many object-oriented programming languages,
where multiple functions can share the same name but differ in the number or type of parameters.
While languages like C++ and Java natively support function overloading,
Ysharp supports Function Overloading in a different way.

### Ysharp Overloading Rules

- Functions can share the same name **only if their parameter count is different**.
- Function resolution is based on:
    - function name
    - number of parameters (arity)

- Parameter **types are NOT considered** during overload resolution.

### Constraints

- If two functions have the same name **and the same number of parameters**, an error is thrown:
### Example

- This is valid since function have different parameter count.
```ysharp
function sum(x) do
  println(x);
end

function sum(x, y) do
  println(x + " " + y);
end
````
- This is **invalid** since function have same name and same parameter count.

```ysharp
function sum(x, y) do
  println(x);
end

function sum(x, y) do
  println(x + " " + y);
end
````

> **Note** <br/>
> Ysharp does NOT support type-based overloading dynamic dispatch based on parameter types.
> Instead, it relies purely on arity-based dispatch, making the function resolution simple and predictable.


## Lambda Expressions

Lambda Expressions is a function without a name, mainly used for specific or short-term tasks, 
and is often assigned to variables or passed as arguments where reuse is not required.

- It omits the function name and is defined using the function keyword or arrow syntax.
- It is commonly used as callbacks or for one-time execution within a limited scope.
- They are lightweight compared to regular functions

### Types of Lambda Expressions

Ysharp supports two types of lambda expressions:


#### 1. Expression Lambda

This form consists of a single expression and returns its result implicitly.

<code>
(parameters) => expression
</code>

- The expression is evaluated
- The result is automatically returned
- No return keyword is required

````
var add = (a, b) => a + b;

println(add(2, 3));   // 5

var add = (a : number, b : number) : number => a + b;

println(add(2, 3));   // 5
````


#### 2. Block Lambda

This form uses a do ... end block and allows multiple statements.

<code>
(parameters) => do
    statements
end
</code>

- Multiple statements can be written
- return must be used explicitly to return a value

````ysharp
var add = (a, b) => do
    var result = a + b
    return result
end

println(add(2, 3))   // 5
````

### Examples


````ysharp
function apply(x, fn) do
    return fn(x);
end

println(apply(5, (x) => x * 2));
````

````ysharp
var arr = [1, 2, 3, 4];

var doubled = arr.map((x) => x * 2);

println(doubled.toString());   // [2, 4, 6, 8]
````

> Notes <br/>
> - Expression lambdas are concise and preferred for simple operations
> - Block lambdas are used when multiple steps are required
> - Only block lambdas support explicit return