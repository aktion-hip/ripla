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

package org.ripla.rap.demo.config.views;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.config.Activator;
import org.ripla.rap.demo.config.controller.LoginConfigController;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.LabelHelper;

/**
 * View to configure the login.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class LoginConfigView extends AbstractRiplaView {

	/**
	 * LoginConfigView constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inLoginConfig
	 *            boolean
	 * @param inController
	 *            {@link LoginConfigController}
	 * @param inEnabled
	 *            boolean <code>true</code> if login configuration is enabled
	 */
	public LoginConfigView(final Composite inParent,
			final boolean inLoginConfig,
			final LoginConfigController inController, final boolean inEnabled) {
		super(inParent);

		final IMessages lMessages = Activator.getMessages();
		createTitle(lMessages.getMessage("config.login.page.title"));
		Label lLabel = LabelHelper.createLabel(this,
				lMessages.getMessage("view.login.remark"), "ripla-font");
		lLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		if (!inEnabled) {
			lLabel = LabelHelper.createLabel(this,
					lMessages.getMessage("view.login.disabled"), "ripla-font");
			lLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		}
		final Button lCheckbox = new Button(this, SWT.CHECK);
		lCheckbox.setText(lMessages.getMessage("view.login.chk.label"));
		lCheckbox.setEnabled(inEnabled);
		lCheckbox.setSelection(inLoginConfig);
		lCheckbox.setFocus();

		final Button lSave = new Button(this, SWT.PUSH);
		getShell().setDefaultButton(lSave);
		lSave.setText(lMessages.getMessage("config.view.button.save"));
		lSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				inController.saveChange(lCheckbox.getSelection());
			}
		});
		lSave.setEnabled(inEnabled);
	}

}
