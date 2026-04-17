---
sidebar_position: 12
---


In Ysharp, `break` and `continue` are control flow statements used inside loops to alter execution.

They are supported in:

- `for`
- `for-in`
- `foreach`
- `while`

## break

`break` immediately **terminates the loop** and transfers control to the next statement after the loop.

### samples

````ysharp
for var i in 1..10 do
    if (i == 5) then do break; end
    print(i + " ");
end
````

````ysharp
var arrayOfInts = [32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127];
var searchFor = 12;

var foundIt = false;
var index = -1;

for var i = 0; i < arrayOfInts.length(); i = i + 1 do
    if arrayOfInts.get(i) == searchFor then do
        foundIt = true;
        index = i;
        break;
    end
end

if foundIt then do
    println("Found " + searchFor + " at index " + index);
end
else do
    println(searchFor + " not in the array");
end
````

- Ysharp does not support labeled break
- You cannot break out of outer loops directly
- break only exits the innermost loop


## continue

continue skips the current iteration and proceeds to the next iteration of the loop.

###  samples

````ysharp
for var i in 1..5 do
    if i == 3 then do continue; end
    print(i + " ");
end

````


```` ysharp
var searchMe = "peter piper picked a peck of pickled peppers";
var max = searchMe.length;
var numPs = 0;

for var i = 0; i < max; i = i + 1 do

    // interested only in 'p'
    if (searchMe.charAt(i) != 'p') then  do
        continue;
    end

    // process 'p'
    numPs = numPs + 1;
end

println("Found " + numPs + " p's in the string.");
````


- Ysharp does not support labeled `continue`
- You cannot skip iterations of outer loops directly
- `continue` only affects the innermost loop