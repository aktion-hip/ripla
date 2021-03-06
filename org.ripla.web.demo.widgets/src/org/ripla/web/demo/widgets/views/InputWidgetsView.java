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

package org.ripla.web.demo.widgets.views;

import org.ripla.interfaces.IMessages;
import org.ripla.web.demo.widgets.Activator;
import org.ripla.web.demo.widgets.data.CountryBean;
import org.ripla.web.demo.widgets.data.CountryData;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * The view to display the Vaadin input widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class InputWidgetsView extends AbstractWidgetsView {
	private static final int WIDTH_FIELD = 25;
	private static final int WIDTH_AREA = 38;
	private static final int FILTER_WIDTH = 170;

	public InputWidgetsView() {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.input"); //$NON-NLS-1$

		final HorizontalLayout lColumns = new HorizontalLayout();
		lColumns.setSpacing(true);
		lLayout.addComponent(lColumns);

		final VerticalLayout lCol1 = new VerticalLayout();
		lCol1.setSizeUndefined();
		lColumns.addComponent(lCol1);
		final VerticalLayout lCol2 = new VerticalLayout();
		lCol2.setSizeUndefined();
		lColumns.addComponent(lCol2);
		final VerticalLayout lCol3 = new VerticalLayout();
		lCol3.setSizeUndefined();
		lColumns.addComponent(lCol3);
		lColumns.setExpandRatio(lCol3, 1);

		// classic input fields
		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.input.normal"))); //$NON-NLS-1$
		final TextField lTextField = new TextField();
		lTextField.setColumns(WIDTH_FIELD);
		lCol1.addComponent(lTextField);

		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.input.prompt"))); //$NON-NLS-1$
		final TextField lTextField2 = new TextField();
		lTextField2.setInputPrompt(lMessages
				.getMessage("widgets.input.input.prompt")); //$NON-NLS-1$
		lTextField2.setColumns(WIDTH_FIELD);
		lCol1.addComponent(lTextField2);

		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.input.password"))); //$NON-NLS-1$
		final PasswordField lPassword = new PasswordField();
		lPassword.setColumns(WIDTH_FIELD);
		lCol1.addComponent(lPassword);

		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.date"))); //$NON-NLS-1$
		final PopupDateField lDate1 = new PopupDateField(
				lMessages.getMessage("widgets.input.popup.date")); //$NON-NLS-1$
		lDate1.setResolution(Resolution.DAY);
		lDate1.setDateFormat("dd. MMMM yyyy"); //$NON-NLS-1$
		lDate1.setWidth(160, Unit.PIXELS);
		lDate1.setValue(new java.util.Date());
		lCol1.addComponent(lDate1);

		// text areas
		lCol2.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.text.area"))); //$NON-NLS-1$
		final TextArea lArea = new TextArea();
		lArea.setColumns(WIDTH_AREA);
		lArea.setRows(7);
		lCol2.addComponent(lArea);

		lCol2.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.rich.text"))); //$NON-NLS-1$
		final RichTextArea lRichText = new RichTextArea();
		lRichText.setWidth(WIDTH_AREA, Unit.EM);
		lRichText.setHeight(15, Unit.EM);
		lCol2.addComponent(lRichText);

		// text input with filter
		final CountryContainer lCountries = new CountryContainer();
		lCol3.addComponent(getSubtitle(lMessages
				.getMessage("widgets.input.subtitle.input.filter"))); //$NON-NLS-1$
		final TextField lFilter = new TextField();
		lFilter.setTextChangeEventMode(TextChangeEventMode.LAZY);
		lFilter.setTextChangeTimeout(200);
		lFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent inEvent) {
				lCountries.removeAllContainerFilters();
				lCountries.addContainerFilter(new SimpleStringFilter(
						CountryContainer.PROPERTY_NAME, inEvent.getText(),
						true, true));
			}
		});
		lFilter.setWidth(FILTER_WIDTH, Unit.PIXELS);

		final Table lTable = new Table(null, lCountries);
		lTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		lTable.setWidth(FILTER_WIDTH, Unit.PIXELS);
		lTable.setPageLength(18);

		lCol3.addComponent(lFilter);
		lCol3.addComponent(lTable);
	}

	private static class CountryContainer extends IndexedContainer {
		protected static final String PROPERTY_NAME = "name"; //$NON-NLS-1$

		@SuppressWarnings("unchecked")
		protected CountryContainer() {
			super();

			addContainerProperty(PROPERTY_NAME, String.class, ""); //$NON-NLS-1$
			for (final CountryBean lCountry : CountryData.INSTANCE
					.getCountryContainer().getItemIds()) {
				final Item lItem = addItem(lCountry);
				lItem.getItemProperty(CountryContainer.PROPERTY_NAME).setValue(
						lCountry.getName());
			}
		}
	}

}
