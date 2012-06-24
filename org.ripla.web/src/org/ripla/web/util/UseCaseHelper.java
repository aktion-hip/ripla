/*
	This package is part of the application VIF.
	Copyright (C) 2011, Benno Luthiger

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.ripla.web.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IControllerConfiguration;
import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.interfaces.IPluggable;

/**
 * Helper class for Ripla use cases.
 * 
 * @author Luthiger
 */
public class UseCaseHelper {
	public static final IControllerSet EMPTY_CONTROLLER_SET = new EmptyControllerSet();
	public static final IMenuSet[] EMPTY_CONTEXT_MENU_SET = new IMenuSet[] {};
	
	/**
	 * Convenience method to create the fully qualified controller name in a consistent way.
	 * 
	 * @param inController Class an instance of the controller class in the calling bundle, must extend <code>IPluggable</code>
	 * @return String the fully qualified name of the controller.
	 */
	public static String createFullyQualifiedControllerName(Class<? extends IPluggable> inController) {
		return createFullyQualifiedControllerName(FrameworkUtil.getBundle(inController), inController.getName());
	}

	/**
	 * Convenience method to create the fully qualified controller name in a consistent way.
	 * 
	 * @param inBundle {@link Bundle} the bundle that provides the controller
	 * @param inControllerName String the unqualified controller name
	 * @return String the fully qualified name of the controller
	 */
	public static String createFullyQualifiedControllerName(Bundle inBundle, String inControllerName) {
		return String.format(Constants.CONTROLLER_PATTERN, inBundle.getSymbolicName(), inControllerName);
	}
	
	/**
	 * Convenience method to create an id in the namespace of the bundle that provides (i.e. defines) the element.
	 * 
	 * @param inID String the ID to qualify
	 * @param inClass Class an arbitrary class from the bundle (used to retrieve the bundle context)
	 * @return String the ID prefixed with the bundle name, i.e. <code>the.bundle.name/ID</code>
	 */
	public static String createFullyQualifiedID(String inID, Class<?> inClass) {
		return String.format(Constants.CONTROLLER_PATTERN, FrameworkUtil.getBundle(inClass).getSymbolicName(), inID);
	}

// --- private classes ---
	
	private static class EmptyControllerSet implements IControllerSet {
		@Override
		public IControllerConfiguration[] getControllerConfigurations() {
			return new IControllerConfiguration[] {};
		}
	}
	
}
