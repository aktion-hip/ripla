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

package org.ripla.demo.skin;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.FooterHelper;
import org.ripla.web.util.LabelHelper;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
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
	public static final String SKIN_ID = "org.ripla.demo.skin";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSkinID()
	 */
	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSkinName()
	 */
	@Override
	public String getSkinName() {
		return "Ripla Demo Skin (Reindeer)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasHeader()
	 */
	@Override
	public boolean hasHeader() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getHeader()
	 */
	@Override
	public Component getHeader(final String inAppName) {
		final HorizontalLayout out = new HorizontalLayout();
		out.setStyleName("demo-header"); //$NON-NLS-1$
		out.setMargin(true);
		out.setWidth("100%"); //$NON-NLS-1$
		out.setHeight(90, Sizeable.UNITS_PIXELS);

		final Label lTitle = LabelHelper.createLabel("Ripla Demo Application",
				"demo-header-text");
		lTitle.setSizeUndefined();
		out.addComponent(lTitle);
		out.setComponentAlignment(lTitle, Alignment.MIDDLE_CENTER);

		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasFooter()
	 */
	@Override
	public boolean hasFooter() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getFooter()
	 */
	@Override
	public Component getFooter() {
		final FooterHelper out = FooterHelper
				.createFooter(FooterHelper.DFT_FOOTER_TEXT);
		out.setHeight(19);
		out.setStyleName("demo-footer");
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hasToolBar()
	 */
	@Override
	public boolean hasToolBar() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#hatMenuBar()
	 */
	@Override
	public boolean hasMenuBar() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getToolbarSeparator()
	 */
	@Override
	public Label getToolbarSeparator() {
		final Label outSeparator = new Label("&bull;", Label.CONTENT_XHTML); //$NON-NLS-1$
		// outSeparator.setSizeUndefined();
		outSeparator.setWidth(4, Sizeable.UNITS_PIXELS);
		return outSeparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getMenuBarLayout()
	 */
	@Override
	public HorizontalLayout getMenuBarLayout() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getMenuBarComponent()
	 */
	@Override
	public HorizontalLayout getMenuBarComponent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.ISkin#getSubMenuIcon()
	 */
	@Override
	public Resource getSubMenuIcon() {
		return null;
	}

}
