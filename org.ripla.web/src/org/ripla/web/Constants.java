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

package org.ripla.web;

import java.util.Locale;

/**
 * Applications constants.
 *
 * @author Luthiger
 */
public class Constants {
	//the languages available for the application
	public static final Locale[] LANGUAGES = new Locale[] {Locale.ENGLISH, Locale.GERMAN};

	public static final String MENU_SET_ID_EMPTY = "empty"; //$NON-NLS-1$

	public static final String CONTROLLER_PATTERN = "%s/%s"; //$NON-NLS-1$
	
	public static final String EVENT_TOPIC_CONTROLLERS = "org/ripla/web/ControllerEvent/CONTROLLER"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NEXT_CONTROLLER = "next.controller"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_CONTEXT_MENU = "org/ripla/web/ControllerEvent/CONTEXTMENU"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CONTEXT_MENU_ID = "context.menu.id"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_NOTIFICATION = "org/ripla/web/ControllerEvent/NOTIFICATION"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NOTIFICATION_MSG = "notification.msg"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_NOTIFICATION_TYPE = "notification.type"; //$NON-NLS-1$
	public static final String EVENT_TOPIC_APPLICATION = "org/ripla/web/ControllerEvent/APPLICATION"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_REFRESH = "app.refresh"; //$NON-NLS-1$
	public static final String EVENT_PROPERTY_CLOSE = "app.close"; //$NON-NLS-1$

	public static final String KEY_REQUEST_PARAMETER = "request"; //$NON-NLS-1$
	
	public static final String RIPLA_CONFIG_PID = "org.ripla.web.configuration"; //$NON-NLS-1$
	
	public static final String PERMISSION_DESCRIPTION_KEY = "ripla.description"; //$NON-NLS-1$

}
