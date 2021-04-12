# Changelog
Below, you can find all releases of this expansion and their changes.

## [1.2.1]
- Added Console logs for invalid placeholders.
- Make provided rounding-mode lowercase to prevent issues.

## [1.2.0]
- Restructured placeholder Syntax.  
New Placeholders:
  ```
  %math_<expression>%
  %math_[precision]:[rounding]_<expression>%
  ```
- `[precision:<number>]` has been removed in favour of the new syntax.
- New `Rounding` config option to set the default rounding behaviour.
  ```yaml
  expansions:
    math:
      Rounding: 'half-up'
  ```
- `Debug` is now a boolean option. Old String options still work.

## [1.1.0]
- Updated to PlaceholderAPI 2.10.7 and Spigot 1.16.1

**Note**:  
This version still has `1.0.7` defined as expansion version. Please update to [the latest version](#120) to fix this.

## [1.0.7]
- Fixed version number being `null`

## [1.0.6]
- `[precision:<number>]` setting added to placeholder.

## [1.0.4]
- Config options `Precision` and `Debug` added.
  ```yaml
  expansions:
    math:
      Precision: 3
      Debug: 'off'
  ```

## [1.0.3]
- Added `[prc]` placeholder as replacement for `%` for expressions.

## [1.0.0]
- First release.

<!-- Links -->
[1.0.0]: https://api.extendedclip.com/expansions/math/versions/math-100
[1.0.3]: https://api.extendedclip.com/expansions/math/versions/math-103
[1.0.4]: https://api.extendedclip.com/expansions/math/versions/math-104
[1.0.6]: https://api.extendedclip.com/expansions/math/versions/math-106
[1.0.7]: https://api.extendedclip.com/expansions/math/versions/math-107
[1.1.0]: https://api.extendedclip.com/expansions/math/versions/math-110
[1.2.0]: https://api.extendedclip.com/expansions/math/versions/math-120
[1.2.1]: https://api.extendedclip.com/expansions/math/versions/math-121