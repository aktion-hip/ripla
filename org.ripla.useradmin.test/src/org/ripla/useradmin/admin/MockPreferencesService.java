/*******************************************************************************
 * Copyright (c) 2012 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.useradmin.admin;

import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

/**
 * 
 * @author Benno
 */
public class MockPreferencesService implements PreferencesService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.prefs.PreferencesService#getSystemPreferences()
	 */
	@Override
	public Preferences getSystemPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.prefs.PreferencesService#getUserPreferences(java.lang
	 * .String)
	 */
	@Override
	public Preferences getUserPreferences(final String inName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.prefs.PreferencesService#getUsers()
	 */
	@Override
	public String[] getUsers() {
		// TODO Auto-generated method stub
		return new String[] {};
	}

}
