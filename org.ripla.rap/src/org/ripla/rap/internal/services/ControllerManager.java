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
package org.ripla.rap.internal.services;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IControllerConfiguration;
import org.ripla.interfaces.IControllerSet;
import org.ripla.rap.Constants;
import org.ripla.rap.interfaces.IPluggable;
import org.ripla.rap.internal.menu.DropDownMenu;
import org.ripla.rap.internal.views.DefaultRiplaView;
import org.ripla.rap.util.UseCaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Loads the specified controller. This is the central part of the Ripla
	 * application: The specified controller is looked up and ran for that it
	 * returns the view it controls.
	 * 
	 * @param inControllerName
	 *            String
	 * @param inParent
	 *            {@link Composite} the parent the created component is placed
	 *            on
	 * @return {@link Composite}
	 */
	public Composite getContent(final String inControllerName,
			final Composite inParent) throws NoControllerFoundException {
		final BundleClassLoader lLoader = controllerMappingTable
				.get(inControllerName);
		if (lLoader == null) {
			throw new NoControllerFoundException(inControllerName);
		}
		return runController(lLoader, inParent);
	}

	private Composite runController(final BundleClassLoader inLoader,
			final Composite inParent) {
		try {
			final IPluggable lController = inLoader.createLoader();
			lController.setUserAdmin(userAdmin);
			lController.setParent(inParent);
			menuActivationHandling(inLoader.getSymbolicName());
			return lController.run();
		} catch (final Exception exc) {
			final Throwable lThrowable = exc;
			LOG.error("Problem during task execution.", lThrowable); //$NON-NLS-1$
			return new DefaultRiplaView(inParent, exc);
		}
	}

	@SuppressWarnings("unchecked")
	private void menuActivationHandling(final String inBundleName) {
		final DropDownMenu lOldItem = (DropDownMenu) RWT.getUISession()
				.getAttribute(Constants.RS_MENU_ACTIVE);
		if (lOldItem != null && !lOldItem.isDisposed()) {
			lOldItem.setSelected(false);
		}

		final Map<String, DropDownMenu> lMenuMap = (Map<String, DropDownMenu>) RWT
				.getUISession().getAttribute(Constants.RS_MENU_MAP);
		final DropDownMenu lNewItem = lMenuMap.get(inBundleName);
		if (lNewItem != null) {
			lNewItem.setSelected(true);
		}
		RWT.getUISession().setAttribute(Constants.RS_MENU_ACTIVE, lNewItem);
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
