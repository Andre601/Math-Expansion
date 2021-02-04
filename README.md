[evalex]: https://github.com/uklimaschewski/EvalEx
[examples]: https://github.com/Andre601/Math-Expansion/wiki/Examples
[rounding]: https://github.com/Andre601/Math-Expansion/wiki/Config-options#rounding

# Math-expansion
This expansion lets you calculate different values (including nummerical placeholders).

## Syntax
The expansion has two specific Placeholder patterns to use.

### `%math_<expression>%`
Evaluates the provided `<expression>` with the precision and rounding defined in the config.yml of PlaceholderAPI.

### `%math_[precision]:[rounding]_<expression>%`
Evaluates the provided `<expression>` with the provided `[precision]` and `[rounding]`.  
Both the precision and rounding can be omitted to use the corresponding setting in the config.yml of PlaceholderAPI.

## Examples
Here are some examples of different calculations, to show what the expansion can do.

You can find more examples in the [wiki][examples].

### `%math_1+5-8*3/9%`
A summary of calculations, that are possible (addition, subtraction, multiplication and division).  
The expansion follows common rules for evaluating equations, meaning that multiplications and divisions are made before additions and subtractions.

The above expression would result in `3.333...` which is correct.

### `%math_{server_online}-1%`
This example evaluates the current amount of players on the server and subtracts 1 from it.

All placeholders which return a number can be used in the expansion, but they need to be in the format `{placeholder}` instead of `%placeholder%`.

### `%math_SQRT(100)%`
EvalEx, the library used here, provides specific Text for common calculations.  
In the above example do we get the square root of 100, which is 10 (`10 * 10 = 100`).

These EvalEx-specific text options are case-sensitive.  
You can find a full list of text options on the [EvalEx Readme][evalex].

### `%math_22[prc]4%`
Due to how PlaceholderAPI finds and handles placeholders are we not able to use the percent symbol (`%`) inside an expression.  
To bypass this limitation do we use a `[prc]` placeholder, which will be replaced with a `%` before the expression gets evaluated.

The `%` is used to return the remainder of a specific operation.  
The reminder is received by removing `n2` as many times from `n1` as possible (`n1%n2`) and then return whatever is left from `n1`.

In the above example do we remove 4 as many times from 20 as possible, which is 4 times and result in a remainder of 2.

### `%math_1:_5.6+4.77%`
Sometimes you may want to return a specific, precise number, which would be different from the default precision set in the config.

In such a case can you use `%math_[precision]:[rounding]_<expression>%` where `[precision]` is the amount of decimals shown in the result.

The above expression would return `10.370` with the default precision set in the config.yml of PlaceholderAPI.  
But with the precision defined as 1 in the placeholder does it return `10.4`.

You can also override the default rounding behaviour, which by default is `HALF_UP` (`>= 5` = rounding up, otherwise down).

Both options can be omitted to use the settings defined in the config.yml of PlaceholderAPI.

Take a look at the [wiki][rounding] for details on what rounding options are available and what they do.

## Config options
The expansion adds a few specific settings to the config.yml of PlaceholderAPI, which can be changed if desired.

### Precision
The default precision to use.  
A precision of 2 would result in the numbers showing as `#.##`, 3 results in `#,###`, etc.

Any number lower than 0 will automatically be changed to 0.

### Rounding
The default rounding behaviour to use.  
When a number returns more decimals than what should be returned is the final number rounded.

Example: `5.684` with a precision of 2 returns `5.68` while a precision of 1 returns `5.7`.

The default rounding is `half-up` which means that any number greater or equal to 5 is rounded up and anything below is rounded down.  
Take a look at the [wiki][rounding] for a list of supported options.

## Credits
Math-expansion uses [EvalEx] for evaluating the math-expressions.  
Check the repository for available math-expressions.

## Changelog
Take a look at the [CHANGELOG.md][changelog] for all versions and their changes.
