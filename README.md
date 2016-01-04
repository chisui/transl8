# translate
[![travis badge](https://travis-ci.org/chisui/translate.svg?branch=master)](https://travis-ci.org/chisui/translate)[![Code Coverage](https://img.shields.io/codecov/c/github/chisui/translate/master.svg)](https://codecov.io/github/chisui/translate?branch=master)

functional java translation library

# Usage

The two core components of this library are the `Translator` and `TranslationHint`. A `TranslationHint` provides information to a `Translator` to create a translation.

**Create a `Translator`**
```
Translator translator = Translator.of(
		MessageLookup.of(MessageSource.ofResouceBundle("translations")),
		Format.ofMessageFormat());
```
This creates a `Translator` that looks for in the  [`ResourceBundle`](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) `"translations"` and formats messages using [`MessageFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html).

**Create a `TranslationHint`**
```
TranslationHint hint = TranslationHint
	.of("key")
	.withArguments(arg0, arg1, ...)
	.withFallback("fallback {0} {1}");
```

**Translate the hint**
```
String translation = translator.translate(hint);
```
