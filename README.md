
# Text Placeholder API Expressions
Adds math and conditional expressions to Text Placeholder API.  
This project uses [Keval](https://github.com/notKamui/Keval) for parsing math expressions, and would have been exponentially more difficult without it. Show the author some love and star his repo!

## Math Placeholder
The bulk of the additions (no pun intended) in this extension are accessed through the `expr:math` placeholder.

You can think of this placeholder as handling three distinct 'types':  
 * Components, such as numbers, functions, constants, and placeholders that resolve to numbers.
 * Binary operators, such as addition or subtraction, which go between two components.
 * Unary operators, such as negation and factorial, which apply to a single component.

### Components and operators supplied by this extension
Binary operators are applied between two components.
| Binary Operator | Function | Example              | Result
| :-------------- | :------- | :------------------- | :-----
| +               | Add      | `%expr:math 2 + 2%`  | 4
| -               | Subtract | `%expr:math 4 - 1%`  | 3
| *               | Multiply | `%expr:math 3 * 3%`  | 9
| /               | Divide   | `%expr:math 9 / 4%`  | 2.25
| ^               | Exponent | `%expr:math 3 ^ 3%`  | 27

Unary operators are applied to a single component.
| Unary Operator | Function  | Example              | Result
| :------------- | :-------- | :------------------- | :-----
| -              | Negate    | `%expr:math 1 + -1%` | 0
| +              | Identity  | `%expr:math ??%`     | What does this even do
| !              | Factorial | `%expr:math 5!%`     | 120

Functions are components, taking expressions as arguments.
| Function | Arguments | Description                    | Example                      | Result
| :------- | :-------- | :----------------------------- | :--------------------------- | :----
| mod      | 2         | Modulo / remainder             | `%expr:math mod(7, 2)%`      | 1
| neg      | 1         | Negate / invert (Like unary -) | `%expr:math neg(10)%`        | -10
| abs      | 1         | Absolute value                 | `%expr:math abs(-10)%`       | 10
| sqrt     | 1         | Square root                    | `%expr:math sqrt(34)%`       | 5.8309
| cbrt     | 1         | Cube root                      | `%expr:math cbrt(30)%`       | 3.1072
| exp      | 1         | Exponential                    | `%expr:math exp(4)%`         | 54.5981
| ln       | 1         | Natural logarithm              | `%expr:math ln(4)%`          | 1.3862
| log10    | 1         | Base 10 logarithm              | `%expr:math log10(4)%`       | 0.6020
| log2     | 1         | Base 2 logarithm               | `%expr:math log2(4)%`        | 2
| rad      | 1         | Degrees to radians             | `%expr:math rad(30)%`        | 0.5235
| sin      | 1         | Sine                           | `%expr:math sin(rad(30))%`   | 0.4999
| cos      | 1         | Cosine                         | `%expr:math cos(rad(30))%`   | 0.8660
| tan      | 1         | Tangent                        | `%expr:math tan(rad(30))%`   | 0.5773
| asin     | 1         | Arcsine                        | `%expr:math asin(rad(30))%`  | 0.5510
| acos     | 1         | Arccosine                      | `%expr:math acos(rad(30))%`  | 1.0197
| atan     | 1         | Arctangent                     | `%expr:math atan(rad(30))%`  | 0.4823
| ceil     | 1         | Round up                       | `%expr:math ceil(0.4)%`      | 1
| floor    | 1         | Round down                     | `%expr:math floor(0.6)%`     | 0
| round    | 1         | Round                          | `%expr:math round(0.5)%`     | 1
| min      | 2+        | Smallest argument              | `%expr:math min(1, 0.3, 7)%` | 0.3
| max      | 2+        | Largest argument               | `%expr:math max(1, 0.3, 7)%` | 7
| avg      | 2+        | Average of arguments           | `%expr:math avg(1, 0.3, 7)%` | 2.7666

Constants are components which resolve to a constant value.
| Constant | Description    |
| :------- | :------------- |
| PI       | π              |
| e        | Euler's number |

### Using Placeholders In Expressions
This extension uses the `${category:placeholder}` syntax to allow supplying placeholders that resolve to numbers as components.  
For example; to display a player's health as a percentage (such as within the tab list), you could use the following:  
`%expr:math ${player:health} * 100 / ${player:max_health}%%`

## Conditional Placeholders
Unlike other placeholders, arguments here must be separated by semicolons (`;`) instead of spaces.

### `%expr:ifeq%`
Test whether `a` and `b` are equal, and return `c` or `d` depending on the result.

`%expr:ifeq aIsMath a bIsMath b cIsMath c dIsMath d%`
| Argument | Description
| :------- | :----------
| aIsMath  | Whether to parse `a` as a math expression. Either `true` or `false`.
| a        | Math expression or text to compare against b. If treated as text, and spaces at the start or end of the string are removed.
| bIsMath  | Whether to parse `b` as a math expression. Either `true` or `false`.
| b        | Math expression or text to compare against a. If treated as text, and spaces at the start or end of the string are removed.
| cIsMath  | Whether to parse `c` as a math expression. Either `true` or `false`.
| c        | Math expression or text to use if `a` and `b` are equal.
| dIsMath  | Whether to parse `d` as a math expression. Either `true` or `false`.
| d        | Math expression or text to use if `a` and `b` are not equal.

#### Example
`Player %expr:ifeq false; ${player:equipment_slot mainhand}; false; Diamond Sword; false; is; false; is not% holding a Diamond Sword.`
| Condition                                           | Result
| :-------------------------------------------------- | :-----
| `%player:equipment_slot mainhand% == "Diamond Sword"` | Player is holding a Diamond Sword.
| `%player:equipment_slot mainhand% != "Diamond Sword"` | Player is not holding a Diamond Sword.

### `%expr:iflt%`
Test whether `a` is less than `b`, and return `c` or `d` depending on the result.

`%expr:iflt a b cIsMath c dIsMath d%`
| Argument | Description
| :------- | :----------
| a        | Math expression to compare against b.
| b        | Math expression to compare against a.
| cIsMath  | Whether to parse `c` as a math expression. Either `true` or `false`.
| c        | Math expression or text to use if `a` is less than `b`.
| dIsMath  | Whether to parse `d` as a math expression. Either `true` or `false`.
| d        | Math expression or text to use if `a` is not less than `b`.

#### Example
`Player is in %expr:iflt ${player:health}; 14; false; poor; false; good% health`
| Condition               | Result
| :---------------------- | :-----
| `%player:health% < 14`  | Player is in poor health
| `%player:health% >= 14` | Player is in good health

### `%expr:ifgt%`
Test whether `a` is greater than `b`, and return `c` or `d` depending on the result.

`%expr:ifgt a b cIsMath c dIsMath d%`
| Argument | Description
| :------- | :----------
| a        | Math expression to compare against b.
| b        | Math expression to compare against a.
| cIsMath  | Whether to parse `c` as a math expression. Either `true` or `false`.
| c        | Math expression or text to use if `a` is greater than `b`.
| dIsMath  | Whether to parse `d` as a math expression. Either `true` or `false`.
| d        | Math expression or text to use if `a` is not greater than `b`.

#### Example
`Health: %expr:ifgt ${player:health}; 4; true; ${player:health} * 100 / ${player:max_health}%; false; Nearly dead!%`
| Condition               | Result
| :---------------------- | :-----
| `%player:health% == 15` | Health: 75
| `%player:health% == 14` | Health: 70
| `%player:health% <= 4`  | Health: Nearly dead!
