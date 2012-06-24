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

package org.ripla.web.internal.services;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.annotations.UseCaseController;
import org.ripla.web.interfaces.IControllerConfiguration;
import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IMenuExtendible;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.menu.ContextMenuManager;
import org.ripla.web.internal.menu.ExtendibleMenuHandler;
import org.ripla.web.internal.menu.MenuFactory;
import org.ripla.web.services.IExtendibleMenuContribution;
import org.ripla.web.services.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class acting as central registery for all use cases. 
 * 
 * @author Luthiger
 */
public class UseCaseManager {
	private static final Logger LOG = LoggerFactory.getLogger(UseCaseManager.class);
	
	private static final String PREFIX_ROOT = "/"; //$NON-NLS-1$
	private static final String PREFIX_BIN = "/bin/"; //$NON-NLS-1$
	private static final String SUFFIX_CLASS = ".class"; //$NON-NLS-1$
	
	private ControllerManager controllerManager = new ControllerManager();
	private ContextMenuManager contextMenuManager = new ContextMenuManager();
	
	private List<IUseCase> useCases = Collections.synchronizedList(new ArrayList<IUseCase>());
	private Map<String, ExtendibleMenuHandler> extendibleMenus = Collections.synchronizedMap(new HashMap<String, ExtendibleMenuHandler>());
	
	private boolean automaticContextMenuRegistration = false;
	
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
	 * @param inEventAdmin {@link EventAdmin}
	 */
	public void setEventAdmin(EventAdmin inEventAdmin) {
		controllerManager.setEventAdmin(inEventAdmin);
		contextMenuManager.setEventAdmin(inEventAdmin);
	}	

	/**
	 * @param inUserAdmin
	 */
	public void setUserAdmin(UserAdmin inUserAdmin) {
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
	 * @param inUseCase {@link IUseCase}
	 */
	public void addUseCase(IUseCase inUseCase) {
		//register
		useCases.add(inUseCase);
		//register task classes
		controllerManager.addControllerSet(inUseCase.getControllerSet());
		controllerManager.addControllerSet(lookupControllers(inUseCase.getControllerClasses(), inUseCase.getClass()));
		
		if (automaticContextMenuRegistration) {			
			registerContextMenus(inUseCase);
		}
		
		LOG.debug("Added use case {}.", inUseCase.toString()); //$NON-NLS-1$
	}
	
	public void registerContextMenus() {
		for (IUseCase lUseCase : useCases) {
			registerContextMenus(lUseCase);
		}
		automaticContextMenuRegistration = true;
	}
	
	private void registerContextMenus(IUseCase inUseCase) {		
		for (IMenuSet lMenuSet : inUseCase.getContextMenus()) {
			contextMenuManager.addContextMenuSet(lMenuSet);
		}
	}

	/**
	 * Remove the use case.
	 * 
	 * @param inUseCase {@link IUseCase}
	 */
	public void removeUseCase(IUseCase inUseCase) {
		useCases.remove(inUseCase);
		controllerManager.removeControllerSet(inUseCase.getControllerSet());
		controllerManager.removeControllerSet(lookupControllers(inUseCase.getControllerClasses(), inUseCase.getClass()));
		for (IMenuSet lMenuSet : inUseCase.getContextMenus()) {
			contextMenuManager.removeContextMenuSet(lMenuSet);
		}
		LOG.debug("Removed use case {}.", inUseCase.toString()); //$NON-NLS-1$
	}
	
// --- extendible menu contributions ---
	
	/**
	 * Registers the contribution to the extendible menus.
	 * 
	 * @param inContribution {@link IExtendibleMenuContribution}
	 */
	public void registerMenuContribution(IExtendibleMenuContribution inContribution) {
		String lMenuID = inContribution.getExtendibleMenuID();
		ExtendibleMenuHandler lExtendibleMenu = extendibleMenus.get(lMenuID);
		if (lExtendibleMenu == null) {
			extendibleMenus.put(lMenuID, new ExtendibleMenuHandler(inContribution));
		}
		else {
			lExtendibleMenu.addContribution(inContribution);
		}
	}	
	
	/**
	 * Unregisters the contribution from the extendible menus.
	 * 
	 * @param inContribution {@link IExtendibleMenuContribution}
	 */
	public void unregisterMenuContribution(IExtendibleMenuContribution inContribution) {
		ExtendibleMenuHandler lExtendibleMenu = extendibleMenus.get(inContribution.getExtendibleMenuID());
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
	
	private Collection<MenuFactory> getMenus(List<IUseCase> inUseCases) {
		List<MenuFactory> outFactories = new Vector<MenuFactory>();		
		for (IUseCase lUseCase : inUseCases) {
			IMenuItem lMenu = lUseCase.getMenu();
			if (lMenu instanceof IMenuExtendible) {
				String lMenuID = ((IMenuExtendible) lMenu).getMenuID();
				ExtendibleMenuHandler lExtendibleMenu = extendibleMenus.get(lMenuID);
				if (lExtendibleMenu != null) {
					outFactories.add(lExtendibleMenu.getMenuFactory((IMenuExtendible) lMenu));					
				}
			}
			else {				
				outFactories.add(new MenuFactory(lUseCase.getMenu()));
			}
		}
		Collections.sort(outFactories);
		return outFactories;		
	}	
	
	private IControllerSet lookupControllers(Package inControllerClasses, Class<?> inClass) {
		if (inControllerClasses == null) {
			return new EmptyControllerSet();
		}
		
		String lPackagName = inControllerClasses.getName();
		String lPath = lPackagName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		Bundle lBundle = FrameworkUtil.getBundle(inClass);
		Enumeration<?> lControllers = lBundle.findEntries(String.format("%s%s", PREFIX_BIN, lPath), "*"+SUFFIX_CLASS, false); //$NON-NLS-1$ //$NON-NLS-2$
		if (lControllers != null) {
			return createControllerSet(lControllers, lBundle, PREFIX_BIN);
		}
		lControllers = lBundle.findEntries(String.format("%s%s", PREFIX_ROOT, lPath), "*"+SUFFIX_CLASS, false); //$NON-NLS-1$ //$NON-NLS-2$
		if (lControllers != null) {
			return createControllerSet(lControllers, lBundle, PREFIX_ROOT);
		}
		return new EmptyControllerSet();
	}
	
	private IControllerSet createControllerSet(Enumeration<?> inControllers, Bundle inBundle, String inPrefix) {
		Collection<IControllerConfiguration> lControllerConfigurations = new ArrayList<IControllerConfiguration>();
		while (inControllers.hasMoreElements()) {
			IControllerConfiguration lControllerConfiguration = createControllerConfiguration((URL) inControllers.nextElement(), inBundle, inPrefix);
			if (lControllerConfiguration != null) {
				lControllerConfigurations.add(lControllerConfiguration);
			}
		}
		return new ControllerSet(lControllerConfigurations);
	}

	@SuppressWarnings("unchecked")
	private IControllerConfiguration createControllerConfiguration(URL inController, Bundle inBundle, String inPrefix) {
		String lPath = inController.getPath();
		String lClassName = lPath.substring(inPrefix.length(), lPath.length() - SUFFIX_CLASS.length()).replace("/", "."); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			Class<IPluggable> lClass = (Class<IPluggable>) inBundle.loadClass(lClassName);
			Annotation lControllerAnnotations = lClass.getAnnotation(UseCaseController.class);
			if (lControllerAnnotations != null) {
				return new ControllerConfiguration(inBundle, lClassName);
			}
		} 
		catch (ClassNotFoundException exc) {
			//intentionally left empty
		}
		catch (NoClassDefFoundError exc) {
			//intentionally left empty
		}
		return null;
	}
	
// --- private classes ---
	
	private static class ControllerSet implements IControllerSet {
		private IControllerConfiguration[] controllers;
		
		ControllerSet(Collection<IControllerConfiguration> inControllers) {
			controllers = new IControllerConfiguration[inControllers.size()];
			Iterator<IControllerConfiguration> lControllers = inControllers.iterator();
			for (int i = 0; i < controllers.length; i++) {
				controllers[i] = lControllers.next();
			}
		}
		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return controllers;
		}
	}
	
	private static class EmptyControllerSet implements IControllerSet {
		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return new IControllerConfiguration[] {};
		}
	}
	
	private static class ControllerConfiguration implements IControllerConfiguration {
		private Bundle bundle;
		private String controllerName;

		ControllerConfiguration(Bundle inBundle, String inControllerName) {
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
