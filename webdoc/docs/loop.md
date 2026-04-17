---
sidebar_position: 11
---

## for loop

The for statement provides a compact way to iterate over a range of values. Programmers often refer to it as the "for loop" 
because of the way in which it repeatedly loops until a particular condition is satisfied. The general form of the for statement can be expressed as follows:

`
for initialization; termination; increment do
    statement(s)
end
`

When using this version of the for statement, keep in mind that:

- The initialization expression initializes the loop; it's executed once, as the loop begins. 
- When the termination expression evaluates to false, the loop terminates.
- The increment expression is invoked after each iteration through the loop; it is perfectly acceptable for this expression to increment or decrement a value.


The following program, ForDemo, uses the general form of the for statement to print the numbers 1 through 10 to standard output:

````ysharp
for var i = 1; i <= 10 ; i++ do
    println "Count is :  " + i;
end

/*

The output of this program is:

Count is: 1
Count is: 2
Count is: 3
Count is: 4
Count is: 5
Count is: 6
Count is: 7
Count is: 8
Count is: 9
Count is: 10

*/
````


Notice how the code declares a variable within the initialization expression. 
The scope of this variable extends from its declaration to the end of the block governed by the for statement,
so it can be used in the termination and increment expressions as well. 
If the variable that controls a for statement is not needed outside of
the loop, it's best to declare the variable in the initialization expression.
The names i, j, and k are often used to control for loops; declaring
them within the initialization expression limits their life span and reduces errors.

The three expressions of the for loop are optional; an infinite loop can be created as follows:

````ysharp
for  ; ;  do
    
    // your code goes here
end
````


## while loop

The while statement continually executes a block of statements while a particular condition is true. Its syntax can be expressed as:

````ysharp
while expression do
     statement(s)
end
````

The while statement evaluates expression, which must return a boolean value. 
If the expression evaluates to true, the while statement executes the statement(s)
in the while block. The while statement continues testing the expression and 
executing its block until the expression evaluates to false. Using the while
statement to print the values from 1 through 10 can be accomplished
as in the following WhileDemo program:

````ysharp
var count = 1;

while count < 11 do
    println "Count is : " + count;
    count++;
end
````

You can implement an infinite loop using the while statement as follows:

````ysharp
while true do
    // your code goes here
end
````

## range expression

### syntax

In Ysharp, a **range** represents an interval between two integer values.  
It is primarily used in `for-in` loops.

A range is **not a collection**  it is a lightweight runtime object that only stores `start` and `end` values.

A range can be created in two ways:


#### 1. Function Syntax

`range(start, end)`

#### 2. Operator Syntax
`start..end`

Both forms are completely equivalent, `start..end` is just syntactic sugar over `range(start, end)`


###  range operator precedence


The **range operator (`..`)** in 
Ysharp has a well defined position in the 
expression grammar and operator precedence hierarchy.

Understanding its precedence is critical because it directly affects how expressions are parsed and evaluated.

From the expression grammar:

> bitwise_shift &rarr; range ( ( "&#62;&#62;" | "&#60;&#60;" ) range )*  <br/>
> range         &rarr; term ( ".." term )?  <br/>
> term          &rarr; factor ( ( "-" | "+" ) factor )*  <br/>

This means: <br/> 
- range sits above term 
- range sits below bitwise_shift

From highest &rarr; lowest (relevant part):

````
unary
↓
factor (* / %)
↓
term (+ -)
↓
range (..)
↓
bitwise_shift (<< >>)
↓
comparison
↓
equality
↓
...
````

#### lower than arithmetic

Arithmetic expressions are evaluated before the range operator.

`1 + 2..5` parsed as `(1 + 2)..5`


####  higher than bitwise shift

`1..5 >> 1` parsed as `(1..5) >> 1`


####  range is not fully associative

Only one .. is allowed per expression level

`1..2..3` // syntax error

#### parsing examples

- `1 + 2..5 * 2` parsed as `(1 + 2)..(5 * 2)` equals to `3..10`
- `10 - 3..8`  parsed as `(10 - 3)..8` equals to `7..8`


## for in loop

In Ysharp, the `for-in` loop is **specifically designed to iterate over range expressions**.

- It is **NOT a general iterator-based loop**
- It **only works with range expressions (`..` or `range()`)**

### samples

````ysharp
for var i in 1..5 do
    println(i);
end


/*
    output

    1
    2
    3
    4
    5
*/
````

Equivalent:

````ysharp
for var i in range(1, 5) do
    println(i);
end

/*
    output

    1
    2
    3
    4
    5
*/
````

> Note <br/>
> Range in Ysharp is inclusive on both ends.


## foreach loop 

In Ysharp, the `foreach` loop is used to iterate over **iterable objects**.

Unlike `for-in`, which is limited to ranges, `foreach` works with objects that implement the **iterator protocol**.

The `expression` must be:

- Built in iterable type (e.g. array)
- Custom object that implements Ysharp's iterator protocol

*If the object is not iterable &rarr; interpreter throws runtime error*

### samples

````ysharp
foreach var item in [1, 2, 3, 4, 5] do
    println item;
end

/*
    output

    1
    2
    3
    4
    5
*/
````

````ysharp
foreach var i in 1..5 do
    // not intended usage
end
````

### behaviour

The interpreter retrieves an iterator from the object, 
Each iteration:
- Gets the next value from the iterator
- Assigns it to the loop variable

Loop continues until the iterator is exhausted


### custom iterator

this is a very basic custom iterator sample, to go deeper check **iterator protocol** documentation 

````ysharp
class Counter {
    var count = 1;
    constructor() do
        this.count = 1;
    end

    iter() do
        return this;
    end

    getNext() do
        if this.count > 10 then do
            return null;
        end
        return this.count++;
    end
}


var counter = new Counter();

foreach var i in counter do
    println i;
end

/*
    output
    1
    2
    3
    4
    5
    6
    7
    8
    9
    10
*/
````