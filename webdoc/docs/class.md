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

There are 2 types of data in Yshapr, primitives and runtime objects. Runtime objects also divide into 5 sub-categories.

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


### Class

`Class` is a runtime object that inherits from both the `RuntimeObject` and `Callable` interfaces. Since it implements `Callable`, 
a class can be invoked directly   calling a class object creates and returns a new `Class-instance`.

### Inheritance

A class can optionally extend another class by referencing a super class name,
stored as `superClassName` on the `ClassObject`. When a class inherits from
another, the `InstancePrototype` of the parent is placed in the prototype chain
of the child's instances   so inherited methods are resolved through normal
prototype chain lookup.

Yshapr also supports **sealed classes** via `SealedClassObject`. A sealed class
cannot be extended by other classes. Attempting to inherit from a sealed class
is a runtime error.

| Class Type          | Extendable            | Description                     |
|---------------------|-----------------------|---------------------------------|
| `ClassObject`       | &#10003; Yes          | Standard user-defined class     |
| `SealedClassObject` | &#10005; No           | Cannot be used as a super class |