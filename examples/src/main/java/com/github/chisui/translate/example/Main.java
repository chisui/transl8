package com.github.chisui.translate.example;

import java.util.Locale;

import com.github.chisui.translate.TranslationHint;
import com.github.chisui.translate.Translator;
import com.github.chisui.translate.format.Format;
import com.github.chisui.translate.lookup.MessageSource;

public class Main {

    public static void main(String[] args) {
        Translator translator = Translator.of(
                () -> Locale.GERMAN,
                MessageSource.ofResourceBundle("messages"),
                Format.ofMessageFormat());

        String msg = translator.translate(TranslationHint.of("app.welcome", new User("Philipp", "Dargel")));
        System.out.println(msg);
    }

}
