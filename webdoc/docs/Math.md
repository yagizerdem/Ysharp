---
sidebar_position: 21
---

The class Math contains methods for performing basic 
numeric operations such as the elementary exponential, 
logarithm, square root, and trigonometric functions.

<h3>**All of the methods and constants under Math class is static**</h3>

## Reference

### Static methods

| Method     | Signature                                           | Return Type | Description                                                                     |
|------------|-----------------------------------------------------|-------------|---------------------------------------------------------------------------------|
| `abs`      | `abs(n : number)`                                   | `number`    | Returns the absolute value of a number                                          |
| `acos`     | `acos(n : number)`                                  | `number`    | Returns the arc cosine (inverse cosine) of a number in radians                  |
| `asin`     | `asin(n : number)`                                  | `number`    | Returns the arc sine (inverse sine) of a number in radians                      |
| `atan2`    | `atan2(y : number, x : number)`                     | `number`    | Returns the angle (in radians) between the positive x-axis and the point (x, y) |
| `atan`     | `atan(n : number)`                                  | `number`    | Returns the arc tangent (inverse tangent) of a number in radians                |
| `cbrt`     | `cbrt(n : number)`                                  | `number`    | Returns the cube root of a number                                               |
| `ceil`     | `ceil(n : number)`                                  | `number`    | Returns the smallest integer greater than or equal to a number                  |
| `clamp`    | `clamp(value : number, min : number, max : number)` | `number`    | Restricts a value to be within the given range [min, max]                       |
| `cos`      | `cos(n : number)`                                   | `number`    | Returns the cosine of a number (in radians)                                     |
| `degToRad` | `degToRad(degrees : number)`                        | `number`    | Converts an angle from degrees to radians                                       |
| `exp`      | `exp(n : number)`                                   | `number`    | Returns Euler’s number (e) raised to the power of the given number              |
| `floor`    | `floor(n : number)`                                 | `number`    | Returns the largest integer less than or equal to a number                      |
| `fract`    | `fract(n : number)`                                 | `number`    | Returns the fractional part of a number (n - floor(n))                          |
| `hypot`    | `hypot(a : number, b : number)`                     | `number`    | Returns the Euclidean distance √(a² + b²)                                       |
| `lerp`     | `lerp(a : number, b : number, t : number)`          | `number`    | Linearly interpolates between a and b using t (a + (b - a) * t)                 |
| `log2`     | `log2(n : number)`                                  | `number`    | Returns the base-2 logarithm of a number                                        |
| `log10`    | `log10(n : number)`                                 | `number`    | Returns the base-10 logarithm of a number                                       |
| `log`      | `log(n : number)`                                   | `number`    | Returns the natural logarithm (base e) of a number                              |
| `max`      | `max(a : number, b : number)`                       | `number`    | Returns the larger of two numbers                                               |
| `min`      | `min(a : number, b : number)`                       | `number`    | Returns the smaller of two numbers                                              |
| `pow`      | `pow(base : number, power : int)`                   | `number`    | Returns the result of raising a number to the given integer power               |
| `radToDeg` | `radToDeg(radians : number)`                        | `number`    | Converts an angle from radians to degrees                                       |
| `random`   | `random()`                                          | `number`    | Returns a pseudo-random number in the range [0, 1)                              |
| `round`    | `round(n : number)`                                 | `number`    | Rounds a number to the nearest integer                                          |
| `sign`     | `sign(n : number)`                                  | `number`    | Returns the sign of a number (-1, 0, or 1)                                      |
| `sin`      | `sin(n : number)`                                   | `number`    | Returns the sine of a number (in radians)                                       |
| `sqrt`     | `sqrt(n : number)`                                  | `number`    | Returns the square root of a number                                             |
| `tan`      | `tan(n : number)`                                   | `number`    | Returns the tangent of a number (in radians)                                    |
| `trunc`    | `trunc(n : number)`                                 | `number`    | Returns the integer part of a number by removing its fractional digits          |


### Static constants

| Constant | Type   | Description                              |
|----------|--------|------------------------------------------|
| `PI`     | number | The mathematical constant π (3.14159...) |
| `E`      | number | Euler’s number (2.71828...)              |

-  `Math.PI`
- `Math.E`