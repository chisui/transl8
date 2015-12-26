package net.chisui.transl8;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import net.chisui.transl8.Translator.AppendableFirstTranslator;
import net.chisui.transl8.Translator.AppendingTranslationFunction;
import net.chisui.transl8.Translator.StringFirstTranslator;
import net.chisui.transl8.Translator.TranslationFunction;
import net.chisui.transl8.format.Format;
import net.chisui.transl8.format.Formatable;
import net.chisui.transl8.lookup.MessageLookup;
import net.chisui.transl8.lookup.MessageSource;

public class TranslatorTest {
	
	
	List<Object[]> translatorInvocations = new LinkedList<>();
	Translator translator = Translator.of(
			() -> Locale.ENGLISH,
			(a, locale, hint, t) -> {
				translatorInvocations.add(new Object[]{ a, locale, hint, t });
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], instanceOf(StringBuilder.class));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], instanceOf(StringBuilder.class));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], is(appendable));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));

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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], instanceOf(StringBuilder.class));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], instanceOf(StringBuilder.class));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
	}
	
	
	@Test
	public void testTranslateObjectObject() throws Exception {
		
		Object obj = Stringable.of("hello world");

		String translation = translator.translate(obj);
		
		
		assertThat(translation, is("hello world"));
		
		assertThat(translatorInvocations, hasSize(1));
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], instanceOf(StringBuilder.class));
		assertThat(invocation[1], is(Locale.ENGLISH));
		TranslationHint hint = (TranslationHint) invocation[2];
		assertThat(hint.getKey(), is(obj));
		assertThat(hint.getFallback(), is(Optional.of("hello world")));
		assertThat(invocation[3], is(translator));
		
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], is(appendable));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
		
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
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], is(appendable));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(hint));
		assertThat(invocation[3], is(translator));
		
		new Verifications() {{
			appendable.append("hello world");
		}};
	}
	
	
	@Test
	public void testTranslateAppendableObjectObject(
	
		@Mocked Appendable appendable
			
	) throws Exception {
		
		new Expectations() {{
		}};

		
		Object obj = Stringable.of("hello world");
		
		
		translator.translate(appendable, obj);
		
		
		assertThat(translatorInvocations, hasSize(1));
		Object[] invocation = translatorInvocations.get(0);
		assertThat(invocation[0], is(appendable));
		assertThat(invocation[1], is(Locale.ENGLISH));
		assertThat(invocation[2], is(TranslationHint.of(obj).withFallback("hello world")));
		assertThat(invocation[3], is(translator));
		
		new Verifications() {{
			appendable.append("hello world");
		}};
	}
	
    
    @Test
    public void testTranslateLocaleTranslationHint(
    
        @Mocked TranslationHint hint
    
    ) throws Exception {
    
        new Expectations() {{
        }};
        
        
        String translation = translator.translate(Locale.CANADA, hint);
        
        
        assertThat(translation, is("hello world"));

        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], instanceOf(StringBuilder.class));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
    }

    
    @Test
    public void testTranslateLocaleTranslatable(
            
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
        
    ) throws Exception {
        
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        
        
        String translation = translator.translate(Locale.CANADA, translatable);
        
        
        assertThat(translation, is("hello world"));
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], instanceOf(StringBuilder.class));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
    }


    @Test
    public void testTranslateLocaleAppendableTranslatable(
        
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint,
        @Mocked Appendable appendable
        
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        
        
        translator.translate(appendable, Locale.CANADA, translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(appendable));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));

        new Verifications() {{
            appendable.append("hello world");
        }};
    }

    
    @Test
    public void testTranslateLocaleObjectTranslationHint(
            
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
        }};
        
        
        String translation = translator.translate(Locale.CANADA, (Object) hint);
        
        
        assertThat(translation, is("hello world"));
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], instanceOf(StringBuilder.class));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
    }
    
    
    @Test
    public void testTranslateLocaleObjectTranslatable(
            
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        
        
        String translation = translator.translate(Locale.CANADA, (Object) translatable);
        
        
        assertThat(translation, is("hello world"));
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], instanceOf(StringBuilder.class));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
    }
    
    
    @Test
    public void testTranslateLocaleObjectObject() throws Exception {
        
        Object obj = Stringable.of("hello world");

        String translation = translator.translate(Locale.CANADA, obj);
        
        
        assertThat(translation, is("hello world"));
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], instanceOf(StringBuilder.class));
        assertThat(invocation[1], is(Locale.CANADA));
        TranslationHint hint = (TranslationHint) invocation[2];
        assertThat(hint.getKey(), is(obj));
        assertThat(hint.getFallback(), is(Optional.of("hello world")));
        assertThat(invocation[3], is(translator));
        
    }

    
    @Test
    public void testTranslateLocaleAppendableObjectTranslationHint(
            
        @Mocked TranslationHint hint,
        @Mocked Appendable appendable
            
    ) throws Exception {
    
        new Expectations() {{
        }};
        
        
        translator.translate(appendable, Locale.CANADA, (Object) hint);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(appendable));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        
        new Verifications() {{
            appendable.append("hello world");
        }};
    }
    
    
    @Test
    public void testTranslateLocaleAppendableObjectTranslatable(
            
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint,
        @Mocked Appendable appendable
            
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        
        
        translator.translate(appendable, Locale.CANADA, (Object) translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(appendable));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        
        new Verifications() {{
            appendable.append("hello world");
        }};
    }
    
    
    @Test
    public void testTranslateLocaleAppendableObjectObject(
    
        @Mocked Appendable appendable
            
    ) throws Exception {
        
        new Expectations() {{
        }};

        
        Object obj = Stringable.of("hello world");
        
        
        translator.translate(appendable, Locale.CANADA, obj);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(appendable));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(TranslationHint.of(obj).withFallback("hello world")));
        assertThat(invocation[3], is(translator));
        
        new Verifications() {{
            appendable.append("hello world");
        }};
    }
    
    @Test
    public void testTranslateStringBuildereTranslatable(
        
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
        
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        
        StringBuilder sb = new StringBuilder();
        
		translator.translate(sb, translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.ENGLISH));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }


    @Test
    public void testTranslateStringBuildereObjectTranslationHint(
            
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
        }};
        StringBuilder sb = new StringBuilder();

        
		translator.translate(sb, (Object) hint);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.ENGLISH));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }
    
    @Test
    public void testTranslateStringBuildereObjectTranslationHintError() throws Exception {
    
    	StringBuilder sb = new StringBuilder();
    	TranslationHint hint = TranslationHint.of("key");
    	Translator t = Translator.of((l, h, translator) -> {
	    		rethrow(new IOException());
	    		return "";
	    	});
    	
    	
        try {
			t.translate(sb, hint);
        } catch (AssertionError e) {
        	return;
        }
        fail();
    }
    
    @Test
    public void testTranslateStringBuildereObjectLocaleTranslationHintError() throws Exception {
    
    	StringBuilder sb = new StringBuilder();
    	TranslationHint hint = TranslationHint.of("key");
    	Translator t = Translator.of((l, h, translator) -> {
	    		rethrow(new IOException());
	    		return "";
	    	});
    	
    	
        try {
			t.translate(sb, Locale.CANADA, hint);
        } catch (AssertionError e) {
        	return;
        }
        fail();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void rethrow(Throwable e) throws T {
        throw (T) e;
    }

	@Test
    public void testTranslateStringBuildereObjectTranslatable(
            
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        StringBuilder sb = new StringBuilder();
        
        
		translator.translate(sb, (Object) translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.ENGLISH));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }
    
    
    @Test
    public void testTranslateStringBuildereObjectObject() throws Exception {
        
    	StringBuilder sb = new StringBuilder();
        Object obj = Stringable.of("hello world");
        
        
		translator.translate(sb, obj);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.ENGLISH));
        assertThat(invocation[2], is(TranslationHint.of(obj).withFallback("hello world")));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }

    @Test
    public void testTranslateLocaleStringBuilderTranslatable(
        
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
        
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        StringBuilder sb = new StringBuilder();
        
        
		translator.translate(sb, Locale.CANADA, translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }

    @Test
    public void testTranslateLocaleStringBuilderObjectTranslationHint(
            
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
        }};
        StringBuilder sb = new StringBuilder();
        
        
		translator.translate(sb, Locale.CANADA, (Object) hint);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }
    
    
    @Test
    public void testTranslateLocaleStringBuilderObjectTranslatable(
            
        @Mocked Translatable translatable,
        @Mocked TranslationHint hint
            
    ) throws Exception {
    
        new Expectations() {{
            translatable.getTranslationHint(); returns(hint);
        }};
        StringBuilder sb = new StringBuilder();
        
        
		translator.translate(sb, Locale.CANADA, (Object) translatable);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(hint));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }
    
    
    @Test
    public void testTranslateLocaleStringBuilderObjectObject() throws Exception {
        
    	StringBuilder sb = new StringBuilder();
        Object obj = Stringable.of("hello world");
        
        
		translator.translate(sb, Locale.CANADA, obj);
        
        
        assertThat(translatorInvocations, hasSize(1));
        Object[] invocation = translatorInvocations.get(0);
        assertThat(invocation[0], is(sb));
        assertThat(invocation[1], is(Locale.CANADA));
        assertThat(invocation[2], is(TranslationHint.of(obj).withFallback("hello world")));
        assertThat(invocation[3], is(translator));
        assertThat(sb.toString(), is("hello world"));
    }
    
    @Test
	public void testOfStringLocale(
			
		@Mocked TranslationFunction translationFunction,
		@Mocked TranslationHint hint,
		
		@Mocked Appendable appendable
	
	) throws Exception {
    	
    	StringFirstTranslator t = Translator.of(
    			() -> Locale.CANADA,
    			translationFunction);

    	new Expectations() {{
    		translationFunction.translate(Locale.CANADA, hint, t); returns("hello world");
    	}};
    	
    	assertThat(t.translate(hint), is("hello world"));
    	
    	t.translate(appendable, hint);
    	
    	new Verifications() {{
			appendable.append("hello world");
		}};
    }
    
    @Test
   	public void testOfString(
   			
   		@Mocked TranslationFunction translationFunction,
   		@Mocked TranslationHint hint
   	
   	) throws Exception {
       	
       	StringFirstTranslator t = Translator.of(
       			translationFunction);

       	new Expectations() {{
       		translationFunction.translate(Locale.getDefault(), hint, t); returns("hello world");
       	}};
       	
       	assertThat(t.translate(hint), is("hello world"));
   	}
    
    @Test
   	public void testOfAppendableLocale(
   			
   		@Mocked AppendingTranslationFunction translationFunction,
   		@Mocked Appendable appendable,
   		@Mocked TranslationHint hint
   	
   	) throws Exception {
       	
       	AppendableFirstTranslator t = Translator.of(
       			() -> Locale.CANADA,
       			translationFunction);

       	new Expectations() {{
       		translationFunction.translate(appendable, Locale.CANADA, hint, t);
       	}};
       	
       	t.translate(appendable, hint);
    }
       
    @Test
  	public void testOfAppendable(
  			
  		@Mocked AppendingTranslationFunction translationFunction,
  		@Mocked Appendable appendable,
  		@Mocked TranslationHint hint
  	
  	) throws Exception {
      	
      	AppendableFirstTranslator t = Translator.of(
      			translationFunction);

      	new Expectations() {{
      		translationFunction.translate(appendable, Locale.getDefault(), hint, t);
      	}};

      	t.translate(appendable, hint);
  	}
    
    @Test
	public void testOfMessageLookupFormatLocale (
			
		@Mocked MessageLookup lookup,
		@Mocked Format format,
		@Mocked Formatable formatable,
		
		@Mocked Appendable appendable
			
	) throws Exception {
	
    	Translator t = Translator.of(
    			() -> Locale.CANADA,
    			lookup, format);
    	TranslationHint hint = TranslationHint
    			.of("key")
    			.withArguments("arg0", "arg1")
    			.withFallback("fallback");
    	 
    	new Expectations() {{
			lookup.getMessage(Locale.CANADA, hint.getKey(), hint.getFallback()); returns("msg");
			format.toFormatable(Locale.CANADA, "msg"); returns(formatable);
			formatable.format(appendable, hint.getArguments(), t);
		}};
		
		t.translate(appendable, hint);
	}
    
    @Test
	public void testOfMessageSourceFormatLocale (
			
		@Mocked MessageSource source,
		@Mocked Format format,
		@Mocked Formatable formatable,
		
		@Mocked Appendable appendable
			
	) throws Exception {
	
    	Translator t = Translator.of(
    			() -> Locale.CANADA,
    			source, format);
    	TranslationHint hint = TranslationHint
    			.of("key")
    			.withArguments("arg0", "arg1")
    			.withFallback("fallback");
    	 
    	new Expectations() {{
			source.findMessage(Locale.CANADA, "key"); returns(Optional.of("msg"));
			format.toFormatable(Locale.CANADA, "msg"); returns(formatable);
			formatable.format(appendable, hint.getArguments(), t);
		}};
		
		t.translate(appendable, hint);
	}
    
    @Test
	public void testOfMessageLookupFormat (
			
		@Mocked MessageLookup lookup,
		@Mocked Format format,
		@Mocked Formatable formatable,
		
		@Mocked Appendable appendable
			
	) throws Exception {
	
    	Translator t = Translator.of(
    			lookup, format);
    	TranslationHint hint = TranslationHint
    			.of("key")
    			.withArguments("arg0", "arg1")
    			.withFallback("fallback");
    	 
    	new Expectations() {{
			lookup.getMessage(Locale.getDefault(), hint.getKey(), hint.getFallback()); returns("msg");
			format.toFormatable(Locale.getDefault(), "msg"); returns(formatable);
			formatable.format(appendable, hint.getArguments(), t);
		}};
		
		t.translate(appendable, hint);
	}
    
    @Test
	public void testOfMessageSourceFormat (
			
		@Mocked MessageSource source,
		@Mocked Format format,
		@Mocked Formatable formatable,
		
		@Mocked Appendable appendable
			
	) throws Exception {
	
    	Translator t = Translator.of(
    			source, format);
    	TranslationHint hint = TranslationHint
    			.of("key")
    			.withArguments("arg0", "arg1")
    			.withFallback("fallback");
    	 
    	new Expectations() {{
			source.findMessage(Locale.getDefault(), "key"); returns(Optional.of("msg"));
			format.toFormatable(Locale.getDefault(), "msg"); returns(formatable);
			formatable.format(appendable, hint.getArguments(), t);
		}};
		
		t.translate(appendable, hint);
	}
	
}
