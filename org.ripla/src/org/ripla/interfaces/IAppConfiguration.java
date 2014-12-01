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
package org.ripla.interfaces;

/**
 * Interface for application configuration objects.
 * 
 * @author Luthiger
 */
public interface IAppConfiguration {

	/**
	 * Returns the application's login authenticator. <br />
	 * This is only relevant if the application needs login.
	 * 
	 * @return {@link IAuthenticator} the authenticator class or
	 *         <code>null</code>, if the application has no login
	 */
	IAuthenticator getLoginAuthenticator();

	/**
	 * @return String welcome title displayed on the application's login page
	 *         (xhtml)
	 */
	String getWelcome();

	/**
	 * @return String the ID of the skin intended to be used by the application
	 */
	String getDftSkinID();

	/**
	 * @return String the name of the application
	 */
	String getAppName();

	/**
	 * @return String a filter expression for the application's menu, e.g.
	 *         <code>demo.*</code>. With this expression the application can
	 *         filter all menu items with tags matching the filter expression.
	 *         Can be <code>null</code> or empty to disable filtering.
	 */
	String getMenuTagFilter();

}
