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
package org.ripla.web.util;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

/**
 * Component to render a two column table to display label - value pairs.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class LabelValueTable extends CustomComponent {
	public static final String STYLE_LABEL = "<div class=\"ripla-label\">%s</div>"; //$NON-NLS-1$
	public static final String STYLE_PLAIN = "<div class=\"ripla-value\">%s</div>"; //$NON-NLS-1$
	private static final String STYLE_EMPH = "<div class=\"ripla-value-emphasized\">%s</div>"; //$NON-NLS-1$
	
	private GridLayout layout;

	/**
	 * Default constructor.
	 */
	public LabelValueTable() {
		this("ripla-label-table"); //$NON-NLS-1$
	}

	/**
	 * Constructor setting the table's style class. 
	 * 
	 * @param inStyleName String the name of the table's style class attribute
	 */
	public LabelValueTable(String inStyleName) {
		layout = new GridLayout(2, 1);
		setCompositionRoot(layout);
		layout.setStyleName(inStyleName);
		layout.setWidth("100%"); //$NON-NLS-1$
		layout.setColumnExpandRatio(1, 1);
	}
	
	/**
	 * Adds a row to the table.
	 * 
	 * @param inLabel String
	 * @param inValue String
	 */
	public void addRow(String inLabel, String inValue) {
		layout.addComponent(createLabel(STYLE_LABEL, inLabel));
		layout.addComponent(createLabel(STYLE_PLAIN, inValue));
		layout.newLine();
	}
	
	/**
	 * Adds a row to the table.
	 * 
	 * @param inLabel String
	 * @param inValue {@link Component}
	 * @return {@link Label} the row's label component
	 */
	public Label addRow(String inLabel, Component inValue) {
		Label outLabel = createLabel(STYLE_LABEL, inLabel);
		layout.addComponent(outLabel);
		layout.addComponent(inValue);
		layout.newLine();
		return outLabel;
	}
	
	/**
	 * Adds a row with valued emphasized to the table.
	 * 
	 * @param inLabel String
	 * @param inValue String
	 */
	public void addRowEmphasized(String inLabel, String inValue) {
		layout.addComponent(createLabel(STYLE_LABEL, inLabel));
		layout.addComponent(createLabel(STYLE_EMPH, inValue));
		layout.newLine();
	}
	
	/**
	 * Adds a row with label emphasized to the table.
	 * 
	 * @param inLabel String
	 * @param inComponent {@link Component}
	 * @return {@link Label} the row's label component
	 */
	public Label addRowEmphasized(String inLabel, Component inComponent) {
		Label outLabel = createLabel(STYLE_EMPH, inLabel);
		layout.addComponent(outLabel);
		layout.addComponent(inComponent);
		layout.newLine();
		return outLabel;
	}
	
	/**
	 * Adds an empty row to the table.
	 */
	public void addEmtpyRow() {
		layout.addComponent(createLabel(STYLE_PLAIN, "&#160;")); //$NON-NLS-1$
		layout.addComponent(createLabel(STYLE_PLAIN, "&#160;")); //$NON-NLS-1$
		layout.newLine();
	}
	
	/**
	 * Adds a row with a label in the first column only.
	 * 
	 * @param inLabel String the label caption
	 * @return {@link Label} the created label component
	 */
	public Label addRow(String inLabel) {
		Label outLabel = createLabel(STYLE_LABEL, inLabel);
		layout.addComponent(outLabel);
		layout.newLine();
		return outLabel;
	}
	
	/**
	 * Adds a row with the specified component in the first column only.
	 * 
	 * @param inComponent {@link Component}
	 */
	public void addRow(Component inComponent) {
		int lRow = layout.getRows();
		layout.setRows(lRow+1);
		layout.addComponent(inComponent, 0, lRow, 1, lRow);
		layout.newLine();
	}
	
	private Label createLabel(String inStyle, String inCaption) {
		Label out = new Label(String.format(inStyle, inCaption), Label.CONTENT_XHTML);
		out.setWidth(null);
		return out;
	}
	
	/**
	 * Convenience method to create a plain label.
	 * 
	 * @param inCaption String the label's caption
	 * @return {@link Label}
	 */
	public static Label createPlainLabel(String inCaption) {
		Label out = new Label(String.format(STYLE_LABEL, inCaption), Label.CONTENT_XHTML);
		out.setWidth(null);
		return out;		
	}
	
}
