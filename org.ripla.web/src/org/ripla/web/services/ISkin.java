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
package org.ripla.web.services;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Interface defining the ripla application's <code>skin</code> service.
 * 
 * @author Luthiger
 */
public interface ISkin extends org.ripla.services.ISkin {

	/**
	 * @return boolean <code>true</code> if the application should display a
	 *         header view
	 */
	boolean hasHeader();

	/**
	 * Create the skin's header component.
	 * 
	 * @param String
	 *            the application's name
	 * @return {@link Component}
	 */
	Component getHeader(String inAppName);

	/**
	 * @return <code>true</code> if the application should display a footer
	 */
	boolean hasFooter();

	/**
	 * Create the skin's footer component.
	 * 
	 * @return {@link Component}
	 */
	Component getFooter();

	/**
	 * @return boolean <code>true</code> if the application should display a
	 *         tool bar
	 */
	boolean hasToolBar();

	/**
	 * @return {@link Label} the toolbar separator
	 */
	Label getToolbarSeparator();

	/**
	 * @return boolean <code>true</code> if the application should display a
	 *         menu bar
	 */
	boolean hasMenuBar();

	/**
	 * <p>
	 * Returns the layout component containing the menu bar.
	 * </p>
	 * <p>
	 * This component is added to the application's body component. This
	 * component has to contain the <code>MenuBar</code>.
	 * </p>
	 * <p>
	 * May be <code>null</code> for the default menu bar component.
	 * </p>
	 * 
	 * @return {@link HorizontalLayout} the menu bar component
	 */
	HorizontalLayout getMenuBarMedium();

	/**
	 * <p>
	 * Returns the menu bar layout.
	 * </p>
	 * <p>
	 * You have to add this layout to the <code>MenuBarMedium</code>. This
	 * layout is the component where the application's
	 * <code>Vaadin MenuBar</code> (i.e. the main menu items) is added to. In
	 * simple cases, the <code>MenuBar</code> is the <code>MenuBarMedium</code>.
	 * </p>
	 * <p>
	 * May be <code>null</code> for the default menu bar layout.
	 * </p>
	 * 
	 * @return {@link HorizontalLayout} a layout for the menu bar
	 */
	HorizontalLayout getMenuBar();

	/**
	 * The menu items in the main menu may display an indicator for the sub
	 * menu.
	 * 
	 * @return {@link Resource} the icon for the sub menu, may be
	 *         <code>null</code>
	 */
	Resource getSubMenuIcon();

}
