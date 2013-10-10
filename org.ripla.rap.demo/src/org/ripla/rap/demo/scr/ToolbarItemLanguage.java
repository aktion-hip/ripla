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

package org.ripla.rap.demo.scr;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.useradmin.User;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.interfaces.IToolbarItemCreator;
import org.ripla.rap.services.IToolbarItem;
import org.ripla.rap.util.LanguageSelect;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item shows
 * a language selection combo.
 * 
 * @author Luthiger
 */
public class ToolbarItemLanguage implements IToolbarItem {
	private transient LanguageSelect languageSelect;

	@Override
	public int getPosition() {
		return 20;
	}

	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Control createToolbarItem(final Composite inToolbar,
					final IBodyComponent inBody, final User inUser) {
				languageSelect = inBody.createToolbarItem(LanguageSelect.class);
				return languageSelect;
			}
		};
	}

	@Override
	public Control getComponent(final Composite inToolbar) {
		return null;
	}

	/**
	 * We accept only one listener.
	 */
	@Override
	public void registerToolbarActionListener(
			final IToolbarActionListener inListener) {
		if (languageSelect != null) {
			languageSelect.setListener(inListener);
		}
	}

}
