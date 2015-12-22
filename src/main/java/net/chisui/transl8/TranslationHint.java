package net.chisui.transl8;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface TranslationHint {

	
	Object getKey();
	
	
	default List<?> getArguments() {
		return Collections.emptyList();
	}

	
	default Optional<String> getFallback() {
		return Optional.empty();
	}
	
	
	static KeyTranslationHint of(final Object key) {
		return new KeyTranslationHint(key);
	}
	
	final class KeyTranslationHint extends ImmutableTranslationHint {

		KeyTranslationHint(final Object key){
			super(key, Collections.emptyList(), Optional.empty());
		}
		
		public KeyFallbackTranslationHint withFallback(final String fallback) {
			return new KeyFallbackTranslationHint(getKey(), fallback);
		}

		public KeyArgumentsTranslationHint withArguments(final Object... args) {
			return new KeyArgumentsTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(args)));
		}
	}
	
	
	static KeyArgumentsTranslationHint of(final Object key, final Object... args) {
		return new KeyArgumentsTranslationHint(key, Collections.unmodifiableList(Arrays.asList(args)));
	}
	
	final class KeyArgumentsTranslationHint extends ImmutableTranslationHint {

		KeyArgumentsTranslationHint(final Object key, final List<?> args) {
			super(key, args, Optional.empty());
		}

		public KeyArgumentsTranslationHint andArguments(final Object... args) {
			final List<?> oldArgs = getArguments();
			final List<Object> newArgs = new ArrayList<>(oldArgs.size() +  args.length);
			newArgs.addAll(oldArgs);
			Collections.addAll(newArgs, args);
			return new KeyArgumentsTranslationHint(getKey(), Collections.unmodifiableList(newArgs));
		}

		public FullTranslationHint withFallback(final String fallback) {
			return new FullTranslationHint(getKey(), getArguments(), Optional.of(fallback));
		}

		
	}
	
	final class KeyFallbackTranslationHint extends ImmutableTranslationHint {

		KeyFallbackTranslationHint(final Object key, final String fallback) {
			super(key, Collections.emptyList(), Optional.of(fallback));
		}
		
		public FullTranslationHint withArguments(final Object... args) {
			return new FullTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(args)), getFallback());
		}

	}
	
	final class FullTranslationHint extends ImmutableTranslationHint {

		FullTranslationHint(final Object key, final List<?> args, final Optional<String> fallback) {
			super(key, args, fallback);
		}
		
		public FullTranslationHint andArguments(final Object... args) {
			final List<?> oldArgs = getArguments();
			final List<Object> newArgs = new ArrayList<>(oldArgs.size() + args.length);
			newArgs.addAll(oldArgs);
			Collections.addAll(newArgs, args);
			return new FullTranslationHint(
					getKey(), 
					Collections.unmodifiableList(newArgs), 
					getFallback());
		}
	}
	
	abstract class AbstractTranslationHint implements TranslationHint {

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getArguments().hashCode();
			result = prime * result + getFallback().hashCode();
			result = prime * result + getKey().hashCode();
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this){
				return true;
			} else if(obj instanceof TranslationHint) {
				final TranslationHint that = (TranslationHint) obj;
				return this.getKey()      .equals(that.getKey())
					&& this.getArguments().equals(that.getArguments())
					&& this.getFallback() .equals(that.getFallback());
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			final List<?> args = getArguments();
			return "TranslationHint [key=\"" + getKey() + "\"" 
					+ (!args.isEmpty() 
							? ", args=" + args
							: "")
					+ getFallback()
							.map(fallback -> ", fallback=\"" + fallback + "\"")
							.orElse("")
					+ "]";
		}
		
	}
	
	class ImmutableTranslationHint extends AbstractTranslationHint {

		private final Object key;
		private final List<?> args;
		private final Optional<String> fallback;

		public ImmutableTranslationHint(
				 final Object key, 
				 final List<?> args, 
				 final Optional<String> fallback) {
			this.key = requireNonNull(key);
			this.args = requireNonNull(args);
			this.fallback = requireNonNull(fallback);
		}
		
		@Override
		public Object getKey() {
			return key;
		}

		@Override
		public List<?> getArguments() {
			return args;
		}
		
		@Override
		public Optional<String> getFallback() {
			return fallback;
		}
	}


}
