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

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.exceptions.NoControllerFoundException;
import org.ripla.web.interfaces.IControllerConfiguration;
import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.views.DefaultRiplaView;
import org.ripla.web.util.UseCaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;

/**
 * The controller manager class.
 * 
 * @author Luthiger
 */
public class ControllerManager {
	private static final Logger LOG = LoggerFactory.getLogger(ControllerManager.class);
	
	private EventAdmin eventAdmin;
	private Map<String, BundleClassLoader> controllerMappingTable = Collections.synchronizedMap(new Hashtable<String, BundleClassLoader>());

	private UserAdmin userAdmin = new NoOpUserAdmin();

	/**
	 * Loads the specified controller.
	 * 
	 * @param inControllerName String
	 * @return {@link Component}
	 */
	public Component getContent(String inControllerName) throws NoControllerFoundException {
		BundleClassLoader lLoader = controllerMappingTable.get(inControllerName);
		if (lLoader == null) {
			throw new NoControllerFoundException(inControllerName);
		}
		return runController(lLoader);
	}

	/**
	 * @param inLoader
	 * @return
	 */
	private Component runController(BundleClassLoader inLoader) {
		try {
			IPluggable lController = inLoader.createLoader();
			lController.setEventAdmin(eventAdmin);
			lController.setUserAdmin(userAdmin);
			ApplicationData.setActiveMenuItem(inLoader.getSymbolicName());
			return lController.run();
		} 
		catch (Exception exc) {
			Throwable lThrowable = exc;
//			if (exc instanceof RiplaException) {
//				lThrowable = ((RiplaException) exc).getRootCause();
//			}
			LOG.error("Problem during task execution.", lThrowable); //$NON-NLS-1$
			return new DefaultRiplaView(exc);
		}
	}

	/**
	 * Registers the specified controller to the manager.
	 * 
	 * @param inControllerSet {@link IControllerSet}
	 */
	public void addControllerSet(IControllerSet inControllerSet) {
		for (IControllerConfiguration lControllerConfiguration : inControllerSet.getControllerConfigurations()) {
			Bundle lBundle = lControllerConfiguration.getBundle();
			String lControllerName = lControllerConfiguration.getControllerName();
			controllerMappingTable.put(UseCaseHelper.createFullyQualifiedControllerName(lBundle, lControllerName), new BundleClassLoader(lControllerName, lBundle));
		}
	}

	/**
	 * Unregisters the specified controller from the manager.
	 * 
	 * @param inControllerSet {@link IControllerSet}
	 */
	public void removeControllerSet(IControllerSet inControllerSet) {
		for (IControllerConfiguration lControllerConfiguration : inControllerSet.getControllerConfigurations()) {
			Bundle lBundle = lControllerConfiguration.getBundle();
			String lControllerName = lControllerConfiguration.getControllerName();
			BundleClassLoader lLoader = controllerMappingTable.get(UseCaseHelper.createFullyQualifiedControllerName(lBundle, lControllerName));
			if (lLoader != null) {
				lLoader.dispose();
				controllerMappingTable.remove(lLoader);
			}
		}
	}
	
	/**
	 * Sets the OSGi event admin.
	 * 
	 * @param inEventAdmin {@link EventAdmin}
	 */
	public void setEventAdmin(EventAdmin inEventAdmin) {
		eventAdmin = inEventAdmin;
		LOG.trace("Set OSGi event admin to Ripla controller manager."); //$NON-NLS-1$
	}
	
	/**
	 * @return {@link EventAdmin}
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}

	/**
	 * @param inUserAdmin
	 */
	public void setUserAdmin(UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin == null ? new NoOpUserAdmin() : inUserAdmin;
	}
	
	/**
	 * @return {@link UserAdmin} the user admin instance
	 */
	public UserAdmin getUserAdmin() {
		return userAdmin;
	}

// ---

	private static class BundleClassLoader	{
		private String controllerName;
		private Bundle bundle;

		public BundleClassLoader(String inControllerName, Bundle inBundle) {
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
		public IPluggable createLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Class <?> lClass = bundle.loadClass(controllerName);
			return (IPluggable)lClass.newInstance();
		}
		@Override
		public String toString() {
			return controllerName;
		}
		String getSymbolicName() {
			return bundle.getSymbolicName();
		}
		public void dispose() {
			bundle = null;
		}
	}
	
}
