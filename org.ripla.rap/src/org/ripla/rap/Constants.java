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
package org.ripla.rap;

import java.util.Locale;

/**
 * Constants for the Ripla RAP bundle.
 * 
 * @author Luthiger
 */
public final class Constants {

	private Constants() {
	}

	// the languages available for the application
	public static final Locale[] LANGUAGES = new Locale[] { Locale.ENGLISH,
			Locale.GERMAN };

	/**
	 * The application's path, i.e. name in the URL.
	 */
	public static String APP_PATH = "/ripla";
	public static final String CONTROLLER_PATTERN = "%s/%s"; //$NON-NLS-1$

	public static final String EVENT_TOPIC_CONTROLLERS = "org/ripla/rap/ControllerEvent/CONTROLLER"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NEXT_CONTROLLER = "next.controller"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_CONTEXT_MENU = "org/ripla/rap/ControllerEvent/CONTEXTMENU"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CONTEXT_MENU_ID = "context.menu.id"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CONTROLLER_ID = "context.menu.controller.id"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_NOTIFICATION = "org/ripla/rap/ControllerEvent/NOTIFICATION"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NOTIFICATION_MSG = "notification.msg"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NOTIFICATION_TYPE = "notification.type"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_APPLICATION = "org/ripla/rap/ControllerEvent/APPLICATION"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_REFRESH = "app.refresh"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CLOSE = "app.close"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_SKIN_ID = "skin.id"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_LOGOUT_DELAY = "logout.delay"; //$NON-NLS-1$

	public static final String MENU_SET_ID_EMPTY = "empty"; //$NON-NLS-1$

	public static final String DFT_SKIN_ID = "org.ripla.rap.skin";
	public static final String DFT_LANGUAGE = Locale.ENGLISH.getLanguage();

	// session attributes
	public static final String RS_USER = "ripla.user";
	public static final String RS_LOCALE = "ripla.locale";
	public static final String RS_PARAMETERS = "ripla.parameters";
	public static final String RS_PREFS = "ripla.preferences";
	public static final String RS_MENU_MAP = "ripla.menu.map";
	public static final String RS_MENU_ACTIVE = "ripla.menu.active";
	public static final String RS_EVENT_DISPATCHER = "ripla.event.dispatcher";

	// skin styles
	public static final String CSS_MENU_BAR = "ripla-menubar";
	public static final String CSS_MENU_HOLDER = "ripla-menu-holder";
	public static final String CSS_MENU = "ripla-menu";
	public static final String CSS_MENU_ACTIVE = "ripla-menu-active";
	public static final String CSS_MENU_ITEM = "ripla-menu-item";

	public static final String FAVICON_PATH = "icons/favicon.png";
	public static final String IMG_LOADING = "icons/loading.gif";
}
