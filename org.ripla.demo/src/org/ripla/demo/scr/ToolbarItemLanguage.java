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

package org.ripla.demo.scr;

import java.util.Locale;

import org.osgi.service.useradmin.User;
import org.ripla.web.RiplaApplication;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.services.IToolbarItem;
import org.ripla.web.util.LanguageSelect;

import com.vaadin.ui.Component;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item shows
 * a language select.
 * 
 * @author Luthiger
 */
public class ToolbarItemLanguage implements IToolbarItem {
	private transient LanguageSelect languageSelect;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.IToolbarItem#getPosition()
	 */
	@Override
	public int getPosition() {
		return 20;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.IToolbarItem#getComponent()
	 */
	@Override
	public Component getComponent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.services.IToolbarItem#getCreator()
	 */
	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Component createToolbarItem(
					final RiplaApplication inApplication, final User inUser) {
				languageSelect = LanguageSelect.getLanguageSelect(inApplication
						.getPreferences().getLocale(Locale.ENGLISH)
						.getLanguage());
				return languageSelect;
			}
		};
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
