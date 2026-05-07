---
sidebar_position: 39
---

# Path

The `Path` module provides utility functions for manipulating file system
paths as strings: joining segments, splitting them apart, normalizing,
checking existence and converting between absolute and relative forms.

`Path` is a static class — it is never instantiated. All members are
accessed directly on `Path`.

<h3>**All methods of `Path` are static. It cannot be instantiated.**</h3>

```ysharp
Path();   // error: cannot take instance of static class
```

`Path` is purely string-based: it does not read the file system except
for the `isFileExist` and `isDirExist` helpers. All other methods work
on the textual form of the path and follow the conventions of the
underlying operating system (e.g. `\` on Windows, `/` on Unix).

## Reference

### Static methods

| Method           | Signature                                        | Return Type          | Description                                                                                                                         |
|------------------|--------------------------------------------------|----------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| `join`           | `join(base : string, ...parts : string)`         | `string`             | Joins one or more path segments into a single path string. Requires at least one argument.                                          |
| `isFileExist`    | `isFileExist(path : string)`                     | `bool`               | Returns `true` if `path` exists on disk and refers to a regular file, otherwise `false`.                                            |
| `isDirExist`     | `isDirExist(path : string)`                      | `bool`               | Returns `true` if `path` exists on disk and refers to a directory, otherwise `false`.                                               |
| `isAbsolute`     | `isAbsolute(path : string)`                      | `bool`               | Returns `true` if `path` is an absolute path according to the current OS.                                                           |
| `getFileName`    | `getFileName(path : string)`                     | `string` or `null`   | Returns the last segment of `path` (file or directory name). Returns `null` if the path has no name component.                      |
| `getParent`      | `getParent(path : string)`                       | `string` or `null`   | Returns the parent path of `path`, or `null` if it has no parent.                                                                   |
| `getRoot`        | `getRoot(path : string)`                         | `string` or `null`   | Returns the root component of `path` (e.g. `C:\` on Windows, `/` on Unix), or `null` if the path has no root.                       |
| `getNameCount`   | `getNameCount(path : string)`                    | `int`                | Returns the number of name elements in `path` (the root is not counted).                                                            |
| `subPath`        | `subPath(path : string, start : int, end : int)` | `string`             | Returns a relative path consisting of the name elements in the range `[start, end)`. Indices are zero-based.                        |
| `getExtension`   | `getExtension(path : string)`                    | `string` or `null`   | Returns the file extension of the last segment, including the leading dot (e.g. `".txt"`). Returns `null` if there is no extension. |
| `normalize`      | `normalize(path : string)`                       | `string`             | Returns `path` with redundant elements (`.` and `..`) removed.                                                                      |
| `resolve`        | `resolve(base : string, other : string)`         | `string`             | Resolves `other` against `base`. If `other` is absolute it is returned as-is; otherwise it is appended to `base`.                   |
| `relativize`     | `relativize(base : string, target : string)`     | `string`             | Returns the relative path from `base` to `target`. Both paths must be either absolute or both relative.                             |

### Errors

The following situations raise a runtime error:

- `join` is called with no arguments.
- Any string argument is missing or has the wrong type.
- `subPath` is called with `start < 0`, `end < 0`, `start >= end`, or `end > getNameCount(path)`.
- `relativize` is called with one absolute and one relative path.

You can handle these with `try / catch`.

## Examples

### Joining segments

```ysharp
let p = Path.join("project", "src", "main.ys");
IO.stdout.writeln(p);
// Windows -> project\src\main.ys
// Unix    -> project/src/main.ys
```

### Inspecting a path

```ysharp
let file = Path.join("home", "yagiz", "notes.txt");

IO.stdout.writeln(Path.getFileName(file));   // notes.txt
IO.stdout.writeln(Path.getParent(file));     // home\yagiz
IO.stdout.writeln(Path.getExtension(file));  // .txt
IO.stdout.writeln(__str(Path.getNameCount(file)));  // 3
```

### Absolute vs relative

```ysharp
IO.stdout.writeln(Path.isAbsolute("C:\\Users\\me"));  // true
IO.stdout.writeln(Path.isAbsolute("src/main.ys"));    // false

IO.stdout.writeln(Path.getRoot("C:\\Users\\me"));     // C:\
IO.stdout.writeln(Path.getRoot("src/main.ys"));       // null
```

### Existence checks

```ysharp
if Path.isFileExist("config.json") then do
    IO.stdout.writeln("Config found.");
end

if !Path.isDirExist("logs") then do
    IO.stdout.writeln("Logs directory is missing.");
end
```

### Normalize, resolve, relativize

```ysharp
IO.stdout.writeln(Path.normalize("a/b/../c/./d"));
// -> a\c\d  (or a/c/d on Unix)

IO.stdout.writeln(Path.resolve("project/src", "main.ys"));
// -> project\src\main.ys

IO.stdout.writeln(Path.resolve("project/src", "/etc/hosts"));
// -> /etc/hosts   (absolute "other" replaces base)

IO.stdout.writeln(Path.relativize("project/src", "project/src/main.ys"));
// -> main.ys
```

### Slicing path segments

```ysharp
let p = "project/src/ysharp/util/Path.java";

IO.stdout.writeln(Path.subPath(p, 0, 2));   // project\src
IO.stdout.writeln(Path.subPath(p, 2, 4));   // ysharp\util
```
