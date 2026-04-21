---
sidebar_position: 14
---

## Try Block

The first step in constructing an exception handler 
is to enclose the code that might throw an exception within a try block. 
In general, a try block looks like the following:


<code>
try do<br/>
&nbsp;&nbsp;&nbsp;&nbsp;code<br/>
end<br/>
catch and finally blocks . . .
</code>


The segment in the example labeled code contains one or more legal lines of code that could throw an exception.
(The catch and finally blocks are explained in the next two subsections.)

## Catch Block

You associate exception handlers with a try block by providing one catch block directly after the try block. 
No code can be between the end of the try block and the beginning of the catch block.

````ysharp
try do

end catch (ex) do

end
````
Ysharp does not use typed exception parameters in `catch` because the language is dynamic 
The type of the thrown value is not known at compile time and may be user-defined
The handler receives the thrown value as a variable and can inspect it at runtime

---

#### try / catch example

````
var map = {};

try do
    // accessing a non-existing key and forcing an error
    var value = map.get("a").length(); // null.length()  runtime error
end
catch (ex) do
    println "An error occurred:";
    println ex;
end
````


## Finally Block


The finally block always executes when the try block exits.
This ensures that the finally block is executed even if an unexpected exception occurs. 
But finally is useful for more than just exception handling — it allows the programmer to avoid
having cleanup code accidentally bypassed by a return, continue, or break. Putting cleanup 
code in a finally block is always a good practice, even when no exceptions are anticipated.


--- 


## try / catch / finally example

- Demonstrates reading a non-existing file, handling the error, and always executing cleanup code

````ysharp
var path = "C:/does_not_exist.txt";

try do
    println "Trying to read file...";

    // this will throw because file does not exist
    var content = File.read(path);

    println content;
end
catch (ex) do
    println "Error occurred while reading file:";
    println ex;
end
finally do
    println "Cleaning up resources...";
end
````


## throw Keyword

The throw keyword in Ysharp is used to explicitly throw an exception from a 
method or any block of code. We can throw either checked or unchecked exception.
The throw keyword is mainly used to throw custom exceptions. 


> Note <br/> 
> - Ysharp allows throwing any value (string, number, object, or custom class instance)
> - Commonly used for custom error handling


--- 


#### Basic throw example

````ysharp
try do
    throw "Something went wrong!";
end
catch (ex) do
    println "Caught error:";
    println ex;
end
````

````ysharp
try do
    throw 404;
end
catch (ex) do
    println "Error code: " + ex;
end
````

````ysharp
class MyError {
    var message;
    constructor(message) do
        this.message = message;
    end

    toString() do
        return "MyError: " + this.message;
    end
}


try do
    var err = new MyError("Invalid operation");
    throw err;
end
catch (ex) do
    println ex.toString(); // calls toString()
end
````