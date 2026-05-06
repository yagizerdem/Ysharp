---
sidebar_position: 40
---

# io

The `IO` module groups together everything related to input and output:
writing to the console, reading from the keyboard, reporting errors and
working with files on disk.

`IO` itself is a static container; you never construct an `IO` instance.
Instead, you use one of its inner static classes:

- `stdout` — standard output stream
- `stderr` — standard error stream
- `stdin` — standard input stream
- `File` — file system operations

<h3>**All members of `IO` and its sub-classes are static. They cannot be instantiated.**</h3>

```ysharp
IO(); // error: IO is a static class, cannot take instance
stdout(); // error: stdout is a static class, cannot take instance
File(); // error: File is a static class, cannot take instance
```

## stdout

Writes text to the standard output stream (the terminal, by default).

### Static methods

| Method     | Signature                  | Return Type | Description                                                        |
|------------|----------------------------|-------------|--------------------------------------------------------------------|
| `write`    | `write(text : string)`     | `null`      | Writes `text` to standard output **without** a trailing newline.   |
| `writeln`  | `writeln(text : string)`   | `null`      | Writes `text` to standard output **with** a trailing newline.      |

```ysharp
IO.stdout.write("Hello, ");
IO.stdout.writeln("world!");
// Output:
// Hello, world!
```

## stderr

Writes text to the standard error stream. Behaves exactly like `stdout`,
but the output is sent to the error channel and is typically displayed in
red by terminals/IDEs.

### Static methods

| Method     | Signature                 | Return Type   | Description                                                 |
|------------|---------------------------|---------------|-------------------------------------------------------------|
| `write`    | `write(text : string)`    | `null`        | Writes `text` to standard error without a trailing newline. |
| `writeln`  | `writeln(text : string)`  | `null`        | Writes `text` to standard error with a trailing newline.    |

```ysharp
IO.stderr.writeln("Something went wrong!");
```

## stdin

Reads input from the standard input stream (the keyboard, by default).

### Static methods

| Method      | Signature     | Return Type | Description                                                                                                  |
|-------------|---------------|-------------|--------------------------------------------------------------------------------------------------------------|
| `readln`    | `readln()`    | `string`    | Reads a single line of text from standard input (up to the next newline). The newline character is stripped. |
| `readKey`   | `readKey()`   | `char`      | Reads a single character (one byte) from standard input.                                                     |

```ysharp
IO.stdout.write("What is your name? ");
let name = IO.stdin.readln();
IO.stdout.writeln("Hello, " + name + "!");
```

```ysharp
IO.stdout.write("Press any key to continue... ");
let k = IO.stdin.readKey();
IO.stdout.writeln("");
IO.stdout.writeln("You pressed: " + k);
```

If the underlying stream throws while reading, a runtime error is raised.

## File

Static helpers for reading, writing and deleting files on disk.

All paths can be either absolute or relative. Relative paths are resolved
against the **current working directory** of the running interpreter, so
`"data.txt"` and `"./data.txt"` both refer to a file next to where the
program was started.

### Static methods

| Method     | Signature                                 | Return Type | Description                                                                                                          |
|------------|-------------------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------|
| `read`     | `read(path : string)`                     | `string`    | Reads the entire content of the file at `path` and returns it as a string.                                           |
| `write`    | `write(path : string, content : string)`  | `null`      | Writes `content` to the file at `path`, **creating** the file if missing and **truncating** it if it already exists. |
| `append`   | `append(path : string, content : string)` | `null`      | Appends `content` to the end of the file at `path`. Creates the file if it does not exist.                           |
| `delete`   | `delete(path : string)`                   | `null`      | Deletes the file (or empty directory) at `path`.                                                                     |

### Examples

Reading a file:

```ysharp
let text = IO.File.read("notes.txt");
IO.stdout.writeln(text);
```

Writing a file (overwrites existing content):

```ysharp
IO.File.write("output.txt", "Hello, file!\n");
```

Appending to a file:

```ysharp
IO.File.append("log.txt", "[INFO] Application started\n");
IO.File.append("log.txt", "[INFO] Loading config...\n");
```

Deleting a file:

```ysharp
IO.File.delete("output.txt");
```

### Errors

`File` operations raise a runtime error in the following cases. You can
catch them with a `try / catch` block.

| Method     | Possible errors                                                                |
|------------|--------------------------------------------------------------------------------|
| `read`     | File not found, access denied, other I/O failure.                              |
| `write`    | Parent directory does not exist, access denied, other I/O failure.             |
| `append`   | Parent directory does not exist, access denied, other I/O failure.             |
| `delete`   | File does not exist, directory is not empty, access denied, other I/O failure. |

```ysharp
try do
  let data = IO.File.read("missing.txt");
end catch (err) do
  IO.stderr.writeln("Could not read file: " + err);
end
```
