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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.config.Activator;
import org.ripla.rap.demo.config.controller.SkinSelectController;
import org.ripla.rap.demo.config.data.SkinBean;
import org.ripla.rap.demo.config.data.SkinConfigRegistry;
import org.ripla.rap.util.AbstractRiplaView;

/**
 * The view to configure the active skin for the application.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class SkinConfigurationView extends AbstractRiplaView {

	/**
	 * SkinConfigurationView constructor.
	 * 
	 * @param inParent
	 * @param inController
	 * @param inSkinId
	 *            String
	 */
	public SkinConfigurationView(final Composite inParent,
			final SkinSelectController inController, final String inSkinId) {
		super(inParent);

		final IMessages lMessages = Activator.getMessages();
		createTitle(lMessages.getMessage("config.skin.page.title"));

		final SkinModel lModel = new SkinModel(inSkinId);

		final Combo lSkinSelect = new Combo(this, SWT.READ_ONLY);
		lSkinSelect.setItems(lModel.getSkins());
		lSkinSelect.select(lModel.getSelected());
		lSkinSelect.setFocus();

		final Button lSave = new Button(this, SWT.PUSH);
		getShell().setDefaultButton(lSave);
		lSave.setText(lMessages.getMessage("config.view.button.save"));
		lSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				inController.save(lModel.getSkinId(lSkinSelect
						.getSelectionIndex()));
			}
		});

	}

	// ---

	private static class SkinModel {
		private final List<SkinBean> skins;
		private final String activeSkinId;
		int selected = 0;

		protected SkinModel(final String inActiveSkin) {
			skins = SkinConfigRegistry.INSTANCE.getSkins();
			activeSkinId = inActiveSkin;
		}

		protected String[] getSkins() {
			final String[] out = new String[skins.size()];
			int i = 0;
			for (final SkinBean lSkin : skins) {
				if (activeSkinId.equals(lSkin.getID())) {
					selected = i;
				}
				out[i++] = lSkin.getName();
			}
			return out;
		}

		protected String getSkinId(final int inIndex) {
			return skins.get(inIndex).getID();
		}

		protected int getSelected() {
			return selected;
		}
	}

}
