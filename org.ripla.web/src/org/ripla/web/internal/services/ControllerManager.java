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

package org.ripla.web.internal.services;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IControllerConfiguration;
import org.ripla.interfaces.IControllerSet;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.views.DefaultRiplaView;
import org.ripla.web.util.UseCaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * The controller manager class.<br />
 * Instances of this class are responsible for managing controllers provided by
 * the use case bundles. All controller classes are registered here. Therefore,
 * when the application calls a controller, this manager instance has to look up
 * the controller class and load it using the providing bundle's class loader.
 * Then, the controller instance is executed and the created view component are
 * returned to the application.
 * 
 * @author Luthiger
 */
public final class ControllerManager {
	private static final Logger LOG = LoggerFactory
			.getLogger(ControllerManager.class);

	private final transient Map<String, BundleClassLoader> controllerMappingTable = Collections
			.synchronizedMap(new Hashtable<String, BundleClassLoader>());

	private UserAdmin userAdmin = new NoOpUserAdmin();

	/**
	 * Loads the specified controller.
	 * 
	 * @param inControllerName
	 *            String
	 * @return {@link Component}
	 */
	public Component getContent(final String inControllerName)
			throws NoControllerFoundException {
		final BundleClassLoader lLoader = controllerMappingTable
				.get(inControllerName);
		if (lLoader == null) {
			throw new NoControllerFoundException(inControllerName);
		}
		return runController(lLoader);
	}

	private Component runController(final BundleClassLoader inLoader) {
		try {
			final IPluggable lController = inLoader.createLoader();
			lController.setUserAdmin(userAdmin);
			setActiveMenuItem(inLoader.getSymbolicName());
			return lController.run();
		}
		catch (final Exception exc) {
			final Throwable lThrowable = exc;
			// if (exc instanceof RiplaException) {
			// lThrowable = ((RiplaException) exc).getRootCause();
			// }
			LOG.error("Problem during task execution.", lThrowable); //$NON-NLS-1$
			return new DefaultRiplaView(exc);
		}
	}

	@SuppressWarnings("unchecked")
	private void setActiveMenuItem(final String inBundleName) {
		final VaadinSession lCurrentSession = VaadinSession.getCurrent();
		MenuItem lOldItem = null;
		try {
			lCurrentSession.getLockInstance().lock();
			lOldItem = (MenuItem) lCurrentSession
					.getAttribute(Constants.SA_ACTIVE_MENU);
		} finally {
			lCurrentSession.getLockInstance().unlock();
		}

		if (lOldItem != null) {
			lOldItem.setStyleName("");
		}
		Map<String, MenuItem> lMenuMap = null;
		try {
			lCurrentSession.getLockInstance().lock();
			lMenuMap = (Map<String, MenuItem>) lCurrentSession
					.getAttribute(Constants.SA_MENU_MAP);
		} finally {
			lCurrentSession.getLockInstance().unlock();
		}
		final MenuItem lNewItem = lMenuMap == null ? null : lMenuMap
				.get(inBundleName);
		if (lNewItem != null) {
			lNewItem.setStyleName(Constants.CSS_ACTIVE_MENU);
		}
		try {
			lCurrentSession.getLockInstance().lock();
			lCurrentSession.setAttribute(Constants.SA_ACTIVE_MENU, lNewItem);
		} finally {
			lCurrentSession.getLockInstance().unlock();
		}
	}

	/**
	 * Registers the specified controller to the manager.
	 * 
	 * @param inControllerSet
	 *            {@link IControllerSet}
	 */
	public void addControllerSet(final IControllerSet inControllerSet) {
		for (final IControllerConfiguration lControllerConfiguration : inControllerSet
				.getControllerConfigurations()) {
			final Bundle lBundle = lControllerConfiguration.getBundle();
			final String lControllerName = lControllerConfiguration
					.getControllerName();
			controllerMappingTable.put(UseCaseHelper
					.createFullyQualifiedControllerName(lBundle,
							lControllerName), new BundleClassLoader( // NOPMD
					lControllerName, lBundle));
		}
	}

	/**
	 * Unregisters the specified controller from the manager.
	 * 
	 * @param inControllerSet
	 *            {@link IControllerSet}
	 */
	public void removeControllerSet(final IControllerSet inControllerSet) {
		for (final IControllerConfiguration lControllerConfiguration : inControllerSet
				.getControllerConfigurations()) {
			final Bundle lBundle = lControllerConfiguration.getBundle();
			final String lControllerName = lControllerConfiguration
					.getControllerName();
			final BundleClassLoader lLoader = controllerMappingTable
					.get(UseCaseHelper.createFullyQualifiedControllerName(
							lBundle, lControllerName));
			if (lLoader != null) {
				lLoader.dispose();
				controllerMappingTable.remove(lLoader);
			}
		}
	}

	/**
	 * @param inUserAdmin
	 */
	public void setUserAdmin(final UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin == null ? new NoOpUserAdmin() : inUserAdmin;
	}

	/**
	 * @return {@link UserAdmin} the user admin instance
	 */
	public UserAdmin getUserAdmin() {
		return userAdmin;
	}

	// ---

	private static class BundleClassLoader {
		private final transient String controllerName;
		private transient Bundle bundle;

		public BundleClassLoader(final String inControllerName,
				final Bundle inBundle) {
			controllerName = inControllerName;
			bundle = inBundle;
		}

		/**
		 * Uses the registered <code>Bundle</code> to load the controller.
		 * 
		 * @return IPluggable The controller
		 * @throws ClassNotFoundException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 */
		public IPluggable createLoader() throws ClassNotFoundException,
				InstantiationException, IllegalAccessException {
			final Class<?> lClass = bundle.loadClass(controllerName);
			return (IPluggable) lClass.newInstance();
		}

		@Override
		public String toString() {
			return controllerName;
		}

		protected String getSymbolicName() {
			return bundle.getSymbolicName();
		}

		public void dispose() {
			bundle = null; // NOPMD by Luthiger on 09.09.12 23:46
		}
	}

}
