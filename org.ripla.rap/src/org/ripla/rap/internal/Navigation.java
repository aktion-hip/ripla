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

package org.ripla.rap.internal;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author Luthiger
 */
public abstract class Navigation {

	private final Composite composite;

	public Navigation(final Composite inParent) {
		composite = new Composite(inParent, SWT.NONE);
		composite.setData(RWT.CUSTOM_VARIANT, "navigation");

	}

	/**
	 * @return {@link Control} the navigation's control
	 */
	public Control getControl() {
		return composite;
	}

}
