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
package org.ripla.rap.demo.skin;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ripla.rap.services.ISkin;
import org.ripla.rap.util.LabelHelper;

/**
 * The demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {
	private static final int HEADER_HEIGHT = 90;
	private static final int FOOTER_HEIGHT = 18;

	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public Composite getHeader(final Composite inParent, final String inAppName) {
		final Composite outHeader = new Composite(inParent, SWT.NONE);
		outHeader.setData(RWT.CUSTOM_VARIANT, "demo-header");
		outHeader.setBackgroundMode(SWT.INHERIT_DEFAULT);

		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		outHeader.setLayout(layout);

		final GridData layoutData = new GridData(SWT.DEFAULT, HEADER_HEIGHT);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		outHeader.setLayoutData(layoutData);

		final Composite lTextCell = new Composite(outHeader, SWT.NONE);
		lTextCell.setLayout(new GridLayout());
		lTextCell
				.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		LabelHelper.createLabel(lTextCell, inAppName, "demo-header-text");

		return outHeader;
	}

	@Override
	public boolean hasFooter() {
		return true;
	}

	@Override
	public Composite getFooter(final Composite inParent) {
		final Composite outFooter = new Composite(inParent, SWT.NONE);
		outFooter.setBackgroundMode(SWT.INHERIT_DEFAULT);
		outFooter.setData(RWT.CUSTOM_VARIANT, "demo-footer");

		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		outFooter.setLayout(layout);

		final GridData layoutData = new GridData(SWT.DEFAULT, FOOTER_HEIGHT);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		outFooter.setLayoutData(layoutData);

		LabelHelper.createLabel(outFooter, "@ RelationWare",
				"demo-footer-text", 12);

		return outFooter;
	}

	@Override
	public boolean hasToolBar() {
		return true;
	}

	@Override
	public Label getToolbarSeparator(final Composite inParent) {
		final Label out = LabelHelper.createLabel(inParent, "&#8226;");
		out.setData(RWT.CUSTOM_VARIANT, "demo-toolbar-sep");
		return out;
	}

	@Override
	public boolean hasMenuBar() {
		return true;
	}

	@Override
	public Composite getMenuBarMedium(final Composite inParent) {
		return null;
	}

	@Override
	public Composite getMenuBar(final Composite inParent) {
		return null;
	}

	@Override
	public Image getSubMenuIcon() {
		return null;
	}

}
