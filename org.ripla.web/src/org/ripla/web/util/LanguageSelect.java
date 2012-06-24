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

package org.ripla.web.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.event.EventAdmin;
import org.ripla.web.Activator;
import org.ripla.web.Constants;
import org.ripla.web.RiplaApplication;
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.internal.services.ApplicationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;

/**
 * Language selection component.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class LanguageSelect extends CustomComponent {
	private static final Logger LOG = LoggerFactory.getLogger(LanguageSelect.class);
	
	private HorizontalLayout layout;
	private IToolbarActionListener listener;
	
	/**
	 * Private LanguageSelect constructor.
	 * 
	 * @param inLanguages
	 * @param inActiveLanguage
	 */
	private LanguageSelect(final Locale[] inLanguages, final String inActiveLanguage) {
		setStyleName("ripla-language-select"); //$NON-NLS-1$
		setSizeUndefined();
		
		layout = new HorizontalLayout();
		setCompositionRoot(layout);
		layout.setHeight(22, UNITS_PIXELS);
		layout.setSpacing(true);

		Label lLabel = new Label(Activator.getMessages().getMessage("toolbar.label.language"), Label.CONTENT_XHTML); //$NON-NLS-1$
		lLabel.setStyleName("ripla-toolbar-label"); //$NON-NLS-1$
		lLabel.setSizeUndefined();
		layout.addComponent(lLabel);
		layout.setComponentAlignment(lLabel, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(lLabel, 1);
		
		final Select lSelect = createSelect(inLanguages, inActiveLanguage);
		lSelect.addListener(new Property.ValueChangeListener() {			
			@Override
			public void valueChange(ValueChangeEvent inEvent) {
				final Locale lNew = ((LocaleAdapter)lSelect.getValue()).getLocale();
				Locale lOld = ApplicationData.getLocale();
				if (!lOld.equals(lNew)) {
					ApplicationData.initLocale(lNew);
					if (listener != null) {
						listener.processAction(new IToolbarAction() {							
							@Override
							public void run(RiplaApplication inApplication, EventAdmin inEventAdmin) {
								LOG.trace("Setting language preference to {}.", lNew.getLanguage());
								inApplication.getPreferences().set(PreferencesHelper.KEY_LANGUAGE, lNew.getLanguage());
								Map<String, Object> lProperties = new HashMap<String, Object>();
								lProperties.put(Constants.EVENT_PROPERTY_REFRESH, "");
								org.osgi.service.event.Event lEvent = new org.osgi.service.event.Event(Constants.EVENT_TOPIC_APPLICATION, lProperties);
								inEventAdmin.sendEvent(lEvent);
							}
						});						
					}
				}
			}
		});
		layout.addComponent(lSelect);
	}	
	

	/**
	 * @param inHeight int the component's height in pixels
	 */
	public void setHeight(int inHeight) {
		layout.setHeight(inHeight, UNITS_PIXELS);
	}
	
	/**
	 * @param inListeners IToolbarActionListener the listener to actions on the toolbar item
	 */
	public void setListener(IToolbarActionListener inListener) {
		listener = inListener;
	}
	
	private static Select createSelect(Locale[] inLanguages, String inActiveLanguage) {
		LanguagesContainer lLanguages = LanguagesContainer.getLanguages(inLanguages, inActiveLanguage);
		final Select outSelect = new Select(null, lLanguages);
		outSelect.select(lLanguages.getActiveLanguage());
		outSelect.setStyleName("ripla-select"); //$NON-NLS-1$
		outSelect.setWidth(55, UNITS_PIXELS);
		outSelect.setNullSelectionAllowed(false);
		outSelect.setImmediate(true);
		return outSelect;
	}	
	
	/**
	 * Factory method, creates a language select component for the application's toolbar.
	 * 
	 * @param inLanguages String[] the languages available to be selected
	 * @param inActiveLanguage String the active, i.e. selected language
	 * @return {@link LanguageSelect}
	 */
	public static LanguageSelect getLanguageSelect(Locale[] inLanguages, String inActiveLanguage) {
		return new LanguageSelect(inLanguages, inActiveLanguage);
	}
	
	/**
	 * Factory method, creates a language select component for the application's toolbar.<br />
	 * The active language is retrieved from the application data.
	 * 
	 * @param inLanguages String[] the languages available to be selected
	 * @return {@link LanguageSelect}
	 */
	public static LanguageSelect getLanguageSelect(Locale[] inLanguages) {
		return new LanguageSelect(inLanguages, ApplicationData.getLocale().getLanguage());
	}
	
	/**
	 * Factory method, creates a language select component for the application's toolbar.<br />
	 * The available languages are taken from Ripla.Constants.
	 * 
	 * @param inActiveLanguage String the active, i.e. selected language
	 * @return {@link LanguageSelect}
	 */
	public static LanguageSelect getLanguageSelect(String inActiveLanguage) {
		return new LanguageSelect(Constants.LANGUAGES, inActiveLanguage);
	}

// ---
	
	private static class LanguagesContainer extends BeanItemContainer<LocaleAdapter> {
		private LocaleAdapter activeLanguage;
		
		private LanguagesContainer() {
			super(LocaleAdapter.class);
		}
		
		static LanguagesContainer getLanguages(Locale[] inLanguages, String inActiveLanguage) {
			LanguagesContainer out = new LanguagesContainer();
			for (Locale lLocale : inLanguages) {
				LocaleAdapter lWrapped = new LocaleAdapter(lLocale);
				out.addItem(lWrapped);
				if (inActiveLanguage.equals(lLocale.getLanguage())) {
					out.setActiveLanguage(lWrapped);
				}
			}
			return out;
		}
		private void setActiveLanguage(LocaleAdapter inActiveLanguage) {
			activeLanguage = inActiveLanguage;
		}
		LocaleAdapter getActiveLanguage() {
			return activeLanguage;
		}
	}
	
	private static class LocaleAdapter {
		private Locale locale;
		
		LocaleAdapter(Locale inLocale) {
			locale = inLocale;
		}
		Locale getLocale() {
			return locale;
		}
		@Override
		public String toString() {
			return locale.getLanguage();
		}
	}
	
}
