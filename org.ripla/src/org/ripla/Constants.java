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

package org.ripla;

/**
 * Constants of the Ripla base bundle.
 * 
 * @author Luthiger
 */
public final class Constants {

	private Constants() {
	}

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

	public static final String PERMISSION_DESCRIPTION_KEY = "ripla.description"; //$NON-NLS-1$
}
