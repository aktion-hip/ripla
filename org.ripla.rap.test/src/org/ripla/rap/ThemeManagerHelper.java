package org.ripla.rap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.theme.Theme;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.service.ResourceLoader;

@SuppressWarnings("restriction")
public class ThemeManagerHelper {

	private static ThemeManager themeManager;
	static {
		replaceStandardResourceLoader();
	}

	public static void adaptApplicationContext(final Object toAdapt) {
		if (toAdapt instanceof ApplicationContextImpl) {
			ensureThemeManager();
			((ApplicationContextImpl) toAdapt).setThemeManager(themeManager);
		}
	}

	public static void resetThemeManager() {
		if (isThemeManagerAvailable()) {
			doThemeManagerReset();
		}
	}

	public static void resetThemeManagerIfNeeded() {
		if (isThemeManagerResetNeeded()) {
			doThemeManagerReset();
		}
	}

	public static void replaceStandardResourceLoader() {
		ThemeManager.STANDARD_RESOURCE_LOADER = new TestResourceLoader();
	}

	private static void ensureThemeManager() {
		if (themeManager == null) {
			themeManager = new TestThemeManager();
		}
	}

	private static void doThemeManagerReset() {
		((TestThemeManager) themeManager).resetInstanceInTestCases();
	}

	private static boolean isThemeManagerResetNeeded() {
		boolean result = isThemeManagerAvailable();
		if (result) {
			final List<String> registeredThemeIds = Arrays.asList(themeManager
					.getRegisteredThemeIds());
			if (registeredThemeIds.size() == 2) {
				result = !registeredThemeIds
						.contains(ThemeManager.FALLBACK_THEME_ID)
						|| !registeredThemeIds.contains(RWT.DEFAULT_THEME_ID);
			}
		}
		return result;
	}

	private static boolean isThemeManagerAvailable() {
		return themeManager != null;
	}

	public static class TestThemeManager extends ThemeManager {
		boolean initialized;
		boolean activated;
		boolean deactivated;

		@Override
		public void initialize() {
			if (!initialized) {
				// Register empty default theme. Execute tests against fall-back
				// theme.
				final Theme lTheme = new Theme(RWT.DEFAULT_THEME_ID,
						"RAP Default Theme", null);
				// addThemeableWidget(Button.class, new TestResourceLoader());
				// lTheme.initialize(getThemeableWidget());
				registerTheme(lTheme);
				initialized = true;
			}
		}

		@Override
		public void activate() {
			if (!activated) {
				super.activate();
				activated = true;
			}
			deactivated = false;
		}

		@Override
		public void deactivate() {
			// ignore reset for test cases to improve performance
			deactivated = true;
		}

		@Override
		public String[] getRegisteredThemeIds() {
			String[] result = new String[0];
			if (!deactivated) {
				result = super.getRegisteredThemeIds();
			}
			return result;
		}

		public void resetInstanceInTestCases() {
			initialized = false;
			activated = false;
			super.deactivate();
		}
	}

	// TODO [ApplicationContext]: Used as performance optimized solution for
	// tests. At the time being
	// buffering speeds up RWTAllTestSuite about 10% on my machine. Think about
	// a less intrusive
	// solution.
	private static class TestResourceLoader implements ResourceLoader {
		private final ClassLoader classLoader = ThemeManager.class
				.getClassLoader();
		private final Map<String, StreamBuffer> resourceStreams = new HashMap<String, StreamBuffer>();

		@Override
		public InputStream getResourceAsStream(final String name)
				throws IOException {
			StreamBuffer result = resourceStreams.get(name);
			if (!hasStreamBuffer(result)) {
				result = bufferStream(name);
			} else {
				result.reset();
			}
			return result;
		}

		private StreamBuffer bufferStream(final String name) {
			StreamBuffer result = null;
			final InputStream in = classLoader.getResourceAsStream(name);
			if (in != null) {
				result = new StreamBuffer(in);
				result.mark(Integer.MAX_VALUE);
				resourceStreams.put(name, result);
			}
			return result;
		}

		private boolean hasStreamBuffer(final StreamBuffer result) {
			return result != null;
		}
	}

	private static class StreamBuffer extends BufferedInputStream {
		private StreamBuffer(final InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
		}
	}

}