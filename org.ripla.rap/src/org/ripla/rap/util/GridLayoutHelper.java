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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Helper class for widgets using <code>GridLayout</code>.
 * 
 * @author Luthiger
 */
public final class GridLayoutHelper {
	public static enum Fill {
		FILL_HORIZONTAL, FILL_VERTICAL, FILL_BOTH;
	}

	private GridLayoutHelper() {
	}

	/**
	 * @return {@link GridLayout} with zero margin
	 */
	public static GridLayout createGridLayout() {
		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		return layout;
	}

	/**
	 * Creates a <code>Composite</code> with zero margin <code>GridLayout</code>
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @return {@link Composite}
	 */
	public static Composite createComposite(final Composite inParent) {
		final Composite out = new Composite(inParent, SWT.NONE);
		out.setLayout(createGridLayout());
		return out;
	}

	/**
	 * Creates a <code>Composite</code> with zero margin <code>GridLayout</code>
	 * using the specified <code>GridData</code>.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inGridData
	 *            {@link GridData}
	 * @return {@link Composite}
	 */
	public static Composite createComposite(final Composite inParent,
			final GridData inGridData) {
		final Composite out = new Composite(inParent, SWT.NONE);
		out.setLayout(createGridLayout());
		out.setLayoutData(inGridData);
		return out;
	}

	/**
	 * Creates a <code>GridData</code> that fills a composite with the specified
	 * height.
	 * 
	 * @param inHeight
	 *            int a minimum height for the row
	 * @return {@link GridData}
	 */
	public static GridData createFillLayoutData(final int inHeight) {
		final GridData out = new GridData(SWT.DEFAULT, inHeight);
		prepareGridData(out, Fill.FILL_HORIZONTAL);
		return out;
	}

	/**
	 * Creates a <code>GridData</code> that fills a composite (in both
	 * dimensions).
	 * 
	 * @return {@link GridData}
	 */
	public static GridData createFillLayoutData() {
		return createFillLayoutData(Fill.FILL_BOTH);
	}

	/**
	 * Creates a <code>GridData</code> that fills a composite.
	 * 
	 * @param inFill
	 *            {@link Fill} the fill dimension
	 * @return {@link GridData}
	 */
	public static GridData createFillLayoutData(final Fill inFill) {
		final GridData out = new GridData();
		prepareGridData(out, inFill);
		return out;
	}

	private static void prepareGridData(final GridData inData, final Fill inFill) {
		inData.grabExcessHorizontalSpace = true;
		switch (inFill) {
		case FILL_HORIZONTAL:
			inData.horizontalAlignment = GridData.FILL;
			break;
		case FILL_VERTICAL:
			inData.verticalAlignment = GridData.FILL;
			break;
		case FILL_BOTH:
			// default
		default:
			inData.horizontalAlignment = GridData.FILL;
			inData.verticalAlignment = GridData.FILL;
		}
	}

}
