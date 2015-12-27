# transl8
![travis badge](https://travis-ci.org/chisui/transl8.svg?branch=master)

functional java translation library

# Usage

The two core components of this library are the `Translator` and `TranslationHint`. A `TranslationHint` provides information to a `Translator` to create a translation.

<<<<<<< HEAD
Create a `Translator`
=======
**Create a `Translator`**
>>>>>>> 6aa6732895e7fad823cbeb653307d4da61a72613
```
Translator translator = Translator.of(
		MessageLookup.of(MessageSource.ofResouceBundle("translations")),
		Format.ofMessageFormat());
```
<<<<<<< HEAD
Create a `TranslationHint`
=======
This creates a `Translator` that looks for in the  [`ResourceBundle`](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) `"translations"` and formats messages using [`MessageFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html).

**Create a `TranslationHint`**
>>>>>>> 6aa6732895e7fad823cbeb653307d4da61a72613
```
TranslationHint hint = TranslationHint
	.of("key")
	.withArguments(arg0, arg1, ...)
	.withFallback("fallback {0} {1}");
```
<<<<<<< HEAD
Translate the hint
=======
**Translate the hint**
```
String translation = translator.translate(hint);
```
>>>>>>> 6aa6732895e7fad823cbeb653307d4da61a72613
