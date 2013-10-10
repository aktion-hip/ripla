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
package org.ripla.rap.internal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.rap.rwt.application.Application;
import org.ripla.rap.Constants;
import org.ripla.rap.services.ISkin;
import org.ripla.rap.services.ISkinService;
import org.ripla.util.PreferencesHelper;

/**
 * Singleton instance to register the skins provided by skin bundles. The
 * provided skins are injected through the service consumer
 * <code>SkinComponent</code>.
 * 
 * @author Luthiger
 * @see SkinComponent
 */
public enum SkinRegistry {
	INSTANCE;

	private final transient Collection<ISkinService> skins = Collections
			.synchronizedList(new ArrayList<ISkinService>());
	private transient ISkinService activeSkin = null;

	private transient PreferencesHelper preferences;
	private transient String dftSkinID = Constants.DFT_SKIN_ID;
	private Application application;

	public void setPreferences(final PreferencesHelper inPreferences) {
		preferences = inPreferences;
	}

	/**
	 * Registering a new skin. Called by the component's bind method.
	 * 
	 * @param inSkin
	 *            {@link ISkinService}
	 */
	public void registerSkin(final ISkinService inSkin) {
		skins.add(inSkin);
	}

	/**
	 * Unregistering a skin. Called by the component's unbind method.
	 * 
	 * @param inSkin
	 *            {@link ISkinService}
	 */
	public void unregisterSkin(final ISkinService inSkin) {
		skins.remove(inSkin);
	}

	/**
	 * Returns the active skin (according to the settings in the application
	 * preferences).
	 * 
	 * @return {@link ISkin} the active skin, may be <code>null</code>, if no
	 *         skins are registered
	 */
	public ISkin getActiveSkin() {
		return (ISkin) getActiveSkinService().createSkin();
	}

	/**
	 * Returns the active skin service, i.e. the service in operation that can
	 * create skin instances (according to the settings in the application
	 * preferences).
	 * 
	 * @return
	 */
	public ISkinService getActiveSkinService() {
		if (activeSkin == null) {
			final String lSkinID = preferences.getActiveSkinID();
			if (lSkinID == null) {
				return calculateSkin(dftSkinID);
			}

			activeSkin = calculateSkin(lSkinID);
		}
		return activeSkin;
	}

	private ISkinService calculateSkin(final String inSkinID) {
		for (final ISkinService lSkin : skins) {
			if (lSkin.getSkinID().equals(inSkinID)) {
				return lSkin;
			}
		}
		return skins.iterator().next();
	}

	/**
	 * Notify the registry about the new skin selection.
	 * 
	 * @param inSkinID
	 *            String the new skin's id
	 */
	public void changeSkin(final String inSkinID) {
		preferences.set(PreferencesHelper.KEY_SKIN, inSkinID);
		activeSkin = calculateSkin(inSkinID);
		activeSkin.handleStyle(application);
	}

	public void flushSkinPref() {
		preferences.set(PreferencesHelper.KEY_SKIN, getActiveSkinService()
				.getSkinID());
	}

	/**
	 * Sets the application's default skin.
	 * 
	 * @param inDftSkinID
	 *            String the default skin's ID
	 */
	public void setDefaultSkin(final String inDftSkinID) {
		dftSkinID = inDftSkinID;
	}

	public boolean isInitialized() {
		return preferences != null;
	}

	/**
	 * Sets the application instance to the registry.
	 * 
	 * @param inApplication
	 *            {@link Application}
	 */
	public void setApplication(final Application inApplication) {
		application = inApplication;
	}
}
