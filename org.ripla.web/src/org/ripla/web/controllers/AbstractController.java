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

package org.ripla.web.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.Constants;
import org.ripla.web.exceptions.PermissionsNotSufficientException;
import org.ripla.web.exceptions.RiplaException;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.services.ApplicationData;
import org.ripla.web.util.ParameterObject;
import org.ripla.web.util.UseCaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window.Notification;

/**
 * Base class for all controllers of the application. A controller class
 * contains the controller and business logic of a use case.
 * 
 * @author Luthiger
 */
public abstract class AbstractController implements IPluggable { // NOPMD
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractController.class);

	private transient EventAdmin eventAdmin;
	private transient UserAdmin userAdmin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.web.interfaces.IEventable#setEventAdmin(org.osgi.service.event
	 * .EventAdmin)
	 */
	@Override
	public final void setEventAdmin(final EventAdmin inEventAdmin) {
		eventAdmin = inEventAdmin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.web.interfaces.IPluggable#setUserAdmin(org.osgi.service.useradmin
	 * .UserAdmin)
	 */
	@Override
	public final void setUserAdmin(final UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IPluggable#run()
	 */
	@Override
	public final Component run() throws RiplaException {
		checkRoles();
		return runChecked();
	}

	/**
	 * Each deriving task has to define the permission it needs to be executed.
	 * Note: An empty String means that every role can execute the task.
	 * 
	 * @return java.lang.String
	 */
	protected abstract String needsPermission();

	protected abstract Component runChecked() throws RiplaException;

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
			lLogMsg.append("   IP number: ").append(((WebBrowser) ApplicationData.getWindow().getTerminal()).getAddress()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
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
		return ApplicationData.getUser();
	}

	/**
	 * @return {@link Locale} the actual session's locale.
	 */
	protected final Locale getAppLocale() {
		return ApplicationData.getLocale();
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
		ApplicationData.setParameters(inParameters);
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
	 * @return {@link ParameterObject} generich parameters
	 */
	protected final ParameterObject getParameters(final boolean inClear) {
		final ParameterObject out = ApplicationData.getParameters();
		if (inClear) {
			ApplicationData.setParameters(null);
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
	 * Use OSGi event service to display the next content view.
	 * 
	 * @param inClass
	 *            Class the next task
	 */
	protected final void sendEvent(
			final Class<? extends IPluggable> inController) {
		sendEvent(createFullyQualifiedControllerName(inController));
	}

	/**
	 * Use OSGi event service to display the next content view.
	 * 
	 * @param inControllerName
	 *            String the fully qualified name of the next controller
	 */
	protected final void sendEvent(final String inControllerName) {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_NEXT_CONTROLLER,
				inControllerName);

		final Event lEvent = new Event(Constants.EVENT_TOPIC_CONTROLLERS,
				lProperties);
		eventAdmin.sendEvent(lEvent);
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
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_NOTIFICATION_MSG, inMessage);
		lProperties.put(Constants.EVENT_PROPERTY_NOTIFICATION_TYPE,
				inNotificationType);

		final Event lEvent = new Event(Constants.EVENT_TOPIC_NOTIFICATION,
				lProperties);
		eventAdmin.sendEvent(lEvent);
	}

	/**
	 * Use OSGi event service to display a notification message with type
	 * <code>Notification.TYPE_TRAY_NOTIFICATION</code>.
	 * 
	 * @param inMessage
	 *            String
	 */
	protected final void showNotification(final String inMessage) {
		showNotification(inMessage, Notification.TYPE_TRAY_NOTIFICATION);
	}

	/**
	 * Use OSGi event service to trigger a refresh of the application's body
	 * view.
	 */
	protected final void refreshBody() {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_REFRESH, "refresh");

		final Event lEvent = new Event(Constants.EVENT_TOPIC_APPLICATION,
				lProperties);
		eventAdmin.sendEvent(lEvent);
	}

	/**
	 * Use OSGi event service to trigger a refresh of the application's body
	 * view.
	 */
	protected final void closeApp() {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_CLOSE, "close");

		final Event lEvent = new Event(Constants.EVENT_TOPIC_APPLICATION,
				lProperties);
		eventAdmin.sendEvent(lEvent);
	}

	/**
	 * Use OSGi event service to display the context menu.
	 * 
	 * @param inSetName
	 *            String the ID of the context menu to display
	 */
	protected final void loadContextMenu(final String inSetName) {
		final Map<String, Object> lProperties = new HashMap<String, Object>();
		lProperties.put(Constants.EVENT_PROPERTY_CONTEXT_MENU_ID,
				UseCaseHelper.createFullyQualifiedID(inSetName, getClass()));

		final Event lEvent = new Event(Constants.EVENT_TOPIC_CONTEXT_MENU,
				lProperties);
		eventAdmin.sendEvent(lEvent);
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
		ApplicationData.getPreferences().set(inKey, inValue);
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
		return ApplicationData.getPreferences().get(inKey, inDftValue);
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
