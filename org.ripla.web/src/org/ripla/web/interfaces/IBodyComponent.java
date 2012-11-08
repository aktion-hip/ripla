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

package org.ripla.web.interfaces;

import org.ripla.web.exceptions.NoControllerFoundException;

import com.vaadin.ui.Component;

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
	 * @return {@link Component}
	 * @throws NoControllerFoundException
	 */
	Component getContentComponent(String inControllerName)
			throws NoControllerFoundException;

	/**
	 * Sets the component for the main content view.
	 * 
	 * @param inComponent
	 *            {@link Component}
	 */
	void setContentView(Component inComponent);

	/**
	 * Loads the context menu with the specified set name into the sidebar (i.e.
	 * conext menu) panel.
	 * 
	 * @param inMenuSetName
	 *            String the name of the context menu set
	 * @param inControllerClass
	 *            Class&lt;? extends IPluggable> the active controller class
	 */
	void setContextMenu(String inMenuSetName,
			Class<? extends IPluggable> inControllerClass);

	/**
	 * Displays a notification in the main window.
	 * 
	 * @param inNotification
	 *            String the message to display
	 * @param inNotificationType
	 *            int the message type, e.g.
	 *            <code>Notification.TYPE_WARNING_MESSAGE</code>
	 */
	void showNotification(String inNotification, int inNotificationType);

	/**
	 * Refresh the application's view e.g. after the user changed the language.
	 */
	void refreshBody();

	/**
	 * Displays the application's default view, e.g. the first view of the menu.
	 */
	void showDefault();

	/**
	 * Ends the application.
	 */
	void close();

}
