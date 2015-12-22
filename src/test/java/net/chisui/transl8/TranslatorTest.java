package net.chisui.transl8;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

public class TranslatorTest {
	
	
	List<Entry<Appendable, TranslationHint>> translatorInvocations = new LinkedList<>();
	Translator translator = Translator.of((a, locale, hint, t) -> {
		translatorInvocations.add(new SimpleEntry<>(a, hint));
		a.append("hello world");
	});

	
	@Test
	public void testTranslateTranslationHint(
	
		@Mocked TranslationHint hint
	
	) throws Exception {
	
		new Expectations() {{
		}};
		
		
		String translation = translator.translate(hint);
		
		
		assertThat(translation, is("hello world"));

		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), instanceOf(StringBuilder.class));
		assertThat(invocation.getValue(), is(hint));
	}

	
	@Test
	public void testTranslateTranslatable(
			
		@Mocked Translatable translatable,
		@Mocked TranslationHint hint
		
	) throws Exception {
		
		new Expectations() {{
			translatable.getTranslationHint(); returns(hint);
		}};
		
		
		String translation = translator.translate(translatable);
		
		
		assertThat(translation, is("hello world"));
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), instanceOf(StringBuilder.class));
		assertThat(invocation.getValue(), is(hint));
	}


	@Test
	public void testTranslateAppendableTranslatable(
		
		@Mocked Translatable translatable,
		@Mocked TranslationHint hint,
		@Mocked Appendable appendable
		
	) throws Exception {
	
		new Expectations() {{
			translatable.getTranslationHint(); returns(hint);
		}};
		
		
		translator.translate(appendable, translatable);
		
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), is(appendable));
		assertThat(invocation.getValue(), is(hint));

		new Verifications() {{
			appendable.append("hello world");
		}};
	}

	
	@Test
	public void testTranslateObjectTranslationHint(
			
		@Mocked TranslationHint hint
			
	) throws Exception {
	
		new Expectations() {{
		}};
		
		
		String translation = translator.translate((Object) hint);
		
		
		assertThat(translation, is("hello world"));
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), instanceOf(StringBuilder.class));
		assertThat(invocation.getValue(), is(hint));
	}
	
	
	@Test
	public void testTranslateObjectTranslatable(
			
		@Mocked Translatable translatable,
		@Mocked TranslationHint hint
			
	) throws Exception {
	
		new Expectations() {{
			translatable.getTranslationHint(); returns(hint);
		}};
		
		
		String translation = translator.translate((Object) translatable);
		
		
		assertThat(translation, is("hello world"));
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), instanceOf(StringBuilder.class));
		assertThat(invocation.getValue(), is(hint));
	}
	
	
	@Test
	public void testTranslateObjectObject(
	
		@Mocked Object obj
			
	) throws Exception {
		
		new Expectations() {{
			obj.toString(); returns("hello world");
		}};

		
		String translation = translator.translate(obj);
		
		
		assertThat(translation, is("hello world"));
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), instanceOf(StringBuilder.class));
		assertThat(invocation.getValue().getKey(), is(obj));
		assertThat(invocation.getValue().getFallback(), is(Optional.of("hello world")));
		
		new Verifications() {{
			obj.toString();
		}};
	}

	
	@Test
	public void testTranslateAppendableObjectTranslationHint(
			
		@Mocked TranslationHint hint,
		@Mocked Appendable appendable
			
	) throws Exception {
	
		new Expectations() {{
		}};
		
		
		translator.translate(appendable, (Object) hint);
		
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), is(appendable));
		assertThat(invocation.getValue(), is(hint));
		
		new Verifications() {{
			appendable.append("hello world");
		}};
	}
	
	
	@Test
	public void testTranslateAppendableObjectTranslatable(
			
		@Mocked Translatable translatable,
		@Mocked TranslationHint hint,
		@Mocked Appendable appendable
			
	) throws Exception {
	
		new Expectations() {{
			translatable.getTranslationHint(); returns(hint);
		}};
		
		
		translator.translate(appendable, (Object) translatable);
		
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), is(appendable));
		assertThat(invocation.getValue(), is(hint));
		
		new Verifications() {{
			appendable.append("hello world");
		}};
	}
	
	
	@Test
	public void testTranslateAppendableObjectObject(
	
		@Mocked Object obj,
		@Mocked Appendable appendable
			
	) throws Exception {
		
		new Expectations() {{
			obj.toString(); returns("hello world");
		}};

		
		translator.translate(appendable, obj);
		
		
		assertThat(translatorInvocations, hasSize(1));
		Entry<Appendable, TranslationHint> invocation = translatorInvocations.get(0);
		assertThat(invocation.getKey(), is(appendable));
		assertThat(invocation.getValue().getKey(), is(obj));
		assertThat(invocation.getValue().getFallback(), is(Optional.of("hello world")));
		
		new Verifications() {{
			obj.toString();
			appendable.append("hello world");
		}};
	}
	
}
