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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.interfaces.IToolbarItemCreator;

/**
 * <p>
 * Interface defining the ripla application's <code>toolbar</code> service, i.e.
 * the service to register toolbar items.
 * </p>
 * <p>
 * Each implementation of this service provides a UI component to the
 * application's toolbar. The UI component is provided either by the
 * {@link #getCreator()} or the {@link #getComponent()} method. If both method's
 * return value is != <code>null</code>, <code>getCreator()</code> takes
 * precedence.
 * </p>
 * 
 * @author Luthiger
 */
public interface IToolbarItem {

	/**
	 * Returns a toolbar item factory to create the toolbar item's ui component.
	 * 
	 * @return {@link IToolbarItemCreator} may be <code>null</code> if
	 *         {@link #getComponent()} is defined
	 */
	IToolbarItemCreator getCreator();

	/**
	 * Returns the toolbar item's ui component.
	 * 
	 * @param inToolbar
	 *            {@link Composite} the toolbar composite
	 * @return {@link Control}, may be <code>null</code> if
	 *         {@link #getCreator()} is defined
	 */
	Control getComponent(Composite inToolbar);

	/**
	 * Returns the item's position on the toolbar. The items are placed from
	 * right to left on the toolbar. The higher the value, the higher the
	 * position from the right margin.
	 * 
	 * @return int the items position on the toolbar
	 */
	int getPosition();

	/**
	 * Registers a listener to <code>IToolbarAction</code> events.
	 * 
	 * @param inListener
	 *            IToolbarActionListener
	 */
	void registerToolbarActionListener(IToolbarActionListener inListener);

}
