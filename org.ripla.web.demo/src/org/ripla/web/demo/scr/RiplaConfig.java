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
package org.ripla.web.demo.scr;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.prefs.PreferencesService;
import org.ripla.util.PreferencesHelper;
import org.ripla.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed service implementation. This class is used to persist the changes
 * made using the <code>ConfigurationAmin</code> into the application's
 * preferences.
 * <p>
 * This component is a service consumer of the
 * <code>org.osgi.service.prefs.PreferencesService</code>.
 * </p>
 * 
 * @author Luthiger
 * @see org.osgi.service.prefs.PreferencesService
 */
public class RiplaConfig implements ManagedService {
	private static final Logger LOG = LoggerFactory
			.getLogger(RiplaConfig.class);
	private final PreferencesHelper preferences = new PreferencesHelper();

	/*
	 * For the properties keys:
	 * 
	 * @see org.ripla.web.internal.services.ConfigManager
	 */
	@Override
	public void updated(final Dictionary<String, ?> inProperties)
			throws ConfigurationException {
		if (inProperties != null) {
			preferences.set(PreferencesHelper.KEY_LANGUAGE,
					(String) inProperties.get(Constants.KEY_CONFIG_LANGUAGE));
			preferences.set(PreferencesHelper.KEY_SKIN,
					(String) inProperties.get(Constants.KEY_CONFIG_SKIN));
		}
	}

	public void setPreferences(final PreferencesService inPreferences) {
		preferences.setPreferences(inPreferences);
		LOG.debug("The OSGi preferences service is made available.");
	}

	public void unsetPreferences(final PreferencesService inPreferences) {
		preferences.dispose();
		LOG.debug("Removed the OSGi preferences service.");
	}

}
