---
sidebar_position: 4
---


<h3>This is **<b>NOT UNIX PIPES</b>** </h3>

Pipe operator takes the value on the left side and passes it as 
the first argument to the function on the right side.
It transforms nested code like f(g(h(x))) 
into a readable sequence: x |> h() |> g() |> f().

```
function append1(data : string) do
    return data + "1";
end

function append2(data : string) do
    return data + "2";
end

var result : int = ( 10 |>
            Type.Converter.toString |>
            append1 |>
            append2 |>
            Type.Converter.toInt );

println result; // 1012 (int)
```

Expressions that are not the first element of the pipe, breaks the pipe and new pipe
forms after that expression. 

For example : 
```
function append1(data : string) do
    return data + "1";
end

function append2(data : string) do
    return data + "2";
end

var result : int = ( 10 |>
            Type.Converter.toString |> // convert to int
            append1 |> // 101
            50 |> // pipe breaks new output is 50
            Type.Converter.toString |>
            append2 |> // 502
            Type.Converter.toInt // convert to int 502 
            );

println result; // 502 (int)
```