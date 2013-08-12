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
import org.ripla.web.demo.widgets.data.CountryContainer;
import org.ripla.web.util.Popup;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * The view to display the Vaadin table widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class TableWidgetsView extends AbstractWidgetsView {
	private static final String TMPL = "<div><em>%s</em> (source: <a href=\"%s\" target=\"_blank\">%s</a>)</div>"; //$NON-NLS-1$
	private static final int TABLE_SIZE = 19;

	public TableWidgetsView(final CountryContainer inCountries) {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.table"); //$NON-NLS-1$
		lLayout.addComponent(new Label(
				String.format(
						TMPL,
						lMessages.getMessage("widgets.table.description"), "http://sedac.ciesin.columbia.edu/mva/downscaling/ciesin.html", "SEDAC"), ContentMode.HTML)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final Table lTable = new Table();
		lTable.setWidth("100%"); //$NON-NLS-1$
		lTable.setContainerDataSource(inCountries);
		for (int i = 4; i < CountryContainer.NATURAL_COL_ORDER.length; i++) {
			lTable.addGeneratedColumn(CountryContainer.NATURAL_COL_ORDER[i],
					new NumberColumnGenerator()); // NOPMD by Luthiger
		}

		lTable.setColumnHeaders(CountryContainer.COL_HEADERS);
		lTable.setPageLength(TABLE_SIZE);
		lTable.setColumnCollapsingAllowed(true);
		lTable.setColumnReorderingAllowed(true);
		lTable.setSelectable(true);
		lTable.setImmediate(true);

		lTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent inEvent) {
				final Object lEntry = inEvent.getProperty().getValue();
				if (lEntry instanceof CountryBean) {
					Popup.displayPopup(
							lMessages.getMessage("widgets.table.popup.title"), createCountryPopup((CountryBean) lEntry), 260, 685); //$NON-NLS-1$
				}
			}
		});

		lLayout.addComponent(lTable);
	}

	private static class NumberColumnGenerator implements Table.ColumnGenerator {
		@Override
		public Object generateCell(final Table inSource, final Object inItemId,
				final Object inColumnId) {
			return new Label(
					String.format(
							"<div style=\"text-align:right;\">%s</div>", inSource.getItem(inItemId).getItemProperty(inColumnId).toString()), ContentMode.HTML); //$NON-NLS-1$
		}

	}

}
