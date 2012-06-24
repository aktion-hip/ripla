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

package org.ripla.demo.config.views;

import org.ripla.demo.config.Activator;
import org.ripla.demo.config.controller.LoginConfigController;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * View to configure the login.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class LoginConfigView extends CustomComponent {
	
	/**
	 * LoginConfigView constructor.
	 * 
	 * @param inLoginConfig 
	 * @param inController {@link LoginConfigController}
	 */
	public LoginConfigView(final boolean inLoginConfig, final LoginConfigController inController) {
		IMessages lMessages = Activator.getMessages();
		VerticalLayout lLayout = new VerticalLayout();
		setCompositionRoot(lLayout);
		lLayout.setStyleName("demo-view"); //$NON-NLS-1$
		lLayout.addComponent(new Label(String.format(RiplaViewHelper.TMPL_TITLE, "demo-pagetitle", lMessages.getMessage("config.login.page.title")), Label.CONTENT_XHTML)); //$NON-NLS-1$ //$NON-NLS-2$
		
		lLayout.addComponent(new Label(lMessages.getMessage("view.login.remark"), Label.CONTENT_XHTML)); //$NON-NLS-1$
		final CheckBox lCheckbox = new CheckBox(lMessages.getMessage("view.login.chk.label")); //$NON-NLS-1$
		lCheckbox.setValue(inLoginConfig);
		lLayout.addComponent(lCheckbox);
		
		Button lSave = new Button(lMessages.getMessage("config.view.button.save")); //$NON-NLS-1$
		lSave.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent inEvent) {
				inController.saveChange(lCheckbox.booleanValue());
			}
		});
		lLayout.addComponent(lSave);
	}

}
