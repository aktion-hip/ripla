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

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Base class for Ripla views.<br/>
 * A Ripla view displays widgets in the application's content view. This view is
 * expected to have a <i>width</i> of less then <i>1300px</i> and a
 * <i>height</i> of less then <i>550px</i>.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public abstract class AbstractRiplaView extends Composite {

	/**
	 * AbstractRiplaView constructor.
	 * 
	 * @param inParent
	 */
	public AbstractRiplaView(final Composite inParent) {
		super(inParent, SWT.NONE);
		setData(RWT.CUSTOM_VARIANT, "ripla-view");
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		setLayout(new GridLayout());
		setLayoutData(GridLayoutHelper.createFillLayoutData());
	}

	protected Label createTitle(final String inTitle) {
		final Label out = new Label(this, SWT.WRAP);
		out.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		out.setData(RWT.CUSTOM_VARIANT, "ripla-viewtitle");
		out.setText(inTitle);
		return out;
	}

}
