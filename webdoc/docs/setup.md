---
sidebar_position: 2
---

## Installation

Ysharp is a JVM based language, JDK 21 or upper versions should be installed in your system. If JDK installations
are incomplete in your system you can [download](https://www.oracle.com/java/technologies/downloads/) here

Ysharp Interpreter is also needed to be installed in system. You can [download](https://github.com/yagizerdem/Ysharp/releases/tag/v0.0.1) Interpreter .jar file
from this this link  https://github.com/yagizerdem/Ysharp/releases/tag/v0.0.1

## Running Ysharp scripts
Check your JDK version from terminal with ```java --version```
command. JDK version should be 21 or upper to run interpreter jar file.

Ysharp supports two execution modes:
- REPL Mode (interactive shell)
- File Mode (execute a script file)

### REPL ode
Starts an interactive session where you can execute Ysharp code line by line:
``java -jar <absolute-path-to-ysharp.jar>``

### File mode
Executes a Ysharp source file:
``java -jar <absolute-path-to-ysharp.jar> <absolute-path-to-main-file>``

### Running sample code
If you fallow till here without any problem, now you should be able to run 
Ysharp scripts on your computer, try this sample script by creating a file
(main.ys ex...), copy paste this code snippet and run  ``java -jar <absolute-path-to-ysharp.jar> <absolute-path-to-main-file>``
command to execute code.

```
const arr = [];

// fill array
for var i in 0..19 do
    arr.push(Random.nextInt(0, 500));
end

// bubble sort
for var i = 0; i < arr.size() -1; i++ do
    for var j = 0; j< arr.size() -i - 1; j++ do
        if arr.get(j) > arr.get(j + 1) then do
            var temp = arr.get(j);
            arr.set(j, arr.get(j + 1));
            arr.set(j + 1, temp);
        end
    end
end

// print all elements
println arr.toString();
// print least 5 elements
println arr.take(5).toString();
```