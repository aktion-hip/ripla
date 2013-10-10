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
package org.ripla.rap.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
	 * @param inParent
	 *            {@link Composite}
	 * @param String
	 *            the application's name
	 * @return {@link Composite}
	 */
	Composite getHeader(Composite inParent, String inAppName);

	/**
	 * @return <code>true</code> if the application should display a footer
	 */
	boolean hasFooter();

	/**
	 * Create the skin's footer component.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @return {@link Composite}
	 */
	Composite getFooter(Composite inParent);

	/**
	 * @return boolean <code>true</code> if the application should display a
	 *         tool bar
	 */
	boolean hasToolBar();

	/**
	 * @param inParent
	 *            {@link Composite}
	 * @return {@link Label} the toolbar separator
	 */
	Label getToolbarSeparator(Composite inParent);

	/**
	 * @return boolean <code>true</code> if the application should display a
	 *         menu bar
	 */
	boolean hasMenuBar();

	/**
	 * <p>
	 * Returns the component containing the menu bar.
	 * </p>
	 * <p>
	 * This component is added to the application's body component. This
	 * component has to contain the <code>MenuBar</code>.
	 * </p>
	 * <p>
	 * May be <code>null</code> for the default menu bar component.
	 * </p>
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @return {@link Composite} the menu bar component
	 */
	Composite getMenuBarMedium(Composite inParent);

	/**
	 * <p>
	 * Returns the menu bar composite.
	 * </p>
	 * <p>
	 * You have to add this composite to the <code>MenuBarMedium</code>. This
	 * layout is the component where the application's pulldown
	 * <code>Menu</code>s are added to. In simple cases, the
	 * <code>MenuBar</code> is the <code>MenuBarMedium</code>.
	 * </p>
	 * <p>
	 * May be <code>null</code> for the default menu bar layout.
	 * </p>
	 * 
	 * @return {@link Composite} the menu bar composite
	 */
	Composite getMenuBar(Composite inParent);

	/**
	 * The menu items in the main menu may display an indicator for the sub
	 * menu.
	 * 
	 * @return {@link Image} the icon for the sub menu, may be <code>null</code>
	 */
	Image getSubMenuIcon();

}
