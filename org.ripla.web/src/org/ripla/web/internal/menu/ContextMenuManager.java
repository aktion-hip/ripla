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

package org.ripla.web.internal.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IContextMenuItem;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.util.ParameterObject;
import org.ripla.web.util.UseCaseHelper;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Helper class responsible for managing all context menu items.
 * <p>
 * The context menu is the menu displayed in the sidebar panel, i.e. the left
 * part of the main window.
 * </p>
 * 
 * @author Luthiger
 */
public final class ContextMenuManager {

	private transient EventAdmin eventAdmin;
	private final transient Map<String, ContextMenuSet> contextMenus = Collections
			.synchronizedMap(new HashMap<String, ContextMenuManager.ContextMenuSet>());

	/**
	 * Private constructor.
	 */
	private ContextMenuManager() {
	}

	/**
	 * Factory method, creates an initialized instance of
	 * {@link ContextMenuManager}.
	 * 
	 * @return {@link ContextMenuManager}
	 */
	public static ContextMenuManager createInstance() {
		final ContextMenuManager out = new ContextMenuManager();
		// add empty menu
		out.addContextMenuSet(new IMenuSet() {
			@Override
			public String getSetID() {
				return Constants.MENU_SET_ID_EMPTY;
			}

			@Override
			public IContextMenuItem[] getContextMenuItems() {
				return new IContextMenuItem[] {};
			}
		});
		return out;
	}

	/**
	 * Sets the <code>EventAdmin</code> to the menu manager for that it can send
	 * events.
	 * 
	 * @param inEventAdmin
	 *            {@link EventAdmin}
	 */
	public void setEventAdmin(final EventAdmin inEventAdmin) {
		eventAdmin = inEventAdmin;
	}

	/**
	 * Adds the configuration of a context menu set to the menu manager.
	 * 
	 * @param inMenuSet
	 *            {@link IMenuSet}
	 */
	public void addContextMenuSet(final IMenuSet inMenuSet) {
		final String lSetID = UseCaseHelper.createFullyQualifiedID(
				inMenuSet.getSetID(), inMenuSet.getClass());
		final ContextMenuSet lContextMenuSet = new ContextMenuSet();
		for (final IContextMenuItem lContextMenuItem : inMenuSet
				.getContextMenuItems()) {
			lContextMenuSet.addContextMenuItem(new ContextMenuItem( // NOPMD
					lContextMenuItem));
		}
		contextMenus.put(lSetID, lContextMenuSet);
	}

	/**
	 * Removes the configuration of a context menu set from the menu manager.
	 * 
	 * @param inMenuSet
	 *            {@link IMenuSet}
	 */
	public void removeContextMenuSet(final IMenuSet inMenuSet) {
		contextMenus.remove(UseCaseHelper.createFullyQualifiedID(
				inMenuSet.getSetID(), inMenuSet.getClass()));
	}

	/**
	 * Method to render the context menu.
	 * 
	 * @param inMenuSetName
	 *            String the fully qualified ID of the context menu
	 * @param inUser
	 *            {@link User} the user instance, might be evaluated to check
	 *            the conditions
	 * @param inAuthorization
	 *            {@link Authorization} the authorization instance, will be
	 *            evaluate to check the conditions
	 * @param inParameters
	 *            {@link ParameterObject} the generic parameter object with
	 *            parameters that could be evaluated to check the conditions
	 * @param inControllerClass
	 *            Class&lt;? extends IPluggable> the active controller class
	 * @return {@link Component} the component that displays the rendered
	 *         context menu
	 */
	public Component renderContextMenu(final String inMenuSetName,
			final User inUser, final Authorization inAuthorization,
			final ParameterObject inParameters,
			final Class<? extends IPluggable> inControllerClass) {
		final VerticalLayout outContextMenu = new VerticalLayout();
		outContextMenu.setMargin(true);
		outContextMenu.setStyleName("ripla-contextmenu"); //$NON-NLS-1$

		final ContextMenuSet lContextMenuSet = contextMenus.get(inMenuSetName);
		if (lContextMenuSet == null) {
			return outContextMenu;
		}

		for (final ContextMenuItem lItem : lContextMenuSet
				.getContextMenuItems()) {
			if (lItem.checkConditions(inUser, inAuthorization, inParameters)) {
				final Button lContextMenuLink = new Button(lItem.getCaption()); // NOPMD
				lContextMenuLink.setStyleName(BaseTheme.BUTTON_LINK);
				lContextMenuLink.addStyleName("ripla-contextmenu-item");
				lContextMenuLink.setSizeUndefined();
				final Class<? extends IPluggable> lControllerClass = lItem
						.getControllerClass();
				if (lControllerClass.equals(inControllerClass)) {
					lContextMenuLink.addStyleName("active");
				}
				lContextMenuLink.addListener(new ContextMenuListener( // NOPMD
						lControllerClass, eventAdmin));
				outContextMenu.addComponent(lContextMenuLink);
			}
		}

		return outContextMenu;
	}

	// --- private classes ---

	@SuppressWarnings("serial")
	private static class ContextMenuListener implements ClickListener {
		private final Class<? extends IPluggable> controllerClass;
		private final EventAdmin eventAdmin;

		ContextMenuListener(
				final Class<? extends IPluggable> inControllerClass,
				final EventAdmin inEventAdmin) {
			controllerClass = inControllerClass;
			eventAdmin = inEventAdmin;
		}

		@Override
		public void buttonClick(final ClickEvent inEvent) {
			final Map<String, Object> lProperties = new HashMap<String, Object>();
			lProperties
					.put(Constants.EVENT_PROPERTY_NEXT_CONTROLLER,
							UseCaseHelper
									.createFullyQualifiedControllerName(controllerClass));

			final Event lEvent = new Event(Constants.EVENT_TOPIC_CONTROLLERS,
					lProperties);
			eventAdmin.sendEvent(lEvent);
		}
	}

	private static class ContextMenuSet {
		private final transient Collection<ContextMenuItem> items = new ArrayList<ContextMenuManager.ContextMenuItem>();

		protected void addContextMenuItem(final ContextMenuItem inItem) {
			items.add(inItem);
		}

		protected Collection<ContextMenuItem> getContextMenuItems() {
			return items;
		}
	}

	/**
	 * Wrapper class for context menu items provided by use case bundles.
	 */
	private static class ContextMenuItem {
		private final transient IContextMenuItem contextMenuItem; // NOPMD

		protected ContextMenuItem(final IContextMenuItem inContextMenuItem) {
			contextMenuItem = inContextMenuItem;
		}

		protected String getCaption() {
			return contextMenuItem.getTitleMsg();
		}

		public Class<? extends IPluggable> getControllerClass() {
			return contextMenuItem.getControllerClass();
		}

		/**
		 * Check the conditions to display the context menu item.
		 * 
		 * @param inUser
		 *            {@link User} the user instance, might be evaluated to
		 *            check the conditions
		 * @param inAuthorization
		 *            {@link Authorization} the authorization instance, will be
		 *            evaluate to check the conditions
		 * @param inParameters
		 *            {@link ParameterObject} the generic parameter object with
		 *            parameters that could be evaluated to check the conditions
		 * @return boolean <code>true</code> if the conditions allow to
		 *         display/enable the context menu item, <code>false</code> if
		 *         not
		 */
		protected boolean checkConditions(final User inUser,
				final Authorization inAuthorization,
				final ParameterObject inParameters) {
			return contextMenuItem.checkConditions(inUser, inAuthorization,
					inParameters);
		}
	}

}
