---
sidebar_position: 23
---

A class that represents an immutable universally unique identifier (UUID). 
A UUID represents a 128-bit value.

## Reference

### Static methods

| Method    | Signature                 | Return Type | Description                                                                       |
|-----------|---------------------------|-------------|-----------------------------------------------------------------------------------|
| `v4`      | `v4()`                    | `string`    | Generates a random UUID (version 4) and returns it as a string                    |
| `nil`     | `nil()`                   | `string`    | Returns the nil UUID (00000000-0000-0000-0000-000000000000)                       |
| `isValid` | `isValid(value : string)` | `bool`      | Returns true if the given string is a valid UUID, otherwise false                 |
| `parse`   | `parse(value : string)`   | `string`    | Parses the given string as a UUID and returns its canonical string representation |


