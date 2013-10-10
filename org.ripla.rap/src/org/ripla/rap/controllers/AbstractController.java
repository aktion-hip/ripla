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
package org.ripla.rap.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.RiplaException;
import org.ripla.interfaces.IRiplaEventDispatcher;
import org.ripla.interfaces.IRiplaEventDispatcher.Event;
import org.ripla.rap.Constants;
import org.ripla.rap.app.RiplaApplication;
import org.ripla.rap.exceptions.PermissionsNotSufficientException;
import org.ripla.rap.interfaces.IPluggable;
import org.ripla.rap.util.UseCaseHelper;
import org.ripla.util.ParameterObject;
import org.ripla.util.PreferencesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all controllers of the application. A controller class
 * contains the controller and business logic of a use case.
 * 
 * @author Luthiger
 */
public abstract class AbstractController implements IPluggable {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractController.class);

	private transient UserAdmin userAdmin;
	private transient Composite parent;

	@Override
	public void setUserAdmin(final UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin;
	}

	@Override
	public void setParent(final Composite inParent) {
		parent = inParent;
	}

	protected Composite getParent() {
		return parent;
	}

	@Override
	public Composite run() throws RiplaException {
		checkRoles();
		return runChecked();
	}

	/**
	 * Each deriving controller has to define the permission it needs to be
	 * executed. Note: An empty String means that every role can execute the
	 * task.
	 * 
	 * @return java.lang.String
	 */
	protected abstract String needsPermission();

	protected abstract Composite runChecked() throws RiplaException;

	private void checkRoles() throws PermissionsNotSufficientException {
		if (userAdmin == null) {
			return;
		}
		final Authorization lAuthorization = userAdmin
				.getAuthorization(getUser());
		if (lAuthorization == null) {
			return;
		}
		if (needsPermission().isEmpty()) {
			return;
		}
		if (!lAuthorization.hasRole(needsPermission())) {
			final StringBuilder lLogMsg = new StringBuilder(
					"Note: The user has not sufficient permissions for the requested task.\n"); //$NON-NLS-1$
			lLogMsg.append("   User: ").append(getUser().getName()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			lLogMsg.append("   IP number: ").append(RWT.getRequest().getRemoteAddr()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			LOG.warn(new String(lLogMsg));
			throw new PermissionsNotSufficientException();
		}
	}

	/**
	 * Allows access to the current user object.
	 * 
	 * @return {@link User}
	 */
	protected final User getUser() {
		return (User) RWT.getUISession().getAttribute(Constants.RS_USER);
	}

	/**
	 * @return {@link Locale} the actual session's locale.
	 */
	protected final Locale getAppLocale() {
		return (Locale) RWT.getUISession().getAttribute(Constants.RS_LOCALE);
	}

	/**
	 * Set generic parameters.<br/>
	 * Use e.g.:
	 * 
	 * <pre>
	 * ParameterObject lParameters = new ParameterObject();
	 * lParameters.set(Constants.KEY_PARAMETER_MEMBER, lMemberID);
	 * setParameters(lParameters);
	 * </pre>
	 * 
	 * @param inParameters
	 *            {@link ParameterObject} the parameters to set or
	 *            <code>null</code> to clear the parameter settings
	 */
	protected final void setParameters(final ParameterObject inParameters) {
		RWT.getUISession().setAttribute(Constants.RS_PARAMETERS, inParameters);
	}

	/**
	 * Returns the generic parameters passed by a task/controller.<br/>
	 * <b>Note</b>: the parameter settings a cleared after calling this method!
	 * 
	 * @return {@link ParameterObject} generic parameters
	 */
	protected final ParameterObject getParameters() {
		return getParameters(true);
	}

	/**
	 * Returns the generic parameters passed by a task/controller.
	 * 
	 * @param inClear
	 *            boolean if <code>true</code>, the parameter settings are
	 *            cleared, if <code>false</code>, they are retained
	 * @return {@link ParameterObject} generic parameters
	 */
	protected final ParameterObject getParameters(final boolean inClear) {
		final ParameterObject out = (ParameterObject) RWT.getUISession()
				.getAttribute(Constants.RS_PARAMETERS);
		if (inClear) {
			setParameters(null);
		}
		return out;
	}

	/**
	 * Create a fully qualified controller name with the specified controller.
	 * 
	 * @param inController
	 *            {@link IPluggable}
	 * @return String the fully qualified name of the controller
	 */
	protected final String createFullyQualifiedControllerName(
			final Class<? extends IPluggable> inController) {
		return UseCaseHelper.createFullyQualifiedControllerName(inController);
	}

	/**
	 * Use OSGi event service to display a notification message.
	 * 
	 * @param inMessage
	 *            String
	 * @param inNotificationType
	 *            int the message type (e.g.
	 *            <code>Notification.TYPE_HUMANIZED_MESSAGE</code>)
	 * @see com.vaadin.ui.Window.Notification
	 */
	protected final void showNotification(final String inMessage,
			final int inNotificationType) {
		// TODO
		// final Map<String, Object> lProperties = new HashMap<String,
		// Object>();
		// lProperties.put(org.ripla.Constants.EVENT_PROPERTY_NOTIFICATION_MSG,
		// inMessage);
		// lProperties.put(org.ripla.Constants.EVENT_PROPERTY_NOTIFICATION_TYPE,
		// inNotificationType);
		//
		// final Event lEvent = new Event(
		// org.ripla.Constants.EVENT_TOPIC_NOTIFICATION, lProperties);
		// eventAdmin.sendEvent(lEvent);
	}

	/**
	 * Use event dispatcher to trigger a refresh of the application's body view.
	 */
	protected final void refreshBody() {
		getDispatcher().dispatch(Event.REFRESH, new HashMap<String, Object>());
	}

	/**
	 * Use event dispatcher to trigger a refresh of the application's skin.
	 * 
	 * @param inSkinID
	 *            String the new skin's ID
	 */
	protected final void changeSkin(final String inSkinID) {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_SKIN_ID, inSkinID);
		getDispatcher().dispatch(Event.REFRESH_SKIN, lProperties);
	}

	/**
	 * Use event dispatcher to trigger a logout.
	 * 
	 * @param inDelay
	 *            int milliseconds the delay of the browser logout
	 */
	protected final void logout(final int inDelay) {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_LOGOUT_DELAY, inDelay);
		getDispatcher().dispatch(Event.LOGOUT, lProperties);
	}

	/**
	 * Trigger a refresh of the application's body view.
	 */
	protected final void closeApp() {
		RiplaApplication.restart();
	}

	/**
	 * Use event dispatcher to display the context menu.
	 * 
	 * @param inSetName
	 *            String the ID of the context menu to display
	 */
	protected final void loadContextMenu(final String inSetName) {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(org.ripla.Constants.EVENT_PROPERTY_CONTEXT_MENU_ID,
				UseCaseHelper.createFullyQualifiedID(inSetName, getClass()));
		lProperties.put(org.ripla.Constants.EVENT_PROPERTY_CONTROLLER_ID,
				getClass());
		getDispatcher().dispatch(Event.LOAD_CONTEXT_MENU, lProperties);
	}

	private IRiplaEventDispatcher getDispatcher() {
		return (IRiplaEventDispatcher) RWT.getUISession().getAttribute(
				Constants.RS_EVENT_DISPATCHER);
	}

	/**
	 * Saves the specified value with the specified key to the application's
	 * preferences.
	 * 
	 * @param inKey
	 *            String
	 * @param inValue
	 *            String
	 */
	protected final void savePreferences(final String inKey,
			final String inValue) {
		getPreferences().set(inKey, inValue);
	}

	/**
	 * Returns the value with the specified key from the application's
	 * preferences.
	 * 
	 * @param inKey
	 *            String
	 * @param inDftValue
	 *            String default value
	 * @return String the value retrieved from the application's preferences
	 */
	protected final String getPreference(final String inKey,
			final String inDftValue) {
		return getPreferences().get(inKey, inDftValue);
	}

	private PreferencesHelper getPreferences() {
		return (PreferencesHelper) RWT.getUISession().getAttribute(
				Constants.RS_PREFS);
	}

	/**
	 * Clear the context menu panel.
	 */
	protected final void emptyContextMenu() {
		loadContextMenu(Constants.MENU_SET_ID_EMPTY);
	}

	/**
	 * Returns the <code>Role</code> object with the specified name. This method
	 * can be used to check the availability of user administration. In case of
	 * no user administration available, this method returns always
	 * <code>null</code>.
	 * 
	 * @param inName
	 *            String
	 * @return {@link Role} or <code>null</code> if this User Admin service does
	 *         not have a Role object with the given name.
	 */
	protected Role getUserAdminRole(final String inName) {
		return userAdmin.getRole(inName);
	}

}
