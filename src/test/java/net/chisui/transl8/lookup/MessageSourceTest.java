package net.chisui.transl8.lookup;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;

public class MessageSourceTest {

	private final Locale locale = Locale.ENGLISH;
	
	@Test
	public void testOfResourceBundle(
	
		@Mocked ResourceBundle resourceBundle
			
	) throws Exception {

		new Expectations() {{
			ResourceBundle.getBundle("baseName", locale); returns(resourceBundle);
			
			resourceBundle.getString("key"); returns("value");
		}};
	
		assertThat(MessageSource.ofResourceBundle("baseName").findMessage(locale, "key"), is(Optional.of("value")));
		
	}
	
	@Test
	public void testOfResourceBundleMissing(
	
		@Mocked ResourceBundle resourceBundle
			
	) throws Exception {

		new Expectations() {{
			ResourceBundle.getBundle("baseName", locale); returns(resourceBundle);
			
			resourceBundle.getString("key"); result = new MissingResourceException("", "", "");
		}};
	
		assertThat(MessageSource.ofResourceBundle("baseName").findMessage(locale, "key"), is(Optional.empty()));
		
	}

	@Test
	public void testOfMap(
	
		@Mocked Function<Locale, Map<String, String>> getMap,
		@Mocked Map<String, String> map
			
	) throws Exception {
		
		new Expectations() {{
			getMap.apply(locale); returns(map);
			
			map.get("key"); returns("value");
		}};
	
		assertThat(MessageSource.ofMap(getMap).findMessage(locale, "key"), is(Optional.of("value")));
	}

	@Test
	public void testOfMapMissing(
	
		@Mocked Function<Locale, Map<String, String>> getMap,
		@Mocked Map<String, String> map
			
	) throws Exception {
		
		new Expectations() {{
			getMap.apply(locale); returns(map);
			
			map.get("key"); returns(null);
		}};
	
		assertThat(MessageSource.ofMap(getMap).findMessage(locale, "key"), is(Optional.empty()));
	}
}
