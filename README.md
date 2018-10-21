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

## Known issues
- You can't use `%` inside of calculations, to use a percentage. This is due to how PlaceholderAPI handles placeholders.  
There is no fix for that atm.

## Credits
Math-expansion uses [EvalEx](https://github.com/uklimaschewski/EvalEx) for evaluating the math-expressions.  
Check the repository for available math-expressions.
