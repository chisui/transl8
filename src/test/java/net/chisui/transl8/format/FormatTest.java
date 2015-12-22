package net.chisui.transl8.format;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

import net.chisui.transl8.Translator;

public class FormatTest {

	@Test
	public void testOfFormatter() throws Exception {
		test(Format.ofFormatter(), "%s");
	}

	@Test
	public void testOfMessageFormat() throws Exception {
		test(Format.ofMessageFormat(), "{0}");
	}

	private void test(Format format, String message) throws IOException {
		Translator translator = Translator.of((l, hint) -> hint.getFallback().orElse("world"));
		StringBuilder sb = new StringBuilder();
		
		format
				.toFormatable(Locale.ENGLISH, message)
				.format(sb, Arrays.asList("hello"), translator);
		
		assertThat(sb.toString(), is("hello"));
	}
}
