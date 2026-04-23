---
sidebar_position: 1
---

# intro

This is the official API reference documentation for Ysharp and you are welcome!

Let's discover **Ysharp in less than 5 minutes**.

## About Ysharp

Ysharp is a JVM based language, which means it relies on the Java runtime for code execution. 
Also, some libraries in the Java ecosystem can be accessed via Ysharp. 
This does not mean that a programmer should use Java style OOP based programming techniques in the Ysharp ecosystem, 
since Ysharp has its own programming methodology.  Ysharp is designed for simplicity, 
effective programming, and a simple module system that even a novice programmer feels at ease with.

While I was first trying to learn programming, I started my journey with C#, which has a solid class and type
abstraction system that looks good at the start.
After some time, I learned the language basics and moved to using libraries to build apps, 
but I realized that there was too much abstraction going on and I didn’t understand how the code executes under the hood.
It was confusing for a beginner to truly understand how .NET DI works or how it handles concurrent requests.
After some time, I moved to Java, which is pretty much the same except it has a more verbose syntax.
This was my second language, and until that time, I had only used OOP-based languages.
After some time, I realized that the problem is related to several OOP-based programming 
languages that put too much abstraction into creating deep modules but have complicated interfaces.
I switched to C++ to get as close as possible to the low level to gain intuition about how computers really work.
C++ is a good language and has many strengths, like accessing native APIs, 
using pointers, and managing memory for performance-critical applications.

At that point I worked with several different programming languages and gained experience about different methodologies.
Based on my experience I found the best API design approach is making modules as deep as possible yet the interface should be simple.
C++ is a perfect choice for my view of programming perspective.
UNIX file API is a great example for this approach.
UNIX kernel manages all the complexity of file IO like file descriptor tables, 
virtual file system etc. yet there are only 6 File/IO functions for users which very simple to use.

Overall I designed the Ysharp the idea based on :  
"MODULES SHOULD BE DEEP YET INTERFACE SHOULD BE SIMPLE"


## Installation

Ysharp is a JVM based language, JDK 21 or upper versions should be installed in your system. If JDK installations 
are incomplete in your system you can [download](https://www.oracle.com/java/technologies/downloads/) here

Ysharp Interpreter is also needed to be installed in system. You can [download](https://github.com/yagizerdem/ysharp/releases/latest) Interpreter .jar file 
from this this link  https://github.com/yagizerdem/ysharp/releases/latest


## Introduction to Ysharp

Ysharp supports a wide range of programming paradigms, such as functional programming, 
object oriented programming with prototype based class objects, and procedural programming.
Ysharp has its own module system similar to Node.js, but unlike Node, the Ysharp module loader
does not try to resolve circular dependencies implicitly, instead it throws an error.
I believe this is a better approach since there should be no circular dependencies in a clean architecture.
Class system is also similar to javascript that both languages support prototype based class objects.
In fact Ysharp supports inheritance by constructing prototype chains across class objects prototypes. 
On top of that Ysharp supports static methods and sealed classes. Overall I designed class model of the language
mixture of Javascript and C#/Java model.
Other supported pillars of OOP are abstraction and polymorphism.
Users can simply create new classes or modules and hide business logic inside these structures.
As for polymorphism, Ysharp supports runtime polymorphism so that the interpreter decides 
which function to execute at runtime.
Ysharp polymorphism also handles compile time polymorphism like type safe OOP based languages do, 
but at runtime, since Ysharp is not a type safe or compiled language.
Ysharp supports type like Type Script but instead of separate semantic analysis phase , type check 
perform in runtime. Function and lambda closures are supported , also classes has closure so 
methods in class captures its parent environment. 100% percent of C expression grammar is available, on
top of that there is modern operators like pipes and nullish coalescing operator, ternary operator ex...
There is a rich set of collection library that contains more than 100% of Javas collections API, on top of
that there are advance collection types Tries , Graphs ex... 
This is just a brief explanation of basic language features, you can review full language spec in rest of the
documentation.

## How much programming knowledge required to get started with Ysharp
You do not need previous programming knowledge to use Ysharp. In fact I believe Ysharp is a better starting
point for beginners instead of Java. Ysharp is modern & comfy and very lightweight and does not spend time on
compilation that development/debugging cycle is very quick. Ysharp is also a turing complete language, Every algorithm 
implemented in Java/C# can also be implemented in Ysharp as well. Ysharp syntax is very easy and standard library provides 
rich set of collection API out of the box to support programmers to do not wast time on re inventing the wheel from scratch.


