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

package org.ripla.web.util;

import java.util.Locale;

import org.ripla.util.AbstractMessages;

import com.vaadin.server.VaadinSession;

/**
 * The base class for messages in a Ripla application.
 * 
 * @author Luthiger
 */
public abstract class AbstractWebMessages extends AbstractMessages {

	@Override
	protected Locale getLocaleChecked() {
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			return VaadinSession.getCurrent().getLocale();
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}

}
