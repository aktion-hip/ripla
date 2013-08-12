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

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.annotations.UseCaseController;
import org.ripla.interfaces.IControllerConfiguration;
import org.ripla.interfaces.IControllerSet;
import org.ripla.interfaces.IMenuExtendible;
import org.ripla.interfaces.IMenuItem;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.menu.ContextMenuManager;
import org.ripla.web.internal.menu.ExtendibleMenuHandler;
import org.ripla.web.internal.menu.MenuFactory;
import org.ripla.web.services.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class acting as central registry for all use cases.
 * 
 * @author Luthiger
 */
public final class UseCaseManager {
	private static final Logger LOG = LoggerFactory
			.getLogger(UseCaseManager.class);

	private static final String PREFIX_ROOT = "/"; //$NON-NLS-1$
	private static final String PREFIX_BIN = "/bin/"; //$NON-NLS-1$
	private static final String SUFFIX_CLASS = ".class"; //$NON-NLS-1$

	private final transient ControllerManager controllerManager = new ControllerManager();
	private final transient ContextMenuManager contextMenuManager = ContextMenuManager
			.createInstance();

	private final transient List<IUseCase> useCases = Collections
			.synchronizedList(new ArrayList<IUseCase>());
	private final transient Map<String, ExtendibleMenuHandler> extendibleMenus = Collections
			.synchronizedMap(new HashMap<String, ExtendibleMenuHandler>());

	private transient boolean automaticContextMenuRegistration = false;

	/**
	 * @return {@link ControllerManager} the application's controller manager
	 */
	public ControllerManager getControllerManager() {
		return controllerManager;
	}

	/**
	 * @return {@link ContextMenuManager} the application's context menu manager
	 */
	public ContextMenuManager getContextMenuManager() {
		return contextMenuManager;
	}

	/**
	 * @param inUserAdmin
	 */
	public void setUserAdmin(final UserAdmin inUserAdmin) {
		controllerManager.setUserAdmin(inUserAdmin);
	}

	/**
	 * @return {@link UserAdmin} the user admin instance
	 */
	public UserAdmin getUserAdmin() {
		return controllerManager.getUserAdmin();
	}

	/**
	 * Add the use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void addUseCase(final IUseCase inUseCase) {
		// register
		useCases.add(inUseCase);
		// register task classes
		controllerManager.addControllerSet(inUseCase.getControllerSet());
		controllerManager.addControllerSet(lookupControllers(
				inUseCase.getControllerClasses(), inUseCase.getClass()));

		if (automaticContextMenuRegistration) {
			registerContextMenus(inUseCase);
		}

		LOG.debug("Added use case {}.", inUseCase.toString()); //$NON-NLS-1$
	}

	public void registerContextMenus() {
		for (final IUseCase lUseCase : useCases) {
			registerContextMenus(lUseCase);
		}
		automaticContextMenuRegistration = true;
	}

	private void registerContextMenus(final IUseCase inUseCase) {
		for (final IMenuSet lMenuSet : inUseCase.getContextMenus()) {
			contextMenuManager.addContextMenuSet(lMenuSet);
		}
	}

	/**
	 * Remove the use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void removeUseCase(final IUseCase inUseCase) {
		useCases.remove(inUseCase);
		controllerManager.removeControllerSet(inUseCase.getControllerSet());
		controllerManager.removeControllerSet(lookupControllers(
				inUseCase.getControllerClasses(), inUseCase.getClass()));
		for (final IMenuSet lMenuSet : inUseCase.getContextMenus()) {
			contextMenuManager.removeContextMenuSet(lMenuSet);
		}
		LOG.debug("Removed use case {}.", inUseCase.toString()); //$NON-NLS-1$
	}

	// --- extendible menu contributions ---

	/**
	 * Registers the contribution to the extendible menus.
	 * 
	 * @param inContribution
	 *            {@link IExtendibleMenuContribution}
	 */
	public void registerMenuContribution(
			final IExtendibleMenuContribution inContribution) {
		final String lMenuID = inContribution.getExtendibleMenuID();
		final ExtendibleMenuHandler lExtendibleMenu = extendibleMenus
				.get(lMenuID);
		if (lExtendibleMenu == null) {
			extendibleMenus.put(lMenuID, new ExtendibleMenuHandler(
					inContribution));
		} else {
			lExtendibleMenu.addContribution(inContribution);
		}
	}

	/**
	 * Unregisters the contribution from the extendible menus.
	 * 
	 * @param inContribution
	 *            {@link IExtendibleMenuContribution}
	 */
	public void unregisterMenuContribution(
			final IExtendibleMenuContribution inContribution) {
		final ExtendibleMenuHandler lExtendibleMenu = extendibleMenus
				.get(inContribution.getExtendibleMenuID());
		if (lExtendibleMenu != null) {
			lExtendibleMenu.removeContribution(inContribution);
		}
	}

	/**
	 * Create the menu based on the registered use cases.
	 * 
	 * @return Collection&lt;MenuFactory> the collection of menus
	 */
	public Collection<MenuFactory> getMenus() {
		return getMenus(useCases);
	}

	private Collection<MenuFactory> getMenus(final List<IUseCase> inUseCases) {
		final List<MenuFactory> outFactories = new ArrayList<MenuFactory>();
		for (final IUseCase lUseCase : inUseCases) {
			final IMenuItem lMenu = lUseCase.getMenu();
			if (lMenu instanceof IMenuExtendible) {
				final String lMenuID = ((IMenuExtendible) lMenu).getMenuID();
				final ExtendibleMenuHandler lExtendibleMenu = extendibleMenus
						.get(lMenuID);
				if (lExtendibleMenu != null) {
					outFactories.add(lExtendibleMenu
							.getMenuFactory((IMenuExtendible) lMenu));
				}
			} else {
				outFactories.add(new MenuFactory(lUseCase.getMenu())); // NOPMD
			}
		}
		Collections.sort(outFactories);
		return outFactories;
	}

	private IControllerSet lookupControllers(final Package inControllerClasses,
			final Class<?> inClass) {
		if (inControllerClasses == null) {
			return new EmptyControllerSet();
		}

		final String lPackagName = inControllerClasses.getName();
		final String lPath = lPackagName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		final Bundle lBundle = FrameworkUtil.getBundle(inClass);
		Enumeration<?> lControllers = lBundle
				.findEntries(
						String.format("%s%s", PREFIX_BIN, lPath), "*" + SUFFIX_CLASS, false); //$NON-NLS-1$ //$NON-NLS-2$
		if (lControllers != null) {
			return createControllerSet(lControllers, lBundle, PREFIX_BIN);
		}
		lControllers = lBundle
				.findEntries(
						String.format("%s%s", PREFIX_ROOT, lPath), "*" + SUFFIX_CLASS, false); //$NON-NLS-1$ //$NON-NLS-2$
		if (lControllers != null) {
			return createControllerSet(lControllers, lBundle, PREFIX_ROOT);
		}
		return new EmptyControllerSet();
	}

	private IControllerSet createControllerSet(
			final Enumeration<?> inControllers, final Bundle inBundle,
			final String inPrefix) {
		final Collection<IControllerConfiguration> lControllerConfigurations = new ArrayList<IControllerConfiguration>();
		while (inControllers.hasMoreElements()) {
			final IControllerConfiguration lControllerConfiguration = createControllerConfiguration(
					(URL) inControllers.nextElement(), inBundle, inPrefix);
			if (lControllerConfiguration != null) {
				lControllerConfigurations.add(lControllerConfiguration);
			}
		}
		return new ControllerSet(lControllerConfigurations);
	}

	@SuppressWarnings("unchecked")
	private IControllerConfiguration createControllerConfiguration(
			final URL inController, final Bundle inBundle, final String inPrefix) {
		final String lPath = inController.getPath();
		final String lClassName = lPath.substring(inPrefix.length(),
				lPath.length() - SUFFIX_CLASS.length()).replace("/", "."); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			final Class<IPluggable> lClass = (Class<IPluggable>) inBundle
					.loadClass(lClassName);
			final Annotation lControllerAnnotations = lClass
					.getAnnotation(UseCaseController.class);
			if (lControllerAnnotations != null) {
				return new ControllerConfiguration(inBundle, lClassName);
			}
		}
		catch (final ClassNotFoundException exc) { // NOPMD
			// intentionally left empty
		}
		catch (final NoClassDefFoundError exc) { // NOPMD
			// intentionally left empty
		}
		return null;
	}

	// --- private classes ---

	private static class ControllerSet implements IControllerSet {
		private final transient IControllerConfiguration[] controllers;

		ControllerSet(final Collection<IControllerConfiguration> inControllers) {
			controllers = new IControllerConfiguration[inControllers.size()];
			final Iterator<IControllerConfiguration> lControllers = inControllers
					.iterator();
			for (int i = 0; i < controllers.length; i++) {
				controllers[i] = lControllers.next();
			}
		}

		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return Arrays.copyOf(controllers, controllers.length);
		}
	}

	private static class EmptyControllerSet implements IControllerSet {
		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return new IControllerConfiguration[] {};
		}
	}

	private static class ControllerConfiguration implements
			IControllerConfiguration {
		private final transient Bundle bundle;
		private final transient String controllerName;

		ControllerConfiguration(final Bundle inBundle,
				final String inControllerName) {
			bundle = inBundle;
			controllerName = inControllerName;
		}

		@Override
		public Bundle getBundle() {
			return bundle;
		}

		@Override
		public String getControllerName() {
			return controllerName;
		}
	}

}
