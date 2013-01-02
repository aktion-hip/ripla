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

package org.ripla.web.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.osgi.service.useradmin.User;
import org.ripla.web.internal.services.ConfigManager;

import com.vaadin.ui.Component;

/**
 * Factory to create toolbar item instances through reflection. <br />
 * Toolbar items created with this factory must have a constructor with the
 * following parameters:
 * <ul>
 * <li>org.ripla.web.util.PreferencesHelper</li>
 * <li>org.ripla.web.internal.services.ConfigManager</li>
 * <li>org.osgi.service.useradmin.User</li>
 * </ul>
 * 
 * @author Luthiger
 */
public class ToolbarItemFactory {

	private transient final PreferencesHelper preferences;
	private transient final ConfigManager configManager;
	private transient User user;

	/**
	 * ToolbarItemFactory constructor.
	 * 
	 * @param inPreferences
	 *            {@link PreferencesHelper}
	 * @param inConfigManager
	 *            {@link ConfigManager}
	 * @param inUser
	 *            {@link User} may be <code>null</code>
	 */
	public ToolbarItemFactory(final PreferencesHelper inPreferences,
			final ConfigManager inConfigManager, final User inUser) {
		preferences = inPreferences;
		configManager = inConfigManager;
		user = inUser;
	}

	/**
	 * Sets a different user to this factory instance.
	 * 
	 * @param inUser
	 *            {@link User}
	 */
	public void setUser(final User inUser) {
		user = inUser;
	}

	/**
	 * The factory method to create a toolbar component instance.
	 * 
	 * @param inClass
	 *            Class the toolbar component class
	 * @return {@link Component} the created toolbar component instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public <T extends Component> T createToolbarComponent(final Class<T> inClass)
			throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Constructor<T> lConst = inClass.getConstructor(new Class[] {
				PreferencesHelper.class, ConfigManager.class, User.class });
		return lConst.newInstance(new Object[] { preferences, configManager,
				user });
	}

}
