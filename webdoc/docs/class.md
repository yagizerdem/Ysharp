---
sidebar_position: 18
---

## General


Classes provide a means of bundling data and functionality together. 
Creating a new class creates a new type of object, allowing new instances of that type to be made. 
Each class instance can have attributes attached to it for maintaining its state. 
Class instances can also have methods (defined by its class) for modifying its state.

A typical Ysharp class consists of several building blocks: 
- Fields (Attributes): Variables that store the object's state (e.g., a Car class might have color and speed).
- Methods: Functions that define the object's actions (e.g., drive() or brake()).
- Constructors: Special methods called when an object is instantiated to initialize its attributes.

Compared with other programming languages, Ysharp's class mechanism adds classes with a minimum of new syntax and semantics.
It is a mixture of the class mechanisms found in Java / Javascript.
Ysharp uses a prototype based inheritance model.
Instead of supporting multiple base classes like classical object-oriented languages,
each object has a single prototype, forming a prototype chain.
Behavior and properties are resolved dynamically through this chain.
a derived class can override any methods of its base class or classes, 
and a method can call the method of a base class with the same name. 
Objects can contain arbitrary amounts and kinds of data. As is true for modules, 
classes partake of the dynamic nature of Ysharp: they are created at runtime, and can be modified further after creation.


## Runtime Objects

### Types

There are 2 types of data in Ysharp, primitives and runtime objects. Runtime objects also divide into 5 sub-categories.

#### Function related runtime-objects
- User-defined Functions
- Lambda 
- Native Functions

#### Class related runtime-objects
- Class
- Class-instance

All of the runtime objects implement the `Callable` interface except `Class-instance`.


### Prototype Chain

Every runtime object has a `prototype` field inherited from `RuntimeObject`,
but the prototype chain is **only active for `Class-instance`** objects.
For all function-related runtime objects (`FunctionObject`, `LambdaObject`,
`NativeFunction`) the prototype is explicitly set to `null`  their behaviour
is fixed and they do not participate in prototype-based lookup.

#### Root Prototype

At the top of the chain sits `ClassPrototype`, a singleton `RuntimeObject`
with the internal type `__RootPrototype__`. Its own `prototype` field is `null`,
making it the absolute root   the chain terminates here.

`ClassPrototype` ships with two built-in methods available to all instances:

| Method | Arity | Description |
|---|---|---|
| `getType()` | 0 | Returns the runtime type name of the receiver (`this`) |
| `getPrototype()` | 0 | Returns the prototype object of the receiver (`this`) |

Both methods resolve `this` from the current environment at call time.

#### Lookup

When a field or method is accessed on a `Class-instance`, the runtime first
checks the instance's own `fields` map. If not found, it walks up the
`prototype` chain until the field is resolved or the chain ends at `null`.


#### Examples

- prototype chain example

````ysharp

class User {
    let f_name : string ;
    let l_name : string ;

    var fullName = () => this.f_name + " " + this.l_name;

    constructor(f_name: string, l_name : string) do
        this.f_name = f_name;
        this.l_name = l_name;
    end
}

sealed class Employee extends User {
    let salary : number;

    constructor(f_name :string, l_name : string, salary : number) do
        super(f_name, l_name);
        this.salary = salary;
    end
}

const emp = new Employee("yagiz", "erdem", 10000);

println emp.fullName(); // yagiz erdem

println emp.getPrototype(); // <prototype:Employee>
println emp.getPrototype().getPrototype(); // <prototype:User>
println emp.getPrototype().getPrototype().getPrototype(); // <prototype:root>
println emp.getPrototype().getPrototype().getPrototype().getPrototype(); // null
````

- getType example

````ysharp

class User {
    let f_name : string ;
    let l_name : string ;

    var fullName = () => this.f_name + " " + this.l_name;

    constructor(f_name: string, l_name : string) do
        this.f_name = f_name;
        this.l_name = l_name;
    end
}

sealed class Employee extends User {
    let salary : number;

    constructor(f_name :string, l_name : string, salary : number) do
        super(f_name, l_name);
        this.salary = salary;
    end
}

const emp = new Employee("yagiz", "erdem", 10000);
const user = new User("John", "Ousterhout");


println Employee.getType(); // _Employee_
println emp.getType(); // Employee
println emp.getPrototype().getType(); // __Employee__
println emp.getPrototype().getPrototype().getType(); // __User__
println emp.getPrototype().getPrototype().getPrototype().getType(); // __RootPrototype__

println User.getType(); // _User_
println user.getType(); // User
println user.getPrototype().getType(); // __User__
println user.getPrototype().getPrototype().getType(); // __RootPrototype__

````
> Note <br/>
> Notice that Class itself type starts and ends with &nbsp; '\_'  &nbsp; like  &nbsp;  \_User\_ &nbsp; , Class-instance
> type is User and prototype types starts and ends with double '__' like &nbsp; \_\_User\_\_ &nbsp; .

<br/>

````ysharp
var a = 10;
println a.getType(); // throws error
````

- Only classes has methods, use explicit type api to get type of other variables like functions and primitives.


## Class

`Class` is a runtime object that inherits from both the `RuntimeObject` and `Callable` interfaces. Since it implements `Callable`, 
a class can be invoked directly   calling a class object creates and returns a new `Class-instance`.

### Closure
A `ClassObject` captures the **environment at the point of its definition** and
stores it as a `closure` field. This closed-over environment is used when the
class is invoked the constructor and instance methods execute within a scope
that has access to the variables visible at the time the class was declared.

This is the same closure semantics used by `FunctionObject` and `LambdaObject`.

This means classes are **first-class values** in Yshapr they can be defined
inside functions or other scopes, and they will correctly capture the surrounding
environment just like a function would.

#### Example
````ysharp
function makeHuman(name) do
    class Human {
        getName() do
            return name;
        end
    }

    return new Human();
end

var h1 = makeHuman("Yagiz");
var h2 = makeHuman("Erdem");

println h1.getName(); // Yagiz
println h2.getName(); // Erdem
````

### Method & Properties

Classes in Ysharp can define both **properties** (state) and **methods** (behavior).  
Instances inherit methods through the prototype chain and hold their own property values.

````ysharp
class Meal {
    let price;
    var name;
    var id : int;

    toString() do
        return this.name + " " + this.price + " - " + this.id ;
    end
}

const m = new Meal();
m.name = "meat";
m.price = 100;
m.id = 1;

println m.toString();
````

- var / let / const can be used for property declarations, this has same behaviour with normal variable declarations.

````ysharp
class Test {
    var t1;
    let t2;
    const t3 = "runtime-constant";
}
````

#### Advanced Example: Methods, Properties & Closures

````ysharp
function createAccount(ownerName) do

    var balance = 0; // private state (closure)

    class Account {

        var owner = ownerName;

        deposit(amount) do
            balance = balance + amount;
        end

        withdraw(amount) do
            if amount > balance then do
                println "Insufficient funds";
                return null;
            end
            balance = balance - amount;
        end

        getBalance() do
            return balance;
        end

        // method returning a lambda (closure inside class)
        createLogger() do
            return () => do
                return this.owner + " -> balance: " + balance;
            end;
        end

        toString() do
            return this.owner + " (" + balance + ")";
        end
    }
    return new Account();
end

const acc = createAccount("Yagiz");

acc.deposit(500);
acc.withdraw(200);

println acc.getBalance(); // 300

const log = acc.createLogger();
println log(); // Yagiz -> balance: 300
````


### static

The static keyword defines a static method or field for a class.
Static properties cannot be directly accessed on instances of the class. Instead, they're accessed on the class itself.

> **Note** <br/>
> Static methods are often utility functions, such as functions to create or clone objects,
> whereas static properties are useful for caches, 
> fixed-configuration, or any other data you don't need to be replicated across instances.

Static members are defined using the `static` keyword.

````ysharp
class MathUtil {

    static var pi = 3.14;

    static add(a, b) do
        return a + b;
    end
}

println MathUtil.pi;        // 3.14
println MathUtil.add(2, 3); // 5
````

- **Static members belong to the class, not the instance.**

````ysharp
class MathUtil {

    static var pi = 3.14;

    static add(a, b) do
        return a + b;
    end
}

const m = new MathUtil();

println MathUtil.pi; //  correct
println m.pi;        //  undefined
````

#### static vs instance example

````ysharp
class Example {
    static var s = 10;
    var x = 5;

    static getStatic() do
        return this.s;
    end

    getInstance() do
        return this.x;
    end
}

println Example.getStatic(); // 10

const e = new Example();
println e.getInstance();     // 5
````

**Use static members when:**
- The data is shared across all instances
- You don’t need per-instance copies
- You want utility-like behavior


### this keyword

The `this` keyword refers to the object on which a method is called.  
Its value is determined **at call time** for methods.

`this` is not a keyword resolved at parse time, it is a regular `Variable`
injected into the method's execution environment before the method body runs.

#### Basic Usage

````ysharp
class User {
    var name;

    constructor(name) do
        this.name = name;
    end

    getName() do
        return this.name;
    end
}

const u = new User("Yagiz");
println u.getName(); // Yagiz
````

> Note <br/>
> Program cannot access instance properties and methods without this keyword, inside of Class-instance. You must 
> write explicit this keyword to access


#### Static Methods and this

In static methods, this refers to the class itself, since classes are objects in Ysharp.

````ysharp
class App {

    static var version = "1.0";

    static getVersion() do
        return this.version;
    end

    static getVersion2() do
        return App.version;
    end
}

println App.getVersion(); // 1.0

println App.getVersion2(); // 1.0
````
 
Program can both access static fields and methods via this keyword or class-name itself.

#### this Inside Lambda Expressions

Lambda expressions do not define their own `this`.  
In Ysharp, `this` is **bound at access time**, when the lambda is retrieved from an object.

````
class Test {

    var value = 10;

    getFn() do
        return () => do
            return this.value;
        end;
    end
}

const t = new Test();
const f = t.getFn();

println f(); // 10
````

---

### constructor function

A class can define at most one `constructor` method. Defining more than one
is a `syntax` error at class declaration time.


````ysharp
class Point {
    var x;
    var y;
    constructor(x: int, y: int) do
        this.x = x;
        this.y = y;
    end
}
````

If no `constructor` is defined, the class accepts zero arguments and the
instance is created with only its declared properties initialized.

#### Parameter Type Checking

Constructor parameters support optional type annotations. Each argument is
checked against its declared type before the constructor body executes. A
type mismatch throws a `process` error:

#### `this` Binding

Before the constructor body runs, a fresh environment is created from the
class's `closure`. The newly created `Class-instance` is injected into this
environment as `this`, making it accessible throughout the constructor body.

#### `super` Binding

If the class extends another class, the parent `ClassObject` is also injected
into the constructor environment as `super`. This allows the constructor to
call the parent constructor explicitly via `super()`.

`super()` **must be the first statement** in the constructor body when the
class has a superclass. Placing it anywhere else is a `process` error:


If no explicit `super()` call is present, the parent constructor is called
implicitly with zero arguments before the child constructor body runs.

#### Instance Initialization Order

When a class is instantiated, the following steps happen in order:

1. A blank `Class-instance` is created.
2. The `InstancePrototype` is assigned to the instance's prototype chain.
3. If the class has a superclass, super fields are copied onto the instance
   (either via explicit `super()` or implicit zero-arg call).
4. Declared instance properties are initialized on the instance.
5. The constructor body executes with `this` and `super` in scope.
6. The fully initialized instance is returned.

#### Invocation

The constructor is called automatically when a class is instantiated via `new`.
The `ClassObject` itself is `Callable` invoking it triggers the constructor
body with the provided arguments.

`var p = new Point(1, 2);`

### Inheritance

A class can optionally extend another class by referencing a super class name,
stored as `superClassName` on the `ClassObject`. When a class inherits from
another, the `InstancePrototype` of the parent is placed in the prototype chain
of the child's instances   so inherited methods are resolved through normal
prototype chain lookup.

Ysharp also supports **sealed classes** via `SealedClassObject`. A sealed class
cannot be extended by other classes. Attempting to inherit from a sealed class
is a runtime error.

| Class Type          | Extendable            | Description                     |
|---------------------|-----------------------|---------------------------------|
| `ClassObject`       | &#10003; Yes          | Standard user-defined class     |
| `SealedClassObject` | &#10005; No           | Cannot be used as a super class |