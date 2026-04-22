---
sidebar_position: 22
---


An instance of this class is used to generate a stream of pseudorandom numbers. 
The class uses a 48-bit seed, which is modified using a linear congruential formula. 
(See Donald Knuth, The Art of Computer Programming, Volume 2, Section 3.2.1.)
If two instances of Random are created with the same seed, and the same sequence of
method calls is made for each, they will generate and return identical 
sequences of numbers. In order to guarantee this property, particular
algorithms are specified for the class Random.

## Reference

### Static methods


| Method         | Signature                               | Return Type | Description                                                                                      |
|----------------|-----------------------------------------|-------------|--------------------------------------------------------------------------------------------------|
| `chance`       | `chance(probability : number)`          | `bool`      | Returns true with the given probability (0.0 - 1.0), otherwise false                             |
| `nextBool`     | `nextBool()`                            | `bool`      | Returns a random boolean value (true or false)                                                   |
| `nextFloat`    | `nextFloat(min : number, max : number)` | `number`    | Returns a random floating-point number between min (inclusive) and max (exclusive)               |
| `next`         | `next()`                                | `number`    | Returns a random floating-point number between 0.0 (inclusive) and 1.0 (exclusive)               |
| `nextGaussian` | `nextGaussian()`                        | `number`    | Returns a normally distributed (Gaussian) random number with mean 0.0 and standard deviation 1.0 |
| `nextInt`      | `nextInt(min : int, max : int)`         | `int`       | Returns a random integer between min (inclusive) and max (exclusive)                             |
| `pick`         | `pick(a : any, b : any)`                | `any`       | Randomly returns either a or b with equal probability                                            |
| `setSeed`      | `setSeed(seed : int)`                   | `null`      | Sets the seed of the random number generator for reproducible results                            |
