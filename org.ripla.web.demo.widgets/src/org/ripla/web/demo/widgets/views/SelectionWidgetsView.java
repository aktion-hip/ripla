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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ripla.interfaces.IMessages;
import org.ripla.web.demo.widgets.Activator;
import org.ripla.web.demo.widgets.data.CountryContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

/**
 * The view to display the Vaadin selection widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class SelectionWidgetsView extends AbstractWidgetsView {
	private static final int OPTION_SIZE = 10;

	public SelectionWidgetsView(final CountryContainer inCountries) {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.select"); //$NON-NLS-1$

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

		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.selection.subtitle.list"))); //$NON-NLS-1$
		final ListSelect lList1 = new ListSelect(null, inCountries);
		lList1.setItemCaptionMode(ItemCaptionMode.ID);
		lList1.setRows(10);
		lList1.setMultiSelect(true);
		lList1.setNullSelectionAllowed(false);
		lList1.select(inCountries.getIdByIndex(0));
		lList1.setImmediate(true);
		lList1.addValueChangeListener(new Listener());
		lCol1.addComponent(lList1);

		lCol1.addComponent(getSubtitle(lMessages
				.getMessage("widgets.selection.subtitle.combox"))); //$NON-NLS-1$
		final ComboBox lCombo = new ComboBox(null, inCountries);
		lCombo.setInputPrompt(lMessages
				.getMessage("widgets.selection.combox.prompt")); //$NON-NLS-1$
		lCombo.setNullSelectionAllowed(false);
		lCombo.setFilteringMode(FilteringMode.STARTSWITH);
		lCombo.setImmediate(true);
		lCombo.addValueChangeListener(new Listener());
		lCol1.addComponent(lCombo);

		lCol2.addComponent(getSubtitle(lMessages
				.getMessage("widgets.selection.subtitle.options.single"))); //$NON-NLS-1$
		List<String> lCountries = getRandomSubset(inCountries, OPTION_SIZE,
				System.currentTimeMillis());
		final OptionGroup lOptions1 = new OptionGroup(null, lCountries);
		lOptions1.setNullSelectionAllowed(false);
		lOptions1.select(lCountries.get(0));
		lOptions1.setImmediate(true);
		lOptions1.addValueChangeListener(new Listener());
		lCol2.addComponent(lOptions1);

		lCol2.addComponent(getSubtitle(lMessages
				.getMessage("widgets.selection.subtitle.options.multiple"))); //$NON-NLS-1$
		lCountries = getRandomSubset(inCountries, OPTION_SIZE,
				System.currentTimeMillis() + 2000);
		final OptionGroup lOptions2 = new OptionGroup(null, lCountries);
		lOptions2.setNullSelectionAllowed(false);
		lOptions2.setMultiSelect(true);
		lOptions2.select(lCountries.get(0));
		lOptions2.setImmediate(true);
		lOptions2.addValueChangeListener(new Listener());
		lCol2.addComponent(lOptions2);

		lCol3.addComponent(getSubtitle(lMessages
				.getMessage("widgets.selection.subtitle.twin"))); //$NON-NLS-1$
		final TwinColSelect lCountrySelect = new TwinColSelect();
		lCountrySelect.setContainerDataSource(inCountries);
		lCountrySelect.setRows(OPTION_SIZE);
		lCountrySelect.setNullSelectionAllowed(true);
		lCountrySelect.setMultiSelect(true);
		lCountrySelect.setWidth(400, Unit.PIXELS);
		lCol3.addComponent(lCountrySelect);
	}

	private List<String> getRandomSubset(final CountryContainer inCountries,
			final int inLength, final long inSeed) {
		final List<String> out = new ArrayList<String>();

		final int lLength = inCountries.getItemIds().size();
		final Random lRandom = new Random(inSeed);

		for (int i = 0; i < inLength; i++) {
			out.add(inCountries.getIdByIndex(lRandom.nextInt(lLength))
					.getName());
		}
		Collections.sort(out);
		return out;
	}

	private class Listener implements Property.ValueChangeListener {
		@Override
		public void valueChange(final ValueChangeEvent inEvent) {
			Notification
					.show(Activator.getMessages().getMessage(
							"widgets.selection.feedback") + inEvent.getProperty().toString()); //$NON-NLS-1$
		}
	}

}
