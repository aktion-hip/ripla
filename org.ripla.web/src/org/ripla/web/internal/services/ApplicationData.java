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

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.useradmin.User;
import org.ripla.web.util.ParameterObject;
import org.ripla.web.util.PreferencesHelper;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;

/**
 * Object that holds data for one user session.
 *
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class ApplicationData implements TransactionListener, Serializable {
	
	private Locale locale; // current locale
	private User user;
	private ParameterObject parameters;
	private MenuItem activeMenuItem;
	private Map<String, MenuItem> menuMap;
	private Window window;
	private PreferencesHelper preferences;
	
	private static ThreadLocal<ApplicationData> instance = new ThreadLocal<ApplicationData>();
	
	/**
	 * Create the session data holder. This method has to be called in the application's init method.
	 * 
	 * @param inApplication {@link Application} the application
	 */
	public static void create(Application inApplication) {
		ApplicationData lSessionData = new ApplicationData();
		instance.set(lSessionData);
		inApplication.getContext().addTransactionListener(lSessionData);
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.service.ApplicationContext.TransactionListener#transactionStart(com.vaadin.Application, java.lang.Object)
	 */
	@Override
	public void transactionStart(Application inApplication, Object inTransactionData) {
		instance.set(this);
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.service.ApplicationContext.TransactionListener#transactionEnd(com.vaadin.Application, java.lang.Object)
	 */
	@Override
	public void transactionEnd(Application inApplication, Object inTransactionData) {
		instance.set(null);
	}
	
	/**
	 * Initializes the <code>ResourceBundle</code> for the i18n messages.
	 * This method has to be called in the application's init method and whenever the user changes the locale.
	 * 
	 * @param inLocale {@link Locale}
	 */
	public static void initLocale(Locale inLocale) {
		instance.get().locale = inLocale;
	}
	
	/**
	 * @return {@link Locale} the application's locale
	 */
	public static Locale getLocale() {
		return instance.get().locale;
	}
	
	/**
	 * Stores the user object to the application's data.
	 * 
	 * @param inUser {@link User}
	 */
	public static void setUser(User inUser) {
		instance.get().user = inUser;
	}
	
	/**
	 * @return {@link User} the logged in user, might be <code>null</code>, in case of no login
	 */
	public static User getUser() {
		return instance.get().user;
	}
	
	/**
	 * Sets the application's main window
	 * 
	 * @param inWindow {@link Window}
	 */
	public static void setWindow(Window inWindow) {
		instance.get().window = inWindow;
	}
	
	/**
	 * @return {@link Window} the application's main window
	 */
	public static Window getWindow() {
		return instance.get().window;
	}
	
	/**
	 * Set's a generic <code>ParameterObject</code> to the application context. <br/>Use e.g.:<pre>
	 * ParameterObject lParameters = new ParameterObject();
	 * lParameters.set(Constants.KEY_PARAMETER_MEMBER, lMemberID);
	 * ApplicationData.setParameters(lParameters);</pre>
	 * 
	 * @param inParameters ParameterObject
	 */
	public static void setParameters(ParameterObject inParameters) {
		instance.get().parameters = inParameters;
	}
	
	/**
	 * Returns the generic <code>ParameterObject</code> previously set.
	 * 
	 * @return {@link ParameterObject}
	 */	
	public static ParameterObject getParameters() {
		return instance.get().parameters;
	}
	
	/**
	 * @param inPreferences {@link PreferencesHelper}
	 */
	public static void setPreferences(PreferencesHelper inPreferences) {
		instance.get().preferences = inPreferences;
	}
	
	/**
	 * Access to the application's preferences.
	 * 
	 * @return {@link PreferencesHelper}
	 */
	public static PreferencesHelper getPreferences() {
		return instance.get().preferences;
	}
	
	/**
	 * @param inMenuMap Map&lt;String, MenuItem> sets the map of top level menu items to the bundle symbolic names
	 */
	public static void setMenuMap(Map<String, MenuItem> inMenuMap) {
		instance.get().menuMap = inMenuMap;
	}

	/**
	 * @param inSymbolicName
	 */
	public static void setActiveMenuItem(String inBundleName) {
		MenuItem lOldItem = instance.get().activeMenuItem;
		if (lOldItem != null) {
			lOldItem.setStyleName("");
		}
		MenuItem lNewItem = instance.get().menuMap.get(inBundleName);
		if (lNewItem != null) {
			lNewItem.setStyleName("ripla-menu-active");
		}
		instance.get().activeMenuItem = lNewItem;		
	}

}
