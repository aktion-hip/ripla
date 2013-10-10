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
package org.ripla.rap.interfaces;

import java.awt.Component;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.useradmin.User;
import org.ripla.exceptions.NoControllerFoundException;

/**
 * Interface for Ripla body components.
 * 
 * @author Luthiger
 */
public interface IBodyComponent {

	/**
	 * Loads the controller with the specified name, i.e. runs the controller
	 * and returns the created <code>Component</code>.
	 * 
	 * @param inControllerName
	 *            String the name of the controller responsible to create the
	 *            component
	 * @return {@link Composite}
	 * @throws NoControllerFoundException
	 */
	// Composite getContentComponent(String inControllerName)
	// throws NoControllerFoundException;

	/**
	 * Sets the component (i.e. the controller with the specified name) for the
	 * main content view.
	 * 
	 * @param inControllerName
	 *            String
	 * @throws NoControllerFoundException
	 */
	void setContentView(final String inControllerName)
			throws NoControllerFoundException;

	/**
	 * Display the default view with an error message.
	 * 
	 * @param inExc
	 *            {@link NoControllerFoundException}
	 */
	void setContentView(final NoControllerFoundException inExc);

	/**
	 * Loads the context menu with the specified set name into the sidebar (i.e.
	 * context menu) panel.
	 * 
	 * @param inMenuSetName
	 *            String the name of the context menu set
	 * @param inControllerClass
	 *            Class&lt;? extends IPluggable> the active controller class
	 * @throws NoControllerFoundException
	 */
	void setContextMenu(final String inMenuSetName,
			final Class<? extends IPluggable> inControllerClass)
			throws NoControllerFoundException;

	/**
	 * Displays a notification in the main window.
	 * 
	 * @param inNotification
	 *            String the message to display
	 * @param inNotificationType
	 *            int the message type, e.g.
	 *            <code>Notification.TYPE_WARNING_MESSAGE</code>
	 */
	// void showNotification(String inNotification, int inNotificationType);

	/**
	 * Refresh the application's view e.g. after the user changed the language.
	 */
	void refreshBody();

	/**
	 * Callback method to display the application's views after the user has
	 * successfully logged in.
	 * 
	 * @param inUser
	 *            {@link User} the user instance
	 */
	void showAfterLogin(User inUser);

	/**
	 * Displays the application's default view, e.g. the first view of the first
	 * menu.
	 */
	void showDefault();

	/**
	 * Ends the application.
	 */
	// void close();

	/**
	 * The factory method to create a toolbar component instance. <br />
	 * Toolbar items created with this factory must have a constructor with the
	 * following parameters:
	 * <ul>
	 * <li>org.eclipse.swt.widgets.Composite</li>
	 * <li>org.ripla.util.PreferencesHelper</li>
	 * <li>org.ripla.rap.internal.services.ConfigManager</li>
	 * <li>org.osgi.service.useradmin.User</li>
	 * </ul>
	 * 
	 * @param inClass
	 *            Class the toolbar component class
	 * @return {@link Component} the created toolbar component instance or
	 *         <code>null</code> in case of an error
	 */
	<T extends Composite> T createToolbarItem(final Class<T> inClass);

}
