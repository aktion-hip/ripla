/*
	This package is part of the application VIF.
	Copyright (C) 2011, Benno Luthiger

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.ripla.web.services;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Interface defining the ripla application's <code>skin</code> service.
 * 
 * @author Luthiger
 */
public interface ISkin {

	/**
	 * @return String this skin bundle's ID, i.e. symbolic name: <code>bundleContext.getBundle().getSymbolicName()</code>.
	 */
	String getSkinID();
	
	/**
	 * @return String the name of the skin, displayed in the skin select view
	 */
	String getSkinName();
	
	/**
	 * @return boolean <code>true</code> if the application should display a header view
	 */
	boolean hasHeader();
	
	/**
	 * Create the skin's header component.
	 * 
	 * @return {@link Component}
	 */
	Component getHeader();
	
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
	 * @return boolean <code>true</code> if the application should display a tool bar
	 */
	boolean hasToolBar();
	
	/**
	 * @return {@link Label} the toolbar separator
	 */
	Label getToolbarSeparator();

	/**
	 * @return boolean <code>true</code> if the application should display a menu bar
	 */
	boolean hasMenuBar();
	
	/**
	 * <p>Returns the menu bar component.</p>
	 * <p>This component is added to the application's body component. 
	 * This component has to contain the <code>MenuBarLayout</code>.</p> 
	 * <p>May be <code>null</code> for the default menu bar component.</p>
	 * 
	 * @return {@link HorizontalLayout} the menu bar component
	 */
	HorizontalLayout getMenuBarComponent();	
	
	/**
	 * <p>Returns the menu bar layout.</p>
	 * <p>You have to add this layout to the <code>MenuBarComponent</code>.
	 * This layout is the component where the application's <code>Vaadin MenuBar</code> is added too.
	 * In simple cases, the <code>MenuBarLayout</code> is the <code>MenuBarComponent</code>.</p>
	 * <p>May be <code>null</code> for the default menu bar layout.</p>
	 * 
	 * @return {@link HorizontalLayout} a layout for the menu bar
	 */
	HorizontalLayout getMenuBarLayout();

}
