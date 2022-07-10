[placeholderapi]: https://www.spigotmc.org/resources/6245/
[evalex]: https://github.com/uklimaschewski/EvalEx
[examples]: https://github.com/Andre601/Math-Expansion/wiki/Examples
[rounding]: https://github.com/Andre601/Math-Expansion/wiki/Config-options#rounding

[changelog]: https://github.com/Andre601/Math-Expansion/blob/master/CHANGELOG.md

# Math-Expansion
> **Note**  
> Versions 1.2.7 and newer require Java 11 or higher to work!  
> This is due to some required dependencies needing it.

The Math-Expansion is an Expansion for [PlaceholderAPI] which allows you to do math-expressions.  
Supported are all expression supported by [EvalEx], which this Expansion uses. See the [examples](#examples) below for some features.

## Syntax
The expansion has two specific Placeholder patterns to use.

### `%math_<expression>%`
Used for normal calculations while using the default [decimal count](#decimals) and [rounding behaviour](#rounding)

### `%math_[decimals]:[rounding]_<expression>%`
Used for calculations using your own [decimal count](#set-amount-of-shown-decimals) and [rounding behaviour](#set-rounding-behaviour).  
Either value is optional and not providing one uses the default values set in the [Config](#config-options).

## Features

### Normal Math expressions
Math-Expansion supports all common math expressions such as $1+2$, $3-4$, $5*6$ or $7/8$  
Those expressions can be used as-is without any real problem. Just keep in mind that expressions follow common logic which is that **multiplication and division come before addition and substraction**.

If you want specific expressions to be done first, even tho logic would prioritize others can you put them into brackets (i.e. `(1+2)*3` would result in `1+2` being done first before multiplying by 3).

----

### Placeholders in Math expressions
You can use any placeholders from PlaceholderAPI that return **valid numbers**.  
When using placeholders will you need to use the curly brackets format (`{some_placeholder}`) rather than the percent placeholders.

As an example can you use `{server_online}` in your expressions to use the amount of online players in your server.

----

### Using percent symbol
Due to how PlaceholderAPI handles placeholders can you not use the percent symbol (`%`) inside math expressions, as PlaceholderAPI would assume this to be the end of a placeholder.  
To bypass this limitation does the Math-Expansion add a `[prc]` placeholder that you can use. This text will be replaced with an actual percent symbol before doing calculations.

> **Warning**  
> The `[prc]` can NOT be used for placeholders in PlaceholderAPI. See the [Previous section](#placeholders-in-math-expressions) for more info.

----

### Special expressions
Certain expressions cannot be reproduced with letters in the Math-Expansion. For example can $\sqrt{100*10}$ not be used as shown.  
Because of this does [EvalEx][evalex], the library used by Math-Expansion, provide specific custom patterns that allow to do these kinds of special calculations.

To f.e. do the above expression would you use `SQRT(100*10)` as the expression, which would now get the square root of $100*10$.

EvalEx offers a lot of different formats you can use, so check out their readme for more information.  
Note: Those patterns are case-insensitive.

----

### Set amount of shown decimals
Sometimes an expression can return a relatively large number such as `3.333333...`.  
The Math-Expansion does by default only show the first three decimals of a number, turning the above shown one into `3.333`.

If you want to lower or increase the amount of decimals without altering the [config option](#decimals) can you set your own decimals count using the [advanced placeholder-pattern](#math_decimalsrounding_expression).

For example will `%math_5:_8+1.234567%` set the amount of decimals to 5, resulting in the returned number looking like this: `9.23457`

> **Note**  
> The final number depends on what rounding behaviour has been set.

----

### Set rounding behaviour
By default, is the Math-Expression using `Half Up` as rounding behaviour, which means that numbers between 1 and 4 will be rounded down while numbers 5 to 9 will be rounded up.

If you would like to use a different rounding behaviour while not altering the [config option](#rounding) can you provide a valid rounding behaviour in the [advanced placeholder-pattern](#math_decimalsrounding_expression) like this: `%math_:ceiling_8+1.2222%`

The above shown expression would - assuming a default decimals count of 3 is used - result in `9.223` even tho by normal standards would it be `9.222`.

You may find more detailed explanations and examples of this option on the [wiki](https://github.com/Andre601/Math-Expansion/wiki/Config-options#rounding).

## Config options
The expansion adds a few specific settings to the config.yml of PlaceholderAPI, which can be changed if desired.

### Debug
The Debug option enables or disables the expansion's debug mode.  
With debug enabled will warnings also print an exception, if the warning is a result of an exception.

This is mostly useful for support and could increase the size of your server log files.

----

### Decimals
Sets the amount of decimal points (digits after the dot) should be shown by default in the final number.

The default value - which is also used whenever an invalid value has been set in the config - is `3` meaning a number such as `123.456789` will be displayed as `123.457`.  
Keep in mind that the actual number also depends on what [rounding behaviour](#rounding) is used.

If the number is lower than 0 will it default to 0.

----

### Rounding
The default rounding behaviour to use.  
When the number returns more decimals than what should be displayed will it get rounded to the set decimal count.

Example: `5.684` with `half up` as rounding (Default) and a decimal count set to 2 returns `5.68` while a decimal count of 1 returns `6.7`.

The default rounding is `half-up` which means that any number greater or equal to 5 is rounded up and anything below is rounded down.  
Take a look at the [wiki][rounding] for a list of supported options.

## Credits
Math-expansion uses [EvalEx] for evaluating the math-expressions.  
Check the repository for available math-expressions.

## Changelog
Take a look at the [CHANGELOG.md][changelog] for all versions and their changes.
