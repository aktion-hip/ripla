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
package org.ripla.rap.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.ripla.interfaces.IControllerConfiguration;
import org.ripla.interfaces.IControllerSet;
import org.ripla.rap.Constants;
import org.ripla.rap.interfaces.IMenuSet;
import org.ripla.rap.interfaces.IPluggable;

/**
 * Helper class for Ripla use cases.
 * 
 * @author Luthiger
 */
public final class UseCaseHelper {
	public static final IControllerSet EMPTY_CONTROLLER_SET = new EmptyControllerSet();
	public static final IMenuSet[] EMPTY_CONTEXT_MENU_SET = new IMenuSet[] {};

	private UseCaseHelper() {
	}

	/**
	 * Convenience method to create the fully qualified controller name in a
	 * consistent way.
	 * 
	 * @param inController
	 *            Class an instance of the controller class in the calling
	 *            bundle, must extend <code>IPluggable</code>
	 * @return String the fully qualified name of the controller.
	 */
	public static String createFullyQualifiedControllerName(
			final Class<? extends IPluggable> inController) {
		return createFullyQualifiedControllerName(
				FrameworkUtil.getBundle(inController), inController.getName());
	}

	/**
	 * Convenience method to create the fully qualified controller name in a
	 * consistent way.
	 * 
	 * @param inBundle
	 *            {@link Bundle} the bundle that provides the controller
	 * @param inControllerName
	 *            String the unqualified controller name
	 * @return String the fully qualified name of the controller
	 */
	public static String createFullyQualifiedControllerName(
			final Bundle inBundle, final String inControllerName) {
		return String.format(Constants.CONTROLLER_PATTERN,
				inBundle.getSymbolicName(), inControllerName);
	}

	/**
	 * Convenience method to create an id in the namespace of the bundle that
	 * provides (i.e. defines) the element.
	 * 
	 * @param inID
	 *            String the ID to qualify
	 * @param inClass
	 *            Class an arbitrary class from the bundle (used to retrieve the
	 *            bundle context)
	 * @return String the ID prefixed with the bundle name, i.e.
	 *         <code>the.bundle.name/ID</code>
	 */
	public static String createFullyQualifiedID(final String inID,
			final Class<?> inClass) {
		return String.format(Constants.CONTROLLER_PATTERN, FrameworkUtil
				.getBundle(inClass).getSymbolicName(), inID);
	}

	// --- private classes ---

	private static class EmptyControllerSet implements IControllerSet {
		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return new IControllerConfiguration[] {};
		}
	}

}
