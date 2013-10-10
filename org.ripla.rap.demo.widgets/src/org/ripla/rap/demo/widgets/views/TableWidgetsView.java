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

package org.ripla.rap.demo.widgets.views;

import java.util.List;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.data.Countries;
import org.ripla.rap.demo.widgets.data.Countries.ColHeader;
import org.ripla.rap.demo.widgets.data.Countries.CountryComparator;
import org.ripla.rap.demo.widgets.data.CountryBean;
import org.ripla.rap.util.GridLayoutHelper;
import org.ripla.rap.util.Popup;
import org.ripla.rap.util.Popup.PopupButtons;

/**
 * The view to display the RAP table widgets.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class TableWidgetsView extends AbstractWidgetsView {
	private static final String TMPL = "<em>%s</em> (source: <a href=\"%s\" target=\"_blank\">%s</a>)"; //$NON-NLS-1$

	private static final IMessages MESSAGES = Activator.getMessages();

	/**
	 * @param inParent
	 */
	public TableWidgetsView(final Composite inParent) {
		super(inParent);
		createTitle(MESSAGES.getMessage("widgets.title.page.table"));

		final Label lDescription = new Label(this, SWT.NONE);
		lDescription.setText(String.format(TMPL,
				MESSAGES.getMessage("widgets.table.description"),
				"http://sedac.ciesin.columbia.edu/mva/downscaling/ciesin.html",
				"SEDAC"));
		lDescription.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		final TableViewer lViewer = new TableViewer(this, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		ColumnViewerToolTipSupport.enableFor(lViewer);

		final Table lTable = lViewer.getTable();
		lTable.setHeaderVisible(true);
		lTable.setLinesVisible(true);
		lTable.setLayout(GridLayoutHelper.createGridLayout());
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.heightHint = 430;
		lTable.setLayoutData(lLayoutData);

		// columns
		int i = 0;
		for (final ColHeader lColHeader : Countries.COL_HEADERS) {
			createColumn(lColHeader, lViewer, i++);
		}

		lViewer.setContentProvider(new CountryContentProvider());
		lViewer.setLabelProvider(new CountryLabelProvider());
		lViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent inEvent) {
				popup((IStructuredSelection) inEvent.getSelection());
			}
		});

		lViewer.setInput(Countries.getCountries());
	}

	protected void popup(final IStructuredSelection inSelection) {
		final CountryBean lCountry = (CountryBean) inSelection
				.getFirstElement();
		final Popup lPopup = new Popup(getShell(),
				MESSAGES.getMessage("widgets.table.popup.title"),
				lCountry.toHtml(), Popup.DFT_WIDTH, 500);
		lPopup.setButtons(PopupButtons.CANCEL);
		lPopup.open();
	}

	private void createColumn(final ColHeader inColHeader,
			final TableViewer inViewer, final int inColumnIndex) {
		final TableViewerColumn lColViewer = new TableViewerColumn(inViewer,
				inColumnIndex < 4 ? SWT.LEFT : SWT.RIGHT);
		final TableColumn lColumn = lColViewer.getColumn();
		lColumn.setText(inColHeader.text);
		lColumn.setWidth(inColHeader.width);
		lColumn.setMoveable(true);
		lColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				sortByColumn((TableColumn) inEvent.widget, inColumnIndex,
						inViewer);
			}
		});
	}

	private void sortByColumn(final TableColumn inColumn, final int inIndex,
			final TableViewer inViewer) {
		final int lSortDirection = updateSortDirection(inColumn);
		inViewer.setComparator(new CountryComparator(inIndex,
				lSortDirection == SWT.DOWN));
	}

	private static int updateSortDirection(final TableColumn inColumn) {
		final Table lTable = inColumn.getParent();
		if (inColumn == lTable.getSortColumn()) {
			if (lTable.getSortDirection() == SWT.UP) {
				lTable.setSortDirection(SWT.DOWN);
			} else {
				lTable.setSortDirection(SWT.UP);
			}
		} else {
			lTable.setSortColumn(inColumn);
			lTable.setSortDirection(SWT.DOWN);
		}
		return lTable.getSortDirection();
	}

	// ---

	private static class CountryLabelProvider extends CellLabelProvider {
		@Override
		public void update(final ViewerCell inCell) {
			final CountryBean lCountry = (CountryBean) inCell.getElement();
			inCell.setText(lCountry.getValue(inCell.getColumnIndex()));
		}

		@Override
		public String getToolTipText(final Object inElement) {
			return ((CountryBean) inElement).getName();
		}
	}

	private static class CountryContentProvider implements
			IStructuredContentProvider {
		private Object[] providedCountries;

		@SuppressWarnings("unchecked")
		@Override
		public void inputChanged(final Viewer inViewer,
				final Object inOldInput, final Object inNewInput) {
			if (inNewInput != null) {
				providedCountries = ((List<CountryBean>) inNewInput).toArray();
			}
		}

		@Override
		public Object[] getElements(final Object inInputElement) {
			return providedCountries;
		}

		@Override
		public void dispose() {
			// nothing to do
		}
	}

}
