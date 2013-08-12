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
package org.ripla.web.demo.skin;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.FooterHelper;
import org.ripla.web.util.LabelHelper;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {

	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public Component getHeader(final String inAppName) {
		final HorizontalLayout out = new HorizontalLayout();
		out.setStyleName("demo-header"); //$NON-NLS-1$
		out.setMargin(true);
		out.setWidth("100%"); //$NON-NLS-1$
		out.setHeight(90, Unit.PIXELS);

		final Label lTitle = LabelHelper.createLabel("Ripla Demo Application",
				"demo-header-text");
		lTitle.setSizeUndefined();
		out.addComponent(lTitle);
		out.setComponentAlignment(lTitle, Alignment.MIDDLE_CENTER);

		return out;
	}

	@Override
	public boolean hasFooter() {
		return true;
	}

	@Override
	public Component getFooter() {
		final FooterHelper out = FooterHelper
				.createFooter(FooterHelper.DFT_FOOTER_TEXT);
		out.setHeight(19);
		out.setStyleName("demo-footer");
		return out;
	}

	@Override
	public boolean hasToolBar() {
		return true;
	}

	@Override
	public boolean hasMenuBar() {
		return true;
	}

	@Override
	public Label getToolbarSeparator() {
		final Label outSeparator = new Label("&bull;", ContentMode.HTML); //$NON-NLS-1$
		// outSeparator.setSizeUndefined();
		outSeparator.setWidth(4, Unit.PIXELS);
		return outSeparator;
	}

	@Override
	public HorizontalLayout getMenuBar() {
		return null;
	}

	@Override
	public HorizontalLayout getMenuBarMedium() {
		return null;
	}

	@Override
	public Resource getSubMenuIcon() {
		return null;
	}

}
