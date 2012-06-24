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

package org.ripla.demo.widgets.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ripla.demo.widgets.Activator;
import org.ripla.demo.widgets.data.CountryContainer;
import org.ripla.web.interfaces.IMessages;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Select;
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

	public SelectionWidgetsView(CountryContainer inCountries) {
		IMessages lMessages = Activator.getMessages();
		VerticalLayout lLayout = initLayout(lMessages, "widgets.title.page.select"); //$NON-NLS-1$

		HorizontalLayout lColumns = new HorizontalLayout();
		lColumns.setSpacing(true);
		lLayout.addComponent(lColumns);
		
		VerticalLayout lCol1 = new VerticalLayout();
		lCol1.setSizeUndefined();
		lColumns.addComponent(lCol1);
		VerticalLayout lCol2 = new VerticalLayout();
		lCol2.setSizeUndefined();
		lColumns.addComponent(lCol2);
		VerticalLayout lCol3 = new VerticalLayout();
		lCol3.setSizeUndefined();
		lColumns.addComponent(lCol3);
		lColumns.setExpandRatio(lCol3, 1);
		
		lCol1.addComponent(getSubtitle(lMessages.getMessage("widgets.selection.subtitle.list"))); //$NON-NLS-1$
		ListSelect lList1 = new ListSelect(null, inCountries);
		lList1.setItemCaptionMode(Select.ITEM_CAPTION_MODE_ID);
		lList1.setRows(10);
		lList1.setMultiSelect(true);
		lList1.setNullSelectionAllowed(false);
		lList1.select(inCountries.getIdByIndex(0));
		lList1.setImmediate(true);
		lList1.addListener(new Listener());
		lCol1.addComponent(lList1);
		
		lCol1.addComponent(getSubtitle(lMessages.getMessage("widgets.selection.subtitle.combox"))); //$NON-NLS-1$
		ComboBox lCombo = new ComboBox(null, inCountries);
		lCombo.setInputPrompt(lMessages.getMessage("widgets.selection.combox.prompt")); //$NON-NLS-1$
		lCombo.setNullSelectionAllowed(false);
		lCombo.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		lCombo.setImmediate(true);
		lCombo.addListener(new Listener());
		lCol1.addComponent(lCombo);

		lCol2.addComponent(getSubtitle(lMessages.getMessage("widgets.selection.subtitle.options.single"))); //$NON-NLS-1$
		List<String> lCountries = getRandomSubset(inCountries, OPTION_SIZE, System.currentTimeMillis());
		OptionGroup lOptions1 = new OptionGroup(null, lCountries);
		lOptions1.setNullSelectionAllowed(false);
		lOptions1.select(lCountries.get(0));
		lOptions1.setImmediate(true);
		lOptions1.addListener(new Listener());
		lCol2.addComponent(lOptions1);
		
		lCol2.addComponent(getSubtitle(lMessages.getMessage("widgets.selection.subtitle.options.multiple"))); //$NON-NLS-1$
		lCountries = getRandomSubset(inCountries, OPTION_SIZE, System.currentTimeMillis()+2000);
		OptionGroup lOptions2 = new OptionGroup(null, lCountries);
		lOptions2.setNullSelectionAllowed(false);
		lOptions2.setMultiSelect(true);
		lOptions2.select(lCountries.get(0));
		lOptions2.setImmediate(true);
		lOptions2.addListener(new Listener());
		lCol2.addComponent(lOptions2);
		
		lCol3.addComponent(getSubtitle(lMessages.getMessage("widgets.selection.subtitle.twin"))); //$NON-NLS-1$
		TwinColSelect lCountrySelect = new TwinColSelect();
		lCountrySelect.setContainerDataSource(inCountries);
		lCountrySelect.setRows(OPTION_SIZE);
		lCountrySelect.setNullSelectionAllowed(true);
		lCountrySelect.setMultiSelect(true);
		lCountrySelect.setWidth(400, UNITS_PIXELS);
		lCol3.addComponent(lCountrySelect);
	}
	
	private List<String> getRandomSubset(CountryContainer inCountries, int inLength, long inSeed) {
		List<String> out = new ArrayList<String>();
		
		int lLength = inCountries.getItemIds().size();
		Random lRandom = new Random(inSeed);
		
		for (int i = 0; i < inLength; i++) {
			out.add(inCountries.getIdByIndex(lRandom.nextInt(lLength)).getName());
		}
		Collections.sort(out);
		return out;
	}
	
	private class Listener implements Property.ValueChangeListener {
		/* (non-Javadoc)
		 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
		 */
		@Override
		public void valueChange(ValueChangeEvent inEvent) {
			getWindow().showNotification(Activator.getMessages().getMessage("widgets.selection.feedback") + inEvent.getProperty().toString()); //$NON-NLS-1$
		}
	}
	
}
