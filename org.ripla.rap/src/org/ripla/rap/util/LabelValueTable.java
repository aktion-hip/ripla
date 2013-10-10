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
 * Component to render a two column table to display label - value pairs.<br />
 * Usage:
 * 
 * <pre>
 * LabelValueTable values = new LabelValueTable(parent);
 * values.addRowEmphasized(&quot;Label 1&quot;, &quot;Value 1&quot;);
 * values.addRowEmphasized(&quot;Label 2&quot;, &quot;Value 2&quot;);
 * </pre>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public final class LabelValueTable extends Composite {
	public static final String STYLE_LABEL = "ripla-label"; //$NON-NLS-1$
	public static final String STYLE_PLAIN = "ripla-value"; //$NON-NLS-1$
	private static final String STYLE_EMPH = "ripla-value-emphasized"; //$NON-NLS-1$

	/**
	 * LabelValueTable default constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 */
	public LabelValueTable(final Composite inParent) {
		this(inParent, "ripla-label-table");
	}

	/**
	 * Constructor setting the table's style class.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inCustomStyle
	 *            String
	 */
	public LabelValueTable(final Composite inParent, final String inCustomStyle) {
		super(inParent, SWT.NONE);
		setData(RWT.CUSTOM_VARIANT, inCustomStyle);
		setLayout(new GridLayout(2, false));
		setLayoutData(GridLayoutHelper.createFillLayoutData());
	}

	/**
	 * Adds a row to the table.
	 * 
	 * @param inLabel
	 *            String
	 * @param inValue
	 *            String
	 */
	public void addRow(final String inLabel, final String inValue) {
		createLabel(STYLE_LABEL, inLabel);
		createLabel(STYLE_PLAIN, inValue);
	}

	/**
	 * Adds a row with valued emphasized to the table.
	 * 
	 * @param inLabel
	 *            String
	 * @param inValue
	 *            String
	 */
	public void addRowEmphasized(final String inLabel, final String inValue) {
		createLabel(STYLE_LABEL, inLabel);
		createLabel(STYLE_EMPH, inValue);
	}

	/**
	 * Adds a row to the table.
	 * 
	 * @param inLabel
	 *            String
	 * @param inValue
	 *            {@link IControlCreator}
	 * @return {@link Label}
	 */
	public Label addRow(final String inLabel, final IControlCreator inValue) {
		final Label out = createLabel(STYLE_LABEL, inLabel);
		inValue.create(this);
		return out;
	}

	/**
	 * Adds a row with label emphasized to the table.
	 * 
	 * @param inLabel
	 *            String
	 * @param inValue
	 *            {@link IControlCreator}
	 * @return {@link Label} the row's label component
	 */
	public Label addRowEmphasized(final String inLabel,
			final IControlCreator inValue) {
		final Label out = createLabel(STYLE_EMPH, inLabel);
		inValue.create(this);
		return out;
	}

	/**
	 * Adds an empty row to the table.
	 */
	public void addEmtpyRow() {
		createLabel(STYLE_PLAIN, "&#160;");
		createLabel(STYLE_PLAIN, "&#160;");
	}

	/**
	 * Adds a row with a label in the first column only.
	 * 
	 * @param inLabel
	 *            String
	 * @return {@link Label}
	 */
	public Label addRow(final String inLabel) {
		final Label out = createLabel(STYLE_LABEL, inLabel);
		createLabel(STYLE_PLAIN, "&#160;");
		return out;
	}

	/**
	 * Adds a row with the specified control in the first column only.
	 * 
	 * @param inControl
	 *            {@link IControlCreator}
	 */
	public void addRow(final IControlCreator inControl) {
		inControl.create(this);
		createLabel(STYLE_PLAIN, "&#160;");
	}

	private Label createLabel(final String inStyle, final String inCaption) {
		final Label out = new Label(this, SWT.NONE);
		out.setData(RWT.CUSTOM_VARIANT, inStyle);
		out.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		return out;
	}

	// ---

	public static interface IControlCreator {
		void create(LabelValueTable inParent);
	}

}
