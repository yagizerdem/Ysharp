---
sidebar_position: 40
---

# IO

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

| Method      | Signature                  | Return Type   | Description                                                      |
|-------------|----------------------------|---------------|------------------------------------------------------------------|
| `write`     | `write(text : string)`     | `null`        | Writes `text` to standard output **without** a trailing newline. |
| `writeln`   | `writeln(text : string)`   | `null`        | Writes `text` to standard output **with** a trailing newline.    |

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

| Method     | Signature                | Return Type  | Description                                                  |
|------------|--------------------------|--------------|--------------------------------------------------------------|
| `write`    | `write(text : string)`   | `null`       | Writes `text` to standard error without a trailing newline.  |
| `writeln`  | `writeln(text : string)` | `null`       | Writes `text` to standard error with a trailing newline.     |

```ysharp
IO.stderr.writeln("Something went wrong!");
```

## stdin

Reads input from the standard input stream (the keyboard, by default).

### Static methods

| Method     | Signature    | Return Type  | Description                                                                                                   |
|------------|--------------|--------------|---------------------------------------------------------------------------------------------------------------|
| `readln`   | `readln()`   | `string`     | Reads a single line of text from standard input (up to the next newline). The newline character is stripped.  |
| `readKey`  | `readKey()`  | `char`       | Reads a single character (one byte) from standard input.                                                      |

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

| Method   | Signature                                 | Return Type | Description                                                                                                          |
|----------|-------------------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------|
| `read`   | `read(path : string)`                     | `string`    | Reads the entire content of the file at `path` and returns it as a string.                                           |
| `write`  | `write(path : string, content : string)`  | `null`      | Writes `content` to the file at `path`, **creating** the file if missing and **truncating** it if it already exists. |
| `append` | `append(path : string, content : string)` | `null`      | Appends `content` to the end of the file at `path`. Creates the file if it does not exist.                           |
| `delete` | `delete(path : string)`                   | `null`      | Deletes the file (or empty directory) at `path`.                                                                     |

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

| Method    | Possible errors                                                                |
|-----------|--------------------------------------------------------------------------------|
| `read`    | File not found, access denied, other I/O failure.                              |
| `write`   | Parent directory does not exist, access denied, other I/O failure.             |
| `append`  | Parent directory does not exist, access denied, other I/O failure.             |
| `delete`  | File does not exist, directory is not empty, access denied, other I/O failure. |

```ysharp
try do
  let data = IO.File.read("missing.txt");
end catch (err) do
  IO.stderr.writeln("Could not read file: " + err);
end
```

## Directory

Static helpers for creating, deleting, listing and querying directories on disk.
Also provides convenient accessors for special system directories (home, desktop,
downloads, temp, etc.).

All paths can be either absolute or relative. Relative paths are resolved
against the **current working directory** of the running interpreter.

### Static methods

#### Directory Operations

| Method      | Signature                  | Return Type | Description                                                                         |
|-------------|----------------------------|-------------|-------------------------------------------------------------------------------------|
| `create`    | `create(path : string)`    | `null`      | Creates a single directory at `path`. Fails if parent directory does not exist.     |
| `createAll` | `createAll(path : string)` | `null`      | Creates a directory and all missing parent directories at `path` (like `mkdir -p`). |
| `exists`    | `exists(path : string)`    | `boolean`   | Returns `true` if a directory exists at `path`, `false` otherwise.                  |
| `delete`    | `delete(path : string)`    | `null`      | Deletes the directory at `path`. Fails if the directory is not empty.               |
| `list`      | `list(path : string)`      | `array`     | Returns an array of file/directory names in the directory at `path`.                |

#### Special Directory Accessors

These methods return the path to system directories and require **no arguments**:

| Method          | Return Type  | Description                                                                         |
|-----------------|--------------|-------------------------------------------------------------------------------------|
| `getHome`       | `string`     | Returns the path to the user's home directory (e.g. `/home/username` on Linux).     |
| `getCurrent`    | `string`     | Returns the current working directory where the program is running.                 |
| `getTemp`       | `string`     | Returns the system temporary directory (e.g. `/tmp` on Unix, `%TEMP%` on Windows).  |
| `getDesktop`    | `string`     | Returns the path to the user's Desktop folder.                                      |
| `getDocuments`  | `string`     | Returns the path to the user's Documents folder.                                    |
| `getDownloads`  | `string`     | Returns the path to the user's Downloads folder.                                    |
| `getAppData`    | `string`     | Returns the path to application data directory (OS-specific).                       |
| `getConfig`     | `string`     | Returns the path to user config directory (OS-specific).                            |
| `getCache`      | `string`     | Returns the path to user cache directory (OS-specific).                             |

### Examples

#### Creating directories

```ysharp
// Create a single directory (fails if parent doesn't exist)
IO.Directory.create("mydir");

// Create nested directories (creates parents as needed)
IO.Directory.createAll("path/to/my/nested/dir");
```

#### Checking if a directory exists

```ysharp
if IO.Directory.exists("mydir") then
    IO.stdout.writeln("Directory exists!");
else
    IO.stdout.writeln("Directory does not exist");
end
```

#### Listing directory contents

```ysharp
let files = IO.Directory.list(".");
for var i = 0; i < files.size(); i = i + 1 do
    IO.stdout.writeln(files.get(i));
end
```

#### Deleting a directory

```ysharp
// Only works if directory is empty
IO.Directory.delete("mydir");
```

#### Getting special directories

```ysharp
// Get home directory
let home = IO.Directory.getHome();
IO.stdout.writeln("Home: " + home);

// Get current working directory
let cwd = IO.Directory.getCurrent();
IO.stdout.writeln("Current: " + cwd);

// Get downloads folder
let downloads = IO.Directory.getDownloads();
IO.stdout.writeln("Downloads: " + downloads);

// Get temporary directory
let temp = IO.Directory.getTemp();
IO.stdout.writeln("Temp: " + temp);

// Get application data directory
let appData = IO.Directory.getAppData();
IO.stdout.writeln("AppData: " + appData);
```

#### Practical example: Create a project structure

```ysharp
let projectRoot = "my-project";

// Create the root directory and all subdirectories
IO.Directory.createAll(projectRoot + "/src");
IO.Directory.createAll(projectRoot + "/tests");
IO.Directory.createAll(projectRoot + "/docs");
IO.Directory.createAll(projectRoot + "/build");

IO.stdout.writeln("Project structure created!");
```

#### Practical example: Working with downloads

```ysharp
let downloads = IO.Directory.getDownloads();
let files = IO.Directory.list(downloads);

IO.stdout.writeln("Files in Downloads:");
for var i = 0; i < files.size(); i = i + 1 do
    IO.stdout.writeln("  - " + files.get(i));
end
```

### Errors

`Directory` operations raise a runtime error in the following cases. You can
catch them with a `try / catch` block.

| Method       | Possible errors                                                      |
|--------------|----------------------------------------------------------------------|
| `create`     | Parent directory does not exist, access denied, path already exists. |
| `createAll`  | Access denied, other I/O failure.                                    |
| `exists`     | Access denied, invalid path.                                         |
| `delete`     | Directory does not exist, directory is not empty, access denied.     |
| `list`       | Directory does not exist, path is not a directory, access denied.    |

```ysharp
try do
    IO.Directory.list("nonexistent");
end catch (err) do
    IO.stderr.writeln("Could not list directory: " + err);
end
```

### Platform-Specific Behavior

The special directory accessors (`getAppData`, `getCache`, `getConfig`) return
different paths depending on the operating system:

| Method        | Windows           | macOS                            | Linux                                 |
|---------------|-------------------|----------------------------------|---------------------------------------|
| `getAppData`  | `%APPDATA%`       | `~/Library/Application Support`  | `$XDG_DATA_HOME` or `~/.local/share`  |
| `getCache`    | `%LOCALAPPDATA%`  | `~/Library/Caches`               | `$XDG_CACHE_HOME` or `~/.cache`       |
| `getConfig`   | `%APPDATA%`       | `~/Library/Preferences`          | `$XDG_CONFIG_HOME` or `~/.config`     |
