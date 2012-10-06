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
import org.ripla.demo.config.controller.SkinSelectController;
import org.ripla.demo.config.data.SkinBean;
import org.ripla.demo.config.data.SkinConfigRegistry;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
						"demo-pagetitle", lMessages.getMessage("config.skin.page.title")), Label.CONTENT_XHTML)); //$NON-NLS-1$ //$NON-NLS-2$

		skinSelect = new ComboBox(null, SkinConfigRegistry.INSTANCE.getSkins());
		skinSelect.setNullSelectionAllowed(false);
		skinSelect.setNewItemsAllowed(false);
		skinSelect.setMultiSelect(false);
		skinSelect.setWidth(200, UNITS_PIXELS);
		skinSelect.focus();
		lLayout.addComponent(skinSelect);

		final Button lSave = new Button(
				lMessages.getMessage("config.view.button.save")); //$NON-NLS-1$
		lSave.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				inController.save((SkinBean) skinSelect.getValue(),
						getApplication());
			}
		});
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.AbstractComponentContainer#attach()
	 */
	@Override
	public void attach() {
		skinSelect.select(getActive(getApplication().getTheme()));
	}

}
