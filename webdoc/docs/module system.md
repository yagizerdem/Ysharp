---
sidebar_position: 20
---

Ysharp supports a file-based module system. Each source file is a module. Modules can export declarations and import them into other modules using `use` and `export` keywords.

---

## Syntax

### Importing a Module

```ysharp
use "path/to/module.ys";
```

- `use` declarations must appear at the **top of the file**, before any other declarations.
- The path is relative to the current file's directory.
- Multiple modules can be imported.

```ysharp
use "math/utils.ys";
use "collections/list.ys";
use "helpers.ys";
```

### Exporting a Declaration

The `export` keyword can be placed before any top-level declaration to make it visible to other modules.

```ysharp
export function add(a, b) do
    return a + b;
end

export var PI = 3.14159;

export const MAX_SIZE = 100;

export class Vector do
    ...
end
```

**Grammar:**

```
exportDecl &rarr;  "export" ( classDecl | funDecl | varDecl | constDecl )
useDecl    &rarr;  "use" STRING ";"
```

Only the following declaration types can be exported: functions, variables (`var`), constants (`const`), and classes.

---

## How It Works

### Load Order

Ysharp resolves modules using a **dependency graph** and executes them in **topological order** dependencies are always executed before the modules that depend on them.

Given this structure:

```
main.ys
  └── use "a.ys"
        └── use "b.ys"
```

Execution order: `b.ys` &rarr; `a.ys` &rarr;  `main.ys`

### Circular Dependency Detection

If a circular dependency is detected during graph construction, the loader raises a runtime error immediately:

```
Circular dependency detected: path/to/module.ys
```

### Scope Isolation

Each module runs in its own isolated `global` environment. Only declarations explicitly marked with `export` are made available to importing modules. Non-exported declarations remain private to the module.

### Function Overloads Across Modules

If a module exports a function overload group, only the overloads that were individually marked `export` are re-exported. Non-exported overloads in a group are filtered out during import.

---

## Example

**math.ys**
```ysharp
export function square(n : int) : int do
    return n * n;
end

export function square2(n : double) : double do
    return n * n;
end

function helper() do
    // private, not exported
end
```

**main.ys**
```ysharp
use "math.ys";

println(square(4));     // 16
println(square2(2.5));   // 6.25
```

---

## Module Pipeline

When a file is executed, the runtime follows this pipeline:

```
Source file
    1.  Preprocessor   (comment removal, line continuation)
    2.  Lexer          (tokenization)
    3.  Parser         (AST generation)
    4.  Resolver       (static scope analysis)
    5.  Loader         (dependency graph + topological sort)
    6.  Interpreter    (execution)
```

Each imported module goes through the same full pipeline independently before its exports are injected into the importing module's environment.

---

## Constraints

| Rule                                                               | Description                                                                       |
|--------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `use` must come first                                              | All `use` declarations must appear before any other declarations in the file      |
| Paths are relative                                                 | Import paths are resolved relative to the importing file's directory              |
| Only global scope is exportable                                    | Only top-level declarations can be exported                                       |
| `export` is declaration-level                                      | You cannot export a variable after the fact; `export` must prefix the declaration |
| `exec()` cannot use `use`                                          | The `exec()` built-in function does not support `use` statements                  |
| Circular imports are forbidden                                     | The loader throws an error if a dependency cycle is detected                      |

---

## Grammar Reference

```
program     ::=  useDecl* declaration* EOF

useDecl     ::=  "use" STRING ";"

exportDecl  ::=  "export" ( classDecl | funDecl | varDecl | constDecl )
```