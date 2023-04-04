# Changelog
Below, you can find all releases of this expansion and their changes.

## [2.0.1]
- Added `Disable-Warnings` option.
  - When set to true, disables all printings of invalid Placeholder warnings. It's recommended to not enable this option.

## [2.0.0]
- Updated EvalEx to their new v3 release. No difference should be noticed.
- Removed Caffeine and instead use the Cache mechanic provided by a Google library (Part of Spigot).

## [1.4.1]
- Fix broken rounding behaviour... for real this time.

## [1.4.0]
- Fix broken rounding behaviour.
- Replaced `Precision` with `Decimals` setting.  
  The expansion should automatically migrate your existing value to the new setting, but make sure to check the config afterwards.

## [1.3.2]
- Fix wrong default for rounding mode.

## [1.3.1]
- Updated to PlaceholderAPI 2.11.0
  - Added support for PlaceholderAPI's native Logger-system.
  - A Legacy Logger system has been added for backwards compatability.

## [1.3.0]
- Fix issue with possible null version.
- Moved project from Maven to Gradle.

## [1.2.7]
**This and future releases will require Java 11+ to be used!**

- Shade in the Caffeine dependency with relocation.

## [1.2.6]
- Possible fix to a LinkageError thrown at startup... Almost nothing has changed lol.

## [1.2.5]
- Add caching for invalid placeholders to reduce warnings printed.

## [1.2.4]
- Updated EvalEx to 2.6
- Minor code improvements

## [1.2.3]
**This version is ONLY compatibe with PlaceholderAPI onwards!**

- Updated to PlaceholderAPI 2.10.10 to use `getBoolean(String, boolean)`

## [1.2.2]
- Math-Expansion now logs the used placeholder and mentions a proper cause whenever it fails to parse a Math-Expression, precision number or placeholder in general.

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
[1.2.2]: https://api.extendedclip.com/expansions/math/versions/math-122
[1.2.3]: https://api.extendedclip.com/expansions/math/versions/math-123
[1.2.4]: https://api.extendedclip.com/expansions/math/versions/math-124
[1.2.5]: https://api.extendedclip.com/expansions/math/versions/math-125
[1.2.6]: https://api.extendedclip.com/expansions/math/versions/math-126
[1.2.7]: https://api.extendedclip.com/expansions/math/versions/math-127
[1.3.0]: https://api.extendedclip.com/expansions/math/versions/math-130
[1.3.1]: https://api.extendedclip.com/expansions/math/versions/math-131
[1.3.2]: https://api.extendedclip.com/expansions/math/versions/math-132
[1.4.0]: https://api.extendedclip.com/expansions/math/versions/math-140
[1.4.1]: https://api.extendedclip.com/expansions/math/versions/math-141
[2.0.0]: https://api.extendedclip.com/expansions/math/versions/math-200
[2.0.1]: https://api.extendedclip.com/expansions/math/versions/math-201
