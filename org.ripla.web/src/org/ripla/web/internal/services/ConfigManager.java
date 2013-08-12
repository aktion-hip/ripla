/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.web.internal.services;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.ripla.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to manage the access to the application's configuration
 * administration <code>org.osgi.service.cm.ConfigurationAdmin</code>.
 * 
 * @author Luthiger
 */
public class ConfigManager {
	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigManager.class);

	public static final String PID = "org.ripla.web.configuration";
	public static final String KEY_SKIN = "org.ripla.config.skin";
	public static final String KEY_LANGUAGE = "org.ripla.config.language";

	private transient ConfigurationAdmin configAdmin = null;;

	/**
	 * Sets the config admin instance.
	 * 
	 * @param inConfigAdmin
	 *            {@link ConfigurationAdmin} the configuration to manage.
	 */
	public void setConfigAdmin(final ConfigurationAdmin inConfigAdmin) {
		configAdmin = inConfigAdmin;
	}

	/**
	 * Removes the config admin.
	 */
	public void clearConfigAdmin() {
		configAdmin = null; // NOPMD by Luthiger on 02.01.13 12:44
	}

	/**
	 * Returns the configured value for the specified key from the configuration
	 * with the specified PID.
	 * 
	 * @param inMetaPID
	 *            String the configuration's PID
	 * @param inKey
	 *            String the value's key
	 * @param inDftValue
	 *            String a suitable default value if something goes wrong
	 * @return String
	 */
	public String getValue(final String inMetaPID, final String inKey,
			final String inDftValue) {
		if (configAdmin == null) {
			return inDftValue;
		}
		try {
			final Configuration lConfiguration = configAdmin
					.getConfiguration(inMetaPID);
			final Dictionary<String, Object> lProperties = lConfiguration
					.getProperties();
			if (lProperties == null) {
				return inDftValue;
			}
			final Object out = lProperties.get(inKey);
			return (String) (out == null ? inDftValue : out);
		}
		catch (final IOException exc) {
			LOG.error(
					"Error encoutered while retrieving the configured value '{}'!",
					new Object[] { inKey, exc });
		}
		return inDftValue;
	}

	/**
	 * Returns the configured value for the specified key from the Ripla
	 * configuration (with PID "org.ripla.configuration").
	 * 
	 * @param inKey
	 *            String the value's key
	 * @param inDftValue
	 *            a suitable default value if something goes wrong
	 * @return String
	 */
	public String getValue(final String inKey, final String inDftValue) {
		return getValue(PID, inKey, inDftValue);
	}

	/**
	 * Convenience method to return the configured skin id.
	 * 
	 * @return String the skin id from configuration admin
	 */
	public String getSkinID() {
		return getValue(KEY_SKIN, Constants.DFT_SKIN_ID);
	}

	/**
	 * Convenience method to return the configured language for the application.
	 * 
	 * @return String the language (lowercase ISO 639 code)
	 */
	public String getLanguage() {
		return getValue(KEY_LANGUAGE, Constants.DFT_LANGUAGE);
	}

	/**
	 * Makes the changed skin id visible in the config admin's metadata.
	 * 
	 * @param inSkinID
	 *            String the new skin id
	 */
	public void setSkinID(final String inSkinID) {
		setValue(PID, KEY_SKIN, inSkinID);
	}

	/**
	 * Makes the changed language visible in the config admin's metadata.
	 * 
	 * @param inLanguage
	 *            String the new language value
	 */
	public void setLanguage(final String inLanguage) {
		setValue(PID, KEY_LANGUAGE, inLanguage);
	}

	private void setValue(final String inMetaPID, final String inKey,
			final String inValue) {
		if (configAdmin == null) {
			return;
		}
		try {
			final Configuration lConfiguration = configAdmin
					.getConfiguration(inMetaPID);
			final Dictionary<String, Object> lProperties = lConfiguration
					.getProperties();
			if (lProperties != null) {
				lProperties.put(inKey, inValue);
				lConfiguration.update(lProperties);
			}
		}
		catch (final IOException exc) {
			LOG.error(
					"Error encoutered while setting value '{}' to configuration!",
					new Object[] { inKey, exc });
		}
	}

}
