# Math-expansion
This expansion lets you calculate different values (including nummerical placeholders).

The syntax is `%math_<math expression>%`, where `<math expression>` can be any kind of math expression, supported by EvalEx.
Placeholders are also supported, but you have to use `{}` and not `%`

## Examples
Here are some examples of different calculations, to show what the expansion can.

### `%math_1+5-8*3/9%`
A summary of calculations, that are possible (addition, substraction, multiplication and divition).  
The result would be `3.333...`, wich is correct, according to known math-rule (multiplication/division before addition/substraction).

### `%math_{server_online}-1%`
This shows the current amount of online players, minus 1.  
Supported are all kinds of placeholders, that return a number.  
You need to use `{}` instead of the default `%` to tell the expansion, what the placeholder is.

### `%math_sqrt(100)%`
This will return the square root of the given number (In this case `10` because `10*10=100`).  
Placeholders can (of course) be used here too.

### `%math_22[prc]4%`
`[prc]` is a replacement of what normally is `%`, because of how PlaceholderAPI handles placeholders.  
The above calculation returns the remainder of `22` (what is left after removing `4` x times, when possible), which would be `2`.

## Config options
On first start does the expansion create two config-options in the config.yml of PlaceholderAPI (Since v.1.0.4)  
Those two options are `Precision` and `Debug`.

`Precision` sets, how many numbers after the `.` are shown (example: `3` would shorthe `5.868741` to `5.869`)  
`Debug` enables/disables printing of the stacktrace in the console, if a invalid calculation was made (default is `off`)

## Credits
Math-expansion uses [EvalEx](https://github.com/uklimaschewski/EvalEx) for evaluating the math-expressions.  
Check the repository for available math-expressions.
