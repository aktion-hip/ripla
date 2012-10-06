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

package org.ripla.web.menu;

import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.ripla.web.interfaces.IContextMenuItem;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.util.ParameterObject;

/**
 * Default implementation of <code>IContextMenuItem</code>.<br />
 * Bundles providing context menu items can create instances of this class.
 * 
 * @author Luthiger
 */
public final class ContextMenuItem implements IContextMenuItem {

	private final transient Class<? extends IPluggable> controllerClass;
	private final transient String msgKey;
	private final transient String menuPermission;
	private final transient IMessages messages;

	/**
	 * ContextMenuItem constructor.
	 * 
	 * @param inControllerClass
	 *            {@link IPluggable}
	 * @param inMsgKey
	 *            String the key for the context menu item's label
	 * @param inMenuPermission
	 *            String
	 * @param inMessages
	 *            {@link IMessages} the messages to display the language
	 *            sensitive label
	 */
	public ContextMenuItem(final Class<? extends IPluggable> inControllerClass,
			final String inMsgKey, final String inMenuPermission,
			final IMessages inMessages) {
		controllerClass = inControllerClass;
		msgKey = inMsgKey;
		menuPermission = inMenuPermission;
		messages = inMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IContextMenuItem#getControllerClass()
	 */
	@Override
	public Class<? extends IPluggable> getControllerClass() {
		return controllerClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IContextMenuItem#getTitleMsg()
	 */
	@Override
	public String getTitleMsg() {
		return messages.getMessage(msgKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IContextMenuItem#getMenuPermission()
	 */
	@Override
	public String getMenuPermission() {
		return menuPermission;
	}

	/**
	 * Default implementation of the check of the conditions to display the
	 * context menu item.<br />
	 * Subclasses may extend.
	 * 
	 * @param inUser
	 *            {@link User} the user instance, might be evaluated to check
	 *            the conditions
	 * @param inAuthorization
	 *            {@link Authorization} the authorization instance, will be
	 *            evaluate to check the conditions
	 * @param inParameters
	 *            {@link ParameterObject} the generic parameter object with
	 *            parameters that could be evaluated to check the conditions
	 * @return boolean <code>true</code> if the conditions allow to
	 *         display/enable the context menu item, <code>false</code> if not
	 */
	@Override
	public boolean checkConditions(final User inUser,
			final Authorization inAuthorization,
			final ParameterObject inParameters) {
		if (inAuthorization != null && getMenuPermission().length() > 0
				&& !inAuthorization.hasRole(getMenuPermission())) {
			return false;
		}
		return true;
	}

}
