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
package org.ripla.web.demo.config.views;

import org.ripla.interfaces.IMessages;
import org.ripla.web.demo.config.Activator;
import org.ripla.web.demo.config.controller.SkinSelectController;
import org.ripla.web.demo.config.data.SkinBean;
import org.ripla.web.demo.config.data.SkinConfigRegistry;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * The view to configure the active skin for the application.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class SkinConfigurationView extends CustomComponent {

	private final ComboBox skinSelect;

	/**
	 * @param inSkinSelectController
	 */
	public SkinConfigurationView(final SkinSelectController inController) {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = new VerticalLayout();
		setCompositionRoot(lLayout);
		lLayout.setStyleName("demo-view"); //$NON-NLS-1$
		lLayout.addComponent(new Label(
				String.format(
						RiplaViewHelper.TMPL_TITLE,
						"demo-pagetitle", lMessages.getMessage("config.skin.page.title")), ContentMode.HTML)); //$NON-NLS-1$ //$NON-NLS-2$

		skinSelect = new ComboBox(null, SkinConfigRegistry.INSTANCE.getSkins());
		skinSelect.setNullSelectionAllowed(false);
		skinSelect.setNewItemsAllowed(false);
		skinSelect.setWidth(230, Unit.PIXELS);
		skinSelect.focus();
		lLayout.addComponent(skinSelect);

		final Button lSave = new Button(
				lMessages.getMessage("config.view.button.save")); //$NON-NLS-1$
		lSave.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent inEvent) {
				inController.save((SkinBean) skinSelect.getValue());
			}
		});
		lSave.setClickShortcut(KeyCode.ENTER);
		lLayout.addComponent(lSave);
	}

	private SkinBean getActive(final String inSkinID) {
		for (final SkinBean lSkin : SkinConfigRegistry.INSTANCE.getSkins()) {
			if (inSkinID.equals(lSkin.getID())) {
				return lSkin;
			}
		}
		return null;
	}

	@Override
	public void attach() {
		super.attach();
		skinSelect.select(getActive(getUI().getTheme()));
	}

}
