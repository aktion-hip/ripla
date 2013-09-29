/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.util;

import java.util.Locale;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.useradmin.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for managing the OSGi preferences service.<br />
 * This class may be extended by subclasses.
 * 
 * @author Luthiger
 */
public class PreferencesHelper {
	private static final Logger LOG = LoggerFactory
			.getLogger(PreferencesHelper.class);

	public static final String KEY_SKIN = "org.ripla.skin";
	public static final String KEY_LANGUAGE = "org.ripla.language";

	private transient PreferencesService preferences;

	/**
	 * @param inPreferences
	 *            {@link PreferencesService} setter for the preferences service
	 */
	public final void setPreferences(final PreferencesService inPreferences) {
		if (inPreferences == null) {
			return;
		}
		preferences = inPreferences;
	}

	/**
	 * Release resources.
	 */
	public final void dispose() {
		preferences = null; // NOPMD by Luthiger on 10.09.12 00:20
	}

	/**
	 * Getter for the property with the specified key.
	 * 
	 * @param inKey
	 *            String the key for the property value
	 * @param inDftValue
	 *            String
	 * @return String the property's value (from the preferences) or the
	 *         specified default value
	 */
	public final String get(final String inKey, final String inDftValue) {
		if (preferences == null) {
			return inDftValue;
		}
		final Preferences lPreferences = preferences.getSystemPreferences();
		return lPreferences.get(inKey, inDftValue);
	}

	/**
	 * Setter for the value of the property with the specified key. The value is
	 * only stored to the preferences, if the passed value is different from the
	 * original value.
	 * 
	 * @param inKey
	 *            String
	 * @param inValue
	 *            String
	 * @return boolean <code>true</code> if the value changed in the preferences
	 */
	public final boolean set(final String inKey, final String inValue) {
		if (preferences == null) {
			return false;
		}

		final Preferences lPreferences = preferences.getSystemPreferences();
		return set(inKey, inValue, lPreferences);
	}

	private boolean set(final String inKey, final String inValue,
			final Preferences inPreferences) {
		if (!inPreferences.get(inKey, "").equals(inValue)) {
			inPreferences.remove(inKey);
			inPreferences.put(inKey, inValue);
			savePreferences(inPreferences);
			return true;
		}
		return false;
	}

	private void savePreferences(final Preferences inPreferences) {
		try {
			inPreferences.flush();
		}
		catch (final BackingStoreException exc) {
			LOG.error("Can't save preferences!", exc);
		}
	}

	/**
	 * @return String the key of the skin stored to the preferences (might be
	 *         <code>null</code>)
	 */
	public String getActiveSkinID() {
		return get(KEY_SKIN, null);
	}

	/**
	 * Retrieves the configured locale (for the system user).
	 * 
	 * @param inDft
	 *            {@link Locale} the system's locale
	 * @return {@link Locale}
	 */
	public Locale getLocale(final Locale inDft) {
		final String lLanguage = get(KEY_LANGUAGE, inDft.getLanguage());
		return new Locale(lLanguage);
	}

	/**
	 * Retrieves the configured locale for the specified user.
	 * 
	 * @param inUser
	 *            {@link User}
	 * @param inDft
	 *            {@link Locale} the system's locale
	 * @return {@link Locale}
	 */
	public Locale getLocale(final User inUser, final Locale inDft) {
		if (preferences == null) {
			return inDft;
		}
		if (inUser == null) {
			return getLocale(inDft);
		}
		final Preferences lPreferences = preferences.getUserPreferences(inUser
				.getName());
		return new Locale(lPreferences.get(KEY_LANGUAGE, inDft.getLanguage()));
	}

	/**
	 * Sets the locale for the system user.
	 * 
	 * @param inLocale
	 *            {@link Locale}
	 */
	public void setLocale(final Locale inLocale) {
		if (preferences != null) {
			set(KEY_LANGUAGE, inLocale.getLanguage(),
					preferences.getSystemPreferences());
		}
	}

	/**
	 * Sets the locale for the specified user.
	 * 
	 * @param inLocale
	 *            {@link Locale}
	 * @param inUser
	 *            {@link User}
	 */
	public void setLocale(final Locale inLocale, final User inUser) {
		if (preferences != null) {
			set(KEY_LANGUAGE, inLocale.getLanguage(),
					preferences.getUserPreferences(inUser.getName()));
		}
	}

}
