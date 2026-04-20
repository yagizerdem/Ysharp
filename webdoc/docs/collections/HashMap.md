---
sidebar_position: 2
---

## General

A HashMap is a part of Ysharp’s Collection Framework. 
It stores elements in key-value pairs, where, Keys are unique. and Values can be duplicated.

- Internally uses Hashing, hence allows efficient key-based retrieval, insertion, and removal with an average of O(1) time.
- HashMap is not thread-safe.
- Insertion order is not preserved in HashMap. To preserve the insertion order, LinkedHashMap is used and to maintain sorted order, TreeMap is used.
- HashMap allows one null key and multiple null values. If a null key is added multiple times, it overwrites the previous value.

## References

### Instance methods

| Method             | Signature                                                  | Return Type | Description                                                                                                                                                                                                                                |
|--------------------|------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `clear`            | `map.clear()`                                              | `null`      | Removes all key-value pairs from the map                                                                                                                                                                                                   |
| `clone`            | `map.clone()`                                              | `HashMap`   | Returns a shallow copy of the map; key-value pairs are copied by reference                                                                                                                                                                 |
| `compute`          | `map.compute(key : any, callback : function)`              | `any`       | Computes a new value for the given key using the callback `(key, oldValue)`; updates or removes the entry based on the result, if key not exists new key value pair is created; callback accepts parameters `(key : any, oldValue : any)`  |
| `computeIfAbsent`  | `map.computeIfAbsent(key : any, callback : function)`      | `any`       | Computes and inserts a value using the callback `(key)` only if the key is not already present; returns the existing or computed value; callback accepts parameters `(key : any)`                                                          |
| `computeIfPresent` | `map.computeIfPresent(key : any, callback : function)`     | `any`       | Computes a new value using `(key, existingValue)` only if the key exists; updates or removes the entry based on the result; callback accepts parameters `(key : any, existingValue : any)`                                                 |
| `containsKey`      | `map.containsKey(key : any)`                               | `bool`      | Returns true if the map contains the specified key                                                                                                                                                                                         |
| `containsValue`    | `map.containsValue(value : any)`                           | `bool`      | Returns true if the map contains the specified value                                                                                                                                                                                       |
| `entries`          | `map.entries()`                                            | `Array`     | Returns an array of `[key, value]` pairs representing all entries in the map                                                                                                                                                               |
| `get`              | `map.get(key : any)`                                       | `any`       | Returns the value associated with the given key, or null if the key does not exist                                                                                                                                                         |
| `getOrDefault`     | `map.getOrDefault(key : any, defaultValue : any)`          | `any`       | Returns the value for the given key, or the provided default value if the key does not exist                                                                                                                                               |
| `isEmpty`          | `map.isEmpty()`                                            | `bool`      | Returns true if the map contains no key-value pairs                                                                                                                                                                                        |
| `keys`             | `map.keys()`                                               | `Array`     | Returns an array containing all keys in the map                                                                                                                                                                                            |
| `merge`            | `map.merge(key : any, value : any, callback : function)`   | `any`       | Merges the given value with the existing value using `(existing, value)`; inserts, updates, or removes the entry based on the result; callback accepts parameters `(existingValue : any, newValue : any)`                                  |
| `put`              | `map.put(key : any, value : any)`                          | `any`       | Associates the given value with the key and returns the previous value, or null if none existed                                                                                                                                            |
| `putIfAbsent`      | `map.putIfAbsent(key : any, value : any)`                  | `any`       | Inserts the value only if the key is not already present; returns the existing value or null if inserted                                                                                                                                   |
| `remove`           | `map.remove(key : any)`                                    | `any`       | Removes the entry associated with the key and returns its value, or null if the key does not exist                                                                                                                                         |
| `replace`          | `map.replace(key : any, value : any)`                      | `any`       | Replaces the value for the given key if it exists and returns the previous value, or null if the key does not exist                                                                                                                        |
| `size`             | `map.size()`                                               | `int`       | Returns the number of key-value pairs in the map                                                                                                                                                                                           |
| `toString`         | `map.toString()`                                           | `string`    | Returns a string representation of the map in `{key=value}` format                                                                                                                                                                         |
| `values`           | `map.values()`                                             | `Array`     | Returns an array containing all values in the map                                                                                                                                                                                          |


