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

package org.ripla.rap.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.useradmin.User;
import org.ripla.rap.internal.services.ConfigManager;
import org.ripla.util.PreferencesHelper;

/**
 * Factory to create toolbar item instances through reflection. <br />
 * Toolbar items created with this factory must have a constructor with the
 * following parameters:
 * <ul>
 * <li>org.eclipse.swt.widgets.Composite</li>
 * <li>org.ripla.util.PreferencesHelper</li>
 * <li>org.ripla.rap.internal.services.ConfigManager</li>
 * <li>org.osgi.service.useradmin.User</li>
 * </ul>
 * 
 * @author Luthiger
 */
public class ToolbarItemFactory {
	private transient final PreferencesHelper preferences;
	private transient final ConfigManager configManager;
	private transient User user;
	private Composite toolbar;

	/**
	 * ToolbarItemFactory constructor.
	 * 
	 * @param inPreferences
	 *            {@link PreferencesHelper}
	 * @param inConfigManager
	 *            {@link ConfigManager}
	 * @param inUser
	 *            {@link User}
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
	 * Sets the toolbar composite that will be the items' parent.
	 * 
	 * @param inToolbar
	 *            {@link Composite}
	 */
	public void setParent(final Composite inToolbar) {
		toolbar = inToolbar;
	}

	/**
	 * The factory method to create a toolbar component instance.
	 * 
	 * @param inClass
	 *            Class the toolbar component class
	 * @return {@link Composite} the created toolbar component instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public <T extends Composite> T createToolbarComponent(final Class<T> inClass)
			throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final Constructor<T> lConst = inClass.getConstructor(new Class[] {
				Composite.class, PreferencesHelper.class, ConfigManager.class,
				User.class });
		return lConst.newInstance(new Object[] { toolbar, preferences,
				configManager, user });
	}

	// ---

	/**
	 * Convenience method, creates a composite to hold a toolbar label. This
	 * composite can be styled with <code>ripla-toolbar-item-holder</code>.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @return {@link Composite}
	 */
	public static Composite createItemHolder(final Composite inParent) {
		final Composite out = new Composite(inParent, SWT.NONE);
		out.setLayout(GridLayoutHelper.createGridLayout());
		out.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-item-holder");
		return out;
	}

}
