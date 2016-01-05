# translate

[![travis badge](https://img.shields.io/travis/chisui/translate.svg)](https://travis-ci.org/chisui/translate)[![Code Coverage](https://img.shields.io/codecov/c/github/chisui/translate/master.svg)](https://codecov.io/github/chisui/translate?branch=master)

translate is a functional java translation library.

To include it in your project using maven add the dependency:

```xml
<dependency>
    <groupId>com.github.chisui.translate</groupId>
    <artifactId>translate</artifactId>
    <version>0.1.0</version>
</dependency>
```

# Usage

The two core components of this library are the `Translator` and `TranslationHint`. A `TranslationHint` provides information to a `Translator` to create a translation.

**Create a `Translator`**
```java
Translator translator = Translator.of(
		MessageLookup.of(MessageSource.ofResouceBundle("translations")),
		Format.ofMessageFormat());
```
This creates a `Translator` that looks for in the  [`ResourceBundle`](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) `"translations"` and formats messages using [`MessageFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html).

**Create a `TranslationHint`**
```java
TranslationHint hint = TranslationHint
	.of("key")
	.withArguments(arg0, arg1, ...)
	.withFallback("fallback {0} {1}");
```

**Translate the hint**
```java
String translation = translator.translate(hint);
```
