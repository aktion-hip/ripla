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

package org.ripla.rap.demo.widgets.views;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.GridLayoutHelper;

/**
 * Base class for all views in the demo widgets bundle.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class AbstractWidgetsView extends AbstractRiplaView {

	/**
	 * AbstractWidgetsView constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 */
	public AbstractWidgetsView(final Composite inParent) {
		super(inParent);
	}

	protected Label createSubTitle(final String inTitle) {
		return createSubTitle(this, inTitle);
	}

	protected Label createSubTitle(final Composite inParent,
			final String inTitle) {
		final Composite lHolder = new Composite(inParent, SWT.NONE);
		lHolder.setLayout(GridLayoutHelper.createGridLayout());
		lHolder.setLayoutData(GridLayoutHelper.createFillLayoutData());
		lHolder.setData(RWT.CUSTOM_VARIANT, "demo-subtitle");

		final Label out = new Label(lHolder, SWT.NONE);
		out.setText(inTitle);
		out.setData(RWT.CUSTOM_VARIANT, "demo-subtitle");
		return out;
	}

	protected Composite createColumns(final Composite inColumns) {
		final Composite outColumn = new Composite(inColumns, SWT.NONE);
		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.marginRight = 15;
		outColumn.setLayout(lLayout);
		outColumn.setLayoutData(GridLayoutHelper.createFillLayoutData());
		return outColumn;
	}

}
