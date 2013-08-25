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

package org.ripla.web;

import java.util.Locale;

/**
 * Applications constants.
 * 
 * @author Luthiger
 */
public final class Constants {

	private Constants() {
	}

	// the languages available for the application
	public static final Locale[] LANGUAGES = new Locale[] { Locale.ENGLISH,
			Locale.GERMAN };

	public static final String MENU_SET_ID_EMPTY = "empty"; //$NON-NLS-1$

	public static final String CONTROLLER_PATTERN = "%s/%s"; //$NON-NLS-1$

	public static final String EVENT_TOPIC_CONTROLLERS = "org/ripla/web/ControllerEvent/CONTROLLER"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NEXT_CONTROLLER = "next.controller"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_CONTEXT_MENU = "org/ripla/web/ControllerEvent/CONTEXTMENU"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CONTEXT_MENU_ID = "context.menu.id"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CONTROLLER_ID = "context.menu.controller.id"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_APPLICATION = "org/ripla/web/ControllerEvent/APPLICATION"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_REFRESH = "app.refresh"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CLOSE = "app.close"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_SKIN_ID = "skin.id"; //$NON-NLS-1$

	public static final String KEY_REQUEST_PARAMETER = "request"; //$NON-NLS-1$

	public static final String RIPLA_CONFIG_PID = "org.ripla.web.configuration"; //$NON-NLS-1$

	public static final String PERMISSION_DESCRIPTION_KEY = "ripla.description"; //$NON-NLS-1$

	public static final String DFT_SKIN_ID = "org.ripla.web.skin";
	public static final String DFT_LANGUAGE = Locale.ENGLISH.getLanguage();

	// session attributes
	public static final String SA_MENU_MAP = "ripla.menu.map";
	public static final String SA_ACTIVE_MENU = "ripla.active.menu";

	public static final String CSS_ACTIVE_MENU = "ripla-menu-active";

	public static final String KEY_CONFIG_SKIN = "org.ripla.config.skin";
	public static final String KEY_CONFIG_LANGUAGE = "org.ripla.config.language";
}
