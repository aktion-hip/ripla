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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.ripla.rap.Activator;

/**
 * Base class for ripla forms.
 * <p>
 * Concreted subclasses have to create the form in the method
 * <code>createControls()</code>. <br/>
 * This class creates a <code>GridLayout</code> to place the <code>SWT</code>
 * input widgets. Implementor classes can use the helper methods
 * <code>createLabel()</code>, <code>createLabelRequired()</code>,
 * <code>createText()</code> or <code>createCombo()</code> to create the
 * widgets.
 * </p>
 * <p>
 * Usage example:
 * 
 * <pre>
 * private static class MyForm extends AbstractRiplaForm {
 * 	// fields, constructor etc.
 * 	&#064;Override
 * 	protected void createControls() {
 * 		// create widgets here
 * 	}
 * }
 * 
 * final MyForm myForm = new MyForm(parent);
 * myForm.create(); // creates the form with the input widgets on the parent
 * // composite
 * Button save = myForm.createFormProcessButton(&quot;Save&quot;);
 * save.addSelectionListener(new SelectionAdapter() {
 * 	public void widgetSelected(final SelectionEvent inEvent) {
 * 		if (myForm.checkStatus()) {
 * 			// save input
 * 		} else {
 * 			System.out.println(myForm.getMessages());
 * 		}
 * 	};
 * });
 * </pre>
 * 
 * Forms using this base class are best used with <code>DataBindingHelper</code>
 * for input validation. E.g. the create a <code>DataBindingHelper</code>
 * instance and delegate the form's methods <code>checkStatus()</code> and
 * <code>getMessages()</code> to <code>DataBindingHelper.checkStatus()</code>
 * and <code>DataBindingHelper.getStatusMsg()</code> respectively.
 * </p>
 * 
 * @author Luthiger
 * @see DataBindingHelper
 */
public abstract class AbstractRiplaForm {
	private static final String STYLE_LABEL = "ripla-label"; //$NON-NLS-1$
	private static final String STYLE_EMPH = "ripla-label-emphasized"; //$NON-NLS-1$

	private final Composite form;
	private final GridLayout layout;

	/**
	 * AbstractRiplaForm constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inWidth
	 *            int the width of the form
	 */
	public AbstractRiplaForm(final Composite inParent, final int inWidth) {
		form = new Composite(inParent, SWT.NONE);

		layout = new GridLayout();
		layout.numColumns = 2;
		form.setLayout(layout);
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		// lLayoutData.widthHint = inWidth;
		lLayoutData.grabExcessHorizontalSpace = false;
		form.setLayoutData(lLayoutData);
	}

	/**
	 * Sets the style of the composite containing the form widgets.
	 * 
	 * @param inStyleName
	 *            String the <code>RWT.CUSTOM_VARIANT</code> CSS style
	 */
	protected final void setStyle(final String inStyleName) {
		form.setData(RWT.CUSTOM_VARIANT, inStyleName);
	}

	/**
	 * Set the number of columns in the <code>GridLayout</code>.
	 * 
	 * @param inColumns
	 *            int the number of columns
	 */
	protected void setNumColums(final int inColumns) {
		layout.numColumns = inColumns;
	}

	/**
	 * Creates the form on the composite passed in the constructor.
	 */
	public final void create() {
		Realm.runWithDefault(SWTObservables.getRealm(form.getDisplay()),
				new Runnable() {
					@Override
					public void run() {
						createControls();
					}
				});
	}

	/**
	 * Classes implementing a concrete form have to create the input controls in
	 * this method.
	 */
	protected abstract void createControls();

	protected Composite getBody() {
		return form;
	}

	/**
	 * Creates a <code>GridData</code> to span a widget over the specified
	 * number of columns. Usage:
	 * 
	 * <pre>
	 * myWidget.setLayoutData(getColSpanData(2, 300));
	 * </pre>
	 * 
	 * @param inColSpan
	 *            int the col span
	 * @param inWidth
	 *            int the minimum width of the widget
	 * @return {@link GridData}
	 */
	protected GridData getColSpanData(final int inColSpan, final int inWidth) {
		final GridData out = new GridData(SWT.FILL, SWT.TOP, true, false);
		out.horizontalSpan = inColSpan;
		out.minimumWidth = inWidth;
		out.widthHint = inWidth;
		return out;
	}

	private Label createLbl(final String inLabel) {
		final Label out = new Label(form, SWT.NONE);
		out.setText(inLabel);
		return out;
	}

	/**
	 * Creates a label for a normal widget.
	 * 
	 * @param inLabel
	 *            String the label text
	 * @return {@link Label} the widget instance
	 */
	protected Label createLabel(final String inLabel) {
		final Label out = createLbl(inLabel);
		out.setData(RWT.CUSTOM_VARIANT, STYLE_LABEL);
		return out;
	}

	protected Label createLabel(final String inLabel, final int inColSpan) {
		final Label out = createLbl(inLabel);
		out.setData(RWT.CUSTOM_VARIANT, STYLE_LABEL);
		out.setLayoutData(getColSpanData(inColSpan, 0));
		return out;
	}

	/**
	 * Creates a label for a widget with required input. This label is displayed
	 * bold.
	 * 
	 * @param inLabel
	 *            String the label text
	 * @return {@link Label} the widget instance
	 */
	protected Label createLabelRequired(final String inLabel) {
		final Label out = createLbl(inLabel);
		out.setData(RWT.CUSTOM_VARIANT, STYLE_EMPH);
		return out;
	}

	/**
	 * Creates a combo for the specified items that spans the specified number
	 * of columns.
	 * 
	 * @param inItems
	 *            String[] the combo's selection list
	 * @param inColSpan
	 *            int the combo's column span
	 * @param inWidth
	 *            int the combo's minimum width
	 * @return {@link CCombo} the widget instance
	 */
	protected CCombo createCombo(final String[] inItems, final int inColSpan,
			final int inWidth) {
		final CCombo out = new CCombo(getBody(), SWT.BORDER);
		out.setItems(inItems);
		out.setLayoutData(getColSpanData(inColSpan, inWidth));
		return out;
	}

	/**
	 * Creates a text input widget that spans the specified number of columns.
	 * The style bits are set to <code>SWT.BORDER | SWT.SINGLE</code>.
	 * 
	 * @param inValue
	 *            String the widgets initial value
	 * @param inColSpan
	 *            int the combo's column span
	 * @param inWidth
	 *            int the combo's minimum width
	 * @return {@link Text} the widget instance
	 */
	protected Text createText(final String inValue, final int inColSpan,
			final int inWidth) {
		return createText(inValue, inColSpan, inWidth, SWT.BORDER | SWT.SINGLE);
	}

	/**
	 * Creates a text input widget that spans the specified number of columns.
	 * 
	 * @param inValue
	 *            String the widgets initial value
	 * @param inColSpan
	 *            int the combo's column span
	 * @param inWidth
	 *            int the combo's minimum width
	 * @param inStyle
	 *            int the style bits, e.g.
	 *            <code>SWT.BORDER | SWT.SINGLE | SWT.PASSWORD</code>
	 * @return {@link Text}
	 */
	protected Text createText(final String inValue, final int inColSpan,
			final int inWidth, final int inStyle) {
		final Text out = new Text(getBody(), inStyle);
		out.setLayoutData(getColSpanData(inColSpan, inWidth));
		return out;
	}

	/**
	 * Creates a validator for a mandatory field.
	 * 
	 * @param inLabel
	 *            String the label of the required field
	 * @return {@link IValidator}
	 */
	protected IValidator createRequiredValidator(final String inLabel) {
		return new RequiredValidator(inLabel);
	}

	/**
	 * Creates the button to process the form and places it beyond the form.
	 * 
	 * @param inLabel
	 *            String the button's label, e.g. <code>save</code>
	 * @return {@link Button}
	 */
	public Button createFormProcessButton(final String inLabel) {
		new Label(form, SWT.NONE); // an empty label to fill the first column
		final Button out = new Button(form, SWT.PUSH);
		out.setText(inLabel);
		final GridData lData = new GridData(SWT.BEGINNING, SWT.TOP, false,
				false);
		lData.horizontalSpan = 2;
		out.setLayoutData(lData);
		return out;
	}

	// ---

	/**
	 * A validator for a required field that can be extended by additional
	 * validators. These validators are checked if the input is not empty and
	 * before this validator returns <code>OK</code>.
	 * <p>
	 * Extend this class and provide additional validators through the extended
	 * class' constructor.
	 * </p>
	 * 
	 * @author Luthiger
	 */
	public static class RequiredValidator implements IValidator {
		private final String label;
		private final IValidator[] additional;

		/**
		 * RequiredValidator constructor.
		 * 
		 * @param inLabel
		 *            String the input field's label
		 * @param inAdditional
		 *            {@link IValidator}[] array of additional validators, may
		 *            be empty
		 */
		protected RequiredValidator(final String inLabel,
				final IValidator... inAdditional) {
			label = inLabel;
			additional = inAdditional;
		}

		@Override
		public IStatus validate(final Object inValue) {
			if (inValue != null) {
				if (inValue.toString().trim().length() > 0) {
					for (final IValidator lValidator : additional) {
						final IStatus lStatus = lValidator.validate(inValue);
						if (!lStatus.isOK()) {
							return lStatus;
						}
					}
					return ValidationStatus.ok();
				}
			}
			return ValidationStatus.warning(Activator.getMessages()
					.getFormattedMessage("errmsg.error.not.empty", label));
		}
	}

}
