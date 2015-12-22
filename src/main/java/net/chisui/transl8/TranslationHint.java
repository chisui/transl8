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
	
	
	static KeyTranslationHint of( Object key) {
		return new KeyTranslationHint(key);
	}
	
	final class KeyTranslationHint extends ImmutableTranslationHint {

		KeyTranslationHint(Object key){
			super(key, Collections.emptyList(), Optional.empty());
		}
		
		public KeyFallbackTranslationHint withFallback(String fallback) {
			return new KeyFallbackTranslationHint(getKey(), fallback);
		}

		public KeyArgumentsTranslationHint withArgument(Object arg) {
			return new KeyArgumentsTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(arg)));
		}
		
		public KeyArgumentsTranslationHint withArguments(Object... args) {
			return new KeyArgumentsTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(args)));
		}
	}
	
	
	static KeyArgumentsTranslationHint of( Object key,  Object... args) {
		return new KeyArgumentsTranslationHint(key, Collections.unmodifiableList(Arrays.asList(args)));
	}
	
	final class KeyArgumentsTranslationHint extends ImmutableTranslationHint {

		KeyArgumentsTranslationHint(Object key, List<?> args) {
			super(key, args, Optional.empty());
		}

		public KeyArgumentsTranslationHint andArguments(Object... args) {
			List<?> oldArgs = getArguments();
			List<Object> newArgs = new ArrayList<>(oldArgs.size() +  args.length);
			newArgs.addAll(oldArgs);
			Collections.addAll(newArgs, args);
			return new KeyArgumentsTranslationHint(getKey(), Collections.unmodifiableList(newArgs));
		}

		public FullTranslationHint withFallback(String fallback) {
			return new FullTranslationHint(getKey(), getArguments(), Optional.of(fallback));
		}

		
	}
	
	final class KeyFallbackTranslationHint extends ImmutableTranslationHint {

		KeyFallbackTranslationHint(Object key, String fallback) {
			super(key, Collections.emptyList(), Optional.of(fallback));
		}
		
		public FullTranslationHint withArgument(Object arg) {
			return new FullTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(arg)), getFallback());
		}
		
		public FullTranslationHint withArguments(Object... args) {
			return new FullTranslationHint(getKey(), Collections.unmodifiableList(Arrays.asList(args)), getFallback());
		}

	}
	
	final class FullTranslationHint extends ImmutableTranslationHint {

		FullTranslationHint(Object key, List<?> args, Optional<String> fallback) {
			super(key, args, fallback);
		}
		
		public FullTranslationHint andArgument(Object arg) {
			List<?> oldArgs = getArguments();
			List<Object> newArgs = new ArrayList<>(oldArgs.size() + 1);
			newArgs.addAll(oldArgs);
			newArgs.add(arg);
			return new FullTranslationHint(
					getKey(), 
					Collections.unmodifiableList(newArgs), 
					getFallback());
		}
		
		public FullTranslationHint andArguments(Object... args) {
			List<?> oldArgs = getArguments();
			List<Object> newArgs = new ArrayList<>(oldArgs.size() + args.length);
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
		public boolean equals(Object obj) {
			if (obj == this){
				return true;
			} else if(obj instanceof TranslationHint) {
				TranslationHint that = (TranslationHint) obj;
				return this.getKey().equals(that.getKey())
					&& this.getArguments().equals(that.getArguments())
					&& this.getFallback().equals(that.getFallback());
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			List<?> args = getArguments();
			return getClass().getSimpleName() 
					+ " [key=\"" + getKey() + "\"" 
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
				 Object key, 
				 List<?> args, 
				 Optional<String> fallback) {
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
